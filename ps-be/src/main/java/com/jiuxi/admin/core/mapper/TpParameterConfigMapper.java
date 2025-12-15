package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpParameterConfig;
import com.jiuxi.admin.core.bean.query.TpParameterConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpParameterConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpParameterConfigMapper
 * @Description: 参数配置表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpParameterConfigMapper {

    IPage<TpParameterConfigVO> getPage(Page<TpParameterConfigVO> page, @Param("query") TpParameterConfigQuery query);

    int add(TpParameterConfig bean);

    int update(TpParameterConfig bean);

    TpParameterConfigVO view(String id);

    /**
     * 删除参数， 更新唯一索引
     *
     * @param id
     * @param pmKey
     * @return int
     * @author jiuxx
     * @date 2024/5/17 16:06
     */
    int delete(@Param("id") String id, @Param("pmKey") String pmKey);

    /**
     * 根据配置key查询配置信息
     *
     * @param pmKey 配置key
     * @return
     */
    TpParameterConfigVO viewByPmKey(String pmKey);

}
