package com.jiuxi.module.org.infra.persistence.assembler;

import com.jiuxi.module.org.domain.model.aggregate.Department;
import com.jiuxi.module.org.domain.model.entity.DepartmentStatus;
import com.jiuxi.module.org.domain.model.entity.DepartmentType;
import com.jiuxi.module.org.infra.persistence.entity.DepartmentPO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门基础设施层装配器
 * 负责Department聚合根与DepartmentPO之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
public class DepartmentInfraAssembler {
    
    /**
     * 将部门聚合根转换为持久化对象
     * @param department 部门聚合根
     * @return 部门持久化对象
     */
    public DepartmentPO toDepartmentPO(Department department) {
        if (department == null) {
            return null;
        }
        
        DepartmentPO departmentPO = new DepartmentPO();
        departmentPO.setDeptId(department.getDeptId());
        departmentPO.setPdeptId(department.getParentDeptId());
        departmentPO.setDeptLevelcode(department.getDeptPath()); // 使用部门路径作为层级编码
        departmentPO.setLeftValue(department.getLeftValue());
        departmentPO.setRightValue(department.getRightValue());
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
     * @param departmentPO 部门持久化对象
     * @return 部门聚合根
     */
    public Department toDepartment(DepartmentPO departmentPO) {
        if (departmentPO == null) {
            return null;
        }
        
        Department department = new Department();
        department.setDeptId(departmentPO.getDeptId());
        department.setDeptName(departmentPO.getDeptFullName());
        department.setDeptSimpleName(departmentPO.getDeptSimpleName());
        department.setDeptFullName(departmentPO.getDeptFullName());
        department.setParentDeptId(departmentPO.getPdeptId());
        department.setDeptPath(departmentPO.getDeptLevelcode());
        department.setDeptLevel(calculateLevel(departmentPO.getDeptLevelcode()));
        department.setLeftValue(departmentPO.getLeftValue());
        department.setRightValue(departmentPO.getRightValue());
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
     * 批量转换持久化对象为部门聚合根
     * @param departmentPOs 部门持久化对象列表
     * @return 部门聚合根列表
     */
    public List<Department> toDepartmentList(List<DepartmentPO> departmentPOs) {
        if (departmentPOs == null || departmentPOs.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return departmentPOs.stream()
                .map(this::toDepartment)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量转换部门聚合根为持久化对象
     * @param departments 部门聚合根列表
     * @return 部门持久化对象列表
     */
    public List<DepartmentPO> toDepartmentPOList(List<Department> departments) {
        if (departments == null || departments.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        
        return departments.stream()
                .map(this::toDepartmentPO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据部门路径计算层级
     * @param deptPath 部门路径
     * @return 部门层级
     */
    private Integer calculateLevel(String deptPath) {
        if (!StringUtils.hasText(deptPath)) {
            return 1;
        }
        // 假设路径格式为 /root/level1/level2，通过斜杠数量计算层级
        return deptPath.split("/").length;
    }
}