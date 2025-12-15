package com.jiuxi.module.org.app.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpEntAccountQuery;
import com.jiuxi.admin.core.bean.query.TpEntBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpEntAccountVO;
import com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO;
import com.jiuxi.admin.core.service.TpEntBasicinfoService;
import com.jiuxi.module.org.app.service.EnterpriseApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: EnterpriseApplicationServiceImpl
 * @Description: 企业应用服务实现 - 委托给原有服务保证功能不变
 * @Author DDD重构
 * @Date 2025-09-12
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service
public class EnterpriseApplicationServiceImpl implements EnterpriseApplicationService {

    @Autowired
    private TpEntBasicinfoService tpEntBasicinfoService;

    @Override
    public IPage queryPage(TpEntBasicQuery query, String ascnId) {
        return tpEntBasicinfoService.queryPage(query, ascnId);
    }

    @Override
    public IPage<TpEntAccountVO> accountQueryPage(TpEntAccountQuery query, String jwtpid, String jwtaid) {
        return tpEntBasicinfoService.accountQueryPage(query, jwtpid, jwtaid);
    }

    @Override
    public IPage<TpEntAccountVO> adminQueryPage(TpEntAccountQuery query) {
        return tpEntBasicinfoService.adminQueryPage(query);
    }

    @Override
    public TpEntBasicinfoVO view(String entId) {
        return tpEntBasicinfoService.view(entId);
    }

    @Override
    public String selectByEntUnifiedCode(String entUnifiedCode) {
        return tpEntBasicinfoService.selectByEntUnifiedCode(entUnifiedCode);
    }

    @Override
    public boolean existsEntUnifiedCode(String entUnifiedCode, String entId) {
        return tpEntBasicinfoService.existsEntUnifiedCode(entUnifiedCode, entId);
    }

    @Override
    public String add(TpEntBasicinfoVO vo, String jwtpid) {
        return tpEntBasicinfoService.add(vo, jwtpid);
    }

    @Override
    public String addInfo(TpEntBasicinfoVO vo, String jwtpid) {
        return tpEntBasicinfoService.addInfo(vo, jwtpid);
    }

    @Override
    public int update(TpEntBasicinfoVO vo, String jwtpid) {
        return tpEntBasicinfoService.update(vo, jwtpid);
    }

    @Override
    public int delete(String entId, String jwtpid) {
        return tpEntBasicinfoService.delete(entId, jwtpid);
    }

    @Override
    public TpEntBasicinfoVO getBaseInfoByEntUnifiedCode(String entUnifiedCode) {
        return tpEntBasicinfoService.getBaseInfoByEntUnifiedCode(entUnifiedCode);
    }

    @Override
    public TpEntBasicinfoVO getBaseInfoIncludeNotActive(String entId) {
        return tpEntBasicinfoService.getBaseInfoIncludeNotActive(entId);
    }

    @Override
    public boolean existsByEntId(String entId) {
        return tpEntBasicinfoService.existsByEntId(entId);
    }
}