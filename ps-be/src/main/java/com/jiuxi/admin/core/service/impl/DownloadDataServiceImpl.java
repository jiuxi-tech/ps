package com.jiuxi.admin.core.service.impl;

// import com.alibaba.excel.EasyExcel;
import com.jiuxi.common.exception.ExceptionUtils;
// import com.jiuxi.easyexcel.service.DownloadDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: excel导出实现
 * @ClassName: DownDataService
 * @Author: pand
 * @Date: 2021-03-26 10:09
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class DownloadDataServiceImpl /* implements DownloadDataService */ {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadDataServiceImpl.class);

    /**
     * 根据参数只导出指定列
     *
     * @param fileName: 导出附件名称
     * @param columns:  指定列名
     * @param data:     需要导出的数据
     * @param clazz:    导出类型
     * @param response: 响应
     * @return void
     * @author pand
     * @date 2021-03-26 13:46
     */
    public  void includeWrite(String fileName, String[] columns, List data, Class clazz, HttpServletResponse response) {
        try {
            // 设置响应类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");

            // 这里URLEncoder.encode可以防止中文乱码，easyexcel没有关系
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 设置用户指定导出字段
            Set<String> includeColumnFiledNames = new HashSet<>();
            for (String column : columns) {
                includeColumnFiledNames.add(column);
            }
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为 sheet1 然后文件流会自动关闭
            // EasyExcel.write(response.getOutputStream(), clazz).includeColumnFieldNames(includeColumnFiledNames).sheet("sheet1").doWrite(data); // Commented out - EasyExcel not available
        } catch (IOException e) {
            LOGGER.error("导出失败！{}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("导出失败！");
        }
    }


    /**
     * 根据参数只导出指定列，且指定表头
     *
     * @param fileName: 导出附件名称
     * @param head:     指定列名
     * @param data:     需要导出的数据
     * @param clazz:    导出类型
     * @param response: 响应
     * @return void
     * @author pand
     * @date 2021-03-26 13:46
     */
    public void includeWrite(String fileName, List<List<String>> head, List data, Class clazz, HttpServletResponse response) {
        try {
            // 设置响应类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");

            // 这里URLEncoder.encode可以防止中文乱码，easyexcel没有关系
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为 sheet1 然后文件流会自动关闭
            // EasyExcel.write(response.getOutputStream(), clazz).head(head).sheet("sheet1").doWrite(data); // Commented out - EasyExcel not available
        } catch (IOException e) {
            LOGGER.error("导出失败！{}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("导出失败！");
        }
    }

}
