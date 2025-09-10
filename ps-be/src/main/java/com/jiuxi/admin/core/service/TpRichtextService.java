package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.vo.TpRichtextVO;

/**
 * @ClassName: TpRichtextService
 * @Description:
 * @Author pand
 * @Date 2021-04-27 14:29:12
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpRichtextService {

    /**
     * 新增
     *
     * @param referId 业务id
     * @param content 内容
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    String add(String referId, String content);

    /**
     * 新增
     *
     * @param referId 业务id
     * @param txtType 业务分类
     * @param content 内容
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    String add(String referId, String txtType, String content);

    /**
     * 根据业务id修改
     *
     * @param referId 业务id
     * @param content 内容
     * @return int
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    int update(String referId, String content);

    /**
     * 根据业务id和分类修改
     *
     * @param referId 业务id
     * @param txtType 业务分类
     * @param content 内容
     * @return int
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    int update(String referId, String txtType, String content);

    /**
     * 查看
     *
     * @param id
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    TpRichtextVO view(String id);

    /**
     * 根据业务id查询
     *
     * @param referId
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author 杨占锐
     * @date 2024/9/2 15:25
     */
    TpRichtextVO selectByReferId(String referId);

    /**
     * 根据业务id和分类查询
     *
     * @param referId
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author 杨占锐
     * @date 2024/9/2 15:25
     */
    TpRichtextVO selectByReferId(String referId, String txtType);

    /**
     * 根据id删除
     *
     * @param referId
     * @return void
     * @author 杨占锐
     * @date 2024/9/2 15:27
     */
    void deleteByReferId(String referId);

    /**
     * 根据id和类型删除
     *
     * @param referId
     * @return void
     * @author 杨占锐
     * @date 2024/9/2 15:27
     */
    void deleteByReferId(String referId, String txtType);
}

