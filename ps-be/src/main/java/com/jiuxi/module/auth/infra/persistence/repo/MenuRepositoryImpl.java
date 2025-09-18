package com.jiuxi.module.auth.infra.persistence.repo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuxi.module.auth.domain.model.entity.Menu;
import com.jiuxi.module.auth.domain.repo.MenuRepository;
import com.jiuxi.module.auth.infra.persistence.entity.MenuPO;
import com.jiuxi.module.auth.infra.persistence.mapper.MenuMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 菜单仓储实现
 * 实现菜单实体的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Repository
public class MenuRepositoryImpl extends ServiceImpl<MenuMapper, MenuPO> implements MenuRepository {
    
    private final MenuMapper menuMapper;
    
    public MenuRepositoryImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }
    
    /**
     * 保存菜单
     * @param menu 菜单实体
     * @return 保存后的菜单
     */
    @Override
    public Menu save(Menu menu) {
        MenuPO menuPO = toPO(menu);
        if (menuPO.getId() == null) {
            menuMapper.insert(menuPO);
        } else {
            menuMapper.updateById(menuPO);
        }
        return toEntity(menuPO);
    }
    
    /**
     * 根据ID查找菜单
     * @param menuId 菜单ID
     * @return 菜单Optional
     */
    @Override
    public Optional<Menu> findById(String menuId) {
        MenuPO menuPO = menuMapper.selectById(menuId);
        return Optional.ofNullable(menuPO).map(this::toEntity);
    }
    
    /**
     * 根据ID删除菜单
     * @param menuId 菜单ID
     */
    @Override
    public void deleteById(String menuId) {
        menuMapper.deleteById(menuId);
    }
    
    /**
     * 根据菜单编码查找菜单
     * @param menuCode 菜单编码
     * @param tenantId 租户ID
     * @return 菜单Optional
     */
    @Override
    public Optional<Menu> findByMenuCode(String menuCode, String tenantId) {
        QueryWrapper<MenuPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_code", menuCode);
        queryWrapper.eq("tenant_id", tenantId);
        MenuPO menuPO = menuMapper.selectOne(queryWrapper);
        return Optional.ofNullable(menuPO).map(this::toEntity);
    }
    
    /**
     * 获取菜单树
     * @param tenantId 租户ID
     * @return 菜单树列表
     */
    @Override
    public List<Menu> getMenuTree(String tenantId) {
        // 这里应该实现获取菜单树的逻辑
        // 为简化示例，暂时返回空列表
        return List.of();
    }
    
    /**
     * 获取子菜单列表
     * @param parentMenuId 父菜单ID
     * @param tenantId 租户ID
     * @return 子菜单列表
     */
    @Override
    public List<Menu> getChildMenus(String parentMenuId, String tenantId) {
        QueryWrapper<MenuPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_menu_id", parentMenuId);
        queryWrapper.eq("tenant_id", tenantId);
        List<MenuPO> menuPOs = menuMapper.selectList(queryWrapper);
        return menuPOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
    
    /**
     * 批量查找菜单
     */
    @Override
    public List<Menu> findByIds(List<String> menuIds, String tenantId) {
        if (menuIds == null || menuIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        QueryWrapper<MenuPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", menuIds);
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.orderByAsc("order_index");
        
        List<MenuPO> menuPOs = menuMapper.selectList(queryWrapper);
        return menuPOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
    
    /**
     * 将Menu实体转换为MenuPO
     * @param menu 菜单实体
     * @return 菜单持久化对象
     */
    private MenuPO toPO(Menu menu) {
        if (menu == null) {
            return null;
        }
        
        MenuPO menuPO = new MenuPO();
        menuPO.setId(menu.getMenuId());
        menuPO.setMenuCode(menu.getMenuCode());
        menuPO.setMenuName(menu.getMenuName());
        menuPO.setMenuTitle(menu.getMenuTitle());
        menuPO.setParentMenuId(menu.getParentMenuId());
        menuPO.setMenuPath(menu.getMenuPath());
        menuPO.setMenuLevel(menu.getMenuLevel());
        menuPO.setMenuType(menu.getMenuType() != null ? menu.getMenuType().getCode() : null);
        menuPO.setMenuUri(menu.getMenuUri());
        menuPO.setMenuIcon(menu.getMenuIcon());
        menuPO.setComponent(menu.getComponent());
        menuPO.setStatus(menu.getStatus() != null ? menu.getStatus().getCode() : null);
        menuPO.setVisible(menu.isVisible());
        menuPO.setKeepAlive(menu.needKeepAlive());
        menuPO.setExternal(menu.isExternal());
        menuPO.setLeaf(menu.isLeaf());
        menuPO.setOrderIndex(menu.getOrderIndex());
        menuPO.setCreator(menu.getCreator());
        menuPO.setCreateTime(menu.getCreateTime());
        menuPO.setUpdator(menu.getUpdator());
        menuPO.setUpdateTime(menu.getUpdateTime());
        menuPO.setTenantId(menu.getTenantId());
        
        return menuPO;
    }
    
    /**
     * 将MenuPO转换为Menu实体
     * @param menuPO 菜单持久化对象
     * @return 菜单实体
     */
    private Menu toEntity(MenuPO menuPO) {
        if (menuPO == null) {
            return null;
        }
        
        Menu menu = new Menu();
        menu.setMenuId(menuPO.getId());
        menu.setMenuCode(menuPO.getMenuCode());
        menu.setMenuName(menuPO.getMenuName());
        menu.setMenuTitle(menuPO.getMenuTitle());
        menu.setParentMenuId(menuPO.getParentMenuId());
        menu.setMenuPath(menuPO.getMenuPath());
        menu.setMenuLevel(menuPO.getMenuLevel());
        menu.setMenuType(menuPO.getMenuType() != null ? 
            com.jiuxi.module.auth.domain.model.entity.MenuType.fromCode(menuPO.getMenuType()) : null);
        menu.setMenuUri(menuPO.getMenuUri());
        menu.setMenuIcon(menuPO.getMenuIcon());
        menu.setComponent(menuPO.getComponent());
        menu.setStatus(menuPO.getStatus() != null ? 
            com.jiuxi.module.auth.domain.model.entity.MenuStatus.fromCode(menuPO.getStatus()) : null);
        menu.setVisible(menuPO.getVisible());
        menu.setKeepAlive(menuPO.getKeepAlive());
        menu.setExternal(menuPO.getExternal());
        menu.setLeaf(menuPO.getLeaf());
        menu.setOrderIndex(menuPO.getOrderIndex());
        menu.setCreator(menuPO.getCreator());
        menu.setCreateTime(menuPO.getCreateTime());
        menu.setUpdator(menuPO.getUpdator());
        menu.setUpdateTime(menuPO.getUpdateTime());
        menu.setTenantId(menuPO.getTenantId());
        
        return menu;
    }
}