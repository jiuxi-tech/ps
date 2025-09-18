package com.jiuxi.module.sys.app.query.handler;

import com.jiuxi.module.sys.app.query.dto.DictionaryResponse;
import com.jiuxi.module.sys.app.assembler.DictionaryAssembler;
import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.repo.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 字典查询处理器
 * 负责处理字典相关的查询操作（R）
 * 
 * @author System Management
 * @date 2025-09-18
 */
@Component
public class DictionaryQueryHandler {
    
    @Autowired
    private DictionaryRepository dictionaryRepository;
    
    @Autowired
    private DictionaryAssembler dictionaryAssembler;
    
    /**
     * 根据ID查询字典
     * @param dictId 字典ID
     * @return 字典响应DTO
     */
    public DictionaryResponse handleGetByIdQuery(String dictId) {
        try {
            Optional<Dictionary> dictionaryOpt = dictionaryRepository.findById(dictId);
            if (!dictionaryOpt.isPresent()) {
                return null;
            }
            
            return convertToResponse(dictionaryOpt.get());
        } catch (Exception e) {
            throw new RuntimeException("查询字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据字典编码查询字典
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 字典响应DTO
     */
    public DictionaryResponse handleGetByCodeQuery(String dictCode, String tenantId) {
        try {
            Optional<Dictionary> dictionaryOpt = dictionaryRepository.findByDictCode(dictCode, tenantId);
            if (!dictionaryOpt.isPresent()) {
                return null;
            }
            
            return convertToResponse(dictionaryOpt.get());
        } catch (Exception e) {
            throw new RuntimeException("根据编码查询字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据字典类型查询字典列表
     * @param dictTypeCode 字典类型代码
     * @param tenantId 租户ID
     * @return 字典响应DTO列表
     */
    public List<DictionaryResponse> handleGetByTypeQuery(String dictTypeCode, String tenantId) {
        try {
            ConfigType dictType = ConfigType.fromCode(dictTypeCode);
            List<Dictionary> dictionaries = dictionaryRepository.findByDictType(dictType, tenantId);
            return dictionaries.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典状态查询字典列表
     * @param dictStatusCode 字典状态代码
     * @param tenantId 租户ID
     * @return 字典响应DTO列表
     */
    public List<DictionaryResponse> handleGetByStatusQuery(String dictStatusCode, String tenantId) {
        try {
            ConfigStatus dictStatus = ConfigStatus.fromCode(dictStatusCode);
            List<Dictionary> dictionaries = dictionaryRepository.findByDictStatus(dictStatus, tenantId);
            return dictionaries.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典编码前缀查询字典列表
     * @param codePrefix 字典编码前缀
     * @param tenantId 租户ID
     * @return 字典响应DTO列表
     */
    public List<DictionaryResponse> handleGetByCodePrefixQuery(String codePrefix, String tenantId) {
        try {
            List<Dictionary> dictionaries = dictionaryRepository.findByCodePrefix(codePrefix, tenantId);
            return dictionaries.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据字典组查询字典列表
     * @param dictGroup 字典组
     * @param tenantId 租户ID
     * @return 字典响应DTO列表
     */
    public List<DictionaryResponse> handleGetByGroupQuery(String dictGroup, String tenantId) {
        try {
            List<Dictionary> dictionaries = dictionaryRepository.findByDictGroup(dictGroup, tenantId);
            return dictionaries.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 根据上级字典ID查询子字典列表
     * @param parentDictId 上级字典ID
     * @param tenantId 租户ID
     * @return 子字典响应DTO列表
     */
    public List<DictionaryResponse> handleGetChildrenQuery(String parentDictId, String tenantId) {
        try {
            List<Dictionary> dictionaries = dictionaryRepository.findByParentDictId(parentDictId, tenantId);
            return dictionaries.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 查询根级字典列表
     * @param tenantId 租户ID
     * @return 根级字典响应DTO列表
     */
    public List<DictionaryResponse> handleGetRootDictionariesQuery(String tenantId) {
        try {
            List<Dictionary> dictionaries = dictionaryRepository.findRootDictionaries(tenantId);
            return dictionaries.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 查询系统预置字典列表
     * @param tenantId 租户ID
     * @return 系统预置字典响应DTO列表
     */
    public List<DictionaryResponse> handleGetSystemPresetQuery(String tenantId) {
        try {
            List<Dictionary> dictionaries = dictionaryRepository.findSystemPresetDictionaries(tenantId);
            return dictionaries.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 查询激活状态的字典列表
     * @param tenantId 租户ID
     * @return 激活字典响应DTO列表
     */
    public List<DictionaryResponse> handleGetActiveQuery(String tenantId) {
        try {
            List<Dictionary> dictionaries = dictionaryRepository.findActiveDictionaries(tenantId);
            return dictionaries.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * 按类型和状态统计字典数量
     * @param dictTypeCode 字典类型代码
     * @param dictStatusCode 字典状态代码  
     * @param tenantId 租户ID
     * @return 字典数量
     */
    public long handleCountByTypeAndStatusQuery(String dictTypeCode, String dictStatusCode, String tenantId) {
        try {
            ConfigType dictType = ConfigType.fromCode(dictTypeCode);
            ConfigStatus dictStatus = ConfigStatus.fromCode(dictStatusCode);
            return dictionaryRepository.countByTypeAndStatus(dictType, dictStatus, tenantId);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 检查字典编码是否存在
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 是否存在
     */
    public boolean handleExistsByCodeQuery(String dictCode, String tenantId) {
        try {
            return dictionaryRepository.existsByDictCode(dictCode, tenantId);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取字典层级信息查询
     * @param dictId 字典ID
     * @return 字典层级路径数组
     */
    public String[] handleGetHierarchyQuery(String dictId) {
        try {
            Optional<Dictionary> dictionaryOpt = dictionaryRepository.findById(dictId);
            if (!dictionaryOpt.isPresent()) {
                return new String[0];
            }
            
            return dictionaryOpt.get().getHierarchy();
        } catch (Exception e) {
            return new String[0];
        }
    }
    
    /**
     * 检查字典是否为根级查询
     * @param dictId 字典ID
     * @return 是否为根级
     */
    public boolean handleIsRootLevelQuery(String dictId) {
        try {
            Optional<Dictionary> dictionaryOpt = dictionaryRepository.findById(dictId);
            if (!dictionaryOpt.isPresent()) {
                return false;
            }
            
            return dictionaryOpt.get().isRootLevel();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查字典是否为指定类型查询
     * @param dictId 字典ID
     * @param dictTypeCode 字典类型代码
     * @return 是否为指定类型
     */
    public boolean handleIsOfTypeQuery(String dictId, String dictTypeCode) {
        try {
            Optional<Dictionary> dictionaryOpt = dictionaryRepository.findById(dictId);
            if (!dictionaryOpt.isPresent()) {
                return false;
            }
            
            ConfigType type = ConfigType.fromCode(dictTypeCode);
            return dictionaryOpt.get().isOfType(type);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 转换Dictionary实体为响应DTO
     * @param dictionary 字典实体
     * @return 字典响应DTO
     */
    private DictionaryResponse convertToResponse(Dictionary dictionary) {
        return dictionaryAssembler.toResponse(dictionary);
    }
}