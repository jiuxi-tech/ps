package com.jiuxi.module.user.infra.persistence.mapper;

import com.jiuxi.module.user.infra.persistence.entity.UserPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * 用户持久化Mapper
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @param tenantId 租户ID
     * @return 用户持久化对象
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE person_username = #{username} AND tenant_id = #{tenantId} AND is_deleted = 0")
    Optional<UserPO> findByUsername(@Param("username") String username, @Param("tenantId") String tenantId);
    
    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @param tenantId 租户ID
     * @return 用户持久化对象
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE person_email = #{email} AND tenant_id = #{tenantId} AND is_deleted = 0")
    Optional<UserPO> findByEmail(@Param("email") String email, @Param("tenantId") String tenantId);
    
    /**
     * 根据手机号查找用户
     * @param phone 手机号
     * @param tenantId 租户ID
     * @return 用户持久化对象
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE person_mobile = #{phone} AND tenant_id = #{tenantId} AND is_deleted = 0")
    Optional<UserPO> findByPhone(@Param("phone") String phone, @Param("tenantId") String tenantId);
    
    /**
     * 根据身份证号查找用户
     * @param idCard 身份证号
     * @param tenantId 租户ID
     * @return 用户持久化对象
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE person_idcard = #{idCard} AND tenant_id = #{tenantId} AND is_deleted = 0")
    Optional<UserPO> findByIdCard(@Param("idCard") String idCard, @Param("tenantId") String tenantId);
    
    /**
     * 根据部门ID查找用户列表
     * @param deptId 部门ID
     * @return 用户列表
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE dept_id = #{deptId} AND is_deleted = 0")
    List<UserPO> findByDeptId(@Param("deptId") String deptId);
    
    /**
     * 根据租户ID查找用户列表
     * @param tenantId 租户ID
     * @return 用户列表
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE tenant_id = #{tenantId} AND is_deleted = 0")
    List<UserPO> findByTenantId(@Param("tenantId") String tenantId);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @param tenantId 租户ID
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM tp_person_basicinfo WHERE person_username = #{username} AND tenant_id = #{tenantId} " +
            "AND person_id != #{excludeUserId} AND is_deleted = 0")
    boolean existsByUsername(@Param("username") String username, @Param("tenantId") String tenantId, @Param("excludeUserId") String excludeUserId);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @param tenantId 租户ID
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM tp_person_basicinfo WHERE person_email = #{email} AND tenant_id = #{tenantId} " +
            "AND person_id != #{excludeUserId} AND is_deleted = 0")
    boolean existsByEmail(@Param("email") String email, @Param("tenantId") String tenantId, @Param("excludeUserId") String excludeUserId);
    
    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @param tenantId 租户ID
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM tp_person_basicinfo WHERE person_mobile = #{phone} AND tenant_id = #{tenantId} " +
            "AND person_id != #{excludeUserId} AND is_deleted = 0")
    boolean existsByPhone(@Param("phone") String phone, @Param("tenantId") String tenantId, @Param("excludeUserId") String excludeUserId);
    
    /**
     * 检查身份证号是否存在
     * @param idCard 身份证号
     * @param tenantId 租户ID
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM tp_person_basicinfo WHERE person_idcard = #{idCard} AND tenant_id = #{tenantId} " +
            "AND person_id != #{excludeUserId} AND is_deleted = 0")
    boolean existsByIdCard(@Param("idCard") String idCard, @Param("tenantId") String tenantId, @Param("excludeUserId") String excludeUserId);
}