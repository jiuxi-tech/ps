package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.query.TpTreePermQuery;
import com.jiuxi.admin.core.bean.vo.TpDataPermissionsVO;
import com.jiuxi.common.bean.TreeNode;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: TpDataPermissionsService
 * @Description: 数据权限服务
 * @Author 杨占锐
 * @Date 2023/11/1 15:38
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpDataPermissionsService {

    /**
     * 保存数据权限，删除原有的数据
     *
     * @author 杨占锐
     * @date 2023/11/1 16:02
     */
    void add(TpDataPermissionsVO permissionsVO);

    /**
     * 查询所有数据权限
     *
     * @param personId 人员id
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpDataPermissionsVO>
     * @author 杨占锐
     * @date 2023/11/2 13:59
     */
    List<TpDataPermissionsVO> listPerm(String personId);

    /**
     * 查询所有数据权限的部门id
     *
     * @param personId 人员id
     * @return java.util.Set<java.lang.String>
     * @author 杨占锐
     * @date 2023/11/2 13:53
     */
    Set<String> listPermIds(String personId);

    /**
     * 查询当前登录人的所有权限部门id，以及权限部门id的父级id
     * <pre>
     *     1. 查询当前登录人的部门
     *     2. 判断部门类型，是企业还是政府
     *     3. 企业则执行企业的查询方法
     *     4. 政府则执行政府的查询方法
     * </pre>
     * @param permQuery 当前登录人id、部门类型
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author 杨占锐
     * @date 2023/11/1 17:00
     */
    List<TreeNode> treePerm(TpTreePermQuery permQuery);

    /**
     * 判断是否具有权限
     *
     * @param personId 人员id
     * @param deptId   部门id
     * @return 有权限返回true
     * @author 杨占锐
     * @date 2023/11/2 13:09
     */
    boolean isExistsDataPermissions(String personId, String deptId);

    /**
     * 查询当前人员部门本级及下级所有数据权限
     *
     * @param personId 人员id
     * @param deptId   部门id
     * @return java.util.Set<java.lang.String>
     * @author 杨占锐
     * @date 2023/11/7 20:20
     */
    Set<String> listAllDeptDataPermissions(String personId, String deptId);
}
