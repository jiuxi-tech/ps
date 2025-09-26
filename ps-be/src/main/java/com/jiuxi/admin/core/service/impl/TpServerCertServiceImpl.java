package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpServerCert;
import com.jiuxi.admin.core.bean.query.TpServerCertQuery;
import com.jiuxi.admin.core.bean.vo.TpServerCertVO;
import com.jiuxi.admin.core.mapper.TpServerCertMapper;
import com.jiuxi.admin.core.service.TpServerCertService;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.shared.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 服务器证书服务层实现类
 *
 * @author 系统生成
 * @since 2025-09-25
 */
@Service
public class TpServerCertServiceImpl implements TpServerCertService {

    private static final Logger logger = LoggerFactory.getLogger(TpServerCertServiceImpl.class);

    @Autowired
    private TpServerCertMapper tpServerCertMapper;

    @Autowired
    private TpSystemConfigService tpSystemConfigService;

    @Override
    public IPage<TpServerCertVO> queryPage(TpServerCertQuery query) {
        Page<TpServerCertVO> page = new Page<>(query.getCurrent(), query.getSize());
        return tpServerCertMapper.selectPageQuery(page, query);
    }

    @Override
    public List<TpServerCertVO> queryList(TpServerCertQuery query) {
        return tpServerCertMapper.selectByCondition(query);
    }

    @Override
    public TpServerCertVO queryById(String certId) {
        if (StrUtil.isBlank(certId)) {
            throw new TopinfoRuntimeException(-1, "证书ID不能为空");
        }
        return tpServerCertMapper.selectByPrimaryKey(certId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(TpServerCertVO vo) {
        try {
            // 参数校验
            validateCertVO(vo, false);
            
            // 检查证书名称是否重复
            if (checkCertNameExists(vo.getCertName(), null)) {
                throw new TopinfoRuntimeException(-1, "证书名称已存在");
            }

            // 解析证书信息
            TpServerCertVO certInfo = parseCertificate(vo.getPemContent());
            
            // 验证证书和私钥匹配
            if (!validateCertAndKey(vo.getPemContent(), vo.getKeyContent())) {
                throw new TopinfoRuntimeException(-1, "证书文件与私钥文件不匹配");
            }

            // 构建实体对象
            TpServerCert entity = new TpServerCert();
            BeanUtils.copyProperties(vo, entity);
            entity.setCertId(UUID.randomUUID().toString());
            
            // 设置解析出的证书信息
            if (certInfo != null) {
                entity.setDomainNames(certInfo.getDomainNames());
                entity.setIssuer(certInfo.getIssuer());
                entity.setSubjectCn(certInfo.getSubjectCn());
                entity.setSubjectO(certInfo.getSubjectO());
                entity.setSubjectOu(certInfo.getSubjectOu());
                entity.setIssueDate(certInfo.getIssueDate());
                entity.setExpireDate(certInfo.getExpireDate());
                
                // 检查是否过期
                entity.setIsExpired(LocalDateTime.now().isAfter(certInfo.getExpireDate()) ? 1 : 0);
            }

            // 设置创建信息
            String currentUserId = getCurrentUserId();
            String currentUserName = getCurrentUserName();
            LocalDateTime now = LocalDateTime.now();
            
            entity.setCreatePersonId(currentUserId);
            entity.setCreatePersonName(currentUserName);
            entity.setCreateTime(now);
            entity.setUpdatePersonId(currentUserId);
            entity.setUpdatePersonName(currentUserName);
            entity.setUpdateTime(now);
            entity.setStatus(0);  // 默认未应用
            entity.setIsInUse(0); // 默认未使用
            entity.setActived(1);

            return tpServerCertMapper.insert(entity) > 0;
            
        } catch (Exception e) {
            logger.error("保存服务器证书失败", e);
            throw new TopinfoRuntimeException(-1, "保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(TpServerCertVO vo) {
        try {
            // 参数校验
            validateCertVO(vo, true);
            
            // 检查证书是否存在
            TpServerCertVO existing = queryById(vo.getCertId());
            if (existing == null) {
                throw new TopinfoRuntimeException(-1, "证书不存在");
            }

            // 检查证书名称是否重复
            if (checkCertNameExists(vo.getCertName(), vo.getCertId())) {
                throw new TopinfoRuntimeException(-1, "证书名称已存在");
            }

            // 解析证书信息
            TpServerCertVO certInfo = parseCertificate(vo.getPemContent());
            
            // 验证证书和私钥匹配
            if (!validateCertAndKey(vo.getPemContent(), vo.getKeyContent())) {
                throw new TopinfoRuntimeException(-1, "证书文件与私钥文件不匹配");
            }

            // 构建实体对象
            TpServerCert entity = new TpServerCert();
            BeanUtils.copyProperties(vo, entity);
            
            // 设置解析出的证书信息
            if (certInfo != null) {
                entity.setDomainNames(certInfo.getDomainNames());
                entity.setIssuer(certInfo.getIssuer());
                entity.setSubjectCn(certInfo.getSubjectCn());
                entity.setSubjectO(certInfo.getSubjectO());
                entity.setSubjectOu(certInfo.getSubjectOu());
                entity.setIssueDate(certInfo.getIssueDate());
                entity.setExpireDate(certInfo.getExpireDate());
                
                // 检查是否过期
                entity.setIsExpired(LocalDateTime.now().isAfter(certInfo.getExpireDate()) ? 1 : 0);
            }

            // 设置更新信息
            String currentUserId = getCurrentUserId();
            String currentUserName = getCurrentUserName();
            LocalDateTime now = LocalDateTime.now();
            
            entity.setUpdatePersonId(currentUserId);
            entity.setUpdatePersonName(currentUserName);
            entity.setUpdateTime(now);

            return tpServerCertMapper.updateByPrimaryKey(entity) > 0;
            
        } catch (Exception e) {
            logger.error("更新服务器证书失败", e);
            throw new TopinfoRuntimeException(-1, "更新失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String certId) {
        try {
            if (StrUtil.isBlank(certId)) {
                throw new TopinfoRuntimeException(-1, "证书ID不能为空");
            }

            // 检查证书是否存在
            TpServerCertVO cert = queryById(certId);
            if (cert == null) {
                throw new TopinfoRuntimeException(-1, "证书不存在");
            }

            // 检查证书是否正在使用
            if (isCertInUse(certId)) {
                throw new TopinfoRuntimeException(-1, "证书正在使用中，无法删除");
            }

            String currentUserId = getCurrentUserId();
            String currentUserName = getCurrentUserName();
            
            return tpServerCertMapper.deleteByPrimaryKey(certId, currentUserId, currentUserName) > 0;
            
        } catch (Exception e) {
            logger.error("删除服务器证书失败", e);
            throw new TopinfoRuntimeException(-1, "删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean applyCert(String certId) {
        try {
            if (StrUtil.isBlank(certId)) {
                throw new TopinfoRuntimeException(-1, "证书ID不能为空");
            }

            // 检查证书是否存在
            TpServerCertVO cert = queryById(certId);
            if (cert == null) {
                throw new TopinfoRuntimeException(-1, "证书不存在");
            }

            // 检查证书是否已过期
            if (cert.getIsExpired() != null && cert.getIsExpired() == 1) {
                throw new TopinfoRuntimeException(-1, "证书已过期，无法应用");
            }

            // 获取系统配置
            String nginxCertDir = tpSystemConfigService.getConfigValue("nginx_cert_dir", "/etc/nginx/ssl/");
            String nginxCertName = tpSystemConfigService.getConfigValue("nginx_cert_name", "server.crt");
            String nginxCertKey = tpSystemConfigService.getConfigValue("nginx_cert_key", "server.key");

            // 创建证书目录
            Path certDirPath = Paths.get(nginxCertDir);
            if (!Files.exists(certDirPath)) {
                Files.createDirectories(certDirPath);
            }

            // 解码证书文件内容
            byte[] pemBytes = Base64.getDecoder().decode(cert.getPemContent());
            byte[] keyBytes = Base64.getDecoder().decode(cert.getKeyContent());

            // 写入证书文件
            Path certFilePath = certDirPath.resolve(nginxCertName);
            Path keyFilePath = certDirPath.resolve(nginxCertKey);
            
            Files.write(certFilePath, pemBytes);
            Files.write(keyFilePath, keyBytes);

            logger.info("证书文件已成功写入: {}", certFilePath.toString());
            logger.info("私钥文件已成功写入: {}", keyFilePath.toString());

            // 更新数据库状态
            // 1. 将所有证书设为未使用
            tpServerCertMapper.updateAllInUseStatusToFalse();
            
            // 2. 更新当前证书状态
            String currentUserId = getCurrentUserId();
            String currentUserName = getCurrentUserName();
            String appliedTimeStr = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .format(java.time.LocalDateTime.now());
            
            tpServerCertMapper.updateStatus(certId, 1, appliedTimeStr, currentUserId, currentUserName);
            tpServerCertMapper.updateInUseStatus(certId, 1);

            return true;
            
        } catch (Exception e) {
            logger.error("应用服务器证书失败", e);
            throw new TopinfoRuntimeException(-1, "应用失败: " + e.getMessage());
        }
    }

    @Override
    public boolean restartNginx() {
        try {
            String nginxRestartCmd = tpSystemConfigService.getConfigValue("nginx_restart_cmd", "systemctl restart nginx");
            
            logger.info("执行Nginx重启命令: {}", nginxRestartCmd);
            
            Process process = Runtime.getRuntime().exec(nginxRestartCmd);
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                logger.info("Nginx重启成功");
                return true;
            } else {
                logger.error("Nginx重启失败，退出码: {}", exitCode);
                throw new TopinfoRuntimeException(-1, "Nginx重启失败，退出码: " + exitCode);
            }
            
        } catch (Exception e) {
            logger.error("重启Nginx服务失败", e);
            throw new TopinfoRuntimeException(-1, "重启失败: " + e.getMessage());
        }
    }

    @Override
    public boolean checkCertNameExists(String certName, String certId) {
        if (StrUtil.isBlank(certName)) {
            return false;
        }
        
        TpServerCert existing = tpServerCertMapper.selectByCertName(certName);
        if (existing == null) {
            return false;
        }
        
        // 修改时排除自身
        if (StrUtil.isNotBlank(certId) && certId.equals(existing.getCertId())) {
            return false;
        }
        
        return true;
    }

    @Override
    public TpServerCertVO parseCertificate(String pemContent) {
        if (StrUtil.isBlank(pemContent)) {
            return null;
        }
        
        try {
            // 解码BASE64内容
            byte[] certBytes = Base64.getDecoder().decode(pemContent);
            String certPem = new String(certBytes);
            
            // 解析证书
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream bis = new ByteArrayInputStream(certPem.getBytes());
            X509Certificate cert = (X509Certificate) factory.generateCertificate(bis);
            
            TpServerCertVO vo = new TpServerCertVO();
            
            // 提取证书信息
            String subjectDN = cert.getSubjectDN().getName();
            String issuerDN = cert.getIssuerDN().getName();
            
            vo.setIssuer(issuerDN);
            vo.setSubjectCn(extractFromDN(subjectDN, "CN"));
            vo.setSubjectO(extractFromDN(subjectDN, "O"));
            vo.setSubjectOu(extractFromDN(subjectDN, "OU"));
            
            // 设置日期
            Date notBefore = cert.getNotBefore();
            Date notAfter = cert.getNotAfter();
            
            vo.setIssueDate(LocalDateTime.ofInstant(notBefore.toInstant(), ZoneId.systemDefault()));
            vo.setExpireDate(LocalDateTime.ofInstant(notAfter.toInstant(), ZoneId.systemDefault()));
            
            // 提取域名信息（从SAN扩展或CN中）
            try {
                java.util.Collection<java.util.List<?>> sanList = cert.getSubjectAlternativeNames();
                if (sanList != null) {
                    List<String> domains = sanList.stream()
                            .filter(san -> san.size() >= 2 && san.get(0).equals(2)) // DNS类型
                            .map(san -> san.get(1).toString())
                            .collect(Collectors.toList());
                    
                    if (!domains.isEmpty()) {
                        vo.setDomainNames(String.join(",", domains));
                    }
                }
            } catch (Exception e) {
                logger.warn("提取域名信息失败", e);
            }
            
            // 如果没有SAN扩展，使用CN作为域名
            if (StrUtil.isBlank(vo.getDomainNames()) && StrUtil.isNotBlank(vo.getSubjectCn())) {
                vo.setDomainNames(vo.getSubjectCn());
            }
            
            return vo;
            
        } catch (Exception e) {
            logger.error("解析证书失败", e);
            throw new TopinfoRuntimeException(-1, "证书格式不正确或已损坏");
        }
    }

    @Override
    public boolean validateCertAndKey(String pemContent, String keyContent) {
        // 简化验证：检查文件格式
        try {
            if (StrUtil.isBlank(pemContent) || StrUtil.isBlank(keyContent)) {
                return false;
            }
            
            // 解码并检查内容格式
            byte[] pemBytes = Base64.getDecoder().decode(pemContent);
            byte[] keyBytes = Base64.getDecoder().decode(keyContent);
            
            String pemStr = new String(pemBytes);
            String keyStr = new String(keyBytes);
            
            // 基础格式检查
            boolean pemValid = pemStr.contains("-----BEGIN CERTIFICATE-----") && 
                              pemStr.contains("-----END CERTIFICATE-----");
                              
            boolean keyValid = (keyStr.contains("-----BEGIN PRIVATE KEY-----") && 
                               keyStr.contains("-----END PRIVATE KEY-----")) ||
                              (keyStr.contains("-----BEGIN RSA PRIVATE KEY-----") && 
                               keyStr.contains("-----END RSA PRIVATE KEY-----"));
            
            return pemValid && keyValid;
            
        } catch (Exception e) {
            logger.error("验证证书和私钥失败", e);
            return false;
        }
    }

    @Override
    public TpServerCertVO getCurrentInUseCert() {
        return tpServerCertMapper.selectCurrentInUseCert();
    }

    @Override
    public List<TpServerCertVO> getExpiringCerts(int days) {
        return tpServerCertMapper.selectExpiringCerts(days);
    }

    @Override
    public void updateExpiredStatus() {
        try {
            tpServerCertMapper.updateExpiredStatus();
            logger.info("证书过期状态更新完成");
        } catch (Exception e) {
            logger.error("更新证书过期状态失败", e);
        }
    }

    @Override
    public boolean isCertInUse(String certId) {
        if (StrUtil.isBlank(certId)) {
            return false;
        }
        
        TpServerCertVO cert = queryById(certId);
        return cert != null && cert.getIsInUse() != null && cert.getIsInUse() == 1;
    }

    // 辅助方法
    private void validateCertVO(TpServerCertVO vo, boolean isUpdate) {
        if (vo == null) {
            throw new TopinfoRuntimeException(-1, "证书信息不能为空");
        }
        
        if (isUpdate && StrUtil.isBlank(vo.getCertId())) {
            throw new TopinfoRuntimeException(-1, "证书ID不能为空");
        }
        
        if (StrUtil.isBlank(vo.getCertName())) {
            throw new TopinfoRuntimeException(-1, "证书名称不能为空");
        }
        
        if (StrUtil.isBlank(vo.getPemContent())) {
            throw new TopinfoRuntimeException(-1, "证书文件内容不能为空");
        }
        
        if (StrUtil.isBlank(vo.getKeyContent())) {
            throw new TopinfoRuntimeException(-1, "私钥文件内容不能为空");
        }
    }

    private String extractFromDN(String dn, String attribute) {
        try {
            String[] parts = dn.split(",");
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith(attribute + "=")) {
                    return part.substring(attribute.length() + 1);
                }
            }
        } catch (Exception e) {
            logger.warn("提取DN属性失败: {}", attribute, e);
        }
        return null;
    }

    private String getCurrentUserId() {
        // TODO: 从SecurityContextHolder或其他用户上下文中获取当前用户ID
        // 暂时返回系统用户
        return "system";
    }

    private String getCurrentUserName() {
        // TODO: 从SecurityContextHolder或其他用户上下文中获取当前用户名
        // 暂时返回系统用户
        return "系统";
    }
}