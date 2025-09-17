package com.jiuxi.module.org.infra.persistence.repo;

import com.jiuxi.module.org.domain.model.aggregate.Enterprise;
import com.jiuxi.module.org.domain.repo.EnterpriseRepository;
import com.jiuxi.module.org.domain.query.EnterpriseQuery;
import com.jiuxi.module.org.infra.persistence.entity.EnterprisePO;
import com.jiuxi.module.org.infra.persistence.assembler.EnterpriseInfraAssembler;
import com.jiuxi.module.org.infra.persistence.mapper.EnterpriseNewMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 企业仓储实现类
 * 负责企业聚合根的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Repository
public class EnterpriseRepositoryImpl implements EnterpriseRepository {
    
    private final EnterpriseNewMapper enterpriseMapper;
    private final EnterpriseInfraAssembler enterpriseInfraAssembler;
    
    public EnterpriseRepositoryImpl(EnterpriseNewMapper enterpriseMapper, 
                                  EnterpriseInfraAssembler enterpriseInfraAssembler) {
        this.enterpriseMapper = enterpriseMapper;
        this.enterpriseInfraAssembler = enterpriseInfraAssembler;
    }

    @Override
    public Enterprise save(Enterprise enterprise) {
        EnterprisePO enterprisePO = enterpriseInfraAssembler.toEnterprisePO(enterprise);
        
        if (StringUtils.hasText(enterprisePO.getEntId())) {
            // 更新
            enterprisePO.setUpdateTime(LocalDateTime.now());
            enterpriseMapper.updateById(enterprisePO);
        } else {
            // 新增
            enterprisePO.setEntId(UUID.randomUUID().toString());
            enterprisePO.setCreateTime(LocalDateTime.now());
            enterprisePO.setActived(1);
            enterprisePO.setEnabled(1);
            enterpriseMapper.insert(enterprisePO);
        }
        
        return enterpriseInfraAssembler.toEnterprise(enterprisePO);
    }

    @Override
    public Optional<Enterprise> findById(String entId) {
        Optional<EnterprisePO> enterprisePOOpt = enterpriseMapper.selectById(entId);
        return enterprisePOOpt.map(enterpriseInfraAssembler::toEnterprise);
    }

    @Override
    public Optional<Enterprise> findByName(String entFullName, String tenantId) {
        Optional<EnterprisePO> enterprisePOOpt = enterpriseMapper.selectByName(entFullName, tenantId);
        return enterprisePOOpt.map(enterpriseInfraAssembler::toEnterprise);
    }

    @Override
    public Optional<Enterprise> findByUnifiedCode(String entUnifiedCode) {
        Optional<EnterprisePO> enterprisePOOpt = enterpriseMapper.selectByUnifiedCode(entUnifiedCode);
        return enterprisePOOpt.map(enterpriseInfraAssembler::toEnterprise);
    }

    @Override
    public List<Enterprise> findByTenantId(String tenantId) {
        List<EnterprisePO> enterprisePOs = enterpriseMapper.selectByTenantId(tenantId);
        return enterpriseInfraAssembler.toEnterpriseList(enterprisePOs);
    }

    @Override
    public List<Enterprise> findByType(String entType, String tenantId) {
        List<EnterprisePO> enterprisePOs = enterpriseMapper.selectByType(entType, tenantId);
        return enterpriseInfraAssembler.toEnterpriseList(enterprisePOs);
    }

    @Override
    public boolean existsByName(String entFullName, String tenantId, String excludeEntId) {
        int count = enterpriseMapper.countByName(entFullName, tenantId, excludeEntId);
        return count > 0;
    }

    @Override
    public boolean existsByUnifiedCode(String entUnifiedCode, String excludeEntId) {
        int count = enterpriseMapper.countByUnifiedCode(entUnifiedCode, excludeEntId);
        return count > 0;
    }

    @Override
    public void deleteById(String entId) {
        enterpriseMapper.deleteById(entId, "system", LocalDateTime.now());
    }

    @Override
    public void deleteByIds(List<String> entIds) {
        enterpriseMapper.deleteByIds(entIds, "system", LocalDateTime.now());
    }

    @Override
    public List<Enterprise> findByLocationRange(String minLongitude, String maxLongitude, 
                                               String minLatitude, String maxLatitude, String tenantId) {
        List<EnterprisePO> enterprisePOs = enterpriseMapper.selectByLocationRange(minLongitude, maxLongitude, minLatitude, maxLatitude, tenantId);
        return enterpriseInfraAssembler.toEnterpriseList(enterprisePOs);
    }

    @Override
    public List<Enterprise> findByAddrCode(String addrCode, String tenantId) {
        List<EnterprisePO> enterprisePOs = enterpriseMapper.selectByAddrCode(addrCode, tenantId);
        return enterpriseInfraAssembler.toEnterpriseList(enterprisePOs);
    }

    @Override
    public long countByTenantId(String tenantId) {
        return enterpriseMapper.countByTenantId(tenantId);
    }

    @Override
    public long countByType(String entType, String tenantId) {
        return enterpriseMapper.countByType(entType, tenantId);
    }

    @Override
    public List<Enterprise> findPage(String tenantId, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<EnterprisePO> enterprisePOs = enterpriseMapper.selectPage(tenantId, offset, pageSize);
        return enterpriseInfraAssembler.toEnterpriseList(enterprisePOs);
    }

    @Override
    public List<Enterprise> searchByKeyword(String keyword, String tenantId) {
        List<EnterprisePO> enterprisePOs = enterpriseMapper.searchByKeyword(keyword, tenantId);
        return enterpriseInfraAssembler.toEnterpriseList(enterprisePOs);
    }

    @Override
    public List<Enterprise> findByStatus(String status, String tenantId) {
        Integer enabled = "ACTIVE".equals(status) ? 1 : 0;
        Integer actived = "CANCELLED".equals(status) ? 0 : 1;
        List<EnterprisePO> enterprisePOs = enterpriseMapper.selectByStatus(enabled, actived, tenantId);
        return enterpriseInfraAssembler.toEnterpriseList(enterprisePOs);
    }

    @Override
    public List<Enterprise> findByIds(List<String> entIds) {
        List<EnterprisePO> enterprisePOs = enterpriseMapper.selectByIds(entIds);
        return enterpriseInfraAssembler.toEnterpriseList(enterprisePOs);
    }

    @Override
    public boolean updateStatus(String entId, String status) {
        Integer enabled = "ACTIVE".equals(status) ? 1 : 0;
        Integer actived = "CANCELLED".equals(status) ? 0 : 1;
        int result = enterpriseMapper.updateStatus(entId, enabled, actived);
        return result > 0;
    }

    @Override
    public int batchUpdateStatus(List<String> entIds, String status) {
        Integer enabled = "ACTIVE".equals(status) ? 1 : 0;
        Integer actived = "CANCELLED".equals(status) ? 0 : 1;
        return enterpriseMapper.batchUpdateStatus(entIds, enabled, actived);
    }

    @Override
    public List<Enterprise> findByQuery(EnterpriseQuery query) {
        // TODO: 实现复杂查询逻辑，需要根据EnterpriseQuery的具体字段来构建查询条件
        throw new UnsupportedOperationException("findByQuery method not implemented yet");
    }

    @Override
    public long countByQuery(EnterpriseQuery query) {
        // TODO: 实现复杂查询统计逻辑，需要根据EnterpriseQuery的具体字段来构建查询条件
        throw new UnsupportedOperationException("countByQuery method not implemented yet");
    }

    @Override
    public List<Enterprise> findPageByQuery(EnterpriseQuery query) {
        // TODO: 实现复杂查询分页逻辑，需要根据EnterpriseQuery的具体字段来构建查询条件
        throw new UnsupportedOperationException("findPageByQuery method not implemented yet");
    }
}