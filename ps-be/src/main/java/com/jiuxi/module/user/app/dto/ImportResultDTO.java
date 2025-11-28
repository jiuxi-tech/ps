package com.jiuxi.module.user.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入结果DTO
 * 用于封装导入操作的结果信息
 * 
 * @author User Import Refactor
 * @date 2025-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 总行数
     */
    private Integer totalRows;

    /**
     * 成功行数
     */
    private Integer successRows;

    /**
     * 失败行数
     */
    private Integer failedRows;

    /**
     * 错误明细
     */
    private List<ImportErrorDTO> errors = new ArrayList<>();

    /**
     * 添加错误
     */
    public void addError(ImportErrorDTO error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }

    /**
     * 添加错误（便捷方法）
     */
    public void addError(Integer row, String field, String value, String message) {
        addError(new ImportErrorDTO(row, field, value, message));
    }

    /**
     * 是否有错误
     */
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    /**
     * 创建成功结果
     */
    public static ImportResultDTO success(int totalRows) {
        ImportResultDTO result = new ImportResultDTO();
        result.setSuccess(true);
        result.setTotalRows(totalRows);
        result.setSuccessRows(totalRows);
        result.setFailedRows(0);
        result.setErrors(new ArrayList<>());
        return result;
    }

    /**
     * 创建失败结果
     */
    public static ImportResultDTO failure(int totalRows, List<ImportErrorDTO> errors) {
        ImportResultDTO result = new ImportResultDTO();
        result.setSuccess(false);
        result.setTotalRows(totalRows);
        result.setSuccessRows(0);
        result.setFailedRows(errors.size());
        result.setErrors(errors);
        return result;
    }
}
