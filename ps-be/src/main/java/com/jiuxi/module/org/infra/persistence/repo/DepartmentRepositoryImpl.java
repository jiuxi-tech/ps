package com.jiuxi.module.org.infra.persistence.repo;

import com.jiuxi.module.org.domain.model.aggregate.Department;
import com.jiuxi.module.org.domain.repo.DepartmentRepository;
import com.jiuxi.module.org.infra.persistence.entity.DepartmentPO;
import com.jiuxi.module.org.infra.persistence.mapper.DepartmentMapper;
import com.jiuxi.module.org.infra.persistence.assembler.DepartmentInfraAssembler;
import com.jiuxi.module.org.infra.cache.OrgCacheConfig;
import com.jiuxi.module.org.infra.cache.OrgCacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 部门仓储实现类
 * 负责部门聚合根的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {
    
    private final DepartmentMapper departmentMapper;
    private final DepartmentInfraAssembler departmentInfraAssembler;
    private final OrgCacheService orgCacheService;
    
    public DepartmentRepositoryImpl(DepartmentMapper departmentMapper, 
                                  DepartmentInfraAssembler departmentInfraAssembler,
                                  OrgCacheService orgCacheService) {
        this.departmentMapper = departmentMapper;
        this.departmentInfraAssembler = departmentInfraAssembler;
        this.orgCacheService = orgCacheService;
    }
    
    @Override
    @CacheEvict(value = OrgCacheConfig.DEPARTMENT_CACHE, key = "#department.deptId", condition = "#department.deptId != null")
    public Department save(Department department) {
        DepartmentPO departmentPO = departmentInfraAssembler.toDepartmentPO(department);
        
        if (StringUtils.hasText(departmentPO.getDeptId())) {
            // 更新
            departmentPO.setUpdateTime(LocalDateTime.now());
            departmentMapper.updateById(departmentPO);
        } else {
            // 新增
            departmentPO.setDeptId(UUID.randomUUID().toString());
            departmentPO.setCreateTime(LocalDateTime.now());
            departmentPO.setActived(1);
            departmentPO.setEnabled(1);
            departmentPO.setLeaf(1);
            departmentMapper.insert(departmentPO);
        }
        
        // 清除相关缓存
        Department result = departmentInfraAssembler.toDepartment(departmentPO);
        orgCacheService.evictDepartmentCache(result.getDeptId(), result.getParentDeptId(), result.getTenantId());
        
        return result;
    }
    
    @Override
    @Cacheable(value = OrgCacheConfig.DEPARTMENT_CACHE, key = "#deptId", unless = "#result.isEmpty()")
    public Optional<Department> findById(String deptId) {
        Optional<DepartmentPO> departmentPOOpt = departmentMapper.selectById(deptId);
        return departmentPOOpt.map(departmentInfraAssembler::toDepartment);
    }
    
    @Override
    @Cacheable(value = OrgCacheConfig.DEPT_CHILDREN_CACHE, key = "#parentDeptId", unless = "#result.isEmpty()")
    public List<Department> findByParentId(String parentDeptId) {
        List<DepartmentPO> departmentPOs = departmentMapper.selectByParentId(parentDeptId);
        return departmentInfraAssembler.toDepartmentList(departmentPOs);
    }
    
    @Override
    public List<Department> findRootDepartments() {
        // 查询父部门ID为空或者特定值的根部门
        return findByParentId(null);
    }
    
    @Override
    public List<Department> findByTenantId(String tenantId) {
        List<DepartmentPO> departmentPOs = departmentMapper.selectByTenantId(tenantId);
        return departmentInfraAssembler.toDepartmentList(departmentPOs);
    }
    
    @Override
    public Optional<Department> findByName(String deptName, String tenantId) {
        Optional<DepartmentPO> departmentPOOpt = departmentMapper.selectByName(deptName, tenantId);
        return departmentPOOpt.map(departmentInfraAssembler::toDepartment);
    }
    
    @Override
    public boolean existsByName(String deptName, String tenantId, String excludeDeptId) {
        int count = departmentMapper.countByName(deptName, tenantId, excludeDeptId);
        return count > 0;
    }
    
    @Override
    @CacheEvict(value = OrgCacheConfig.DEPARTMENT_CACHE, key = "#deptId")
    public void deleteById(String deptId) {
        // 获取部门信息用于清除相关缓存
        Optional<Department> deptOpt = findById(deptId);
        
        departmentMapper.deleteById(deptId, "system", LocalDateTime.now());
        
        // 清除相关缓存
        if (deptOpt.isPresent()) {
            Department dept = deptOpt.get();
            orgCacheService.evictDepartmentCache(dept.getDeptId(), dept.getParentDeptId(), dept.getTenantId());
        }
    }
    
    @Override
    public void deleteByIds(List<String> deptIds) {
        departmentMapper.deleteByIds(deptIds, "system", LocalDateTime.now());
    }
    
    @Override
    public List<Department> findDescendants(String deptPath) {
        List<DepartmentPO> departmentPOs = departmentMapper.selectDescendants(deptPath);
        return departmentInfraAssembler.toDepartmentList(departmentPOs);
    }
    
    @Override
    public long countUsersByDeptId(String deptId) {
        return departmentMapper.countUsersByDeptId(deptId);
    }
    
    @Override
    @Cacheable(value = OrgCacheConfig.DEPT_TREE_CACHE, key = "#tenantId", unless = "#result.isEmpty()")
    public List<Department> findDepartmentTree(String tenantId) {
        List<DepartmentPO> departmentPOs = departmentMapper.selectDepartmentTree(tenantId);
        return departmentInfraAssembler.toDepartmentList(departmentPOs);
    }
    
    @Override
    public List<Department> findByLeftRightValue(Integer leftValue, Integer rightValue, String tenantId) {
        // TODO: 需要在mapper中实现对应的查询方法
        List<DepartmentPO> departmentPOs = departmentMapper.selectByLeftRightValue(leftValue, rightValue, tenantId);
        return departmentInfraAssembler.toDepartmentList(departmentPOs);
    }
    
    @Override
    public List<Department> findAncestors(String deptId) {
        // 通过部门路径查询祖先部门
        Optional<Department> deptOpt = findById(deptId);
        if (!deptOpt.isPresent()) {
            return List.of();
        }
        
        Department dept = deptOpt.get();
        List<DepartmentPO> ancestorPOs = departmentMapper.selectAncestors(dept.getDeptPath());
        return departmentInfraAssembler.toDepartmentList(ancestorPOs);
    }
    
    @Override
    public List<Department> findDescendants(String deptId, boolean includeInactive) {
        Optional<Department> deptOpt = findById(deptId);
        if (!deptOpt.isPresent()) {
            return List.of();
        }
        
        Department dept = deptOpt.get();
        List<DepartmentPO> descendantPOs;
        if (includeInactive) {
            descendantPOs = departmentMapper.selectAllDescendants(dept.getDeptPath());
        } else {
            descendantPOs = departmentMapper.selectActiveDescendants(dept.getDeptPath());
        }
        return departmentInfraAssembler.toDepartmentList(descendantPOs);
    }
    
    @Override
    public List<Department> findByLevel(Integer level, String tenantId) {
        List<DepartmentPO> departmentPOs = departmentMapper.selectByLevel(level, tenantId);
        return departmentInfraAssembler.toDepartmentList(departmentPOs);
    }
    
    @Override
    public long countDirectChildren(String deptId) {
        return departmentMapper.countDirectChildren(deptId);
    }
    
    @Override
    public long countAllDescendants(String deptId) {
        Optional<Department> deptOpt = findById(deptId);
        if (!deptOpt.isPresent()) {
            return 0;
        }
        
        Department dept = deptOpt.get();
        return departmentMapper.countAllDescendants(dept.getDeptPath());
    }
    
    @Override
    public boolean isAncestor(String ancestorDeptId, String descendantDeptId) {
        Optional<Department> ancestorOpt = findById(ancestorDeptId);
        Optional<Department> descendantOpt = findById(descendantDeptId);
        
        if (!ancestorOpt.isPresent() || !descendantOpt.isPresent()) {
            return false;
        }
        
        Department ancestor = ancestorOpt.get();
        Department descendant = descendantOpt.get();
        
        // 通过路径判断祖先关系
        return StringUtils.hasText(descendant.getDeptPath()) && 
               StringUtils.hasText(ancestor.getDeptPath()) &&
               descendant.getDeptPath().startsWith(ancestor.getDeptPath() + "/");
    }
    
    @Override
    public List<Department> findDepartmentsWithChildren(List<String> deptIds, boolean includeDescendants) {
        List<DepartmentPO> departmentPOs;
        if (includeDescendants) {
            departmentPOs = departmentMapper.selectDepartmentsWithDescendants(deptIds);
        } else {
            departmentPOs = departmentMapper.selectDepartmentsWithDirectChildren(deptIds);
        }
        return departmentInfraAssembler.toDepartmentList(departmentPOs);
    }
    
    @Override
    public Optional<Department> findByDeptNo(String deptNo, String tenantId) {
        Optional<DepartmentPO> departmentPOOpt = departmentMapper.selectByDeptNo(deptNo, tenantId);
        return departmentPOOpt.map(departmentInfraAssembler::toDepartment);
    }
    
    @Override
    public boolean existsByDeptNo(String deptNo, String tenantId, String excludeDeptId) {
        int count = departmentMapper.countByDeptNo(deptNo, tenantId, excludeDeptId);
        return count > 0;
    }
    
    @Override
    public void updateLeftRightValue(String deptId, Integer leftValue, Integer rightValue) {
        departmentMapper.updateLeftRightValue(deptId, leftValue, rightValue);
    }
    
    @Override
    public void batchUpdateLeftRightValue(List<Department> departments) {
        List<DepartmentPO> departmentPOs = departmentInfraAssembler.toDepartmentPOList(departments);
        departmentMapper.batchUpdateLeftRightValue(departmentPOs);
    }
    
    
    // 新增的查询规范方法（临时实现）
    @Override
    public List<Department> findByQuery(com.jiuxi.module.org.domain.query.DepartmentQuery query) {
        // TODO: 实现复杂查询逻辑
        throw new UnsupportedOperationException("findByQuery method not implemented yet");
    }
    
    @Override
    public long countByQuery(com.jiuxi.module.org.domain.query.DepartmentQuery query) {
        // TODO: 实现复杂查询统计逻辑
        throw new UnsupportedOperationException("countByQuery method not implemented yet");
    }
    
    @Override
    public List<Department> findPageByQuery(com.jiuxi.module.org.domain.query.DepartmentQuery query) {
        // TODO: 实现复杂查询分页逻辑
        throw new UnsupportedOperationException("findPageByQuery method not implemented yet");
    }
    
    @Override
    public long countByTenantId(String tenantId) {
        // TODO: 实现租户部门统计逻辑
        return 0;
    }
}