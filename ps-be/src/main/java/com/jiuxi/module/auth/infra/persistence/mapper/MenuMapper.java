package com.jiuxi.module.auth.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.module.auth.infra.persistence.entity.MenuPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单Mapper接口
 * 定义菜单持久化对象的数据库操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuPO> {
    
}