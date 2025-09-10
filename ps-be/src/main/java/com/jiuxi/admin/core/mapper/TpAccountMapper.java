package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpAccount;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpAccountMapper
 * @Description: 账户表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpAccountMapper {

    int save(TpAccount bean);

    TpAccountVO viewByPersonId(String personId);

    TpAccount selectByUsername(String username);

    TpAccountVO selectByAccountId(String accountId);

    int update(TpAccount bean);

    List<TpAccount> selectByPhone(String phone);

    /**
     * TAG：后续留着备用
     * 根据用户id更新账号
     *
     * @param bean:
     * @return int
     * @author pand
     * @date 2020-12-04 14:56
     */
    int updateByPersonId(TpAccount bean);

    /**
     * 根据人员id，删除账号
     *
     * @param personId
     * @param updateTime
     * @param username   账号，删除时需要添加删除时间
     * @return int
     * @author 杨占锐
     * @date 2024/5/20 10:56
     */
    int deleteByPersonId(@Param("personId") String personId, @Param("updateTime") String updateTime, @Param("username") String username, @Param("phone") String phone);

    /**
     * 根据手机号查询账户信息
     * @author 杨攀
     * @date 2024/5/27 13:44
     * @param phone
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     */
    TpAccountVO getTpAccountByPhone(@Param("phone") String phone);

    /**
     * 根据用户名查询账号信息
     *
     * @param username 用户名
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     * @author 杨占锐
     * @date 2024/5/29 9:15
     */
    TpAccountVO getTpAccountByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询账户信息
     *
     * @param email 邮箱地址
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     * @author system
     * @date 2024-01-21
     */
    TpAccountVO getTpAccountByEmail(@Param("email") String email);

    /**
     * 根据邮箱查询账户列表（用于找回密码）
     *
     * @param email 邮箱地址
     * @return List<TpAccount>
     * @author system
     * @date 2024-01-21
     */
    List<TpAccount> selectByEmail(String email);

    /**
     * 更新账号的Keycloak ID
     *
     * @param accountId 账号ID
     * @param keycloakId Keycloak用户ID
     * @return 更新行数
     * @author system
     * @date 2024-01-21
     */
    int updateKeycloakId(@Param("accountId") String accountId, @Param("keycloakId") String keycloakId);

}
