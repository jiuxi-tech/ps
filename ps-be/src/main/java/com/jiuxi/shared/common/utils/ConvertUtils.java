package com.jiuxi.shared.common.utils;

import com.jiuxi.shared.common.base.dto.BaseDTO;
import com.jiuxi.shared.common.base.entity.BaseEntity;
import com.jiuxi.shared.common.base.response.BaseResponse;
import com.jiuxi.shared.common.base.response.PageResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 类型转换工具类
 * 提供Entity、DTO、Response之间的转换功能
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Entity转DTO
     * 
     * @param entity    实体对象
     * @param dtoClass  DTO类型
     * @param <E>       实体类型
     * @param <D>       DTO类型
     * @return DTO对象
     */
    public static <E extends BaseEntity, D extends BaseDTO> D entityToDto(E entity, Class<D> dtoClass) {
        if (entity == null || dtoClass == null) {
            return null;
        }

        try {
            D dto = dtoClass.newInstance();
            BeanUtils.copyPropertiesIgnoreNull(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Entity to DTO conversion failed", e);
        }
    }

    /**
     * DTO转Entity
     * 
     * @param dto         DTO对象
     * @param entityClass Entity类型
     * @param <D>         DTO类型
     * @param <E>         实体类型
     * @return Entity对象
     */
    public static <D extends BaseDTO, E extends BaseEntity> E dtoToEntity(D dto, Class<E> entityClass) {
        if (dto == null || entityClass == null) {
            return null;
        }

        try {
            E entity = entityClass.newInstance();
            BeanUtils.copyPropertiesIgnoreNull(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("DTO to Entity conversion failed", e);
        }
    }

    /**
     * Entity列表转DTO列表
     * 
     * @param entityList Entity列表
     * @param dtoClass   DTO类型
     * @param <E>        实体类型
     * @param <D>        DTO类型
     * @return DTO列表
     */
    public static <E extends BaseEntity, D extends BaseDTO> List<D> entityListToDtoList(
            List<E> entityList, Class<D> dtoClass) {
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }

        return entityList.stream()
                .map(entity -> entityToDto(entity, dtoClass))
                .collect(Collectors.toList());
    }

    /**
     * DTO列表转Entity列表
     * 
     * @param dtoList     DTO列表
     * @param entityClass Entity类型
     * @param <D>         DTO类型
     * @param <E>         实体类型
     * @return Entity列表
     */
    public static <D extends BaseDTO, E extends BaseEntity> List<E> dtoListToEntityList(
            List<D> dtoList, Class<E> entityClass) {
        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }

        return dtoList.stream()
                .map(dto -> dtoToEntity(dto, entityClass))
                .collect(Collectors.toList());
    }

    /**
     * 自定义转换
     * 
     * @param sourceList 源列表
     * @param converter  转换函数
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标列表
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Function<S, T> converter) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }

        return sourceList.stream()
                .map(converter)
                .collect(Collectors.toList());
    }

    /**
     * MyBatis Plus分页对象转分页响应
     * 
     * @param page      MyBatis Plus分页对象
     * @param dtoClass  DTO类型
     * @param <E>       实体类型
     * @param <D>       DTO类型
     * @return 分页响应
     */
    public static <E extends BaseEntity, D extends BaseDTO> PageResponse<D> pageToPageResponse(
            Page<E> page, Class<D> dtoClass) {
        if (page == null) {
            return PageResponse.empty(1L, 10L);
        }

        List<D> dtoList = entityListToDtoList(page.getRecords(), dtoClass);
        return PageResponse.success(dtoList, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * MyBatis Plus分页对象转分页响应（自定义转换）
     * 
     * @param page      MyBatis Plus分页对象
     * @param converter 转换函数
     * @param <E>       实体类型
     * @param <D>       DTO类型
     * @return 分页响应
     */
    public static <E, D> PageResponse<D> pageToPageResponse(Page<E> page, Function<E, D> converter) {
        if (page == null) {
            return PageResponse.empty(1L, 10L);
        }

        List<D> dtoList = convertList(page.getRecords(), converter);
        return PageResponse.success(dtoList, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 列表转成功响应
     * 
     * @param list 列表数据
     * @param <T>  数据类型
     * @return 成功响应
     */
    public static <T> BaseResponse<List<T>> listToResponse(List<T> list) {
        return BaseResponse.success(list != null ? list : Collections.emptyList());
    }

    /**
     * 单个对象转成功响应
     * 
     * @param data 数据对象
     * @param <T>  数据类型
     * @return 成功响应
     */
    public static <T> BaseResponse<T> dataToResponse(T data) {
        return BaseResponse.success(data);
    }

    /**
     * 更新Entity的审计字段
     * 
     * @param entity   实体对象
     * @param operator 操作者ID
     * @param operatorName 操作者姓名
     * @param isCreate 是否为创建操作
     * @param <E>      实体类型
     */
    public static <E extends BaseEntity> void fillAuditFields(E entity, String operator, 
            String operatorName, boolean isCreate) {
        if (entity == null) {
            return;
        }

        if (isCreate) {
            entity.setCreateBy(operator);
            entity.setCreateByName(operatorName);
        }
        entity.setUpdateBy(operator);
        entity.setUpdateByName(operatorName);
    }

    /**
     * 复制属性并填充审计字段
     * 
     * @param source   源对象
     * @param target   目标对象
     * @param operator 操作者ID
     * @param operatorName 操作者姓名
     * @param <S>      源类型
     * @param <T>      目标类型
     */
    public static <S, T extends BaseEntity> void copyAndFillAudit(S source, T target, 
            String operator, String operatorName) {
        if (source == null || target == null) {
            return;
        }

        BeanUtils.copyPropertiesIgnoreNull(source, target);
        
        // 判断是否为创建操作（ID为空表示新创建）
        boolean isCreate = StringUtils.isBlank(target.getId());
        fillAuditFields(target, operator, operatorName, isCreate);
    }
}