package com.jiuxi.shared.common.base.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Collections;
import java.util.List;

/**
 * 分页响应类
 * 封装分页查询结果
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageResponse<T> extends BaseResponse<List<T>> {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long size;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 默认构造函数
     */
    public PageResponse() {
        super();
    }

    /**
     * 构造函数
     */
    public PageResponse(List<T> records, Long current, Long size, Long total) {
        super();
        this.setData(records);
        this.current = current;
        this.size = size;
        this.total = total;
        this.pages = calculatePages(total, size);
        this.hasPrevious = current > 1;
        this.hasNext = current < this.pages;
    }

    /**
     * 成功分页响应
     */
    public static <T> PageResponse<T> success(List<T> records, Long current, Long size, Long total) {
        PageResponse<T> response = new PageResponse<>(records, current, size, total);
        response.setCode("200");
        response.setMessage("查询成功");
        return response;
    }

    /**
     * 空分页响应
     */
    public static <T> PageResponse<T> empty(Long current, Long size) {
        return success(Collections.emptyList(), current, size, 0L);
    }

    /**
     * 计算总页数
     */
    private Long calculatePages(Long total, Long size) {
        if (total == null || size == null || total <= 0 || size <= 0) {
            return 0L;
        }
        return (total + size - 1) / size;
    }

    /**
     * 是否为空页面
     */
    public boolean isEmpty() {
        return getData() == null || getData().isEmpty();
    }

    /**
     * 获取记录数量
     */
    public int getRecordCount() {
        return getData() == null ? 0 : getData().size();
    }

    /**
     * 是否为第一页
     */
    public boolean isFirstPage() {
        return current != null && current.equals(1L);
    }

    /**
     * 是否为最后一页
     */
    public boolean isLastPage() {
        return current != null && current.equals(pages);
    }
}