package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpAccount;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账号Mapper适配器 - 为了兼容旧代码
 * 实际实现将委托给新的用户模块Mapper
 * 
 * @deprecated 请使用 com.jiuxi.module.user.infra.persistence.mapper.UserAccountMapper
 */
@Deprecated
@Mapper
public interface TpAccountMapper {

    int insert(TpAccount record);
    
    int updateByPrimaryKey(TpAccount record);
    
    int deleteByPrimaryKey(String accountId);
    
    TpAccount selectByPrimaryKey(String accountId);
    
    TpAccount selectByPersonId(String personId);
    
    TpAccount selectByUsername(String username);
    
    List<TpAccount> selectByCondition(TpAccountVO condition);
    
    int updatePassword(@Param("accountId") String accountId, @Param("password") String password);
    
    int updateStatus(@Param("accountId") String accountId, @Param("enabled") Integer enabled);
    
    int updateLockStatus(@Param("accountId") String accountId, @Param("locked") Integer locked);
}