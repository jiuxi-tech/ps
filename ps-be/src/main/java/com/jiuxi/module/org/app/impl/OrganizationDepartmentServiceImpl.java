package com.jiuxi.module.org.app.impl;

import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpDeptExinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonDeptVO;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.module.org.app.service.OrganizationDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: OrganizationDepartmentServiceImpl
 * @Description: 组织部门服务实现 - 委托给原有服务保证功能不变
 * @Author DDD重构
 * @Date 2025-09-12
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service
public class OrganizationDepartmentServiceImpl implements OrganizationDepartmentService {

    @Autowired
    private TpDeptBasicinfoService tpDeptBasicinfoService;

    @Override
    public List<TreeNode> parttimeJobTree(String personId, String deptId, int category) {
        return tpDeptBasicinfoService.parttimeJobTree(personId, deptId, category);
    }

    @Override
    public List<TreeNode> tree(String deptId, String jwtdid, int sync, int category) {
        return tpDeptBasicinfoService.tree(deptId, jwtdid, sync, category);
    }

    @Override
    public List<TreeNode> getChildren(String parentId, String deptType, String filterCommAscn, Integer category) {
        return tpDeptBasicinfoService.getChildren(parentId, deptType, filterCommAscn, category);
    }

    @Override
    public TreeNode deptCityTree(String jwtaid, int sync, boolean expand) {
        return tpDeptBasicinfoService.deptCityTree(jwtaid, sync, expand);
    }

    @Override
    public TpDeptBasicinfoVO add(TpDeptBasicinfoVO vo, String pid, int category) {
        return tpDeptBasicinfoService.add(vo, pid, category);
    }

    @Override
    public TpDeptExinfoVO expAdd(TpDeptExinfoVO vo) {
        return tpDeptBasicinfoService.expAdd(vo);
    }

    @Override
    public TpDeptBasicinfoVO view(String deptId) {
        return tpDeptBasicinfoService.view(deptId);
    }

    @Override
    public TpDeptBasicinfoVO getById(String deptId, String jwtpid, String jwtaid) {
        return tpDeptBasicinfoService.getById(deptId, jwtpid, jwtaid);
    }

    @Override
    public TpDeptExinfoVO expView(String deptId) {
        return tpDeptBasicinfoService.expView(deptId);
    }

    @Override
    public TpDeptBasicinfoVO update(TpDeptBasicinfoVO vo, String pid) {
        return tpDeptBasicinfoService.update(vo, pid);
    }

    @Override
    public List<TpPersonDeptVO> selectBindByDeptId(String deptId) {
        return tpDeptBasicinfoService.selectBindByDeptId(deptId);
    }

    @Override
    public int removeById(String id, String jwtpid) {
        return tpDeptBasicinfoService.removeById(id, jwtpid);
    }

    @Override
    public String getLineCode(String deptId) {
        return tpDeptBasicinfoService.getLineCode(deptId);
    }

    @Override
    public void deleteDeptByAscnId(String ascnId, String jwtpid) {
        tpDeptBasicinfoService.deleteDeptByAscnId(ascnId, jwtpid);
    }

    @Override
    public List<TreeNode> getFullTree(String rootId, int category) {
        return tpDeptBasicinfoService.getFullTree(rootId, category);
    }
}