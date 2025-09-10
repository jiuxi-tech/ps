package com.jiuxi.module.org.infra.persistence.repo;

import com.jiuxi.module.org.domain.entity.Department;
import com.jiuxi.module.org.domain.entity.DepartmentStatus;
import com.jiuxi.module.org.domain.entity.DepartmentType;
import com.jiuxi.module.org.domain.repo.DepartmentRepository;
import com.jiuxi.module.org.infra.persistence.entity.DepartmentPO;
import com.jiuxi.module.org.infra.persistence.mapper.DepartmentMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    
    public DepartmentRepositoryImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }
    
    @Override
    public Department save(Department department) {
        DepartmentPO departmentPO = toDepartmentPO(department);
        
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
        
        return toDepartment(departmentPO);
    }
    
    @Override
    public Optional<Department> findById(String deptId) {
        Optional<DepartmentPO> departmentPOOpt = departmentMapper.selectById(deptId);
        return departmentPOOpt.map(this::toDepartment);
    }
    
    @Override
    public List<Department> findByParentId(String parentDeptId) {
        List<DepartmentPO> departmentPOs = departmentMapper.selectByParentId(parentDeptId);
        return departmentPOs.stream()
                .map(this::toDepartment)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Department> findRootDepartments() {
        // 查询父部门ID为空或者特定值的根部门
        return findByParentId(null);
    }
    
    @Override
    public List<Department> findByTenantId(String tenantId) {
        List<DepartmentPO> departmentPOs = departmentMapper.selectByTenantId(tenantId);
        return departmentPOs.stream()
                .map(this::toDepartment)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Department> findByName(String deptName, String tenantId) {
        Optional<DepartmentPO> departmentPOOpt = departmentMapper.selectByName(deptName, tenantId);
        return departmentPOOpt.map(this::toDepartment);
    }
    
    @Override
    public boolean existsByName(String deptName, String tenantId, String excludeDeptId) {
        int count = departmentMapper.countByName(deptName, tenantId, excludeDeptId);
        return count > 0;
    }
    
    @Override
    public void deleteById(String deptId) {
        departmentMapper.deleteById(deptId, "system", LocalDateTime.now());
    }
    
    @Override
    public void deleteByIds(List<String> deptIds) {
        departmentMapper.deleteByIds(deptIds, "system", LocalDateTime.now());
    }
    
    @Override
    public List<Department> findDescendants(String deptPath) {
        List<DepartmentPO> departmentPOs = departmentMapper.selectDescendants(deptPath);
        return departmentPOs.stream()
                .map(this::toDepartment)
                .collect(Collectors.toList());
    }
    
    @Override
    public long countUsersByDeptId(String deptId) {
        return departmentMapper.countUsersByDeptId(deptId);
    }
    
    @Override
    public List<Department> findDepartmentTree(String tenantId) {
        List<DepartmentPO> departmentPOs = departmentMapper.selectDepartmentTree(tenantId);
        return departmentPOs.stream()
                .map(this::toDepartment)
                .collect(Collectors.toList());
    }
    
    /**
     * 将部门聚合根转换为持久化对象
     */
    private DepartmentPO toDepartmentPO(Department department) {
        DepartmentPO departmentPO = new DepartmentPO();
        departmentPO.setDeptId(department.getDeptId());
        departmentPO.setPdeptId(department.getParentDeptId());
        departmentPO.setDeptLevelcode(department.getDeptPath()); // 使用部门路径作为层级编码
        // departmentPO.setDeptNo(department.getDeptNo()); // Department实体暂未定义deptNo字段
        departmentPO.setDeptFullName(department.getDeptFullName());
        departmentPO.setDeptSimpleName(department.getDeptSimpleName());
        departmentPO.setDeptType(department.getType() != null ? department.getType().name() : null);
        departmentPO.setDeptDesc(department.getDescription());
        departmentPO.setOrderIndex(department.getOrderIndex() != null ? department.getOrderIndex().doubleValue() : null);
        departmentPO.setCategory(0); // 默认政府类型
        departmentPO.setPrincipalName(department.getManagerId()); // 暂时用管理员ID作为负责人
        departmentPO.setPrincipalTel(department.getContactPhone());
        departmentPO.setAscnId(department.getDeptId()); // 暂时设置为自身ID
        departmentPO.setActived(department.getStatus() == DepartmentStatus.ACTIVE ? 1 : 0);
        departmentPO.setEnabled(1);
        departmentPO.setTenantId(department.getTenantId());
        departmentPO.setCreator(department.getCreator());
        departmentPO.setCreateTime(department.getCreateTime());
        departmentPO.setUpdator(department.getUpdator());
        departmentPO.setUpdateTime(department.getUpdateTime());
        
        return departmentPO;
    }
    
    /**
     * 将持久化对象转换为部门聚合根
     */
    private Department toDepartment(DepartmentPO departmentPO) {
        Department department = new Department();
        department.setDeptId(departmentPO.getDeptId());
        department.setDeptName(departmentPO.getDeptFullName());
        department.setDeptSimpleName(departmentPO.getDeptSimpleName());
        department.setDeptFullName(departmentPO.getDeptFullName());
        department.setParentDeptId(departmentPO.getPdeptId());
        department.setDeptPath(departmentPO.getDeptLevelcode());
        department.setDeptLevel(calculateLevel(departmentPO.getDeptLevelcode()));
        // department.setDeptNo(departmentPO.getDeptNo()); // Department实体暂未定义deptNo字段
        department.setType(departmentPO.getDeptType() != null ? DepartmentType.valueOf(departmentPO.getDeptType()) : null);
        department.setDescription(departmentPO.getDeptDesc());
        department.setOrderIndex(departmentPO.getOrderIndex() != null ? departmentPO.getOrderIndex().intValue() : null);
        department.setManagerId(departmentPO.getPrincipalName());
        department.setContactPhone(departmentPO.getPrincipalTel());
        department.setAddress(null); // 地址信息不在基本信息表中
        department.setStatus(departmentPO.getActived() == 1 ? DepartmentStatus.ACTIVE : DepartmentStatus.INACTIVE);
        department.setTenantId(departmentPO.getTenantId());
        department.setCreator(departmentPO.getCreator());
        department.setCreateTime(departmentPO.getCreateTime());
        department.setUpdator(departmentPO.getUpdator());
        department.setUpdateTime(departmentPO.getUpdateTime());
        
        return department;
    }
    
    /**
     * 根据部门路径计算层级
     */
    private Integer calculateLevel(String deptPath) {
        if (!StringUtils.hasText(deptPath)) {
            return 1;
        }
        // 假设路径格式为 /root/level1/level2，通过斜杠数量计算层级
        return deptPath.split("/").length;
    }
}