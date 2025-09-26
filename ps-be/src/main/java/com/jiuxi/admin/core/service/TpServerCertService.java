package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpServerCertQuery;
import com.jiuxi.admin.core.bean.vo.TpServerCertVO;

import java.util.List;

/**
 * 服务器证书服务层接口
 *
 * @author 系统生成
 * @since 2025-09-25
 */
public interface TpServerCertService {

    /**
     * 分页查询服务器证书列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpServerCertVO> queryPage(TpServerCertQuery query);

    /**
     * 根据条件查询服务器证书列表
     *
     * @param query 查询条件
     * @return 证书列表
     */
    List<TpServerCertVO> queryList(TpServerCertQuery query);

    /**
     * 根据主键查询服务器证书信息
     *
     * @param certId 证书ID
     * @return 证书信息
     */
    TpServerCertVO queryById(String certId);

    /**
     * 新增服务器证书
     *
     * @param vo 证书信息
     * @return 操作结果
     */
    boolean save(TpServerCertVO vo);

    /**
     * 更新服务器证书
     *
     * @param vo 证书信息
     * @return 操作结果
     */
    boolean update(TpServerCertVO vo);

    /**
     * 删除服务器证书
     *
     * @param certId 证书ID
     * @return 操作结果
     */
    boolean delete(String certId);

    /**
     * 应用证书到文件系统
     *
     * @param certId 证书ID
     * @return 操作结果
     */
    boolean applyCert(String certId);

    /**
     * 重启Nginx服务
     *
     * @return 操作结果
     */
    boolean restartNginx();

    /**
     * 验证证书名称是否重复
     *
     * @param certName 证书名称
     * @param certId   证书ID（修改时传入，新增时为null）
     * @return 是否重复
     */
    boolean checkCertNameExists(String certName, String certId);

    /**
     * 解析证书内容并提取证书信息
     *
     * @param pemContent PEM证书内容
     * @return 证书信息
     */
    TpServerCertVO parseCertificate(String pemContent);

    /**
     * 验证证书文件和私钥文件是否匹配
     *
     * @param pemContent PEM证书内容
     * @param keyContent 私钥内容
     * @return 是否匹配
     */
    boolean validateCertAndKey(String pemContent, String keyContent);

    /**
     * 查询当前正在使用的证书
     *
     * @return 正在使用的证书
     */
    TpServerCertVO getCurrentInUseCert();

    /**
     * 查询即将过期的证书（指定天数内过期）
     *
     * @param days 天数
     * @return 即将过期的证书列表
     */
    List<TpServerCertVO> getExpiringCerts(int days);

    /**
     * 更新证书过期状态（定时任务调用）
     */
    void updateExpiredStatus();

    /**
     * 检查证书是否正在使用中（删除时校验）
     *
     * @param certId 证书ID
     * @return 是否正在使用
     */
    boolean isCertInUse(String certId);
}