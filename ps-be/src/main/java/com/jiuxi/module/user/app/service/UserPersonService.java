package com.jiuxi.module.user.app.service;

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
 * @ClassName: TpPersonBasicinfoService
 * @Description: 人员基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface UserPersonService {

    IPage<TpPersonBasicinfoVO> queryPage(TpPersonBasicQuery query);

    TpPersonBasicinfoVO add(TpPersonBasicinfoVO vo, String pid, int category);

    TpPersonExinfoVO expAdd(TpPersonExinfoVO vo);

    TpPersonBasicinfoVO view(String personId, String deptId);

    TpPersonExinfoVO expView(String personId);

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

    int parttime(String personId, String deptIds);

    List<TpPersonRoleVO> personRoles(String deptId, String personId);

    int auth(String personId, String deptId, String roleIds);

    /**
     * 根据人员id 获取人员基本信息
     * @author Ypp
     * @date 2023/11/15 16:28
     * @param personId
     * @return com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO
     */
    TpPersonBasicinfoVO getPersonBasicinfo(String personId);

    TpPersonBasicinfoVO getBaseInfoByIdCard(String idcard);

    /**
     * 校验手机号是否存在
     *
     * @param phone     手机号
     * @param personId  人员id（修改校验时必填）
     * @return boolean 存在返回 true, 不存在返回 false
     * @author jiuxx
     * @date 2024/5/30 15:49
     */
    boolean existsPhone(String phone, String personId);

    /**
     * 查询出在该单位所有部门下的所有主部门关联关系的人员id
     *
     * @param ascnId 单位id
     * @return java.util.List<java.lang.String>
     * @author jiuxx
     * @date 2024/6/4 19:12
     */
    List<String> selectPersonIdByAscnId(String ascnId);

    /**
     * 根据企业id删除人员
     *
     * @param ascnId 单位id
     * @param jwtpid 操作人id
     * @return void
     * @author jiuxx
     * @date 2024/6/4 19:24
     */
    void deletePersonDeptByAscnId(String ascnId, String jwtpid);

    /**
     * 修改手机号
     *
     * @param personId 人员id
     * @param phone    手机号
     * @param jwtpid   操作人id
     * @return int
     * @author jiuxx
     * @date 2024/5/30 17:10
     */
    void updatePhone(String personId, String phone, String jwtpid);

    /**
     * 导出用户信息到Excel
     *
     * @param query    查询条件
     * @param jwtpid   操作人id
     * @param response HTTP响应对象
     * @throws Exception 导出异常
     */
    void exportExcel(TpPersonBasicQuery query, String jwtpid, HttpServletResponse response) throws Exception;

    /**
     * 导入用户信息从Excel
     *
     * @param file   Excel文件
     * @param deptId 部门ID
     * @param jwtpid 操作人id
     * @return 导入结果
     * @throws Exception 导入异常
     */
    JsonResponse importExcel(MultipartFile file, String deptId, String jwtpid) throws Exception;

    /**
     * 下载Excel导入模板
     *
     * @param response HTTP响应对象
     * @throws Exception 下载异常
     */
    void downloadTemplate(HttpServletResponse response) throws Exception;
}

