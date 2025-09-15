package com.jiuxi.module.role.infra.persistence.repo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.module.role.domain.model.aggregate.Role;
import com.jiuxi.module.role.domain.model.vo.RoleId;
import com.jiuxi.module.role.domain.model.vo.RoleName;
import com.jiuxi.module.role.domain.repo.RoleRepository;
import com.jiuxi.module.role.infra.persistence.assembler.RolePersistenceAssembler;
import com.jiuxi.module.role.infra.persistence.entity.RolePO;
import com.jiuxi.module.role.infra.persistence.mapper.RoleBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色仓储实现
 * 基于MyBatis Plus的数据访问实现
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Repository("roleModuleRepositoryImpl")
public class RoleRepositoryImpl implements RoleRepository {
    
    @Autowired
    private RoleBaseMapper roleBaseMapper;
    
    @Autowired
    private RolePersistenceAssembler rolePersistenceAssembler;
    
    @Override
    public void save(Role role) {
        RolePO po = rolePersistenceAssembler.toPO(role);
        
        // 检查是否已存在
        RolePO existing = roleBaseMapper.selectById(po.getRoleId());
        if (existing == null) {
            roleBaseMapper.insert(po);
        } else {
            roleBaseMapper.updateById(po);
        }
    }
    
    @Override
    public void deleteById(RoleId roleId) {
        roleBaseMapper.deleteById(roleId.getValue());
    }
    
    @Override
    public Optional<Role> findById(RoleId roleId) {
        RolePO po = roleBaseMapper.selectById(roleId.getValue());
        if (po == null) {
            return Optional.empty();
        }
        return Optional.of(rolePersistenceAssembler.toDO(po));
    }
    
    @Override
    public Optional<Role> findByRoleName(RoleName roleName) {
        LambdaQueryWrapper<RolePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePO::getRoleName, roleName.getValue())
                   .eq(RolePO::getIsDeleted, 0);
        
        RolePO po = roleBaseMapper.selectOne(queryWrapper);
        if (po == null) {
            return Optional.empty();
        }
        return Optional.of(rolePersistenceAssembler.toDO(po));
    }
    
    @Override
    public List<Role> findBySpec(com.jiuxi.module.role.domain.repo.RoleSpecification spec) {
        LambdaQueryWrapper<RolePO> queryWrapper = buildQueryWrapper(spec);
        List<RolePO> poList = roleBaseMapper.selectList(queryWrapper);
        return rolePersistenceAssembler.toDOList(poList);
    }
    
    @Override
    public IPage<Role> findPage(Page<Role> page, com.jiuxi.module.role.domain.repo.RoleSpecification spec) {
        // 创建PO的分页对象
        Page<RolePO> poPage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<RolePO> queryWrapper = buildQueryWrapper(spec);
        
        // 执行分页查询
        IPage<RolePO> poPageResult = roleBaseMapper.selectPage(poPage, queryWrapper);
        
        // 转换为领域对象分页结果
        IPage<Role> result = new Page<>(
            poPageResult.getCurrent(),
            poPageResult.getSize(),
            poPageResult.getTotal()
        );
        
        List<Role> roleList = rolePersistenceAssembler.toDOList(poPageResult.getRecords());
        result.setRecords(roleList);
        
        return result;
    }
    
    @Override
    public List<Role> findAuthorizedRoles(String personId, String deptId, List<String> roleIds) {
        // 这里需要复杂的权限查询逻辑
        // 简化实现：根据角色ID列表查询角色
        if (roleIds == null || roleIds.isEmpty()) {
            return List.of();
        }
        
        LambdaQueryWrapper<RolePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePO::getRoleId, roleIds)
                   .eq(RolePO::getIsEnabled, 1)
                   .eq(RolePO::getIsDeleted, 0);
        
        List<RolePO> poList = roleBaseMapper.selectList(queryWrapper);
        return rolePersistenceAssembler.toDOList(poList);
    }
    
    @Override
    public long countUsersWithRole(RoleId roleId) {
        // 这里需要查询用户角色关联表
        // 简化实现，返回0
        // 实际实现需要关联查询用户角色关系表
        return 0;
    }
    
    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<RolePO> buildQueryWrapper(com.jiuxi.module.role.domain.repo.RoleSpecification spec) {
        LambdaQueryWrapper<RolePO> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.eq(RolePO::getIsDeleted, 0);
        
        if (spec.getRoleName() != null && !spec.getRoleName().trim().isEmpty()) {
            queryWrapper.like(RolePO::getRoleName, spec.getRoleName());
        }
        
        if (spec.getRoleDesc() != null && !spec.getRoleDesc().trim().isEmpty()) {
            queryWrapper.like(RolePO::getRoleDesc, spec.getRoleDesc());
        }
        
        if (spec.getCategory() != null) {
            queryWrapper.eq(RolePO::getCategory, spec.getCategory());
        }
        
        if (spec.getCreator() != null && !spec.getCreator().trim().isEmpty()) {
            queryWrapper.eq(RolePO::getCreator, spec.getCreator());
        }
        
        if (spec.getApplicationId() != null && !spec.getApplicationId().trim().isEmpty()) {
            queryWrapper.eq(RolePO::getApplicationId, spec.getApplicationId());
        }
        
        if (spec.getRoleIds() != null && !spec.getRoleIds().isEmpty()) {
            queryWrapper.in(RolePO::getRoleId, spec.getRoleIds());
        }
        
        // 默认查询启用的角色
        queryWrapper.eq(RolePO::getIsEnabled, 1);
        
        // 按创建时间倒序
        queryWrapper.orderByDesc(RolePO::getCreateTime);
        
        return queryWrapper;
    }
    
}