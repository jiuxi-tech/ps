package com.jiuxi.module.org.infra.performance;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 批量操作工具
 * 提供高效的批量处理操作，优化大数据集的性能
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
public class BatchOperationUtil {
    
    /**
     * 默认批处理大小
     */
    private static final int DEFAULT_BATCH_SIZE = 1000;
    
    /**
     * 分批执行操作
     * 将大列表分割成小批次处理，避免内存溢出和数据库超时
     * 
     * @param items 待处理的项目列表
     * @param batchSize 批处理大小
     * @param processor 批处理器
     * @param <T> 项目类型
     */
    public <T> void processBatch(List<T> items, int batchSize, Consumer<List<T>> processor) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        for (int i = 0; i < items.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, items.size());
            List<T> batch = items.subList(i, endIndex);
            processor.accept(batch);
        }
    }
    
    /**
     * 分批执行操作（使用默认批大小）
     * 
     * @param items 待处理的项目列表
     * @param processor 批处理器
     * @param <T> 项目类型
     */
    public <T> void processBatch(List<T> items, Consumer<List<T>> processor) {
        processBatch(items, DEFAULT_BATCH_SIZE, processor);
    }
    
    /**
     * 分批执行并收集结果
     * 
     * @param items 待处理的项目列表
     * @param batchSize 批处理大小
     * @param processor 批处理器，返回处理结果
     * @param <T> 输入类型
     * @param <R> 输出类型
     * @return 所有批次的处理结果
     */
    public <T, R> List<R> processBatchWithResults(List<T> items, int batchSize, 
                                                Function<List<T>, List<R>> processor) {
        if (items == null || items.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<R> allResults = new ArrayList<>();
        
        for (int i = 0; i < items.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, items.size());
            List<T> batch = items.subList(i, endIndex);
            List<R> batchResults = processor.apply(batch);
            if (batchResults != null) {
                allResults.addAll(batchResults);
            }
        }
        
        return allResults;
    }
    
    /**
     * 批量插入优化
     * 使用批量插入减少数据库交互次数
     * 
     * @param items 待插入的实体列表
     * @param insertFunction 插入函数
     * @param <T> 实体类型
     * @return 插入成功的数量
     */
    @Transactional
    public <T> int batchInsert(List<T> items, Function<List<T>, Integer> insertFunction) {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        
        final int OPTIMAL_BATCH_SIZE = 500; // MySQL建议的批量插入大小
        int totalInserted = 0;
        
        for (int i = 0; i < items.size(); i += OPTIMAL_BATCH_SIZE) {
            int endIndex = Math.min(i + OPTIMAL_BATCH_SIZE, items.size());
            List<T> batch = items.subList(i, endIndex);
            
            Integer batchResult = insertFunction.apply(batch);
            totalInserted += (batchResult != null ? batchResult : 0);
        }
        
        return totalInserted;
    }
    
    /**
     * 批量更新优化
     * 
     * @param items 待更新的实体列表
     * @param updateFunction 更新函数
     * @param <T> 实体类型
     * @return 更新成功的数量
     */
    @Transactional
    public <T> int batchUpdate(List<T> items, Function<List<T>, Integer> updateFunction) {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        
        final int OPTIMAL_BATCH_SIZE = 200; // 更新操作建议更小的批大小
        int totalUpdated = 0;
        
        for (int i = 0; i < items.size(); i += OPTIMAL_BATCH_SIZE) {
            int endIndex = Math.min(i + OPTIMAL_BATCH_SIZE, items.size());
            List<T> batch = items.subList(i, endIndex);
            
            Integer batchResult = updateFunction.apply(batch);
            totalUpdated += (batchResult != null ? batchResult : 0);
        }
        
        return totalUpdated;
    }
    
    /**
     * 批量删除优化
     * 
     * @param ids 待删除的ID列表
     * @param deleteFunction 删除函数
     * @return 删除成功的数量
     */
    @Transactional
    public int batchDelete(List<String> ids, Function<List<String>, Integer> deleteFunction) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        
        final int OPTIMAL_BATCH_SIZE = 1000; // 删除操作可以使用较大的批大小
        int totalDeleted = 0;
        
        for (int i = 0; i < ids.size(); i += OPTIMAL_BATCH_SIZE) {
            int endIndex = Math.min(i + OPTIMAL_BATCH_SIZE, ids.size());
            List<String> batch = ids.subList(i, endIndex);
            
            Integer batchResult = deleteFunction.apply(batch);
            totalDeleted += (batchResult != null ? batchResult : 0);
        }
        
        return totalDeleted;
    }
    
    /**
     * 去重合并操作
     * 在批量操作前进行去重，提高性能
     * 
     * @param items 待处理的项目列表
     * @param keyExtractor 唯一键提取器
     * @param <T> 项目类型
     * @param <K> 键类型
     * @return 去重后的列表
     */
    public <T, K> List<T> deduplicateByKey(List<T> items, Function<T, K> keyExtractor) {
        if (items == null || items.isEmpty()) {
            return new ArrayList<>();
        }
        
        Map<K, T> uniqueItems = new LinkedHashMap<>();
        for (T item : items) {
            K key = keyExtractor.apply(item);
            if (key != null) {
                uniqueItems.put(key, item);
            }
        }
        
        return new ArrayList<>(uniqueItems.values());
    }
    
    /**
     * 分组批量操作
     * 根据某个属性将数据分组，然后对每组进行批量处理
     * 
     * @param items 待处理的项目列表
     * @param groupKeyExtractor 分组键提取器
     * @param groupProcessor 分组处理器
     * @param <T> 项目类型
     * @param <K> 分组键类型
     */
    public <T, K> void processGroupedBatch(List<T> items, Function<T, K> groupKeyExtractor, 
                                         Consumer<List<T>> groupProcessor) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        Map<K, List<T>> groupedItems = items.stream()
                .collect(Collectors.groupingBy(groupKeyExtractor));
        
        for (List<T> group : groupedItems.values()) {
            groupProcessor.accept(group);
        }
    }
    
    /**
     * 并行批量处理
     * 使用并行流进行批量处理，适用于CPU密集型操作
     * 
     * @param items 待处理的项目列表
     * @param batchSize 批处理大小
     * @param processor 批处理器
     * @param <T> 项目类型
     */
    public <T> void processParallelBatch(List<T> items, int batchSize, Consumer<List<T>> processor) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        // 将列表分割成批次
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < items.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, items.size());
            batches.add(items.subList(i, endIndex));
        }
        
        // 并行处理批次
        batches.parallelStream().forEach(processor);
    }
    
    /**
     * 验证批量操作结果
     * 检查批量操作是否成功，提供错误处理建议
     * 
     * @param expectedCount 期望处理的数量
     * @param actualCount 实际处理的数量
     * @param operationType 操作类型（用于日志）
     * @return 是否成功
     */
    public boolean validateBatchResult(int expectedCount, int actualCount, String operationType) {
        if (expectedCount == actualCount) {
            return true;
        }
        
        // 记录不一致的情况
        System.err.printf("批量%s操作结果不一致: 期望=%d, 实际=%d, 差异=%d%n", 
                operationType, expectedCount, actualCount, expectedCount - actualCount);
        
        return false;
    }
    
    /**
     * 计算最优批大小
     * 根据数据大小和系统资源动态计算最优的批处理大小
     * 
     * @param totalItems 总项目数
     * @param itemSizeEstimate 单个项目的大小估计（字节）
     * @param maxMemoryMB 最大内存限制（MB）
     * @return 最优批大小
     */
    public int calculateOptimalBatchSize(int totalItems, int itemSizeEstimate, int maxMemoryMB) {
        // 基于内存限制计算
        long maxMemoryBytes = maxMemoryMB * 1024L * 1024L;
        long maxItemsInMemory = maxMemoryBytes / itemSizeEstimate;
        
        // 确保批大小在合理范围内
        int memoryBasedBatchSize = (int) Math.min(maxItemsInMemory, totalItems);
        
        // 应用经验值限制
        int minBatchSize = 100;
        int maxBatchSize = 5000;
        
        return Math.max(minBatchSize, Math.min(maxBatchSize, memoryBasedBatchSize));
    }
}