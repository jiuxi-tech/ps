package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.query.TpOperateLogQuery;
import com.jiuxi.admin.core.bean.entity.TpOperateLog;
import com.jiuxi.admin.core.bean.vo.TpOperateLogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @ClassName: TpOperateLogService
 * @Description: 操作日志表服务接口
 * @Author: Qdd
 * @Date: 2024-01-17
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
public interface TpOperateLogService {

    /**
     * 分页查询操作日志
     * @param query 查询条件
     * @param jwtpid JWT中的人员ID
     * @return 分页结果
     */
    IPage<TpOperateLogVO> queryPage(TpOperateLogQuery query, String jwtpid);

    /**
     * 新增操作日志
     * @param vo 操作日志VO
     * @param jwtpid JWT中的人员ID
     * @return 日志ID
     */
    String add(TpOperateLogVO vo, String jwtpid);

    /**
     * 更新操作日志
     * @param vo 操作日志VO
     * @param jwtpid JWT中的人员ID
     * @return 影响行数
     */
    int update(TpOperateLogVO vo, String jwtpid);

    /**
     * 查看操作日志详情
     * @param id 日志ID
     * @return 操作日志VO
     */
    TpOperateLogVO view(String id);

    /**
     * 批量删除操作日志
     * @param ids 日志ID列表
     * @param jwtpid JWT中的人员ID
     * @return 影响行数
     */
    int deleteByIds(List<String> ids, String jwtpid);

    /**
     * 登录日志采集
     * @param ticket 滑动验证票据
     * @param username 用户名
     * @param category 用户类别
     * @param token token
     * @param ip IP地址
     * @param userAgent 用户代理
     * @param extend01 扩展字段1
     * @param extend02 扩展字段2
     * @param extend03 扩展字段3
     */
    void login(String ticket, String username, String category, String token, String ip, String userAgent, String extend01, String extend02, String extend03);

    /**
     * 退出日志采集
     * @param username 用户名
     * @param category 用户类别
     * @param token token
     * @param ip IP地址
     * @param userAgent 用户代理
     * @param extend01 扩展字段1
     * @param extend02 扩展字段2
     * @param extend03 扩展字段3
     */
    void logout(String username, String category, String token, String ip, String userAgent, String extend01, String extend02, String extend03);

    /**
     * 操作日志采集
     * @param moduleCode 模块代码
     * @param operterType 操作类型
     * @param operterRid 操作资源ID
     * @param username 用户名
     * @param category 用户类别
     * @param token token
     * @param ip IP地址
     * @param userAgent 用户代理
     * @param extend01 扩展字段1
     * @param extend02 扩展字段2
     * @param extend03 扩展字段3
     */
    void collection(String moduleCode, String operterType, String operterRid, String username, String category, String token, String ip, String userAgent, String extend01, String extend02, String extend03);

    /**
     * 导出操作日志到Excel
     * @param query 查询条件
     * @param jwtpid JWT中的人员ID
     * @param response HTTP响应对象
     * @throws Exception 导出异常
     */
    void exportExcel(TpOperateLogQuery query, String jwtpid, HttpServletResponse response) throws Exception;
}