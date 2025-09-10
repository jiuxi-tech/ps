package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpRichtext;
import com.jiuxi.admin.core.bean.vo.TpRichtextVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpRichtextMapper
 * @Description:
 * @Author pand
 * @Date 2021-04-27 14:29:12
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpRichtextMapper {
    /**
     * 新增
     *
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    int add(TpRichtext bean);

    /**
     * 根据业务id和分类修改
     *
     * @return int
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    int update(TpRichtext bean);

    /**
     * 查看
     *
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    TpRichtextVO view(String txtId);

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
    TpRichtextVO selectByReferIdAndType(@Param("referId") String referId, @Param("txtType") String txtType);

    /**
     * 根据id删除
     *
     * @param referId
     * @return void
     * @author 杨占锐
     * @date 2024/9/2 15:27
     */
    int deleteByReferId(String referId);

    /**
     * 根据id和类型删除
     *
     * @param referId
     * @return void
     * @author 杨占锐
     * @date 2024/9/2 15:27
     */
    void deleteByReferIdAndType(@Param("referId") String referId, @Param("txtType") String txtType);
}
