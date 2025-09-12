package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.module.user.infra.persistence.mapper.UserPersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 人员基础信息Mapper适配器实现 - 委托给新的用户模块Mapper
 * 
 * @deprecated 为了兼容旧代码而保留
 */
@Deprecated
@Component
public class TpPersonBasicinfoMapperAdapter implements TpPersonBasicinfoMapper {

    @Autowired
    private UserPersonMapper userPersonMapper;

    @Override
    public int insert(TpPersonBasicinfo record) {
        return userPersonMapper.save(record);
    }

    @Override
    public int updateByPrimaryKey(TpPersonBasicinfo record) {
        return userPersonMapper.update(record);
    }

    @Override
    public int deleteByPrimaryKey(String personId) {
        TpPersonBasicinfo bean = new TpPersonBasicinfo();
        bean.setPersonId(personId);
        return userPersonMapper.deleteByPersonId(bean);
    }

    @Override
    public TpPersonBasicinfo selectByPrimaryKey(String personId) {
        TpPersonBasicinfoVO vo = userPersonMapper.view(personId);
        if (vo == null) return null;
        TpPersonBasicinfo entity = new TpPersonBasicinfo();
        org.springframework.beans.BeanUtils.copyProperties(vo, entity);
        return entity;
    }

    @Override
    public List<TpPersonBasicinfo> selectByCondition(TpPersonBasicQuery condition) {
        return java.util.Collections.emptyList();
    }

    @Override
    public IPage<TpPersonBasicinfoVO> selectPageQuery(Page<TpPersonBasicinfoVO> page, TpPersonBasicQuery query) {
        return userPersonMapper.getPage(page, query);
    }

    @Override
    public List<TpPersonBasicinfoVO> selectList(TpPersonBasicQuery query) {
        Page<TpPersonBasicinfoVO> page = new Page<>(1, 1000);
        IPage<TpPersonBasicinfoVO> result = userPersonMapper.getPage(page, query);
        return result.getRecords();
    }

    @Override
    public TpPersonBasicinfo selectByPhone(String phone) {
        return userPersonMapper.selectByPhone(phone);
    }

    @Override
    public TpPersonBasicinfo selectByEmail(String email) {
        return userPersonMapper.selectByEmail(email);
    }

    @Override
    public TpPersonBasicinfo selectByIdCard(String idCard) {
        List<TpPersonBasicinfoVO> list = userPersonMapper.getBaseInfoByIdCard(idCard);
        if (list == null || list.isEmpty()) return null;
        TpPersonBasicinfo entity = new TpPersonBasicinfo();
        org.springframework.beans.BeanUtils.copyProperties(list.get(0), entity);
        return entity;
    }
    
    @Override
    public TpPersonBasicinfoVO view(String personId) {
        return userPersonMapper.view(personId);
    }
    
    @Override
    public IPage<TpPersonBasicinfoVO> getPage(Page<TpPersonBasicinfoVO> page, TpPersonBasicQuery query) {
        return userPersonMapper.getPage(page, query);
    }
    
    @Override
    public int save(TpPersonBasicinfo bean) {
        return userPersonMapper.save(bean);
    }
    
    @Override
    public int update(TpPersonBasicinfo bean) {
        return userPersonMapper.update(bean);
    }
    
    @Override
    public int deleteByPersonId(TpPersonBasicinfo bean) {
        return userPersonMapper.deleteByPersonId(bean);
    }
    
    @Override
    public TpPersonBasicinfo selectByPhoneAndPersonId(String phone, String personId) {
        return userPersonMapper.selectByPhoneAndPersonId(phone, personId);
    }
    
    @Override
    public List<String> selectPersonIdByAscnId(String ascnId) {
        return userPersonMapper.selectPersonIdByAscnId(ascnId);
    }
    
    @Override
    public void deletePersonDeptByAscnId(String ascnId, String updator, String updateTime) {
        userPersonMapper.deletePersonDeptByAscnId(ascnId, updator, updateTime);
    }
    
    @Override
    public void updateAscnIdByDeptIds(List<String> deptIds, String ascnId, String updateTime) {
        userPersonMapper.updateAscnIdByDeptIds(deptIds, ascnId, null);
    }
    
    @Override
    public List<TpPersonBasicinfoVO> getBaseInfoByIdCard(String idcard) {
        return userPersonMapper.getBaseInfoByIdCard(idcard);
    }
}