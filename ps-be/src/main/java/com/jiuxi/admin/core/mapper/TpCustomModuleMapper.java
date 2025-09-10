package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpCustomModule;
import com.jiuxi.admin.core.bean.query.TpCustomModuleQuery;
import com.jiuxi.admin.core.bean.vo.TpCustomModuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpCustomModuleMapper
 * @Description: 自定义模块信息表 存储模块的信息，按钮信息、路由信息
 * @Author pand
 * @Date 2021-05-11 11:22:40
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpCustomModuleMapper extends BaseMapper<TpCustomModule> {

    IPage<TpCustomModuleVO> getPage(Page<TpCustomModuleVO> page, @Param("query") TpCustomModuleQuery query);

    int add(TpCustomModule bean);

    int update(TpCustomModule bean);

    TpCustomModuleVO view(String id);

    TpCustomModuleVO selectByMcode(String mcode);

    int delete(String id);

}
