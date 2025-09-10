package com.jiuxi.mybatis.util;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 杨攀
 * @description: 分页工具类
 * @date 2020/6/8 10:49
 */
public class PageUtils implements Serializable {

    private static final long serialVersionUID = -3678071160843398317L;

    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页数
     */
    private int currPage;
    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 分页
     */
    public PageUtils(IPage<?> page) {
        this.list = page.getRecords();
        this.totalCount = (int) page.getTotal();
        this.pageSize = (int) page.getSize();
        this.currPage = (int) page.getCurrent();
        this.totalPage = (int) page.getPages();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    /**
     * 分表查询数据，如查询a_2021和a_2020,当a_2021不满足size时，需要从a_2020补n条满足一页数量。
     *
     * @param current  当前页
     * @param size     一页的数量
     * @param ontTotle 第一张表的总数量
     * @return java.util.Map<java.lang.String, java.lang.Integer>
     * * * skip：跳过skip条
     * * * limit：查询limit条
     * @author pand
     * @date 2021-08-25 11:18
     */
    public static Map<String, Integer> count(int current, int size, int ontTotle) {
        // 1。第一个集合的数据不足够当前页，查询第二个集合，先计算出需要查询的总条数
        int allNum = current * size;
        // 2。第二个集合需要补充的条数，查询的总条数 - 第一个集合的总数
        int surplusNum = allNum - ontTotle;
        // 3。第二个集合需要跳过 surplusNum - logQuery.getSize() 条 查询出 logQuery.getSize()条
        int skip;
        int limit;
        if (surplusNum <= size) {
            // 直接第一页查询 surplusNum 条
            skip = 0;
            limit = surplusNum;
        } else {
            // 跳过 surplusNum - logQuery.getSize() 条 查询出 logQuery.getSize()条
            skip = surplusNum - size;
            limit = size;
        }
        Map<String, Integer> map = new HashMap<>(2);
        map.put("skip", skip);
        map.put("limit", limit);

        return map;
    }
}
