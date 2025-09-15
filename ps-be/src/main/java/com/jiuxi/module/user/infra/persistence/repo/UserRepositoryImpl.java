package com.jiuxi.module.user.infra.persistence.repo;

import com.jiuxi.module.user.domain.model.aggregate.User;
import com.jiuxi.module.user.domain.model.entity.UserAccount;
import com.jiuxi.module.user.domain.repo.UserRepository;
import com.jiuxi.module.user.infra.persistence.entity.UserPO;
import com.jiuxi.module.user.infra.persistence.entity.AccountPO;
import com.jiuxi.module.user.infra.persistence.mapper.UserMapper;
import com.jiuxi.module.user.infra.persistence.mapper.AccountMapper;
import com.jiuxi.module.user.infra.persistence.assembler.UserPOAssembler;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 用户仓储实现类
 * 负责用户聚合根的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;
    private final UserPOAssembler userPOAssembler;
    
    public UserRepositoryImpl(UserMapper userMapper, AccountMapper accountMapper, UserPOAssembler userPOAssembler) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.userPOAssembler = userPOAssembler;
    }
    
    @Override
    @Transactional
    public User save(User user) {
        UserPO userPO = userPOAssembler.toUserPO(user);
        
        if (StringUtils.hasText(userPO.getPersonId())) {
            // 更新用户基本信息
            userPO.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(userPO);
        } else {
            // 新增用户
            userPO.setPersonId(UUID.randomUUID().toString());
            userPO.setCreateTime(LocalDateTime.now());
            userPO.setActived(1);
            userMapper.insert(userPO);
            user.setPersonId(userPO.getPersonId());
        }
        
        // 处理账户信息
        UserAccount account = user.getAccount();
        if (account != null) {
            AccountPO accountPO = userPOAssembler.toAccountPO(account);
            accountPO.setPersonId(userPO.getPersonId());
            
            if (StringUtils.hasText(accountPO.getAccountId())) {
                // 更新账户
                accountPO.setUpdateTime(LocalDateTime.now());
                accountMapper.updateById(accountPO);
            } else {
                // 新增账户
                accountPO.setAccountId(UUID.randomUUID().toString());
                accountPO.setCreateTime(LocalDateTime.now());
                accountPO.setActived(1);
                accountMapper.insert(accountPO);
                account.setAccountId(accountPO.getAccountId());
            }
        }
        
        // 重新查询完整数据返回
        return findById(userPO.getPersonId()).orElse(user);
    }
    
    @Override
    public Optional<User> findById(String userId) {
        UserPO userPO = userMapper.selectById(userId);
        if (userPO == null || userPO.getActived() == 0) {
            return Optional.empty();
        }
        
        // 查询关联的账户信息
        Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userId);
        
        User user = userPOAssembler.toUser(userPO, accountPOOpt.orElse(null));
        return Optional.of(user);
    }
    
    @Override
    public Optional<User> findByUsername(String username, String tenantId) {
        Optional<UserPO> userPOOpt = userMapper.findByUsername(username, tenantId);
        if (userPOOpt.isPresent()) {
            UserPO userPO = userPOOpt.get();
            Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userPO.getPersonId());
            return Optional.of(userPOAssembler.toUser(userPO, accountPOOpt.orElse(null)));
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByEmail(String email, String tenantId) {
        Optional<UserPO> userPOOpt = userMapper.findByEmail(email, tenantId);
        if (userPOOpt.isPresent()) {
            UserPO userPO = userPOOpt.get();
            Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userPO.getPersonId());
            return Optional.of(userPOAssembler.toUser(userPO, accountPOOpt.orElse(null)));
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByPhone(String phone, String tenantId) {
        Optional<UserPO> userPOOpt = userMapper.findByPhone(phone, tenantId);
        if (userPOOpt.isPresent()) {
            UserPO userPO = userPOOpt.get();
            Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userPO.getPersonId());
            return Optional.of(userPOAssembler.toUser(userPO, accountPOOpt.orElse(null)));
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByIdCard(String idCard, String tenantId) {
        Optional<UserPO> userPOOpt = userMapper.findByIdCard(idCard, tenantId);
        if (userPOOpt.isPresent()) {
            UserPO userPO = userPOOpt.get();
            Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userPO.getPersonId());
            return Optional.of(userPOAssembler.toUser(userPO, accountPOOpt.orElse(null)));
        }
        return Optional.empty();
    }
    
    @Override
    public boolean existsByUsername(String username, String tenantId, String excludeUserId) {
        return userMapper.existsByUsername(username, tenantId, excludeUserId != null ? excludeUserId : "");
    }
    
    @Override
    public boolean existsByEmail(String email, String tenantId, String excludeUserId) {
        return userMapper.existsByEmail(email, tenantId, excludeUserId != null ? excludeUserId : "");
    }
    
    @Override
    public boolean existsByPhone(String phone, String tenantId, String excludeUserId) {
        return userMapper.existsByPhone(phone, tenantId, excludeUserId != null ? excludeUserId : "");
    }
    
    @Override
    public boolean existsByIdCard(String idCard, String tenantId, String excludeUserId) {
        return userMapper.existsByIdCard(idCard, tenantId, excludeUserId != null ? excludeUserId : "");
    }
    
    @Override
    public List<User> findByDeptId(String deptId) {
        List<UserPO> userPOs = userMapper.findByDeptId(deptId);
        return userPOs.stream()
                .map(userPO -> {
                    Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userPO.getPersonId());
                    return userPOAssembler.toUser(userPO, accountPOOpt.orElse(null));
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> findByTenantId(String tenantId) {
        List<UserPO> userPOs = userMapper.findByTenantId(tenantId);
        return userPOs.stream()
                .map(userPO -> {
                    Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userPO.getPersonId());
                    return userPOAssembler.toUser(userPO, accountPOOpt.orElse(null));
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteById(String userId) {
        // 逻辑删除用户
        UserPO userPO = new UserPO();
        userPO.setPersonId(userId);
        userPO.setActived(0);
        userPO.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(userPO);
        
        // 同时逻辑删除账户
        Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userId);
        if (accountPOOpt.isPresent()) {
            AccountPO accountPO = new AccountPO();
            accountPO.setAccountId(accountPOOpt.get().getAccountId());
            accountPO.setActived(0);
            accountPO.setUpdateTime(LocalDateTime.now());
            accountMapper.updateById(accountPO);
        }
    }
    
    @Override
    public List<User> findUsers(String tenantId, String deptId, String keyword, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<UserPO> userPOs = userMapper.findUsers(tenantId, deptId, keyword, offset, pageSize);
        
        return userPOs.stream()
                .map(userPO -> {
                    Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userPO.getPersonId());
                    return userPOAssembler.toUser(userPO, accountPOOpt.orElse(null));
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public long countUsers(String tenantId, String deptId, String keyword) {
        return userMapper.countUsers(tenantId, deptId, keyword);
    }
    
    @Override
    public List<User> findUsersWithAccount(String tenantId) {
        List<UserPO> userPOs = userMapper.findUsersWithAccount(tenantId);
        
        return userPOs.stream()
                .map(userPO -> {
                    Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userPO.getPersonId());
                    return userPOAssembler.toUser(userPO, accountPOOpt.orElse(null));
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> findByStatus(Integer status, String tenantId) {
        List<UserPO> userPOs = userMapper.findByStatus(status, tenantId);
        
        return userPOs.stream()
                .map(userPO -> {
                    Optional<AccountPO> accountPOOpt = accountMapper.findByPersonId(userPO.getPersonId());
                    return userPOAssembler.toUser(userPO, accountPOOpt.orElse(null));
                })
                .collect(Collectors.toList());
    }
}