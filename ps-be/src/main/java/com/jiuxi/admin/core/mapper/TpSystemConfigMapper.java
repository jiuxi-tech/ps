package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.entity.TpSystemConfig;
import com.jiuxi.admin.core.bean.query.TpSystemConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统配置Mapper接口
 * @author system
 * @date 2025-01-20
 */
@Mapper
public interface TpSystemConfigMapper extends BaseMapper<TpSystemConfig> {

    /**
     * 根据配置键获取配置值
     * @param configKey 配置键
     * @return 配置值
     */
    String getConfigValue(@Param("configKey") String configKey);

    /**
     * 获取所有配置
     * @return 配置列表
     */
    List<TpSystemConfig> selectList();

    /**
     * 分页查询配置
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpSystemConfig> selectPage(IPage<TpSystemConfig> page, @Param("query") TpSystemConfigQuery query);
}