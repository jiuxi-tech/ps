package com.jiuxi.shared.infrastructure.persistence.util;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.shared.infrastructure.persistence.xss.SQLFilter;

import java.util.Map;

/**
 * @description: 查询参数
 * @author 杨攀
 * @date 2020/6/8 10:57
 */
public class Query<T> {

    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "order_index";

    /**
     * 排序方式
     */
    public static final String ORDER = "order";

    /**
     *  升序
     */
    public static final String ASC = "asc";


    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(params.get(PAGE) != null){
            curPage = Long.parseLong((String)params.get(PAGE));
        }
        if(params.get(LIMIT) != null){
            limit = Long.parseLong((String)params.get(LIMIT));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String)params.get(ORDER_FIELD));
        String order = (String)params.get(ORDER);


        //前端字段排序
        if(StrUtil.isNotEmpty(orderField) && StrUtil.isNotEmpty(order)){
            if(ASC.equalsIgnoreCase(order)) {
                return  page.addOrder(OrderItem.asc(orderField));
            }else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if(StrUtil.isBlank(defaultOrderField)){
            return page;
        }

        //默认排序
        if(isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        }else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }
}
