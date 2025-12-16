package com.jiuxi.module.user.app.service;

/**
 * @Description: 人员和账号信息相关接口，接口可以被项目覆盖
 * @ClassName: PersonAccountService
 * @Author: pdd
 * @Date: 2021-06-18 14:23
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface PersonAccountApplicationService {

    /**
     * 根据用户名查询账号表用户名是否已存在，如果覆盖此方法，需要查询自己的业务表以及账号表，如小综合的临时注册表.
     *
     * @param userName: 账号表，用户名
     * @return true: 已经被占用     false：没有被占用
     * @author pand
     * @date 2021-06-18 14:28
     */
    boolean selectByUsername(String userName);

    /**
     * 根据用户名和账号id查询账号表用户名是否已存在。修改用户名操作，需要查询用户名 = userName并且账号id != accountId 的账号，如果已经存在需要提醒用户名已存在。
     *
     * @param userName:  账号表，用户名
     * @param accountId: 账号表，用户名
     * @return true: 已经被占用     false：没有被占用
     * @author pand
     * @date 2021-06-18 14:28
     */
    default boolean selectByUsernameAndAccountId(String userName, String accountId) {
        return false;
    }

    /**
     * 根据手机号查询人员表，手机号是否已被占用，如果覆盖此方法，需要查询自己的业务表以及人员表.
     *
     * @param phone: 人员表，手机号
     * @return true: 已经被占用     false：没有被占用
     * @author pand
     * @date 2021-06-18 14:28
     */
    boolean selectByPhone(String phone);

    /**
     * 修改手机号
     *
     * @param personId 人员id
     * @param phone    手机号
     * @return void
     * @author jiuxx
     * @date 2024/6/5 17:32
     */
    void updatePhone(String personId, String phone);

    /**
     * 修改身份证号
     *
     * @param personId 人员id
     * @param idCard   身份证号
     * @return void
     * @author Qoder AI
     * @date 2024/12/15
     */
    void updateIdCard(String personId, String idCard);
}
