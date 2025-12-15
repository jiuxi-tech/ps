package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpDataPermissions;
import com.jiuxi.admin.core.bean.query.TpTreePermQuery;
import com.jiuxi.admin.core.bean.vo.TpDataPermissionsVO;
import com.jiuxi.common.bean.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: TpDataPermissionsMapper
 * @Description:
 * @Author jiuxx
 * @Date 2023/11/1 15:50
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpDataPermissionsMapper {

    /**
     *  根据人员id，删除所有权限
     *
     * @param personId 人员id
     * @return void
     * @author jiuxx
     * @date 2023/11/1 15:53
     */
    void deleteByPersonId(@Param("personId") String personId);

    /**
     * 批量保存数据权限
     *
     * @author jiuxx
     * @date 2023/11/1 15:59
     */
    void batchAdd(@Param("list") List<TpDataPermissions> list);

    /**
     * 查询所有数据权限
     *
     * @param personId 人员id
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpDataPermissionsVO>
     * @author jiuxx
     * @date 2023/11/2 13:59
     */
    List<TpDataPermissionsVO> listPerm(@Param("personId") String personId);

    /**
     * 查询所有数据权限的部门id
     *
     * @param personId 人员id
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/2 13:53
     */
    Set<String> listPermIds(@Param("personId") String personId);

    /**
     * 查询数据权限-部门信息
     *
     * @param permQuery 人员id、部门类型
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author jiuxx
     * @date 2023/11/1 20:46
     */
    List<TreeNode> treeSimplePerm(@Param("query") TpTreePermQuery permQuery);

    /**
     * 根据层级编码和单位id，查询出所有部门信息
     *
     * @param deptLevelCodes 部门层级编码
     * @param ascnIds        单位id
     * @param permIds        具有数据权限的部门id
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author jiuxx
     * @date 2023/11/1 20:45
     */
    List<TreeNode> listByDeptLevelCodes(@Param("list") Set<String> deptLevelCodes, @Param("ascnIds") Set<String> ascnIds, @Param("permIds") Set<String> permIds);

    /**
     * 判断数据是否存在
     *
     * @param personId 人员id
     * @param deptId   部门id
     * @return 人员id
     * @author jiuxx
     * @date 2023/11/2 13:12
     */
    String isExistsDataPermissions(@Param("personId") String personId, @Param("deptId") String deptId);

    /**
     * 查询当前人员部门本级及下级所有数据权限
     *
     * @param personId       人员id
     * @param deptLevelcode  部门层级编码
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/7 20:20
     */
    Set<String> listAllDeptDataPermissions(@Param("personId") String personId, @Param("deptLevelcode") String deptLevelcode);

}
