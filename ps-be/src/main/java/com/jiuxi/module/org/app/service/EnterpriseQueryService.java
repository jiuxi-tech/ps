package com.jiuxi.module.org.app.service;

import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.domain.repo.EnterpriseRepository;
import com.jiuxi.module.org.app.query.dto.EnterpriseResponseDTO;
import com.jiuxi.module.org.app.assembler.EnterpriseAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 企业查询服务
 * 专门负责企业的查询、统计和分析功能
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Service
@Transactional(readOnly = true)
public class EnterpriseQueryService {
    
    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseAssembler enterpriseAssembler;
    
    public EnterpriseQueryService(EnterpriseRepository enterpriseRepository,
                                 EnterpriseAssembler enterpriseAssembler) {
        this.enterpriseRepository = enterpriseRepository;
        this.enterpriseAssembler = enterpriseAssembler;
    }
    
    /**
     * 根据ID查询企业
     * @param entId 企业ID
     * @return 企业响应
     */
    public Optional<EnterpriseResponseDTO> findEnterpriseById(String entId) {
        if (!StringUtils.hasText(entId)) {
            throw new IllegalArgumentException("企业ID不能为空");
        }
        return enterpriseRepository.findById(entId)
                .map(enterpriseAssembler::toResponseDTO);
    }
    
    /**
     * 根据ID查询企业（兼容控制器调用）
     * @param entId 企业ID
     * @return 企业响应
     */
    public EnterpriseResponseDTO getEnterpriseById(String entId) {
        return findEnterpriseById(entId)
                .orElseThrow(() -> new IllegalArgumentException("企业不存在: " + entId));
    }
    
    /**
     * 根据企业名称查询企业
     * @param entFullName 企业全称
     * @param tenantId 租户ID
     * @return 企业响应
     */
    public Optional<EnterpriseResponseDTO> findEnterpriseByName(String entFullName, String tenantId) {
        if (!StringUtils.hasText(entFullName) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("企业名称和租户ID不能为空");
        }
        return enterpriseRepository.findByName(entFullName, tenantId)
                .map(enterpriseAssembler::toResponseDTO);
    }
    
    /**
     * 根据统一社会信用代码查询企业
     * @param entUnifiedCode 统一社会信用代码
     * @return 企业响应
     */
    public Optional<EnterpriseResponseDTO> findEnterpriseByUnifiedCode(String entUnifiedCode) {
        if (!StringUtils.hasText(entUnifiedCode)) {
            throw new IllegalArgumentException("统一社会信用代码不能为空");
        }
        return enterpriseRepository.findByUnifiedCode(entUnifiedCode)
                .map(enterpriseAssembler::toResponseDTO);
    }
    
    /**
     * 根据租户ID查询企业列表
     * @param tenantId 租户ID
     * @return 企业列表
     */
    public List<EnterpriseResponseDTO> findEnterprisesByTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        List<Enterprise> enterprises = enterpriseRepository.findByTenantId(tenantId);
        return enterprises.stream()
                .map(enterpriseAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据企业类型查询企业列表
     * @param entType 企业类型
     * @param tenantId 租户ID
     * @return 企业列表
     */
    public List<EnterpriseResponseDTO> findEnterprisesByType(String entType, String tenantId) {
        if (!StringUtils.hasText(entType) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("企业类型和租户ID不能为空");
        }
        List<Enterprise> enterprises = enterpriseRepository.findByType(entType, tenantId);
        return enterprises.stream()
                .map(enterpriseAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据状态查询企业列表
     * @param status 企业状态
     * @param tenantId 租户ID
     * @return 企业列表
     */
    public List<EnterpriseResponseDTO> findEnterprisesByStatus(String status, String tenantId) {
        if (!StringUtils.hasText(status) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("企业状态和租户ID不能为空");
        }
        List<Enterprise> enterprises = enterpriseRepository.findByStatus(status, tenantId);
        return enterprises.stream()
                .map(enterpriseAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据地理位置范围查询企业
     * @param minLongitude 最小经度
     * @param maxLongitude 最大经度
     * @param minLatitude 最小纬度
     * @param maxLatitude 最大纬度
     * @param tenantId 租户ID
     * @return 企业列表
     */
    public List<EnterpriseResponseDTO> findEnterprisesByLocationRange(String minLongitude, String maxLongitude,
                                                                     String minLatitude, String maxLatitude, 
                                                                     String tenantId) {
        if (!StringUtils.hasText(minLongitude) || !StringUtils.hasText(maxLongitude) ||
            !StringUtils.hasText(minLatitude) || !StringUtils.hasText(maxLatitude) ||
            !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("地理位置参数和租户ID不能为空");
        }
        
        List<Enterprise> enterprises = enterpriseRepository.findByLocationRange(
                minLongitude, maxLongitude, minLatitude, maxLatitude, tenantId);
        return enterprises.stream()
                .map(enterpriseAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据行政区划代码查询企业
     * @param addrCode 行政区划代码
     * @param tenantId 租户ID
     * @return 企业列表
     */
    public List<EnterpriseResponseDTO> findEnterprisesByAddrCode(String addrCode, String tenantId) {
        if (!StringUtils.hasText(addrCode) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("行政区划代码和租户ID不能为空");
        }
        List<Enterprise> enterprises = enterpriseRepository.findByAddrCode(addrCode, tenantId);
        return enterprises.stream()
                .map(enterpriseAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据关键词搜索企业
     * @param keyword 搜索关键词
     * @param tenantId 租户ID
     * @return 企业列表
     */
    public List<EnterpriseResponseDTO> searchEnterprisesByKeyword(String keyword, String tenantId) {
        if (!StringUtils.hasText(keyword) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("搜索关键词和租户ID不能为空");
        }
        List<Enterprise> enterprises = enterpriseRepository.searchByKeyword(keyword, tenantId);
        return enterprises.stream()
                .map(enterpriseAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量查询企业
     * @param entIds 企业ID列表
     * @return 企业列表
     */
    public List<EnterpriseResponseDTO> findEnterprisesByIds(List<String> entIds) {
        if (entIds == null || entIds.isEmpty()) {
            throw new IllegalArgumentException("企业ID列表不能为空");
        }
        List<Enterprise> enterprises = enterpriseRepository.findByIds(entIds);
        return enterprises.stream()
                .map(enterpriseAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页查询企业
     * @param tenantId 租户ID
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 企业列表
     */
    public List<EnterpriseResponseDTO> findEnterprisesPage(String tenantId, int pageNum, int pageSize) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        if (pageNum < 1 || pageSize < 1) {
            throw new IllegalArgumentException("页码和页大小必须大于0");
        }
        List<Enterprise> enterprises = enterpriseRepository.findPage(tenantId, pageNum, pageSize);
        return enterprises.stream()
                .map(enterpriseAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 检查企业名称是否存在
     * @param entFullName 企业全称
     * @param tenantId 租户ID
     * @param excludeEntId 排除的企业ID
     * @return 是否存在
     */
    public boolean existsByEnterpriseName(String entFullName, String tenantId, String excludeEntId) {
        if (!StringUtils.hasText(entFullName) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("企业名称和租户ID不能为空");
        }
        return enterpriseRepository.existsByName(entFullName, tenantId, excludeEntId);
    }
    
    /**
     * 检查统一社会信用代码是否存在
     * @param entUnifiedCode 统一社会信用代码
     * @param excludeEntId 排除的企业ID
     * @return 是否存在
     */
    public boolean existsByUnifiedCode(String entUnifiedCode, String excludeEntId) {
        if (!StringUtils.hasText(entUnifiedCode)) {
            throw new IllegalArgumentException("统一社会信用代码不能为空");
        }
        return enterpriseRepository.existsByUnifiedCode(entUnifiedCode, excludeEntId);
    }
    
    /**
     * 根据企业ID检查是否存在
     * @param entId 企业ID
     * @return 是否存在
     */
    public boolean existsByEnterpriseId(String entId) {
        if (!StringUtils.hasText(entId)) {
            throw new IllegalArgumentException("企业ID不能为空");
        }
        return enterpriseRepository.findById(entId).isPresent();
    }
    
    /**
     * 统计租户的企业数量
     * @param tenantId 租户ID
     * @return 企业数量
     */
    public long countEnterprisesByTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        return enterpriseRepository.countByTenantId(tenantId);
    }
    
    /**
     * 统计指定类型的企业数量
     * @param entType 企业类型
     * @param tenantId 租户ID
     * @return 企业数量
     */
    public long countEnterprisesByType(String entType, String tenantId) {
        if (!StringUtils.hasText(entType) || !StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("企业类型和租户ID不能为空");
        }
        return enterpriseRepository.countByType(entType, tenantId);
    }
}