package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpServerCert;
import com.jiuxi.admin.core.bean.query.TpServerCertQuery;
import com.jiuxi.admin.core.bean.vo.TpServerCertVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 服务器证书数据访问层接口
 *
 * @author 系统生成
 * @since 2025-09-25
 */
@Mapper
public interface TpServerCertMapper {

    /**
     * 分页查询服务器证书列表
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpServerCertVO> selectPageQuery(Page<TpServerCertVO> page, @Param("query") TpServerCertQuery query);

    /**
     * 根据条件查询服务器证书列表
     *
     * @param query 查询条件
     * @return 证书列表
     */
    List<TpServerCertVO> selectByCondition(@Param("query") TpServerCertQuery query);

    /**
     * 根据主键查询服务器证书信息
     *
     * @param certId 证书ID
     * @return 证书信息
     */
    TpServerCertVO selectByPrimaryKey(@Param("certId") String certId);

    /**
     * 根据证书名称查询证书信息
     *
     * @param certName 证书名称
     * @return 证书信息
     */
    TpServerCert selectByCertName(@Param("certName") String certName);

    /**
     * 新增服务器证书
     *
     * @param record 证书实体
     * @return 影响行数
     */
    int insert(TpServerCert record);

    /**
     * 根据主键更新服务器证书
     *
     * @param record 证书实体
     * @return 影响行数
     */
    int updateByPrimaryKey(TpServerCert record);

    /**
     * 根据主键逻辑删除服务器证书
     *
     * @param certId            证书ID
     * @param updatePersonId    更新人ID
     * @param updatePersonName  更新人姓名
     * @return 影响行数
     */
    int deleteByPrimaryKey(@Param("certId") String certId, 
                           @Param("updatePersonId") String updatePersonId, 
                           @Param("updatePersonName") String updatePersonName);

    /**
     * 更新证书应用状态
     *
     * @param certId            证书ID
     * @param status            状态
     * @param appliedTime       应用时间
     * @param updatePersonId    更新人ID
     * @param updatePersonName  更新人姓名
     * @return 影响行数
     */
    int updateStatus(@Param("certId") String certId, 
                     @Param("status") Integer status,
                     @Param("appliedTime") String appliedTime,
                     @Param("updatePersonId") String updatePersonId, 
                     @Param("updatePersonName") String updatePersonName);

    /**
     * 更新证书使用状态
     *
     * @param certId   证书ID
     * @param isInUse  是否使用
     * @return 影响行数
     */
    int updateInUseStatus(@Param("certId") String certId, @Param("isInUse") Integer isInUse);

    /**
     * 批量更新证书使用状态（将所有证书设为未使用）
     *
     * @return 影响行数
     */
    int updateAllInUseStatusToFalse();

    /**
     * 根据过期时间更新过期状态
     *
     * @return 影响行数
     */
    int updateExpiredStatus();

    /**
     * 查询当前正在使用的证书
     *
     * @return 正在使用的证书
     */
    TpServerCertVO selectCurrentInUseCert();

    /**
     * 查询即将过期的证书（指定天数内过期）
     *
     * @param days 天数
     * @return 即将过期的证书列表
     */
    List<TpServerCertVO> selectExpiringCerts(@Param("days") int days);
}