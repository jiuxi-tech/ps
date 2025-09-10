package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpDeptExinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonDeptVO;
import com.jiuxi.common.bean.TreeNode;

import java.util.List;

/**
 * @ClassName: TpDeptBasicinfoService
 * @Description: 单位/部门/网格/其他基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpDeptBasicinfoService {

    /**
     * 人员赋予兼职部门，查询部门树
     *
     * @param personId
     * @param deptId
     * @return
     */
    List<TreeNode> parttimeJobTree(String personId, String deptId, int category);

    /**
     * 机构树
     *
     * @param deptId
     * @return com.jiuxi.common.bean.TreeNode
     * @author 杨攀
     * @date 2020/11/20 11:29
     */
    List<TreeNode> tree(String deptId, String jwtdid, int sync, int category);

    /**
     * 查询下一级单位或部门，如果deptType为空，下一级单位和部门都返回。
     *
     * @param parentId 上级id
     * @param deptType 查询下级的类型
     * @param filterCommAscn 过滤委员会  1：过滤
     * @return com.jiuxi.common.bean.TreeNode
     * @author pand
     * @date 2023/11/20 11:29
     */
    List<TreeNode> getChildren(String parentId, String deptType, String filterCommAscn, Integer category);

    /**
     * 当前登陆人所在单位的行政区划树 本级及下级树
     *
     * @param jwtaid 当前登陆人所在部门id
     * @param sync   是否同步
     * @param expand 是否展开
     * @return com.jiuxi.common.bean.TreeNode
     * @author 杨攀
     * @date 2020/11/20 11:29
     */
    TreeNode deptCityTree(String jwtaid, int sync, boolean expand);

    /**
     * 添加根/树节点
     *
     * @param vo:       部门信息
     * @param pid:      当前登陆人id
     * @param category: 0政府 1企业 2其他
     * @return int
     * @author pand
     * @date 2020-11-23 16:23
     */
    TpDeptBasicinfoVO add(TpDeptBasicinfoVO vo, String pid, int category);

    /**
     * 添加部门扩展信息
     *
     * @param vo: 部门扩展信息
     * @author pand
     * @date 2020-11-23 16:23
     */
    TpDeptExinfoVO expAdd(TpDeptExinfoVO vo);

    /**
     * 内部接口，查看部门/机构基本信息，由于没有校验权限，不建议直接被调用
     *
     * @param deptId: 部门id
     * @return com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO
     * @author pand
     * @date 2020-11-23 16:55
     */
    TpDeptBasicinfoVO view(String deptId);

    /**
     * 根据部门id查看部门详情
     *
     * @param deptId: 部门id
     * @param jwtaid: 当前登陆人所在单位id
     * @return com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO
     * @author pand
     * @date 2020-11-23 16:56
     */
    TpDeptBasicinfoVO getById(String deptId, String jwtpid, String jwtaid);


    /**
     * 根据部门id查看部门扩展详情
     *
     * @param deptId: 部门id
     * @return com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO
     * @author pand
     * @date 2020-11-23 16:56
     */
    TpDeptExinfoVO expView(String deptId);

    /**
     * 修改部门基本信息
     *
     * @param vo:  部门基本信息
     * @param pid: 操作人id
     * @author pand
     * @date 2020-11-23 17:19
     */
    TpDeptBasicinfoVO update(TpDeptBasicinfoVO vo, String pid);

    /**
     * 根据部门id，查询部门下绑定的人员
     *
     * @param deptId:
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpPersonDeptVO>
     * @author pand
     * @date 2021-01-14 15:56
     */
    List<TpPersonDeptVO> selectBindByDeptId(String deptId);

    /**
     * 根据部门id删除部门信息
     *
     * @param id: 部门id
     * @return int
     * @author pand
     * @date 2020-11-23 20:32
     */
    int removeById(String id, String jwtpid);

    /**
     * 根据部门id获取条线code
     *
     * @param deptId
     * @return
     */
    String getLineCode(String deptId);

    /**
     * 根据单位id，删除部门
     *
     * @param ascnId 单位id
     * @param jwtpid 操作人
     * @return void
     * @author 杨占锐
     * @date 2024/5/30 10:09
     */
    void deleteDeptByAscnId(String ascnId, String jwtpid);

    /**
     * 获取完整的组织机构树（包含所有节点）
     * 用于记录变更历史时保存完整的组织架构快照
     *
     * @param rootId 根节点ID，如果为空则从顶级节点开始
     * @param category 类别：0政府 1企业 2其他
     * @return 完整的组织机构树结构
     * @author 系统生成
     * @date 2025-01-27
     */
    List<TreeNode> getFullTree(String rootId, int category);
}

