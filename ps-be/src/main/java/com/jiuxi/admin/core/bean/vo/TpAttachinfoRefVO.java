package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;
import java.util.List;

public class TpAttachinfoRefVO implements Serializable {

    /**
     * 关联业务类别",
     * "referType",
     * "字典编码：SYS10"
     */
    private String referType;
    /**
     * "附件详情",
     */
    private List<TpAttachinfoVO> fileList;


    public TpAttachinfoRefVO() {
    }

    public String getReferType() {
        return referType;
    }

    public void setReferType(String referType) {
        this.referType = referType;
    }

    public List<TpAttachinfoVO> getFileList() {
        return fileList;
    }

    public void setFileList(List<TpAttachinfoVO> fileList) {
        this.fileList = fileList;
    }
}
