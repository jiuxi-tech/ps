package com.jiuxi.module.user.infra.persistence.mapper;

import com.jiuxi.module.user.infra.persistence.entity.AccountPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * 账户持久化Mapper
 * 
 * @author DDD Refactor Phase 4.3.1.2
 * @date 2025-09-11
 */
@Mapper
public interface AccountMapper extends BaseMapper<AccountPO> {
    
    /**
     * 根据人员ID查找账户
     * @param personId 人员ID
     * @return 账户持久化对象
     */
    @Select("SELECT * FROM tp_account WHERE person_id = #{personId} AND actived = 1")
    Optional<AccountPO> findByPersonId(@Param("personId") String personId);
    
    /**
     * 根据用户名查找账户
     * @param username 用户名
     * @return 账户持久化对象
     */
    @Select("SELECT * FROM tp_account WHERE username = #{username} AND actived = 1")
    Optional<AccountPO> findByUsername(@Param("username") String username);
    
    /**
     * 根据Keycloak ID查找账户
     * @param keycloakId Keycloak ID
     * @return 账户持久化对象
     */
    @Select("SELECT * FROM tp_account WHERE extend01 = #{keycloakId} AND actived = 1")
    Optional<AccountPO> findByKeycloakId(@Param("keycloakId") String keycloakId);
    
    /**
     * 根据第三方ID查找账户
     * @param thirdId 第三方ID
     * @return 账户持久化对象
     */
    @Select("SELECT * FROM tp_account WHERE three_id = #{thirdId} AND actived = 1")
    Optional<AccountPO> findByThirdId(@Param("thirdId") String thirdId);
}