package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.admin.core.bean.entity.TpCustomForm;
import com.jiuxi.admin.core.bean.vo.TpCustomFormVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpCustomFormMapper
 * @Description: 表单设计表 表单设计表
 * @Author pand
 * @Date 2021-05-11 11:22:40
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpCustomFormMapper extends BaseMapper<TpCustomForm> {

    int add(TpCustomForm bean);

    int update(TpCustomForm bean);

    TpCustomFormVO viewByFid(@Param("fid") String fid);

    TpCustomFormVO view(@Param("mid") String mid, @Param("ftype") String ftype, @Param("fSource") String fSource);

    TpCustomFormVO viewByMcode(@Param("mcode") String mcode, @Param("ftype") String ftype, @Param("fSource") String fSource);

    int delete(String id);

}
