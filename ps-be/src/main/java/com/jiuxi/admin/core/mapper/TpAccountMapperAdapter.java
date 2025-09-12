package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpAccount;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.module.user.infra.persistence.mapper.UserAccountMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 账号Mapper适配器实现 - 委托给新的用户模块Mapper
 * 
 * @deprecated 为了兼容旧代码而保留
 */
@Deprecated
@Component
public class TpAccountMapperAdapter implements TpAccountMapper {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Override
    public int insert(TpAccount record) {
        return userAccountMapper.insert(record);
    }

    @Override
    public int updateByPrimaryKey(TpAccount record) {
        return userAccountMapper.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByPrimaryKey(String accountId) {
        return userAccountMapper.deleteByPrimaryKey(accountId);
    }

    @Override
    public TpAccount selectByPrimaryKey(String accountId) {
        return userAccountMapper.selectByPrimaryKey(accountId);
    }

    @Override
    public TpAccount selectByPersonId(String personId) {
        return userAccountMapper.selectByPersonId(personId);
    }

    @Override
    public TpAccount selectByUsername(String username) {
        return userAccountMapper.selectByUsername(username);
    }

    @Override
    public List<TpAccount> selectByCondition(TpAccountVO condition) {
        return userAccountMapper.selectByCondition(condition);
    }

    @Override
    public int updatePassword(@Param("accountId") String accountId, @Param("password") String password) {
        return userAccountMapper.updatePassword(accountId, password);
    }

    @Override
    public int updateStatus(@Param("accountId") String accountId, @Param("enabled") Integer enabled) {
        return userAccountMapper.updateStatus(accountId, enabled);
    }

    @Override
    public int updateLockStatus(@Param("accountId") String accountId, @Param("locked") Integer locked) {
        return userAccountMapper.updateLockStatus(accountId, locked);
    }
}