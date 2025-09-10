package com.jiuxi.admin.core.service.api;

import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.service.TpPersonBasicinfoService;
// import com.jiuxi.plugin.api.bean.dto.TpPersonBasicinfoDTO;
// import com.jiuxi.plugin.api.interfaces.TpPersonApiService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: TpPersonApiServiceImpl
 * @Description: 人员 api
 * @Author: 杨攀
 * @Date: 2023/11/15 16:23
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
@Service("tpPersonApiService")
public class TpPersonApiServiceImpl /* implements TpPersonApiService */ {

    @Autowired
    private TpPersonBasicinfoService tpPersonBasicinfoService;

    @Autowired
    private TpApiCommonService tpApiCommonService;

    /**
     * 根据人员id 获取人员基本信息
     *
     * @param personId 人员id
     * @param clazz    返回的目标对象类型
     * @return T
     * @author 杨攀
     * @date 2023/11/15 17:13
     */
    public <T> T getBaseInfo(String personId, Class<T> clazz) {

        TpPersonBasicinfoVO vo = tpPersonBasicinfoService.getPersonBasicinfo(personId);

        return tpApiCommonService.copy(vo, clazz);
    }

    /**
     * 根据人员id 获取人员名称
     *
     * @param personId
     * @return java.lang.String
     * @author 杨攀
     * @date 2023/11/15 14:51
     */
    public String getPersonName(String personId) {
        TpPersonBasicinfoVO basicinfoVO = tpPersonBasicinfoService.getPersonBasicinfo(personId);
        if (null == basicinfoVO) {
            return null;
        }
        return basicinfoVO.getPersonName();
    }

    /**
     * 根据身份证号查询人员信息
     *
     * @param idcard 身份证号
     * @param clazz
     * @return T
     * @author 杨占锐
     * @date 2024/5/29 13:10
     */
    public <T> T getBaseInfoByIdCard(String idcard, Class<T> clazz) {
        TpPersonBasicinfoVO vo = tpPersonBasicinfoService.getBaseInfoByIdCard(idcard);
        return tpApiCommonService.copy(vo, clazz);
    }

    /**
     * 校验手机号是否存在
     *
     * @param phone    手机号
     * @param personId 人员id（修改校验时必填）
     * @return boolean 存在返回 true, 不存在返回 false
     * @author 杨占锐
     * @date 2024/5/30 15:49
     */
    /*
    @Override
    public boolean existsPhone(String phone, String personId) {

        return tpPersonBasicinfoService.existsPhone(phone, personId);
    }
    */

    /**
     * 新增人员
     *
     * <pre>
     *     数据校验：
     *         1. 人员名称不能为空
     *         2. 部门id不能为空
     *         3. 单位id不能为空
     *         4. 人员类型不能为空
     *         5. 操作人id不能为空
     *         6. 如果手机号不为空，则不能重复
     *     处理逻辑：
     *         1. 校验
     *         2. 新增人员信息
     *         3. 新增人员部门绑定信息
     *         4. 发布事件
     * </pre>
     *
     * @param person 人员信息
     * @param jwtpid 当前操作人
     * @return 人员id
     * @author 杨占锐
     * @date 2024/5/30 16:33
     */
    /*
    @Override
    public String add(TpPersonBasicinfoDTO person, String jwtpid) {
        this.validateAdd(person, jwtpid);
        TpPersonBasicinfoVO vo = tpApiCommonService.copy(person, TpPersonBasicinfoVO.class);
        TpPersonBasicinfoVO result = tpPersonBasicinfoService.add(vo, jwtpid, vo.getCategory());
        return result.getPersonId();
    }
    */

    /**
     * 新增时数据校验
     *
     * @param person 人员信息
     * @return void
     * @author 杨占锐
     * @date 2024/5/30 16:34
     */
    /*
    private void validateAdd(TpPersonBasicinfoDTO person, String jwtpid) {

        Validate.notBlank(person.getPersonName(), "人员名称不能为空！");
        Validate.notBlank(person.getDeptId(), "部门id不能为空！");
        Validate.notBlank(person.getAscnId(), "单位id不能为空！");
        Validate.notNull(person.getCategory(), "人员类型不能为空！");
        Validate.notBlank(jwtpid, "操作人id不能为空！");
    }
    */

    /**
     * 修改人员信息
     * <pre>
     *     数据校验：
     *         1. 人员id不能为空
     *         2. 操作人id不能为空
     *         3. 手机号不能重复
     *         4. 手机号发生变化时，修改账号表中的手机号。手机号不能重复
     *     处理逻辑
     *         1. 校验
     *         2. 如果部门发生变化，修改部门关联关系
     *         3. 如果手机号发生变化，修改账号表中的手机号
     *         4. 更新人员信息
     *         5. 发布事件
     * </pre>
     *
     * @param person 人员信息
     * @param jwtpid 操作人id
     * @return int
     * @author 杨占锐
     * @date 2024/5/30 17:10
     */
    /*
    public int update(TpPersonBasicinfoDTO person, String jwtpid) {
        // 修改时数据校验
        this.validateUpdate(person, jwtpid);

        TpPersonBasicinfoVO vo = tpApiCommonService.copy(person, TpPersonBasicinfoVO.class);
        return tpPersonBasicinfoService.update(vo, jwtpid);
    }
    */

    /**
     * 修改时数据校验
     *
     * @param person 人员信息
     * @return void
     * @author 杨占锐
     * @date 2024/5/30 16:34
     */
    /*
    private void validateUpdate(TpPersonBasicinfoDTO person, String jwtpid) {
        Validate.notBlank(person.getPersonId(), "人员id不能为空！");
        Validate.notBlank(jwtpid, "操作人id不能为空！");
    }
    */

    /**
     * 删除人员（可以是兼职人）
     *
     * <pre>
     *     处理逻辑
     *         1. 校验
     *         2. 不能删除主部门中自己, 可以删除兼职部门中自己
     *         3. 如果删除的是主部门
     *             3.1 删除账号
     *             3.2 删除人员
     *         4. 删除用户，部门关联关系
     *         5. 删除用户，角色关联关系
     *         6. 发布事件
     * </pre>
     *
     * @param deptId   部门id
     * @param personId 人员id
     * @param jwtpid   操作人id
     * @return void
     * @author 杨占锐
     * @date 2024/5/30 17:20
     */
    public void delete(String deptId, String personId, String jwtpid) {
        Validate.notBlank(deptId, "部门id不能为空！");
        Validate.notBlank(personId, "人员id不能为空！");
        Validate.notBlank(jwtpid, "操作人id不能为空！");

        tpPersonBasicinfoService.deletes(deptId, personId, jwtpid);
    }

    /**
     * 人员授权
     *
     * <pre>
     *     必填字段：
     *         1. 人员id
     *         2. 部门id
     *     处理逻辑
     *         1. 校验参数
     *         2. 根据 人员id + 部门id 删除角色
     *         3. 新增角色关系
     * </pre>
     * @param personId 人员id
     * @param deptId   部门id
     * @param roleIds  角色id，多个使用逗号分割
     * @return void
     * @author 杨占锐
     * @date 2024/5/30 18:04
     */
    public void auth(String personId, String deptId, String roleIds) {
        tpPersonBasicinfoService.auth(personId, deptId, roleIds);
    }

    /**
     * 查询出在该单位所有部门下的所有主部门关联关系的人员id
     *
     * @param ascnId 单位id
     * @return java.util.List<java.lang.String>
     * @author 杨占锐
     * @date 2024/6/4 19:12
     */
    /*
    @Override
    public List<String> selectPersonIdByAscnId(String ascnId) {
        return tpPersonBasicinfoService.selectPersonIdByAscnId(ascnId);
    }
    */

    /**
     * 根据企业id删除人员
     *
     * @param ascnId 单位id
     * @param jwtpid 操作人id
     * @return void
     * @author 杨占锐
     * @date 2024/6/4 19:24
     */
    /*
    @Override
    public void deletePersonDeptByAscnId(String ascnId, String jwtpid) {
        tpPersonBasicinfoService.deletePersonDeptByAscnId(ascnId, jwtpid);
    }
    */

    /**
     * 修改手机号
     *
     * @param personId 人员id
     * @param phone    手机号
     * @param jwtpid   操作人id
     * @return int
     * @author 杨占锐
     * @date 2024/5/30 17:10
     */
    /*
    @Override
    public void updatePhone(String personId, String phone, String jwtpid) {
        Validate.notBlank(personId, "人员id不能为空！");
        Validate.notBlank(phone, "手机号不能为空！");
        Validate.notBlank(jwtpid, "操作人id不能为空！");

        tpPersonBasicinfoService.updatePhone(personId, phone, jwtpid);
    }
    */
}
