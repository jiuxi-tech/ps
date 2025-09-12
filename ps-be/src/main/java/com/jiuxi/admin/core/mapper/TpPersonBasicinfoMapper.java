package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人员基础信息Mapper适配器 - 为了兼容旧代码
 * 实际实现将委托给新的用户模块Mapper
 * 
 * @deprecated 请使用 com.jiuxi.module.user.infra.persistence.mapper.UserPersonMapper
 */
@Deprecated
@Mapper
public interface TpPersonBasicinfoMapper {

    int insert(TpPersonBasicinfo record);
    
    int updateByPrimaryKey(TpPersonBasicinfo record);
    
    int deleteByPrimaryKey(String personId);
    
    TpPersonBasicinfo selectByPrimaryKey(String personId);
    
    List<TpPersonBasicinfo> selectByCondition(TpPersonBasicQuery condition);
    
    IPage<TpPersonBasicinfoVO> selectPageQuery(Page<TpPersonBasicinfoVO> page, @Param("query") TpPersonBasicQuery query);
    
    List<TpPersonBasicinfoVO> selectList(@Param("query") TpPersonBasicQuery query);
    
    TpPersonBasicinfo selectByPhone(String phone);
    
    TpPersonBasicinfo selectByEmail(String email);
    
    TpPersonBasicinfo selectByIdCard(String idCard);
    
    // 添加缺失的方法签名以兼容现有代码调用
    TpPersonBasicinfoVO view(String personId);
    
    IPage<TpPersonBasicinfoVO> getPage(Page<TpPersonBasicinfoVO> page, @Param("query") TpPersonBasicQuery query);
    
    int save(TpPersonBasicinfo bean);
    
    int update(TpPersonBasicinfo bean);
    
    int deleteByPersonId(TpPersonBasicinfo bean);
    
    TpPersonBasicinfo selectByPhoneAndPersonId(@Param("phone") String phone, @Param("personId") String personId);
    
    List<String> selectPersonIdByAscnId(String ascnId);
    
    void deletePersonDeptByAscnId(@Param("ascnId") String ascnId, @Param("updator") String updator, @Param("updateTime") String updateTime);
    
    void updateAscnIdByDeptIds(@Param("deptIds") List<String> deptIds, @Param("ascnId") String ascnId, @Param("updateTime") String updateTime);
    
    List<TpPersonBasicinfoVO> getBaseInfoByIdCard(String idcard);
}