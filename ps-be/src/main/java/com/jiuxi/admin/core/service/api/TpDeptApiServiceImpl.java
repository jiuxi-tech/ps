package com.jiuxi.admin.core.service.api;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
// import com.jiuxi.plugin.api.bean.dto.TpDeptBasicinfoDTO;
// import com.jiuxi.plugin.api.interfaces.TpDeptApiService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: TpDeptApiServiceImpl
 * @Description: 部门 api 实现
 * @Author: 杨攀
 * @Date: 2023/11/15 15:01
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
@Service("tpDeptApiService")
public class TpDeptApiServiceImpl /* implements TpDeptApiService */ {

    @Autowired
    private TpDeptBasicinfoService tpDeptBasicinfoService;

    /**
     * 根据部门id获取部门全称
     *
     * @param deptId
     * @return java.lang.String
     * @author 杨攀
     * @date 2023/11/15 14:00
     */
    public String getFullNameById(String deptId) {
        TpDeptBasicinfoVO basicinfoVO = tpDeptBasicinfoService.view(deptId);
        if (null == basicinfoVO) {
            return null;
        }
        return basicinfoVO.getDeptFullName();
    }

    /**
     * 根据部门id获取部门简称
     *
     * @param deptId
     * @return java.lang.String
     * @author 杨攀
     * @date 2023/11/15 14:00
     */
    public String getSimpleNameById(String deptId) {
        TpDeptBasicinfoVO basicinfoVO = tpDeptBasicinfoService.view(deptId);
        if (null == basicinfoVO) {
            return null;
        }
        return basicinfoVO.getDeptSimpleName();
    }

    /**
     * 部门层级编码
     *
     * @param deptId
     * @return java.lang.String
     * @author 杨攀
     * @date 2023/11/15 14:02
     */
    public String getDeptLevelcodeById(String deptId) {
        TpDeptBasicinfoVO basicinfoVO = tpDeptBasicinfoService.view(deptId);
        if (null == basicinfoVO) {
            return null;
        }
        return basicinfoVO.getDeptLevelcode();
    }

    /**
     * 获取部门条线
     *
     * @param deptId
     * @return java.lang.String
     * @author 杨攀
     * @date 2023/11/15 16:36
     */
    public String getLineCode(String deptId) {
        return tpDeptBasicinfoService.getLineCode(deptId);
    }

    /**
     * 根据上级单位id查询下级单位和当前查询单位的部门
     * @author 杨攀
     * @date 2023/11/17 17:11
     * @param parentId 上级id
     * @param deptType SYS05 部门类型 单位or部门or网格or其他，其他是为了满足公众等没有部门的用户分类使用，归为政府编制下
     *  @param filterCommAscn 过滤委员会  1：过滤
     * @return java.util.List<T>
     */
    public List<TreeNode> getChildren(String parentId, String deptType, String filterCommAscn) {
        return tpDeptBasicinfoService.getChildren(parentId, deptType, filterCommAscn, 0);
    }

    /**
     * 新增部门
     *
     * @param dto  部门信息
     * @param pid 登录人id
     * @return com.jiuxi.plugin.api.bean.dto.TpDeptBasicinfoDTO
     * @author 杨占锐
     * @date 2024/5/28 9:39
     */
    /*
    @Override
    public TpDeptBasicinfoDTO add(TpDeptBasicinfoDTO dto, String pid) {
        // 校验数据
        this.validateAdd(dto, pid);
        TpDeptBasicinfoVO basicinfoVO = new TpDeptBasicinfoVO();
        BeanUtils.copyProperties(dto, basicinfoVO);
        TpDeptBasicinfoVO result = tpDeptBasicinfoService.add(basicinfoVO, pid, basicinfoVO.getCategory());
        BeanUtils.copyProperties(result, dto);
        return dto;
    }
    */

    /**
     * 校验部门信息
     * <pre>
     *     1. 部门全称
     *     2. 部门简称
     *     3. 部门类型 SYS0501：单位， SYS0502： 部门
     *     4. 部门类别 0政府 1企业 2其他
     *     5. 部门类型 != SYS0501时，需要传单位id: ascnId
     *     6. 创建人
     * </pre>
     * @author 杨占锐
     * @date 2024/5/28 10:38
     * @throws TopinfoRuntimeException 校验失败抛出异常
     */
    /*
    private void validateAdd(TpDeptBasicinfoDTO dto, String pid) throws TopinfoRuntimeException {
        Validate.notBlank(dto.getDeptFullName(), "部门全称不能为空！");
        Validate.notBlank(dto.getDeptSimpleName(), "部门简称不能为空！");
        Validate.notBlank(dto.getDeptType(), "部门类型不能为空！");
        Validate.notNull(dto.getCategory(), "部门类别不能为空！");
        Validate.notBlank(pid, "创建人不能为空！");

        // 部门类型 != SYS0501时，需要传单位id: ascnId
        if (!TpConstant.DEPT_TYPE.equals(dto.getDeptType()) && StrUtil.isBlank(dto.getAscnId())){
            throw new TopinfoRuntimeException(-1, "单位id为空");
        }
    }
    */

    /**
     * 修改部门
     * <pre>
     *     必填字段：
     *         1. 部门id： deptId
     *         2. 父级部门id： deptId
     *     不能修改的字段：
     *         1. deptLevelcode
     *         2. category
     *         3. ascnId
     *         4. creator
     *         5. createTime
     *     处理逻辑
     *         1. 查询原数据
     *         2. 如果部门类型发生改变
     *             2.1 查询本单位下面所有的部门 list
     *             2.2. “部门”转换为“单位”
     *               1. 新单位id = deptId
     *             2.2. “单位”转换为“部门”
     *               1. 如果当前单位为根节点，则不允许修改
     *               2. 查询父级部门 pdeptInfo
     *               3. 新单位id = pdeptInfo.ascnId
     *             3.3 更新 list中所有单位id
     *             3.4 更新 人员表中的单位id
     *         3. 更显数据库
     *         4. 发布TpDeptBasicinfoNewEvent事件
     * </pre>
     * @param dto  部门信息
     * @param pid  登录人id
     * @return com.jiuxi.plugin.api.bean.dto.TpDeptBasicinfoDTO
     * @author 杨占锐
     * @date 2024/5/28 10:50
     */
    /*
    @Override
    public TpDeptBasicinfoDTO update(TpDeptBasicinfoDTO dto, String pid) {
        // 校验数据
        this.validateUpdate(dto, pid);

        TpDeptBasicinfoVO basicinfoVO = new TpDeptBasicinfoVO();
        BeanUtils.copyProperties(dto, basicinfoVO);
        TpDeptBasicinfoVO result = tpDeptBasicinfoService.update(basicinfoVO, pid);
        BeanUtils.copyProperties(result, dto);

        return dto;
    }
    */

    /**
     * 校验必要的字段
     *
     * @return void
     * @author 杨占锐
     * @date 2024/5/28 13:17
     */
    /*
    private void validateUpdate(TpDeptBasicinfoDTO dto, String pid) {

        Validate.notBlank(dto.getDeptId(), "部门id不能为空！");
        Validate.notBlank(dto.getPdeptId(), "部门父id不能为空！");
        Validate.notBlank(pid, "操作人不能为空！");
    }
    */

    /**
     * 根据部门id，删除部门信息
     *
     * <pre>
     *     操作逻辑
     *     1. 存在子级部门，不能删除！
     *     2. 部门下有挂职人员，不能删除！
     *     3. 逻辑删除部门，deptLevelCode 增加删除时间
     *     4. 发布事件 TpDeptBasicinfoNewEvent
     * </pre>
     * @param deptId 部门id
     * @param jwtpid    操作人id
     * @return 修改的行数
     * @author 杨占锐
     * @date 2024/5/28 13:28
     */
    /*
    @Override
    public int delete(String deptId, String jwtpid) {
        return tpDeptBasicinfoService.removeById(deptId, jwtpid);
    }
    */

    /**
     * 根据单位id，删除部门
     *
     * @param ascnId 单位id
     * @param jwtpid 操作人
     * @return void
     * @author 杨占锐
     * @date 2024/5/30 10:09
     */
    /*
    @Override
    public void deleteDeptByAscnId(String ascnId, String jwtpid) {
        tpDeptBasicinfoService.deleteDeptByAscnId(ascnId, jwtpid);
    }
    */
}
