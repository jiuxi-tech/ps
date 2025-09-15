package com.jiuxi.module.org.infra.persistence.mapper;

import com.jiuxi.admin.core.bean.entity.TpDeptBasicinfo;
import com.jiuxi.admin.core.bean.entity.TpPersonDept;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.common.bean.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: OrganizationDepartmentMapper
 * @Description: 组织部门映射接口
 * @Author DDD重构
 * @Date 2025-09-12
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface OrganizationDepartmentMapper {

    /**
     * 根据部门层级code查询部门所有的下级部门列表
     *
     * @param levelCode 层级code
     * @param category  部门类别
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO>
     * @author 杨攀
     * @date 2020/11/20 14:37
     */
    List<TreeNode> selectDeptListByLevelCode(@Param("levelCode") String levelCode, @Param("category") int category);

    /**
     * 根据父部门ID Pid查询下级部门列表信息
     *
     * @param deptId
     * @param category 部门类别
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO>
     * @author 杨攀
     * @date 2020/11/20 14:57
     */
    List<TpDeptBasicinfoVO> selectDeptListByPId(@Param("deptId") String deptId, @Param("category") int category);

    /**
     * 根据部门ID查询部门信息，与view方法相比，查询字段较少。
     *
     * @param deptId
     * @return com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO
     * @author 杨攀
     * @date 2020/11/20 15:32
     */
    TpDeptBasicinfoVO selectDeptById(String deptId);

    /**
     * 根据单位id查询机构所在行政区划code
     *
     * @param ascnId:
     * @return java.lang.String
     * @author pand
     * @date 2021-02-03 10:47
     */
    String selectCityCodeByAscnId(String ascnId);

    List<TreeNode> selectChildren(@Param("deptId") String deptId, @Param("category") int category);

    /**
     * 机构类型发生变化时，
     * 根据层级code和单位id查询下级部门
     *
     * @param deptLevelcode
     * @param ascnId
     * @return
     */
    List<String> selectByDeptLevelcodeAndAscnId(@Param("deptLevelcode") String deptLevelcode, @Param("ascnId") String ascnId);

    int deleteByDeptId(@Param("deptId") String deptId, @Param("updateTime") String updateTime, @Param("updator") String updator, @Param("deptLevelcode") String deptLevelcode);

    int save(TpDeptBasicinfo bean);

    /**
     * 查询字段很多
     *
     * @param deptId
     * @return
     */
    TpDeptBasicinfoVO view(String deptId);

    int update(TpDeptBasicinfo bean);

    /**
     * 批量更新机构的单位id
     *
     * @param deptIds
     * @param ascnId
     * @return
     */
    int updateAscnIdByDeptIds(@Param("deptIds") List<String> deptIds, @Param("ascnId") String ascnId);

    int findChildren(String deptId);

    List<TpPersonDept> findPersonDeptById(@Param("deptId") String deptId, @Param("personId") String personId);

    List<TpDeptBasicinfoVO> personDepts(String personId);

    /**
     * 查询下一级单位或部门，如果deptType为空，下一级单位和部门都返回。
     *
     * @param parentId 上级id
     * @param deptType 查询下级的类型
     * @param filterCommAscn 过滤委员会  1：过滤
     * @param category 部门类型  0：政府， 1：企业
     * @return com.jiuxi.common.bean.TreeNode
     * @author pand
     * @date 2023/11/20 11:29
     */
    List<TreeNode> getChildren(@Param("parentId") String parentId, @Param("deptType") String deptType, @Param("filterCommAscn") String filterCommAscn, @Param("category") Integer category);

    /**
     * 根据企业id，查询所有部门id
     *
     * @param ascnId
     * @return java.util.List<java.lang.String>
     * @author 杨占锐
     * @date 2024/5/30 10:03
     */
    List<TpDeptBasicinfoVO> listByEntId(String ascnId);

    /**
     * 根据部门名称查询部门信息
     *
     * @param deptName 部门名称
     * @return TpDeptBasicinfoVO
     * @author Trae AI
     * @date 2024/12/19
     */
    TpDeptBasicinfoVO selectDeptByName(String deptName);

    // Additional methods for compatibility and DDD pattern
    TpDeptBasicinfoVO selectTpDeptBasicinfoVOByDeptId(String deptId);
    TpDeptBasicinfo selectByDeptId(String deptId);
    TpDeptBasicinfoVO selectTpDeptBasicinfoVOByDeptIdAndCategory(String deptId, int category);
    int removeById(String id);
    List<TpPersonDept> selectBindByDeptId(String deptId);
    String getLineCode(String deptId);
    List<TpDeptBasicinfoVO> selectDeptByAscnId(String ascnId);
    int deleteDeptByAscnId(String ascnId, String updateTime);
    List<TreeNode> selectCompleteTree(String rootId, int category);

}