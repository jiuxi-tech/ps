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
     * 根据用户名查找用户（通过账户表关联）
     * @param username 用户名
     * @param tenantId 租户ID
     * @return 用户持久化对象
     */
    @Select("SELECT p.* FROM tp_person_basicinfo p " +
            "INNER JOIN tp_account a ON p.person_id = a.person_id " +
            "WHERE a.username = #{username} AND p.tenant_id = #{tenantId} AND p.actived = 1")
    Optional<UserPO> findByUsername(@Param("username") String username, @Param("tenantId") String tenantId);
    
    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @param tenantId 租户ID
     * @return 用户持久化对象
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE email = #{email} AND tenant_id = #{tenantId} AND actived = 1")
    Optional<UserPO> findByEmail(@Param("email") String email, @Param("tenantId") String tenantId);
    
    /**
     * 根据手机号查找用户
     * @param phone 手机号
     * @param tenantId 租户ID
     * @return 用户持久化对象
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE phone = #{phone} AND tenant_id = #{tenantId} AND actived = 1")
    Optional<UserPO> findByPhone(@Param("phone") String phone, @Param("tenantId") String tenantId);
    
    /**
     * 根据身份证号查找用户
     * @param idCard 身份证号
     * @param tenantId 租户ID
     * @return 用户持久化对象
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE idcard = #{idCard} AND tenant_id = #{tenantId} AND actived = 1")
    Optional<UserPO> findByIdCard(@Param("idCard") String idCard, @Param("tenantId") String tenantId);
    
    /**
     * 根据部门ID查找用户列表
     * @param deptId 部门ID
     * @return 用户列表
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE ascn_id = #{deptId} AND actived = 1")
    List<UserPO> findByDeptId(@Param("deptId") String deptId);
    
    /**
     * 根据租户ID查找用户列表
     * @param tenantId 租户ID
     * @return 用户列表
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE tenant_id = #{tenantId} AND actived = 1")
    List<UserPO> findByTenantId(@Param("tenantId") String tenantId);
    
    /**
     * 检查用户名是否存在（通过账户表）
     * @param username 用户名
     * @param tenantId 租户ID
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM tp_account a " +
            "INNER JOIN tp_person_basicinfo p ON a.person_id = p.person_id " +
            "WHERE a.username = #{username} AND p.tenant_id = #{tenantId} " +
            "AND a.person_id != #{excludeUserId} AND p.actived = 1")
    boolean existsByUsername(@Param("username") String username, @Param("tenantId") String tenantId, @Param("excludeUserId") String excludeUserId);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @param tenantId 租户ID
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM tp_person_basicinfo WHERE email = #{email} AND tenant_id = #{tenantId} " +
            "AND person_id != #{excludeUserId} AND actived = 1")
    boolean existsByEmail(@Param("email") String email, @Param("tenantId") String tenantId, @Param("excludeUserId") String excludeUserId);
    
    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @param tenantId 租户ID
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM tp_person_basicinfo WHERE phone = #{phone} AND tenant_id = #{tenantId} " +
            "AND person_id != #{excludeUserId} AND actived = 1")
    boolean existsByPhone(@Param("phone") String phone, @Param("tenantId") String tenantId, @Param("excludeUserId") String excludeUserId);
    
    /**
     * 检查身份证号是否存在
     * @param idCard 身份证号
     * @param tenantId 租户ID
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(1) > 0 FROM tp_person_basicinfo WHERE idcard = #{idCard} AND tenant_id = #{tenantId} " +
            "AND person_id != #{excludeUserId} AND actived = 1")
    boolean existsByIdCard(@Param("idCard") String idCard, @Param("tenantId") String tenantId, @Param("excludeUserId") String excludeUserId);
    
    /**
     * 分页查询用户
     * @param tenantId 租户ID
     * @param deptId 部门ID
     * @param keyword 关键词
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("<script>" +
            "SELECT * FROM tp_person_basicinfo WHERE actived = 1 " +
            "<if test='tenantId != null and tenantId != \"\"'> AND tenant_id = #{tenantId} </if>" +
            "<if test='deptId != null and deptId != \"\"'> AND ascn_id = #{deptId} </if>" +
            "<if test='keyword != null and keyword != \"\"'> " +
            " AND (person_name LIKE CONCAT('%', #{keyword}, '%') " +
            " OR phone LIKE CONCAT('%', #{keyword}, '%') " +
            " OR email LIKE CONCAT('%', #{keyword}, '%') " +
            " OR person_no LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            " ORDER BY create_time DESC " +
            " LIMIT #{offset}, #{limit}" +
            "</script>")
    List<UserPO> findUsers(@Param("tenantId") String tenantId, @Param("deptId") String deptId, 
                          @Param("keyword") String keyword, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 统计用户数量
     * @param tenantId 租户ID
     * @param deptId 部门ID
     * @param keyword 关键词
     * @return 用户数量
     */
    @Select("<script>" +
            "SELECT COUNT(1) FROM tp_person_basicinfo WHERE actived = 1 " +
            "<if test='tenantId != null and tenantId != \"\"'> AND tenant_id = #{tenantId} </if>" +
            "<if test='deptId != null and deptId != \"\"'> AND ascn_id = #{deptId} </if>" +
            "<if test='keyword != null and keyword != \"\"'> " +
            " AND (person_name LIKE CONCAT('%', #{keyword}, '%') " +
            " OR phone LIKE CONCAT('%', #{keyword}, '%') " +
            " OR email LIKE CONCAT('%', #{keyword}, '%') " +
            " OR person_no LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "</script>")
    long countUsers(@Param("tenantId") String tenantId, @Param("deptId") String deptId, @Param("keyword") String keyword);
    
    /**
     * 查找有账户的用户
     * @param tenantId 租户ID
     * @return 用户列表
     */
    @Select("SELECT p.* FROM tp_person_basicinfo p " +
            "INNER JOIN tp_account a ON p.person_id = a.person_id " +
            "WHERE p.tenant_id = #{tenantId} AND p.actived = 1 AND a.actived = 1")
    List<UserPO> findUsersWithAccount(@Param("tenantId") String tenantId);
    
    /**
     * 根据状态查找用户
     * @param status 状态
     * @param tenantId 租户ID
     * @return 用户列表
     */
    @Select("SELECT * FROM tp_person_basicinfo WHERE actived = #{status} AND tenant_id = #{tenantId}")
    List<UserPO> findByStatus(@Param("status") Integer status, @Param("tenantId") String tenantId);
}