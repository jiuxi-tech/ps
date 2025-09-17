package com.jiuxi.module.org.app.service;

import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.domain.model.entity.EnterpriseStatus;
import com.jiuxi.module.org.domain.repo.EnterpriseRepository;
import com.jiuxi.module.org.domain.service.EnterpriseDomainService;
import com.jiuxi.module.org.app.command.dto.EnterpriseCreateDTO;
import com.jiuxi.module.org.app.command.dto.EnterpriseUpdateDTO;
import com.jiuxi.module.org.app.query.dto.EnterpriseResponseDTO;
import com.jiuxi.module.org.app.assembler.EnterpriseAssembler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 企业命令服务
 * 负责企业相关的命令操作（创建、更新、删除、状态变更）
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Service
@Transactional
public class EnterpriseCommandService {
    
    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseDomainService enterpriseDomainService;
    private final EnterpriseAssembler enterpriseAssembler;
    private final ApplicationEventPublisher eventPublisher;
    
    public EnterpriseCommandService(EnterpriseRepository enterpriseRepository,
                                   EnterpriseDomainService enterpriseDomainService,
                                   EnterpriseAssembler enterpriseAssembler,
                                   ApplicationEventPublisher eventPublisher) {
        this.enterpriseRepository = enterpriseRepository;
        this.enterpriseDomainService = enterpriseDomainService;
        this.enterpriseAssembler = enterpriseAssembler;
        this.eventPublisher = eventPublisher;
    }
    
    /**
     * 创建企业
     * @param createDTO 创建请求
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 企业响应
     */
    public EnterpriseResponseDTO createEnterprise(EnterpriseCreateDTO createDTO, String tenantId, String operator) {
        // 创建企业聚合根
        Enterprise enterprise = new Enterprise();
        enterprise.setEntId(UUID.randomUUID().toString());
        enterprise.setEntFullName(createDTO.getEntFullName());
        enterprise.setEntSimpleName(createDTO.getEntSimpleName());
        enterprise.setEntUnifiedCode(createDTO.getEntUnifiedCode());
        enterprise.setEntType(createDTO.getEntType());
        enterprise.setLinkPsnTel(createDTO.getEntPhone());
        // 暂时移除email设置，Enterprise类中没有此字段
        enterprise.setEntAddr(createDTO.getEntAddress());
        enterprise.setLegalRepr(createDTO.getEntLegalPerson());
        enterprise.setLinkPsnName(createDTO.getEntManager());
        enterprise.setEntDesc(createDTO.getEntDescription());
        enterprise.setTenantId(tenantId);
        enterprise.setCreator(operator);
        enterprise.setCreateTime(LocalDateTime.now());
        
        // 设置地理坐标
        if (createDTO.getEntLongitude() != null && createDTO.getEntLatitude() != null) {
            enterprise.setLongitude(createDTO.getEntLongitude());
            enterprise.setLatitude(createDTO.getEntLatitude());
        }
        
        // 业务规则验证
        enterpriseDomainService.validateForCreate(enterprise, tenantId);
        
        // 保存企业
        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        
        // 发布创建事件 (将来可以添加)
        // EnterpriseCreatedEvent event = new EnterpriseCreatedEvent(savedEnterprise);
        // eventPublisher.publishEvent(event);
        
        return enterpriseAssembler.toResponseDTO(savedEnterprise);
    }
    
    /**
     * 更新企业
     * @param entId 企业ID
     * @param updateDTO 更新请求
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 企业响应
     */
    public EnterpriseResponseDTO updateEnterprise(String entId, EnterpriseUpdateDTO updateDTO, 
                                                 String tenantId, String operator) {
        // 查找现有企业
        Optional<Enterprise> existingEntOpt = enterpriseRepository.findById(entId);
        if (existingEntOpt.isEmpty()) {
            throw new IllegalArgumentException("企业不存在: " + entId);
        }
        
        Enterprise existingEnt = existingEntOpt.get();
        String oldName = existingEnt.getEntFullName();
        
        // 更新企业信息
        existingEnt.setEntFullName(updateDTO.getEntFullName());
        existingEnt.setEntSimpleName(updateDTO.getEntSimpleName());
        existingEnt.setEntUnifiedCode(updateDTO.getEntUnifiedCode());
        existingEnt.setEntType(updateDTO.getEntType());
        existingEnt.setLinkPsnTel(updateDTO.getEntPhone());
        // 暂时移除email设置，Enterprise类中没有此字段
        existingEnt.setEntAddr(updateDTO.getEntAddress());
        existingEnt.setLegalRepr(updateDTO.getEntLegalPerson());
        existingEnt.setLinkPsnName(updateDTO.getEntManager());
        existingEnt.setEntDesc(updateDTO.getEntDescription());
        existingEnt.setUpdator(operator);
        existingEnt.setUpdateTime(LocalDateTime.now());
        
        // 更新地理坐标
        if (updateDTO.getEntLongitude() != null && updateDTO.getEntLatitude() != null) {
            existingEnt.setLongitude(updateDTO.getEntLongitude());
            existingEnt.setLatitude(updateDTO.getEntLatitude());
        }
        
        // 业务规则验证
        enterpriseDomainService.validateForUpdate(existingEnt, tenantId);
        
        // 保存企业
        Enterprise savedEnterprise = enterpriseRepository.save(existingEnt);
        
        // 发布更新事件 (将来可以添加)
        // EnterpriseUpdatedEvent event = new EnterpriseUpdatedEvent(savedEnterprise, oldName);
        // eventPublisher.publishEvent(event);
        
        return enterpriseAssembler.toResponseDTO(savedEnterprise);
    }
    
    /**
     * 删除企业
     * @param entId 企业ID
     * @param tenantId 租户ID
     * @param operator 操作者
     */
    public void deleteEnterprise(String entId, String tenantId, String operator) {
        // 查找现有企业
        Optional<Enterprise> existingEntOpt = enterpriseRepository.findById(entId);
        if (existingEntOpt.isEmpty()) {
            throw new IllegalArgumentException("企业不存在: " + entId);
        }
        
        Enterprise existingEnt = existingEntOpt.get();
        
        // 业务规则验证
        enterpriseDomainService.validateForDelete(entId);
        
        // 删除企业
        enterpriseRepository.deleteById(entId);
        
        // 发布删除事件 (将来可以添加)
        // EnterpriseDeletedEvent event = new EnterpriseDeletedEvent(existingEnt);
        // eventPublisher.publishEvent(event);
    }
    
    /**
     * 批量删除企业
     * @param entIds 企业ID列表
     * @param tenantId 租户ID
     * @param operator 操作者
     */
    public void deleteEnterprises(List<String> entIds, String tenantId, String operator) {
        for (String entId : entIds) {
            deleteEnterprise(entId, tenantId, operator);
        }
    }
    
    /**
     * 更新企业状态
     * @param entId 企业ID
     * @param status 新状态
     * @param operator 操作者
     * @return 是否更新成功
     */
    public boolean updateEnterpriseStatus(String entId, String status, String operator) {
        Optional<Enterprise> entOpt = enterpriseRepository.findById(entId);
        if (entOpt.isEmpty()) {
            throw new IllegalArgumentException("企业不存在: " + entId);
        }
        
        Enterprise enterprise = entOpt.get();
        
        // 验证状态变更是否合法
        EnterpriseStatus newStatus = EnterpriseStatus.fromString(status);
        enterpriseDomainService.validateStatusChange(entId, newStatus);
        
        // 更新状态
        boolean result = enterpriseRepository.updateStatus(entId, status);
        
        if (result) {
            enterprise.setUpdator(operator);
            enterprise.setUpdateTime(LocalDateTime.now());
            enterpriseRepository.save(enterprise);
        }
        
        return result;
    }
    
    /**
     * 批量更新企业状态
     * @param entIds 企业ID列表
     * @param status 新状态
     * @param operator 操作者
     * @return 更新成功的数量
     */
    public int batchUpdateEnterpriseStatus(List<String> entIds, String status, String operator) {
        return enterpriseRepository.batchUpdateStatus(entIds, status);
    }
}