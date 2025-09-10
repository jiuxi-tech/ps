package com.jiuxi.admin.domain;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模块依赖管理器
 * 
 * 负责检查和管理模块间的依赖关系，确保模块边界的完整性
 * 
 * @author 系统重构
 * @since 2.2.2
 */
@Component
public class ModuleDependencyManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ModuleDependencyManager.class);
    
    @Autowired
    private ApplicationContext applicationContext;
    
    private Map<String, ModuleBoundary> moduleRegistry = new HashMap<>();
    
    @PostConstruct
    public void initialize() {
        scanModuleBoundaries();
        validateModuleDependencies();
    }
    
    /**
     * 扫描所有模块边界注解
     */
    private void scanModuleBoundaries() {
        // 这里可以通过反射扫描所有带有@ModuleBoundary注解的类和包
        // 简化实现，手动注册已知模块
        registerKnownModules();
    }
    
    /**
     * 注册已知模块
     */
    private void registerKnownModules() {
        // 用户管理模块
        ModuleBoundary userModule = createModuleBoundary(
            "user", 
            new String[]{"common", "security"}, 
            new String[]{"organization", "authorization"},
            "用户管理模块"
        );
        moduleRegistry.put("user", userModule);
        
        // 组织架构模块
        ModuleBoundary orgModule = createModuleBoundary(
            "organization", 
            new String[]{"common"}, 
            new String[]{"user"},
            "组织架构管理模块"
        );
        moduleRegistry.put("organization", orgModule);
        
        // 权限管理模块
        ModuleBoundary authModule = createModuleBoundary(
            "authorization", 
            new String[]{"common", "user", "organization"}, 
            new String[]{},
            "权限管理模块"
        );
        moduleRegistry.put("authorization", authModule);
        
        // 系统管理模块
        ModuleBoundary sysModule = createModuleBoundary(
            "system", 
            new String[]{"common"}, 
            new String[]{"user", "organization", "authorization"},
            "系统管理模块"
        );
        moduleRegistry.put("system", sysModule);
    }
    
    /**
     * 创建模块边界定义
     */
    private ModuleBoundary createModuleBoundary(String module, String[] allowed, String[] forbidden, String desc) {
        return new ModuleBoundary() {
            @Override
            public String module() { return module; }
            
            @Override
            public String[] allowedDependencies() { return allowed; }
            
            @Override
            public String[] forbiddenDependencies() { return forbidden; }
            
            @Override
            public boolean publicApi() { return false; }
            
            @Override
            public String description() { return desc; }
            
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return ModuleBoundary.class;
            }
        };
    }
    
    /**
     * 验证模块依赖关系
     */
    private void validateModuleDependencies() {
        logger.info("开始验证模块依赖关系...");
        
        for (Map.Entry<String, ModuleBoundary> entry : moduleRegistry.entrySet()) {
            String moduleName = entry.getKey();
            ModuleBoundary boundary = entry.getValue();
            
            logger.info("验证模块: {} - {}", moduleName, boundary.description());
            
            // 检查允许的依赖
            Set<String> allowedDeps = Arrays.stream(boundary.allowedDependencies())
                .collect(Collectors.toSet());
            logger.debug("允许依赖: {}", allowedDeps);
            
            // 检查禁止的依赖
            Set<String> forbiddenDeps = Arrays.stream(boundary.forbiddenDependencies())
                .collect(Collectors.toSet());
            logger.debug("禁止依赖: {}", forbiddenDeps);
            
            // 验证依赖关系的一致性
            validateDependencyConsistency(moduleName, allowedDeps, forbiddenDeps);
        }
        
        logger.info("模块依赖关系验证完成");
    }
    
    /**
     * 验证依赖关系的一致性
     */
    private void validateDependencyConsistency(String module, Set<String> allowed, Set<String> forbidden) {
        // 检查是否有冲突的依赖定义
        Set<String> conflicts = allowed.stream()
            .filter(forbidden::contains)
            .collect(Collectors.toSet());
            
        if (!conflicts.isEmpty()) {
            logger.warn("模块 {} 存在冲突的依赖定义: {}", module, conflicts);
        }
        
        // 检查循环依赖
        checkCircularDependencies(module, allowed, new HashSet<>());
    }
    
    /**
     * 检查循环依赖
     */
    private void checkCircularDependencies(String module, Set<String> dependencies, Set<String> visited) {
        if (visited.contains(module)) {
            logger.warn("检测到循环依赖: {}", visited);
            return;
        }
        
        visited.add(module);
        
        for (String dep : dependencies) {
            if (moduleRegistry.containsKey(dep)) {
                ModuleBoundary depBoundary = moduleRegistry.get(dep);
                Set<String> depDependencies = Arrays.stream(depBoundary.allowedDependencies())
                    .collect(Collectors.toSet());
                checkCircularDependencies(dep, depDependencies, new HashSet<>(visited));
            }
        }
    }
    
    /**
     * 检查模块是否可以依赖另一个模块
     */
    public boolean canDependOn(String fromModule, String toModule) {
        ModuleBoundary boundary = moduleRegistry.get(fromModule);
        if (boundary == null) {
            return true; // 未定义边界的模块默认允许
        }
        
        Set<String> allowed = Arrays.stream(boundary.allowedDependencies())
            .collect(Collectors.toSet());
        Set<String> forbidden = Arrays.stream(boundary.forbiddenDependencies())
            .collect(Collectors.toSet());
            
        if (forbidden.contains(toModule)) {
            return false;
        }
        
        return allowed.isEmpty() || allowed.contains(toModule);
    }
    
    /**
     * 获取模块信息
     */
    public Map<String, ModuleBoundary> getModuleRegistry() {
        return Collections.unmodifiableMap(moduleRegistry);
    }
}