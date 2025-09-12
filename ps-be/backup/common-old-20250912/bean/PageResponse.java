package com.jiuxi.common.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: PageResponse
 * @Description: 标准化分页响应格式
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页码
     */
    private long current;

    /**
     * 每页大小
     */
    private long size;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 是否有上一页
     */
    private boolean hasPrevious;

    /**
     * 是否有下一页
     */
    private boolean hasNext;

    /**
     * 是否为第一页
     */
    private boolean isFirst;

    /**
     * 是否为最后一页
     */
    private boolean isLast;

    public PageResponse() {
    }

    public PageResponse(List<T> records, long total, long current, long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = size > 0 ? (total + size - 1) / size : 0;
        this.calculateFlags();
    }

    /**
     * 从MyBatis Plus的IPage创建PageResponse
     *
     * @param page IPage对象
     * @param <T>  数据类型
     * @return PageResponse
     */
    public static <T> PageResponse<T> of(IPage<T> page) {
        return new PageResponse<>(
                page.getRecords(),
                page.getTotal(),
                page.getCurrent(),
                page.getSize()
        );
    }

    /**
     * 创建空的分页响应
     *
     * @param current 当前页码
     * @param size    每页大小
     * @param <T>     数据类型
     * @return PageResponse
     */
    public static <T> PageResponse<T> empty(long current, long size) {
        return new PageResponse<>(List.of(), 0, current, size);
    }

    /**
     * 创建单页响应
     *
     * @param records 数据列表
     * @param <T>     数据类型
     * @return PageResponse
     */
    public static <T> PageResponse<T> of(List<T> records) {
        return new PageResponse<>(records, records.size(), 1, records.size());
    }

    /**
     * 创建自定义分页响应
     *
     * @param records 数据列表
     * @param total   总记录数
     * @param current 当前页码
     * @param size    每页大小
     * @param <T>     数据类型
     * @return PageResponse
     */
    public static <T> PageResponse<T> of(List<T> records, long total, long current, long size) {
        return new PageResponse<>(records, total, current, size);
    }

    /**
     * 计算分页标志
     */
    private void calculateFlags() {
        this.hasPrevious = current > 1;
        this.hasNext = current < pages;
        this.isFirst = current == 1;
        this.isLast = current == pages || pages == 0;
    }

    /**
     * 获取起始记录号（从1开始）
     *
     * @return 起始记录号
     */
    public long getStartRecord() {
        return total == 0 ? 0 : (current - 1) * size + 1;
    }

    /**
     * 获取结束记录号
     *
     * @return 结束记录号
     */
    public long getEndRecord() {
        return Math.min(current * size, total);
    }

    /**
     * 是否为空页面
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return records == null || records.isEmpty();
    }

    /**
     * 获取记录数量
     *
     * @return 记录数量
     */
    public int getRecordCount() {
        return records == null ? 0 : records.size();
    }

    // Getters and Setters
    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        this.pages = size > 0 ? (total + size - 1) / size : 0;
        this.calculateFlags();
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
        this.calculateFlags();
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
        this.pages = size > 0 ? (total + size - 1) / size : 0;
        this.calculateFlags();
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    @Override
    public String toString() {
        return "PageResponse{" +
                "records=" + (records != null ? records.size() : 0) + " items" +
                ", total=" + total +
                ", current=" + current +
                ", size=" + size +
                ", pages=" + pages +
                ", hasPrevious=" + hasPrevious +
                ", hasNext=" + hasNext +
                ", isFirst=" + isFirst +
                ", isLast=" + isLast +
                '}';
    }
}