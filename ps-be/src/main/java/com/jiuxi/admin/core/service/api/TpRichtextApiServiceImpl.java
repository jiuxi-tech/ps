package com.jiuxi.admin.core.service.api;

import cn.hutool.core.bean.BeanUtil;
import com.jiuxi.admin.core.bean.vo.TpRichtextVO;
import com.jiuxi.admin.core.service.TpRichtextService;
// import com.jiuxi.plugin.api.bean.dto.TpRichtextDTO;
// import com.jiuxi.plugin.api.interfaces.TpRichtextApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: TpRichtextApiServiceImpl
 * @Description:
 * @Author jiuxx
 * @Date 2024/9/2 15:36
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service
public class TpRichtextApiServiceImpl /* implements TpRichtextApiService */ {

    @Autowired
    private TpRichtextService tpRichtextService;

    /**
     * 新增
     *
     * @param referId 业务id
     * @param content 内容
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/9/2 15:24
     */
    public String add(String referId, String content) {
        return tpRichtextService.add(referId, content);
    }

    /**
     * 新增
     *
     * @param referId 业务id
     * @param txtType 业务分类
     * @param content 内容
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/9/2 15:24
     */
    public String add(String referId, String txtType, String content) {
        return tpRichtextService.add(referId, txtType, content);
    }

    /**
     * 根据业务id修改
     *
     * @param referId 业务id
     * @param content 内容
     * @return int
     * @author jiuxx
     * @date 2024/9/2 15:24
     */
    public int update(String referId, String content) {
        return tpRichtextService.update(referId, content);
    }

    /**
     * 根据业务id和分类修改
     *
     * @param referId 业务id
     * @param txtType 业务分类
     * @param content 内容
     * @return int
     * @author jiuxx
     * @date 2024/9/2 15:24
     */
    public int update(String referId, String txtType, String content) {
        return tpRichtextService.update(referId, txtType, content);
    }

    /**
     * 查看
     *
     * @param id
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author jiuxx
     * @date 2024/9/2 15:24
     */
    /*
    @Override
    public TpRichtextDTO view(String id) {
        TpRichtextVO view = tpRichtextService.view(id);
        return BeanUtil.copyProperties(view, TpRichtextDTO.class);
    }
    */

    /**
     * 根据业务id查询
     *
     * @param referId
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author jiuxx
     * @date 2024/9/2 15:25
     */
    /*
    @Override
    public TpRichtextDTO selectByReferId(String referId) {
        TpRichtextVO view = tpRichtextService.selectByReferId(referId);
        return BeanUtil.copyProperties(view, TpRichtextDTO.class);
    }
    */

    /**
     * 根据业务id和分类查询
     *
     * @param referId
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author jiuxx
     * @date 2024/9/2 15:25
     */
    /*
    @Override
    public TpRichtextDTO selectByReferId(String referId, String txtType) {
        TpRichtextVO view = tpRichtextService.selectByReferId(referId, txtType);
        return BeanUtil.copyProperties(view, TpRichtextDTO.class);
    }
    */

    /**
     * 根据id删除
     *
     * @param referId
     * @return void
     * @author jiuxx
     * @date 2024/9/2 15:27
     */
    /*
    @Override
    public void deleteByReferId(String referId) {
        tpRichtextService.deleteByReferId(referId);
    }
    */

    /**
     * 根据id和类型删除
     *
     * @param referId
     * @return void
     * @author jiuxx
     * @date 2024/9/2 15:27
     */
    /*
    @Override
    public void deleteByReferId(String referId, String txtType) {
        tpRichtextService.deleteByReferId(referId, txtType);
    }
    */
}
