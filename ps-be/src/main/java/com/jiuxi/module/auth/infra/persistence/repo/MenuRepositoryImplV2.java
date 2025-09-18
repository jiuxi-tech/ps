package com.jiuxi.module.auth.infra.persistence.repo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuxi.module.auth.domain.model.entity.Menu;
import com.jiuxi.module.auth.domain.repo.MenuRepository;
import com.jiuxi.module.auth.infra.persistence.assembler.MenuPersistenceAssembler;
import com.jiuxi.module.auth.infra.persistence.base.BaseRepositoryImpl;
import com.jiuxi.module.auth.infra.persistence.entity.MenuPO;
import com.jiuxi.module.auth.infra.persistence.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 菜单仓储实现（重构版本）
 * 实现菜单实体的持久化操作，使用统一的装配器和异常处理
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Repository("menuRepositoryV2")
@Primary
public class MenuRepositoryImplV2 extends BaseRepositoryImpl implements MenuRepository {
    
    @Autowired
    private MenuMapper menuMapper;
    
    @Autowired
    private MenuPersistenceAssembler menuPersistenceAssembler;
    
    private static final String ENTITY_TYPE = "Menu";
    
    @Override
    public Menu save(Menu menu) {
        validateEntity(menu, ENTITY_TYPE);
        
        return executeWithExceptionHandling("SAVE", ENTITY_TYPE, menu.getMenuId(), () -> {
            MenuPO menuPO = menuPersistenceAssembler.toPO(menu);
            
            if (menuPO.getId() == null || !exists(menuPO.getId())) {
                // 新增操作
                menuMapper.insert(menuPO);
                logger.info("新增菜单成功: menuId={}, menuCode={}", menuPO.getId(), menuPO.getMenuCode());
            } else {
                // 更新操作
                menuMapper.updateById(menuPO);
                logger.info("更新菜单成功: menuId={}, menuCode={}", menuPO.getId(), menuPO.getMenuCode());
            }
            
            return menuPersistenceAssembler.toEntity(menuPO);
        });
    }
    
    @Override
    public Optional<Menu> findById(String menuId) {
        validateId(menuId, ENTITY_TYPE);
        
        return executeWithExceptionHandling("FIND_BY_ID", ENTITY_TYPE, menuId, () -> {
            MenuPO menuPO = menuMapper.selectById(menuId);
            Menu menu = menuPersistenceAssembler.toEntity(menuPO);
            
            if (menu != null) {
                logger.debug("查找菜单成功: menuId={}", menuId);
            } else {
                logger.debug("菜单不存在: menuId={}", menuId);
            }
            
            return Optional.ofNullable(menu);
        });
    }
    
    @Override
    public void deleteById(String menuId) {
        validateId(menuId, ENTITY_TYPE);
        
        executeWithExceptionHandling("DELETE_BY_ID", ENTITY_TYPE, menuId, () -> {
            int deletedRows = menuMapper.deleteById(menuId);
            if (deletedRows > 0) {
                logger.info("删除菜单成功: menuId={}", menuId);
            } else {
                logger.warn("删除菜单失败，菜单不存在: menuId={}", menuId);
            }
        });
    }
    
    @Override
    public Optional<Menu> findByMenuCode(String menuCode, String tenantId) {
        if (menuCode == null || menuCode.trim().isEmpty()) {
            throw new IllegalArgumentException("菜单编码不能为空");
        }
        validateTenantId(tenantId);
        
        return executeWithExceptionHandling("FIND_BY_CODE", ENTITY_TYPE, menuCode, () -> {
            QueryWrapper<MenuPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("menu_code", menuCode);
            queryWrapper.eq("tenant_id", tenantId);
            
            MenuPO menuPO = menuMapper.selectOne(queryWrapper);
            Menu menu = menuPersistenceAssembler.toEntity(menuPO);
            
            if (menu != null) {
                logger.debug("根据编码查找菜单成功: menuCode={}, tenantId={}", menuCode, tenantId);
            } else {
                logger.debug("根据编码未找到菜单: menuCode={}, tenantId={}", menuCode, tenantId);
            }
            
            return Optional.ofNullable(menu);
        });
    }
    
    @Override
    public List<Menu> getMenuTree(String tenantId) {
        validateTenantId(tenantId);
        
        return executeWithExceptionHandling("GET_MENU_TREE", ENTITY_TYPE, tenantId, () -> {
            QueryWrapper<MenuPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tenant_id", tenantId);
            queryWrapper.orderByAsc("menu_level", "order_index");
            
            List<MenuPO> menuPOs = menuMapper.selectList(queryWrapper);
            List<Menu> menus = menuPersistenceAssembler.toEntityList(menuPOs);
            
            logger.debug("获取菜单树成功: tenantId={}, 菜单数量={}", tenantId, menus.size());
            
            return buildMenuTree(menus);
        });
    }
    
    @Override
    public List<Menu> getChildMenus(String parentMenuId, String tenantId) {
        validateId(parentMenuId, "ParentMenu");
        validateTenantId(tenantId);
        
        return executeWithExceptionHandling("GET_CHILD_MENUS", ENTITY_TYPE, parentMenuId, () -> {
            QueryWrapper<MenuPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_menu_id", parentMenuId);
            queryWrapper.eq("tenant_id", tenantId);
            queryWrapper.orderByAsc("order_index");
            
            List<MenuPO> menuPOs = menuMapper.selectList(queryWrapper);
            List<Menu> menus = menuPersistenceAssembler.toEntityList(menuPOs);
            
            logger.debug("获取子菜单成功: parentMenuId={}, tenantId={}, 子菜单数量={}", 
                    parentMenuId, tenantId, menus.size());
            
            return menus;
        });
    }
    
    @Override
    public List<Menu> findByIds(List<String> menuIds, String tenantId) {
        if (menuIds == null || menuIds.isEmpty()) {
            return List.of();
        }
        validateTenantId(tenantId);
        
        return executeWithExceptionHandling("FIND_BY_IDS", ENTITY_TYPE, String.join(",", menuIds), () -> {
            QueryWrapper<MenuPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", menuIds);
            queryWrapper.eq("tenant_id", tenantId);
            queryWrapper.orderByAsc("order_index");
            
            List<MenuPO> menuPOs = menuMapper.selectList(queryWrapper);
            List<Menu> menus = menuPersistenceAssembler.toEntityList(menuPOs);
            
            logger.debug("批量查找菜单成功: 请求数量={}, 查找到数量={}, tenantId={}", 
                    menuIds.size(), menus.size(), tenantId);
            
            return menus;
        });
    }
    
    /**
     * 检查菜单是否存在
     */
    private boolean exists(String menuId) {
        if (menuId == null) {
            return false;
        }
        
        try {
            MenuPO existing = menuMapper.selectById(menuId);
            return existing != null;
        } catch (Exception e) {
            logger.warn("检查菜单存在性时发生异常: menuId={}", menuId, e);
            return false;
        }
    }
    
    /**
     * 构建菜单树结构
     */
    private List<Menu> buildMenuTree(List<Menu> menus) {
        // 构建菜单树的逻辑
        // 这里简化处理，实际应该实现完整的树形结构构建
        return menus.stream()
                .filter(menu -> menu.getParentMenuId() == null || menu.getParentMenuId().isEmpty())
                .peek(menu -> buildChildren(menu, menus))
                .toList();
    }
    
    /**
     * 递归构建子菜单
     */
    private void buildChildren(Menu parent, List<Menu> allMenus) {
        List<Menu> children = allMenus.stream()
                .filter(menu -> parent.getMenuId().equals(menu.getParentMenuId()))
                .peek(child -> buildChildren(child, allMenus))
                .toList();
        
        parent.setChildren(children);
    }
}