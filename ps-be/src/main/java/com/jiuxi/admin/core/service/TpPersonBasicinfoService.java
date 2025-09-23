package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonExinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 人员基础信息服务适配器接口 - 为了兼容旧代码
 * 实际实现将委托给新的用户模块服务
 * 
 * @deprecated 请使用 com.jiuxi.module.user.app.service.UserPersonService
 */
@Deprecated
public interface TpPersonBasicinfoService {

    IPage<TpPersonBasicinfoVO> queryPage(TpPersonBasicQuery query);
    
    TpPersonBasicinfoVO add(TpPersonBasicinfoVO vo, String pid, int category);
    
    int update(TpPersonBasicinfoVO vo, String pid);
    
    void deletes(String deptIds, String personIds, String pid);
    
    /**
     * 根据人员ID物理删除人员信息
     *
     * @param personId 人员ID
     * @return void
     * @author 系统生成
     * @date 2024/12/24
     */
    void physicalDeleteByPersonId(String personId);
    
    TpPersonBasicinfoVO view(String personId, String deptId);
    
    TpPersonExinfoVO expAdd(TpPersonExinfoVO vo);
    
    TpPersonExinfoVO expView(String personId);
    
    List<TpPersonBasicinfoVO> personBasicinfoList(TpPersonBasicQuery query);
    
    void personBasicinfoExport(TpPersonBasicQuery query, HttpServletResponse response);
    
    JsonResponse personBasicinfoImport(MultipartFile file);
    
    List<TpPersonExinfoVO> personExinfoList(String personId);
    
    int personExinfoUpdate(TpPersonExinfoVO vo);
    
    List<TpPersonRoleVO> personRoleList(String personId);
    
    int personRoleUpdate(String personId, String roleIds);
    
    // 添加UserPersonController需要的方法
    int parttime(String personId, String deptIds);
    
    List<TpPersonRoleVO> personRoles(String deptId, String personId);
    
    int auth(String personId, String deptId, String roleIds);
    
    void exportExcel(TpPersonBasicQuery query, String jwtpid, HttpServletResponse response) throws Exception;
    
    JsonResponse importExcel(MultipartFile file, String deptId, String jwtpid) throws Exception;
    
    void downloadTemplate(HttpServletResponse response) throws Exception;
    
    // 添加TpPersonApiServiceImpl需要的方法
    TpPersonBasicinfoVO getPersonBasicinfo(String personId);
    
    TpPersonBasicinfoVO getBaseInfoByIdCard(String idcard);
}