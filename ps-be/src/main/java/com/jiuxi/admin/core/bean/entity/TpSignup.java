package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * 签约表 企业-中介，政府-中介签约绑定表，多对多关系
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-30 20:53:39
 */
public class TpSignup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 签约单位id 签约单位（甲方）id，政府id或企业id。
     */
    @TableId
    private String partyA;
    /**
     * 中介单位id 中介（乙方）id
     */
    private String interId;
    /**
     * 创建时间
     */
    private String createTime;

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getInterId() {
        return interId;
    }

    public void setInterId(String interId) {
        this.interId = interId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
