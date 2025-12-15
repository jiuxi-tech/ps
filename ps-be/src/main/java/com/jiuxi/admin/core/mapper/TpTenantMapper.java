package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpTenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: TpTenantMapper
 * @Description: 租户表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpTenantMapper extends BaseMapper<TpTenant> {
	
}
