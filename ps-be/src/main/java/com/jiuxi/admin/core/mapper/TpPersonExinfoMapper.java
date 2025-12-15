package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpPersonExinfo;
import com.jiuxi.admin.core.bean.vo.TpPersonExinfoVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: TpPersonExinfoMapper
 * @Description: 人员基本信息扩展表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpPersonExinfoMapper {

    int save(TpPersonExinfo bean);

    TpPersonExinfoVO view(String personId);

    int count(String personId);

    int update(TpPersonExinfo bean);

}
