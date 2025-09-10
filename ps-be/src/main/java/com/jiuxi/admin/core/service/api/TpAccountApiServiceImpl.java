package com.jiuxi.admin.core.service.api;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.core.bean.TopinfoRuntimeException;
// import com.jiuxi.plugin.api.bean.dto.TpAccountDTO;
// import com.jiuxi.plugin.api.interfaces.TpAccountApiService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: TpAccountApiServiceImpl
 * @Description: 账户 api
 * @Author: 杨攀
 * @Date: 2024/5/27 13:23
 * @Copyright: 2024 www.tuxun.net Inc. All rights reserved.
 */
@Service("tpAccountApiServiceImpl")
public class TpAccountApiServiceImpl /* implements TpAccountApiService */ {

    @Autowired
    private TpAccountService tpAccountService;

    @Autowired
    private TpApiCommonService tpApiCommonService;


    /**
     * 根据手机号查询账户信息
     *
     * @param phone 手机号
     * @param clazz
     * @return T
     * @author 杨攀
     * @date 2024/5/27 13:47
     */
    public <T> T getTpAccountByPhone(String phone, Class<T> clazz) {

        TpAccountVO vo = tpAccountService.getTpAccountByPhone(phone);

        return tpApiCommonService.copy(vo, clazz);
    }

    /**
     * 根据手机号查询账户信息
     *
     * @param phone 手机号
     * @return T
     * @author 杨攀
     * @date 2024/5/27 13:47
     */
    public Object getTpAccountByPhone(String phone) {
        // return getTpAccountByPhone(phone, TpAccountDTO.class); // Commented out - TpAccountDTO not available
        return null;
    }

    /**
     * 根据用户名查询账户信息
     *
     * @param username 用户名
     * @param clazz
     * @return T
     * @author 杨占锐
     * @date 2024/5/29 8:51
     */
    public <T> T getTpAccountByUsername(String username, Class<T> clazz) {
        TpAccountVO vo = tpAccountService.getTpAccountByUsername(username);
        return tpApiCommonService.copy(vo, clazz);
    }

    /**
     * 根据用户名查询账户信息
     *
     * @param username 用户名
     * @return T
     * @author 杨占锐
     * @date 2024/5/29 8:51
     */
    public Object getTpAccountByUsername(String username) {
        // return getTpAccountByUsername(username, TpAccountDTO.class); // Commented out - TpAccountDTO not available
        return null;
    }

    /**
     * 根据人员id查询账户信息
     *
     * @param personId 人员id
     * @param clazz
     * @return T
     * @author 杨占锐
     * @date 2024/5/29 8:51
     */
    public <T> T getTpAccountByPersonId(String personId, Class<T> clazz) {
        TpAccountVO vo = tpAccountService.accountView(personId);
        return tpApiCommonService.copy(vo, clazz);
    }

    /**
     * 根据人员id查询账户信息
     *
     * @param personId 人员id
     * @return T
     * @author 杨占锐
     * @date 2024/5/29 8:51
     */
    public Object getTpAccountByPersonId(String personId) {
        // return getTpAccountByPersonId(personId, TpAccountDTO.class); // Commented out - TpAccountDTO not available
        return null;
    }

    /**
     * 根据人员id查询账户信息
     *
     * @param accountId 账号id
     * @param clazz
     * @return T
     * @author 杨占锐
     * @date 2024/5/29 8:51
     */
    public <T> T getTpAccountByAccountId(String accountId, Class<T> clazz) {
        TpAccountVO vo = tpAccountService.selectByAccountId(accountId);
        return tpApiCommonService.copy(vo, clazz);
    }

    /**
     * 根据人员id查询账户信息
     *
     * @param accountId 账号id
     * @return T
     * @author 杨占锐
     * @date 2024/5/29 8:51
     */
    public Object getTpAccountByAccountId(String accountId) {
        // return getTpAccountByAccountId(accountId, TpAccountDTO.class); // Commented out - TpAccountDTO not available
        return null;
    }

    /**
     * 新增账号
     * <pre>
     *     必填字段
     *         1. username 用户名
     *         2. userpwd  密码，需要名文
     *         3. personId 人员id
     *     处理逻辑
     *         1. 校验参数是否为空
     *         2. 校验用户名是否重复
     *         3. 新增账号信息
     *         4. 发布事件
     *         5. 返回账号id
     * </pre>
     *
     * @param accountDTO 账号信息
     * @param jwtpid     操作人
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/5/29 9:50
     */
    public int addAccount(Object accountDTO, String jwtpid) {
        // 新增时校验参数
        // this.validateAdd(accountDTO, jwtpid); // Commented out - TpAccountDTO not available

        // TpAccountVO vo = new TpAccountVO();
        // BeanUtils.copyProperties(accountDTO, vo);

        // return tpAccountService.accountManage(vo, false); // Commented out - TpAccountDTO not available
        return 0;
    }


    /*
    @Override
    public int updateAccount(TpAccountDTO accountDTO, String jwtpid) {
        // 修改时校验参数
        this.validateUpdate(accountDTO, jwtpid);

        TpAccountVO vo = new TpAccountVO();
        BeanUtils.copyProperties(accountDTO, vo);

        return tpAccountService.accountManage(vo, false);
    }
    */

    /**
     * 根据人员id，删除账号信息
     *
     * @param personId 人员id
     * @return void
     * @author 杨占锐
     * @date 2024/5/29 11:08
     */
    public void deleteByPersonId(String personId, String jwtpid) {

        Validate.notBlank(jwtpid, "操作人id不能为空！");
        Validate.notBlank(personId, "人员id不能为空！");
        tpAccountService.deleteByPersonId(personId);
    }


    /**
     * 修改账号时校验
     *
     * @param accountDTO
     * @param jwtpid
     * @return void
     * @author 杨占锐
     * @date 2024/5/29 10:56
     */
    /*
    private void validateUpdate(TpAccountDTO accountDTO, String jwtpid) {
        Validate.notBlank(jwtpid, "操作人id不能为空！");
        Validate.notBlank(accountDTO.getAccountId(), "账号id不能为空！");
        Validate.notBlank(accountDTO.getPersonId(), "人员id不能为空！");
    }
    */

    /**
     * 新增时校验参数
     *
     * @param accountDTO
     * @return void
     * @author 杨占锐
     * @date 2024/5/29 10:26
     */
    /*
    private void validateAdd(TpAccountDTO accountDTO, String jwtpid) {

        Validate.notBlank(jwtpid, "操作人id不能为空！");
        Validate.notBlank(accountDTO.getUsername(), "用户名不能为空！");
        Validate.notBlank(accountDTO.getUserpwd(), "密码不能为空！");
        Validate.notBlank(accountDTO.getPersonId(), "人员不能为空！");

    }
    */
}
