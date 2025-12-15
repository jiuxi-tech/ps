package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpOperateLog;
import com.jiuxi.admin.core.bean.query.TpOperateLogQuery;
import com.jiuxi.admin.core.bean.vo.TpOperateLogVO;
import com.jiuxi.admin.core.mapper.TpOperateLogMapper;
import com.jiuxi.admin.core.service.TpOperateLogService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: TpOperateLogServiceImpl
 * @Description: 操作日志表服务实现类
 * @Author: Qdd
 * @Date: 2024-01-17
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@Service
public class TpOperateLogServiceImpl implements TpOperateLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpOperateLogServiceImpl.class);

    @Autowired
    private TpOperateLogMapper tpOperateLogMapper;

    @Override
    public IPage<TpOperateLogVO> queryPage(TpOperateLogQuery query, String jwtpid) {
        Page<TpOperateLogVO> page = new Page<>(query.getCurrent(), query.getSize());
        return tpOperateLogMapper.getPage(page, query);
    }

    @Override
    public String add(TpOperateLogVO vo, String jwtpid) {
        TpOperateLog entity = new TpOperateLog();
        BeanUtils.copyProperties(vo, entity);
        
        // 设置主键ID
        if (StrUtil.isBlank(entity.getLogId())) {
            entity.setLogId(IdUtil.simpleUUID());
        }
        
        // 设置创建信息
        entity.setCreator(jwtpid);
        entity.setCreateTime(new Date());
        entity.setUpdator(jwtpid);
        entity.setUpdateTime(new Date());
        
        tpOperateLogMapper.add(entity);
        return entity.getLogId();
    }

    @Override
    public int update(TpOperateLogVO vo, String jwtpid) {
        // 操作日志一般不允许修改，此方法可根据业务需要实现
        return 0;
    }

    @Override
    public TpOperateLogVO view(String id) {
        return tpOperateLogMapper.view(id);
    }

    @Override
    public int deleteByIds(List<String> ids, String jwtpid) {
        // 操作日志一般不允许删除，此方法可根据业务需要实现
        return 0;
    }

    @Override
    public void login(String ticket, String username, String category, String token, String ip, String userAgent, String extend01, String extend02, String extend03) {
        TpOperateLog log = new TpOperateLog();
        log.setLogId(IdUtil.simpleUUID());
        log.setUsername(username);
        log.setCategory(category);
        log.setModuleCode("LOGIN");
        log.setOperterType("LOGIN");
        log.setOperterTime(new Date());
        log.setOperterIp(ip);
        log.setOperterBrowser(userAgent);
        log.setExtend01(extend01);
        log.setExtend02(extend02);
        log.setExtend03(extend03);
        log.setActived("1");
        log.setCreator("SYSTEM");
        log.setCreateTime(new Date());
        log.setUpdator("SYSTEM");
        log.setUpdateTime(new Date());
        
        tpOperateLogMapper.add(log);
    }

    @Override
    public void logout(String username, String category, String token, String ip, String userAgent, String extend01, String extend02, String extend03) {
        TpOperateLog log = new TpOperateLog();
        log.setLogId(IdUtil.simpleUUID());
        log.setUsername(username);
        log.setCategory(category);
        log.setModuleCode("LOGOUT");
        log.setOperterType("LOGOUT");
        log.setOperterTime(new Date());
        log.setOperterIp(ip);
        log.setOperterBrowser(userAgent);
        log.setExtend01(extend01);
        log.setExtend02(extend02);
        log.setExtend03(extend03);
        log.setActived("1");
        log.setCreator("SYSTEM");
        log.setCreateTime(new Date());
        log.setUpdator("SYSTEM");
        log.setUpdateTime(new Date());
        
        tpOperateLogMapper.add(log);
    }

    @Override
    public void collection(String moduleCode, String operterType, String operterRid, String username, String category, String token, String ip, String userAgent, String extend01, String extend02, String extend03) {
        TpOperateLog log = new TpOperateLog();
        log.setLogId(IdUtil.simpleUUID());
        log.setModuleCode(moduleCode);
        log.setOperterType(operterType);
        log.setOperterRid(operterRid);
        log.setUsername(username);
        log.setCategory(category);
        log.setOperterTime(new Date());
        log.setOperterIp(ip);
        log.setOperterBrowser(userAgent);
        log.setExtend01(extend01);
        log.setExtend02(extend02);
        log.setExtend03(extend03);
        log.setActived("1");
        log.setCreator("SYSTEM");
        log.setCreateTime(new Date());
        log.setUpdator("SYSTEM");
        log.setUpdateTime(new Date());
        
        tpOperateLogMapper.add(log);
    }

    @Override
    public void exportExcel(TpOperateLogQuery query, String jwtpid, HttpServletResponse response) throws Exception {
        try {
            // 查询所有数据（不分页）
            query.setCurrent(1);
            query.setSize(Integer.MAX_VALUE);
            IPage<TpOperateLogVO> pageResult = queryPage(query, jwtpid);
            List<TpOperateLogVO> logList = pageResult.getRecords();

            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("操作日志");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"用户名", "模块", "操作类型", "操作时间"};
            
            // 设置标题样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 创建数据行
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < logList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                TpOperateLogVO log = logList.get(i);
                
                row.createCell(0).setCellValue(StrUtil.isNotBlank(log.getUsername()) ? log.getUsername() : "");
                row.createCell(1).setCellValue(StrUtil.isNotBlank(log.getModuleCode()) ? log.getModuleCode() : "");
                row.createCell(2).setCellValue(StrUtil.isNotBlank(log.getOperterType()) ? log.getOperterType() : "");
                row.createCell(3).setCellValue(log.getOperterTime() != null ? dateFormat.format(log.getOperterTime()) : "");
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 设置响应头
            String fileName = "操作日志_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            
        } catch (IOException e) {
            LOGGER.error("导出Excel失败！query:{}, e: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "导出Excel失败！");
        } catch (Exception e) {
            LOGGER.error("导出Excel失败！query:{}, e: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "导出Excel失败！" : e.getMessage());
        }
    }
}