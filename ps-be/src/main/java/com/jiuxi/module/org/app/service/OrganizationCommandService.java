package com.jiuxi.module.org.app.service;

import com.jiuxi.module.org.domain.model.aggregate.Organization;
import com.jiuxi.module.org.domain.model.entity.OrganizationStatus;
import com.jiuxi.module.org.domain.repo.OrganizationRepository;
import com.jiuxi.module.org.domain.service.OrganizationDomainService;
import com.jiuxi.module.org.app.command.dto.OrganizationCreateDTO;
import com.jiuxi.module.org.app.command.dto.OrganizationUpdateDTO;
import com.jiuxi.module.org.app.query.dto.OrganizationResponseDTO;
import com.jiuxi.module.org.app.assembler.OrganizationAssembler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 组织命令服务
 * 负责组织相关的命令操作（创建、更新、删除、状态变更）
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Service
@Transactional
public class OrganizationCommandService {
    
    private final OrganizationRepository organizationRepository;
    private final OrganizationDomainService organizationDomainService;
    private final OrganizationAssembler organizationAssembler;
    private final ApplicationEventPublisher eventPublisher;
    
    public OrganizationCommandService(OrganizationRepository organizationRepository,
                                     OrganizationDomainService organizationDomainService,
                                     OrganizationAssembler organizationAssembler,
                                     ApplicationEventPublisher eventPublisher) {
        this.organizationRepository = organizationRepository;
        this.organizationDomainService = organizationDomainService;
        this.organizationAssembler = organizationAssembler;
        this.eventPublisher = eventPublisher;
    }
    
    /**
     * 创建组织
     * @param createDTO 创建请求
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 组织响应
     */
    public OrganizationResponseDTO createOrganization(OrganizationCreateDTO createDTO, String tenantId, String operator) {
        // 创建组织聚合根
        Organization organization = new Organization();
        organization.setOrganizationId(UUID.randomUUID().toString());
        organization.setOrganizationName(createDTO.getOrganizationName());
        organization.setOrganizationCode(createDTO.getOrganizationCode());
        organization.setOrganizationType(createDTO.getOrganizationType());
        organization.setParentOrganizationId(createDTO.getParentOrganizationId());
        organization.setDescription(createDTO.getDescription());
        // 暂时移除orderIndex设置，Organization类中没有此字段
        organization.setTenantId(tenantId);
        organization.setCreator(operator);
        organization.setCreateTime(LocalDateTime.now());
        organization.setStatus(OrganizationStatus.ACTIVE);
        
        // 计算组织层级和路径
        Integer orgLevel = organizationDomainService.calculateOrganizationLevel(createDTO.getParentOrganizationId());
        organization.setOrganizationLevel(orgLevel);
        
        String orgPath = organizationDomainService.buildOrganizationPath(createDTO.getParentOrganizationId(), organization.getOrganizationId());
        organization.setOrganizationPath(orgPath);
        
        // 业务规则验证
        organizationDomainService.validateForCreate(organization, tenantId);
        
        // 保存组织
        Organization savedOrganization = organizationRepository.save(organization);
        
        // 发布创建事件 (将来可以添加)
        // OrganizationCreatedEvent event = new OrganizationCreatedEvent(savedOrganization);
        // eventPublisher.publishEvent(event);
        
        return organizationAssembler.toResponseDTO(savedOrganization);
    }
    
    /**
     * 更新组织
     * @param organizationId 组织ID
     * @param updateDTO 更新请求
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 组织响应
     */
    public OrganizationResponseDTO updateOrganization(String organizationId, OrganizationUpdateDTO updateDTO, 
                                                     String tenantId, String operator) {
        // 查找现有组织
        Optional<Organization> existingOrgOpt = organizationRepository.findById(organizationId);
        if (existingOrgOpt.isEmpty()) {
            throw new IllegalArgumentException("组织不存在: " + organizationId);
        }
        
        Organization existingOrg = existingOrgOpt.get();
        String oldName = existingOrg.getOrganizationName();
        String oldParentId = existingOrg.getParentOrganizationId();
        
        // 更新组织信息
        existingOrg.setOrganizationName(updateDTO.getOrganizationName());
        existingOrg.setOrganizationCode(updateDTO.getOrganizationCode());
        existingOrg.setOrganizationType(updateDTO.getOrganizationType());
        existingOrg.setParentOrganizationId(updateDTO.getParentOrganizationId());
        existingOrg.setDescription(updateDTO.getDescription());
        // 暂时移除orderIndex设置，Organization类中没有此字段
        existingOrg.setUpdator(operator);
        existingOrg.setUpdateTime(LocalDateTime.now());
        
        // 如果父组织发生变化，重新计算层级和路径
        if (!java.util.Objects.equals(oldParentId, updateDTO.getParentOrganizationId())) {
            Integer newLevel = organizationDomainService.calculateOrganizationLevel(updateDTO.getParentOrganizationId());
            existingOrg.setOrganizationLevel(newLevel);
            
            String newPath = organizationDomainService.buildOrganizationPath(updateDTO.getParentOrganizationId(), organizationId);
            existingOrg.setOrganizationPath(newPath);
        }
        
        // 业务规则验证
        organizationDomainService.validateForUpdate(existingOrg, tenantId);
        
        // 保存组织
        Organization savedOrganization = organizationRepository.save(existingOrg);
        
        // 发布更新事件 (将来可以添加)
        // OrganizationUpdatedEvent event = new OrganizationUpdatedEvent(savedOrganization, oldName, oldParentId);
        // eventPublisher.publishEvent(event);
        
        return organizationAssembler.toResponseDTO(savedOrganization);
    }
    
    /**
     * 删除组织
     * @param organizationId 组织ID
     * @param tenantId 租户ID
     * @param operator 操作者
     */
    public void deleteOrganization(String organizationId, String tenantId, String operator) {
        // 查找现有组织
        Optional<Organization> existingOrgOpt = organizationRepository.findById(organizationId);
        if (existingOrgOpt.isEmpty()) {
            throw new IllegalArgumentException("组织不存在: " + organizationId);
        }
        
        Organization existingOrg = existingOrgOpt.get();
        
        // 业务规则验证
        organizationDomainService.validateForDelete(organizationId);
        
        // 删除组织
        organizationRepository.deleteById(organizationId);
        
        // 发布删除事件 (将来可以添加)
        // OrganizationDeletedEvent event = new OrganizationDeletedEvent(existingOrg);
        // eventPublisher.publishEvent(event);
    }
    
    /**
     * 批量删除组织
     * @param organizationIds 组织ID列表
     * @param tenantId 租户ID
     * @param operator 操作者
     */
    public void deleteOrganizations(List<String> organizationIds, String tenantId, String operator) {
        for (String organizationId : organizationIds) {
            deleteOrganization(organizationId, tenantId, operator);
        }
    }
    
    /**
     * 更新组织状态
     * @param organizationId 组织ID
     * @param status 新状态
     * @param operator 操作者
     * @return 是否更新成功
     */
    public boolean updateOrganizationStatus(String organizationId, OrganizationStatus status, String operator) {
        Optional<Organization> orgOpt = organizationRepository.findById(organizationId);
        if (orgOpt.isEmpty()) {
            throw new IllegalArgumentException("组织不存在: " + organizationId);
        }
        
        Organization organization = orgOpt.get();
        
        // TODO: 需要实现组织状态变更验证逻辑
        // OrganizationStatus newStatus = OrganizationStatus.fromString(status);
        // organizationDomainService.validateStatusChange(organization, newStatus);
        
        // 更新状态
        boolean result = organizationRepository.updateStatus(organizationId, status);
        
        if (result) {
            organization.setUpdator(operator);
            organization.setUpdateTime(LocalDateTime.now());
            organizationRepository.save(organization);
        }
        
        return result;
    }
    
    /**
     * 批量更新组织状态
     * @param organizationIds 组织ID列表
     * @param status 新状态
     * @param operator 操作者
     * @return 更新成功的数量
     */
    public int batchUpdateOrganizationStatus(List<String> organizationIds, OrganizationStatus status, String operator) {
        return organizationRepository.batchUpdateStatus(organizationIds, status);
    }
    
    /**
     * 移动组织到新的父组织下
     * @param organizationId 组织ID
     * @param newParentId 新父组织ID
     * @param operator 操作者
     */
    public void moveOrganization(String organizationId, String newParentId, String operator) {
        // 查找组织
        Optional<Organization> orgOpt = organizationRepository.findById(organizationId);
        if (orgOpt.isEmpty()) {
            throw new IllegalArgumentException("组织不存在: " + organizationId);
        }
        
        Organization organization = orgOpt.get();
        
        // 验证移动操作是否合法
        // TODO: \u9700\u8981\u5b9e\u73b0\u7ec4\u7ec7\u79fb\u52a8\u9a8c\u8bc1\u903b\u8f91
        
        // 更新父组织、层级和路径
        organization.setParentOrganizationId(newParentId);
        
        Integer newLevel = organizationDomainService.calculateOrganizationLevel(newParentId);
        organization.setOrganizationLevel(newLevel);
        
        String newPath = organizationDomainService.buildOrganizationPath(newParentId, organizationId);
        organization.setOrganizationPath(newPath);
        
        organization.setUpdator(operator);
        organization.setUpdateTime(LocalDateTime.now());
        
        // 保存组织
        organizationRepository.save(organization);
        
        // 更新所有子组织的路径和层级
        organizationRepository.updatePathAndLevel(organizationId, newPath, newLevel);
    }
}