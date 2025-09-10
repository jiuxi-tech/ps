package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.vo.TpSignupVO;
import com.jiuxi.admin.core.bean.entity.TpSignup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: TpSignupMapper
 * @Description: 签约表 企业-中介，政府-中介签约绑定表，多对多关系
 * @Author pand
 * @Date 2020-11-30 20:53:39
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpSignupMapper {

    int save(TpSignup bean);

    int delete(TpSignup bean);

    List<TpSignupVO> selectByInterId(List<String> interIds);

    int deleteByInterIds(List<String> interIds);

}
