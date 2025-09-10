package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpDeptExinfo;
import com.jiuxi.admin.core.bean.vo.TpDeptExinfoVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: TpDeptExinfoMapper
 * @Description: 部门扩展信息表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpDeptExinfoMapper {

    int save(TpDeptExinfo bean);

    TpDeptExinfoVO view(String deptId);

    int count(String deptId);

    int update(TpDeptExinfo bean);

    String getLineCode(String deptId);
}
