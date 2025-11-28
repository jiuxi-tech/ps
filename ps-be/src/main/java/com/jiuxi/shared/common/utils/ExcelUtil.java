package com.jiuxi.shared.common.utils;

import com.jiuxi.module.user.app.dto.UserExportDTO;
import com.jiuxi.module.user.app.dto.UserImportDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel文件读写工具类
 * 支持用户导入导出功能的Excel文件处理
 * 
 * @author User Import Refactor
 * @date 2025-11-27
 */
public class ExcelUtil {

    /**
     * 导入模板表头定义（按顺序）
     */
    private static final String[] IMPORT_HEADERS = {
        "账号名", "初始密码", "姓名", "性别", "部门", 
        "参加工作时间", "职务职级", "职称", "身份证号码"
    };

    /**
     * 导出模板表头定义（按顺序，不包含初始密码）
     */
    private static final String[] EXPORT_HEADERS = {
        "账号名", "姓名", "性别", "部门", "参加工作时间", 
        "职务职级", "职称", "身份证号码"
    };

    /**
     * 读取Excel文件并解析为用户导入DTO列表
     * 
     * @param inputStream Excel文件输入流
     * @return 用户导入DTO列表
     * @throws Exception 解析异常
     */
    public static List<UserImportDTO> parseImportExcel(InputStream inputStream) throws Exception {
        List<UserImportDTO> dataList = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Excel文件为空或格式错误");
            }

            // 验证表头
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Excel文件缺少表头");
            }
            validateImportHeaders(headerRow);

            // 解析数据行（从第2行开始，第1行是表头）
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                UserImportDTO dto = parseImportRow(row, i + 1); // Excel行号从1开始
                dataList.add(dto);
            }
        }

        return dataList;
    }

    /**
     * 将用户导出DTO列表写入Excel文件
     * 
     * @param dataList 用户导出DTO列表
     * @param outputStream 输出流
     * @throws Exception 写入异常
     */
    public static void writeExportExcel(List<UserExportDTO> dataList, OutputStream outputStream) throws Exception {
        // 使用SXSSFWorkbook支持大数据量导出，内存中保留100行
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            Sheet sheet = workbook.createSheet("用户信息");

            // 创建表头样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // 写入表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < EXPORT_HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(EXPORT_HEADERS[i]);
                cell.setCellStyle(headerStyle);
                // 设置列宽
                sheet.setColumnWidth(i, getColumnWidth(i));
            }

            // 写入数据
            int rowIndex = 1;
            for (UserExportDTO dto : dataList) {
                Row row = sheet.createRow(rowIndex++);
                writeExportRow(row, dto, dataStyle);
            }

            workbook.write(outputStream);
        }
    }

    /**
     * 生成导入模板Excel文件
     * 
     * @param outputStream 输出流
     * @throws Exception 写入异常
     */
    public static void generateImportTemplate(OutputStream outputStream) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("用户导入模板");

            // 创建表头样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle exampleStyle = createDataStyle(workbook);

            // 写入表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < IMPORT_HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(IMPORT_HEADERS[i]);
                cell.setCellStyle(headerStyle);
                // 设置列宽
                sheet.setColumnWidth(i, getColumnWidth(i));
            }

            // 写入示例数据（注：参加工作时间支持 YYYY、YYYY-MM、YYYY-MM-DD 三种格式）
            Row exampleRow = sheet.createRow(1);
            String[] exampleData = {
                "zhangsan", "123456", "张三", "男", "办公室", 
                "2001-08-15", "四级调研员", "副教授", "411723196710175102"
            };
            for (int i = 0; i < exampleData.length; i++) {
                Cell cell = exampleRow.createCell(i);
                cell.setCellValue(exampleData[i]);
                cell.setCellStyle(exampleStyle);
            }

            workbook.write(outputStream);
        }
    }

    /**
     * 验证导入文件表头
     */
    private static void validateImportHeaders(Row headerRow) {
        for (int i = 0; i < IMPORT_HEADERS.length; i++) {
            Cell cell = headerRow.getCell(i);
            if (cell == null) {
                throw new IllegalArgumentException("Excel表头缺少第" + (i + 1) + "列");
            }
            String headerValue = getCellValueAsString(cell).trim();
            if (!IMPORT_HEADERS[i].equals(headerValue)) {
                throw new IllegalArgumentException(
                    "Excel表头第" + (i + 1) + "列应为[" + IMPORT_HEADERS[i] + "]，实际为[" + headerValue + "]"
                );
            }
        }
    }

    /**
     * 解析导入数据行
     */
    private static UserImportDTO parseImportRow(Row row, int rowNumber) {
        UserImportDTO dto = new UserImportDTO();
        dto.setRowNumber(rowNumber);
        dto.setUsername(getCellValueAsString(row.getCell(0)));
        dto.setPassword(getCellValueAsString(row.getCell(1)));
        dto.setPersonName(getCellValueAsString(row.getCell(2)));
        dto.setSex(getCellValueAsString(row.getCell(3)));
        dto.setDeptPath(getCellValueAsString(row.getCell(4)));
        dto.setPartWorkDate(getCellValueAsString(row.getCell(5)));
        // 新字段：职务职级、职称
        dto.setZwzj(getCellValueAsString(row.getCell(6)));
        dto.setZhicheng(getCellValueAsString(row.getCell(7)));
        dto.setIdcard(getCellValueAsString(row.getCell(8)));
        return dto;
    }

    /**
     * 写入导出数据行
     */
    private static void writeExportRow(Row row, UserExportDTO dto, CellStyle style) {
        int colIndex = 0;
        createStyledCell(row, colIndex++, dto.getUsername(), style);
        createStyledCell(row, colIndex++, dto.getPersonName(), style);
        createStyledCell(row, colIndex++, dto.getSexName(), style);
        createStyledCell(row, colIndex++, dto.getDeptFullPath(), style);
        createStyledCell(row, colIndex++, dto.getPartWorkDate(), style);
        // 新字段：职务职级、职称
        createStyledCell(row, colIndex++, dto.getZwzj(), style);
        createStyledCell(row, colIndex++, dto.getZhicheng(), style);
        createStyledCell(row, colIndex++, dto.getIdcard(), style);
    }

    /**
     * 创建带样式的单元格
     */
    private static void createStyledCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
    }

    /**
     * 获取单元格值并转为字符串
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    // 数字类型转字符串，去除小数点
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BLANK:
            default:
                return "";
        }
    }

    /**
     * 判断行是否为空
     */
    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 创建表头样式
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // 设置字体
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        
        // 设置对齐
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // 设置背景色（浅灰色）
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        return style;
    }

    /**
     * 创建数据单元格样式
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // 设置对齐
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        return style;
    }

    /**
     * 获取列宽度（单位：1/256字符宽度）
     */
    private static int getColumnWidth(int columnIndex) {
        switch (columnIndex) {
            case 0: // 账号名
                return 4000;
            case 1: // 姓名
                return 3000;
            case 2: // 性别
                return 2000;
            case 3: // 部门
                return 8000;
            case 4: // 参加工作时间
                return 4000;
            case 5: // 职务职级
                return 4000;
            case 6: // 职称
                return 3000;
            case 7: // 身份证号码
                return 5000;
            case 8: // 初始密码（仅导入模板）
                return 3000;
            default:
                return 3000;
        }
    }
}
