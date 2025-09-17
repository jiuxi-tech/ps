package com.jiuxi.module.org.domain.service;

import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.domain.model.entity.EnterpriseStatus;
import com.jiuxi.module.org.domain.repo.EnterpriseRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 企业领域服务
 * 负责企业相关的业务规则和领域逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
@Service
public class EnterpriseDomainService {
    
    private final EnterpriseRepository enterpriseRepository;
    
    // 统一社会信用代码正则表达式（18位）
    private static final Pattern UNIFIED_CODE_PATTERN = Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");
    
    // 法定代表人姓名正则表达式（中文、英文、数字）
    private static final Pattern LEGAL_REPR_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9·]{2,50}$");
    
    // 联系电话正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$|^0\\d{2,3}-?\\d{7,8}$");
    
    public EnterpriseDomainService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }
    
    /**
     * 验证企业创建规则
     * @param enterprise 待创建的企业
     * @param tenantId 租户ID
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateForCreate(Enterprise enterprise, String tenantId) {
        // 验证必填字段
        validateRequiredFields(enterprise, tenantId);
        
        // 验证企业名称唯一性
        if (enterpriseRepository.existsByName(enterprise.getEntFullName(), tenantId, null)) {
            throw new IllegalArgumentException("企业名称已存在: " + enterprise.getEntFullName());
        }
        
        // 验证统一社会信用代码唯一性
        if (StringUtils.hasText(enterprise.getEntUnifiedCode())) {
            if (enterpriseRepository.existsByUnifiedCode(enterprise.getEntUnifiedCode(), null)) {
                throw new IllegalArgumentException("统一社会信用代码已存在: " + enterprise.getEntUnifiedCode());
            }
        }
        
        // 验证格式规范
        validateFormat(enterprise);
    }
    
    /**
     * 验证企业更新规则
     * @param enterprise 待更新的企业
     * @param tenantId 租户ID
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateForUpdate(Enterprise enterprise, String tenantId) {
        // 验证企业是否存在
        if (!StringUtils.hasText(enterprise.getEntId())) {
            throw new IllegalArgumentException("企业ID不能为空");
        }
        
        Optional<Enterprise> existingEnt = enterpriseRepository.findById(enterprise.getEntId());
        if (existingEnt.isEmpty()) {
            throw new IllegalArgumentException("企业不存在: " + enterprise.getEntId());
        }
        
        // 验证必填字段
        validateRequiredFields(enterprise, tenantId);
        
        // 验证企业名称唯一性（排除自己）
        if (enterpriseRepository.existsByName(enterprise.getEntFullName(), tenantId, enterprise.getEntId())) {
            throw new IllegalArgumentException("企业名称已存在: " + enterprise.getEntFullName());
        }
        
        // 验证统一社会信用代码唯一性（排除自己）
        if (StringUtils.hasText(enterprise.getEntUnifiedCode())) {
            if (enterpriseRepository.existsByUnifiedCode(enterprise.getEntUnifiedCode(), enterprise.getEntId())) {
                throw new IllegalArgumentException("统一社会信用代码已存在: " + enterprise.getEntUnifiedCode());
            }
        }
        
        // 验证格式规范
        validateFormat(enterprise);
        
        // 验证状态变更合法性
        validateStatusChange(existingEnt.get(), enterprise);
    }
    
    /**
     * 验证企业删除规则
     * @param entId 企业ID
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateForDelete(String entId) {
        if (!StringUtils.hasText(entId)) {
            throw new IllegalArgumentException("企业ID不能为空");
        }
        
        // 验证企业是否存在
        Optional<Enterprise> enterprise = enterpriseRepository.findById(entId);
        if (enterprise.isEmpty()) {
            throw new IllegalArgumentException("企业不存在: " + entId);
        }
        
        // 检查企业状态，只有停用状态的企业才能删除
        if (enterprise.get().getStatus() == EnterpriseStatus.ACTIVE) {
            throw new IllegalArgumentException("企业处于活跃状态，不能删除。请先停用企业。");
        }
    }
    
    /**
     * 验证企业状态变更规则
     * @param entId 企业ID
     * @param newStatus 新状态
     * @throws IllegalArgumentException 如果验证失败
     */
    public void validateStatusChange(String entId, EnterpriseStatus newStatus) {
        Optional<Enterprise> enterprise = enterpriseRepository.findById(entId);
        if (enterprise.isEmpty()) {
            throw new IllegalArgumentException("企业不存在: " + entId);
        }
        
        validateStatusChange(enterprise.get(), newStatus);
    }
    
    /**
     * 生成企业编号
     * @param entType 企业类型
     * @param tenantId 租户ID
     * @return 企业编号
     */
    public String generateEnterpriseCode(String entType, String tenantId) {
        // 根据企业类型和租户生成编号规则
        String prefix = "ENT";
        if (StringUtils.hasText(entType)) {
            // 根据企业类型设置前缀
            switch (entType.toUpperCase()) {
                case "PRIVATE":
                    prefix = "PRI";
                    break;
                case "STATE_OWNED":
                    prefix = "SOE";
                    break;
                case "FOREIGN":
                    prefix = "FOR";
                    break;
                case "JOINT_VENTURE":
                    prefix = "JV";
                    break;
                default:
                    prefix = "ENT";
            }
        }
        
        // 获取当前租户下的企业数量，用于生成序号
        long count = enterpriseRepository.countByTenantId(tenantId);
        String sequence = String.format("%06d", count + 1);
        
        return prefix + tenantId.substring(0, Math.min(3, tenantId.length())).toUpperCase() + sequence;
    }
    
    /**
     * 验证地理坐标格式
     * @param longitude 经度
     * @param latitude 纬度
     * @return 是否有效
     */
    public boolean validateCoordinates(String longitude, String latitude) {
        if (!StringUtils.hasText(longitude) || !StringUtils.hasText(latitude)) {
            return false;
        }
        
        try {
            double lng = Double.parseDouble(longitude);
            double lat = Double.parseDouble(latitude);
            
            // 经度范围：-180 到 180
            // 纬度范围：-90 到 90
            return lng >= -180 && lng <= 180 && lat >= -90 && lat <= 90;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 计算两个企业之间的距离（千米）
     * @param ent1 企业1
     * @param ent2 企业2
     * @return 距离（千米），如果坐标无效则返回-1
     */
    public double calculateDistance(Enterprise ent1, Enterprise ent2) {
        if (!validateCoordinates(ent1.getLongitude(), ent1.getLatitude()) ||
            !validateCoordinates(ent2.getLongitude(), ent2.getLatitude())) {
            return -1;
        }
        
        double lng1 = Double.parseDouble(ent1.getLongitude());
        double lat1 = Double.parseDouble(ent1.getLatitude());
        double lng2 = Double.parseDouble(ent2.getLongitude());
        double lat2 = Double.parseDouble(ent2.getLatitude());
        
        return calculateHaversineDistance(lat1, lng1, lat2, lng2);
    }
    
    // 私有方法
    
    /**
     * 验证必填字段
     */
    private void validateRequiredFields(Enterprise enterprise, String tenantId) {
        if (!StringUtils.hasText(enterprise.getEntFullName())) {
            throw new IllegalArgumentException("企业全称不能为空");
        }
        
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        
        // 验证字段长度
        if (enterprise.getEntFullName().length() > 200) {
            throw new IllegalArgumentException("企业全称长度不能超过200个字符");
        }
        
        if (StringUtils.hasText(enterprise.getEntSimpleName()) && 
            enterprise.getEntSimpleName().length() > 100) {
            throw new IllegalArgumentException("企业简称长度不能超过100个字符");
        }
        
        if (StringUtils.hasText(enterprise.getEntDesc()) && 
            enterprise.getEntDesc().length() > 1000) {
            throw new IllegalArgumentException("企业简介长度不能超过1000个字符");
        }
    }
    
    /**
     * 验证格式规范
     */
    private void validateFormat(Enterprise enterprise) {
        // 验证统一社会信用代码格式
        if (StringUtils.hasText(enterprise.getEntUnifiedCode())) {
            if (!UNIFIED_CODE_PATTERN.matcher(enterprise.getEntUnifiedCode()).matches()) {
                throw new IllegalArgumentException("统一社会信用代码格式不正确");
            }
        }
        
        // 验证法定代表人姓名格式
        if (StringUtils.hasText(enterprise.getLegalRepr())) {
            if (!LEGAL_REPR_PATTERN.matcher(enterprise.getLegalRepr()).matches()) {
                throw new IllegalArgumentException("法定代表人姓名格式不正确");
            }
        }
        
        // 验证联系电话格式
        if (StringUtils.hasText(enterprise.getLegalReprTel())) {
            if (!PHONE_PATTERN.matcher(enterprise.getLegalReprTel()).matches()) {
                throw new IllegalArgumentException("法人联系电话格式不正确");
            }
        }
        
        if (StringUtils.hasText(enterprise.getLinkPsnTel())) {
            if (!PHONE_PATTERN.matcher(enterprise.getLinkPsnTel()).matches()) {
                throw new IllegalArgumentException("联系电话格式不正确");
            }
        }
        
        // 验证地理坐标
        if (StringUtils.hasText(enterprise.getLongitude()) || StringUtils.hasText(enterprise.getLatitude())) {
            if (!validateCoordinates(enterprise.getLongitude(), enterprise.getLatitude())) {
                throw new IllegalArgumentException("地理坐标格式不正确");
            }
        }
    }
    
    /**
     * 验证状态变更合法性
     */
    private void validateStatusChange(Enterprise existingEnt, Enterprise newEnt) {
        EnterpriseStatus oldStatus = existingEnt.getStatus();
        EnterpriseStatus newStatus = newEnt.getStatus();
        
        if (oldStatus == newStatus) {
            return; // 状态未变更
        }
        
        validateStatusChange(existingEnt, newStatus);
    }
    
    /**
     * 验证状态变更合法性
     */
    private void validateStatusChange(Enterprise enterprise, EnterpriseStatus newStatus) {
        EnterpriseStatus currentStatus = enterprise.getStatus();
        
        // 定义状态转换规则
        switch (currentStatus) {
            case ACTIVE:
                // 活跃状态可以转为：停用、注销
                if (newStatus != EnterpriseStatus.INACTIVE && newStatus != EnterpriseStatus.CANCELLED) {
                    throw new IllegalArgumentException("企业处于活跃状态，只能转为停用或注销状态");
                }
                break;
            case INACTIVE:
                // 停用状态可以转为：活跃、注销
                if (newStatus != EnterpriseStatus.ACTIVE && newStatus != EnterpriseStatus.CANCELLED) {
                    throw new IllegalArgumentException("企业处于停用状态，只能转为活跃或注销状态");
                }
                break;
            case CANCELLED:
                // 注销状态不能转换为其他状态
                throw new IllegalArgumentException("企业已注销，不能更改状态");
            default:
                throw new IllegalArgumentException("未知的企业状态: " + currentStatus);
        }
    }
    
    /**
     * 使用Haversine公式计算两点间距离
     */
    private double calculateHaversineDistance(double lat1, double lng1, double lat2, double lng2) {
        final double R = 6371; // 地球半径（千米）
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}