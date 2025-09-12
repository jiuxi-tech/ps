package com.jiuxi.admin.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonExinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.service.TpPersonBasicinfoService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.module.user.app.service.UserPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 人员基础信息服务适配器实现 - 委托给新的用户模块服务
 * 
 * @deprecated 为了兼容旧代码而保留
 */
@Deprecated
@Service("tpPersonBasicinfoService")
public class TpPersonBasicinfoServiceAdapter implements TpPersonBasicinfoService {

    @Autowired
    private UserPersonService userPersonService;

    @Override
    public IPage<TpPersonBasicinfoVO> queryPage(TpPersonBasicQuery query) {
        return userPersonService.queryPage(query);
    }

    @Override
    public TpPersonBasicinfoVO add(TpPersonBasicinfoVO vo, String pid, int category) {
        return userPersonService.add(vo, pid, category);
    }

    @Override
    public int update(TpPersonBasicinfoVO vo, String pid) {
        return userPersonService.update(vo, pid);
    }

    @Override
    public void deletes(String deptIds, String personIds, String pid) {
        userPersonService.deletes(deptIds, personIds, pid);
    }

    @Override
    public TpPersonBasicinfoVO view(String personId, String deptId) {
        return userPersonService.view(personId, deptId);
    }
    
    @Override
    public TpPersonExinfoVO expAdd(TpPersonExinfoVO vo) {
        return userPersonService.expAdd(vo);
    }
    
    @Override
    public TpPersonExinfoVO expView(String personId) {
        return userPersonService.expView(personId);
    }

    @Override
    public List<TpPersonBasicinfoVO> personBasicinfoList(TpPersonBasicQuery query) {
        // Convert IPage to List by getting records
        IPage<TpPersonBasicinfoVO> page = userPersonService.queryPage(query);
        return page.getRecords();
    }

    @Override
    public void personBasicinfoExport(TpPersonBasicQuery query, HttpServletResponse response) {
        try {
            userPersonService.exportExcel(query, null, response);
        } catch (Exception e) {
            throw new RuntimeException("Export failed", e);
        }
    }

    @Override
    public JsonResponse personBasicinfoImport(MultipartFile file) {
        try {
            return userPersonService.importExcel(file, null, null);
        } catch (Exception e) {
            return JsonResponse.failByMsg("Import failed: " + e.getMessage());
        }
    }

    @Override
    public List<TpPersonExinfoVO> personExinfoList(String personId) {
        // UserPersonService doesn't have this method, return empty list for now
        return java.util.Collections.emptyList();
    }

    @Override
    public int personExinfoUpdate(TpPersonExinfoVO vo) {
        // UserPersonService.expAdd returns TpPersonExinfoVO, assume success if no exception
        TpPersonExinfoVO result = userPersonService.expAdd(vo);
        return result != null ? 1 : 0;
    }

    @Override
    public List<TpPersonRoleVO> personRoleList(String personId) {
        return userPersonService.personRoles(null, personId);
    }

    @Override
    public int personRoleUpdate(String personId, String roleIds) {
        return userPersonService.auth(personId, null, roleIds);
    }
    
    @Override
    public int parttime(String personId, String deptIds) {
        return userPersonService.parttime(personId, deptIds);
    }
    
    @Override
    public List<TpPersonRoleVO> personRoles(String deptId, String personId) {
        return userPersonService.personRoles(deptId, personId);
    }
    
    @Override
    public int auth(String personId, String deptId, String roleIds) {
        return userPersonService.auth(personId, deptId, roleIds);
    }
    
    @Override
    public void exportExcel(TpPersonBasicQuery query, String jwtpid, HttpServletResponse response) throws Exception {
        userPersonService.exportExcel(query, jwtpid, response);
    }
    
    @Override
    public JsonResponse importExcel(MultipartFile file, String deptId, String jwtpid) throws Exception {
        return userPersonService.importExcel(file, deptId, jwtpid);
    }
    
    @Override
    public void downloadTemplate(HttpServletResponse response) throws Exception {
        userPersonService.downloadTemplate(response);
    }
    
    @Override
    public TpPersonBasicinfoVO getPersonBasicinfo(String personId) {
        return userPersonService.getPersonBasicinfo(personId);
    }
    
    @Override
    public TpPersonBasicinfoVO getBaseInfoByIdCard(String idcard) {
        return userPersonService.getBaseInfoByIdCard(idcard);
    }
}