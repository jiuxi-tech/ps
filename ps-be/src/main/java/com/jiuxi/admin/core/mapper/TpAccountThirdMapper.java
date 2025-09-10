package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpAccountThird;
import com.jiuxi.admin.core.bean.query.TpAccountThirdQuery;
import com.jiuxi.admin.core.bean.vo.TpAccountThirdVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: IotThreeAccountMapper
 * @Description: 合作方管理表
 * @Author pand
 * @Date 2022-04-20 15:02:39
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpAccountThirdMapper {

    IPage<TpAccountThirdVO> getPage(Page<TpAccountThirdVO> page, @Param("query") TpAccountThirdQuery query);

    int add(TpAccountThird bean);

    int reset(TpAccountThird bean);

    TpAccountThirdVO view(String id);

    int delete(@Param("id") String id, @Param("updateTime") String updateTime, @Param("updator") String updator);

}
