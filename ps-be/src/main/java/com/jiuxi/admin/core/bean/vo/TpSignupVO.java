package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.core.core.validator.group.AddGroup;
import com.jiuxi.core.core.validator.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 签约表 企业-中介，政府-中介签约绑定表，多对多关系
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-30 20:53:39
 */
public class TpSignupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 签约单位id 签约单位（甲方）id，政府id或企业id。
     */
    @NotBlank(message = "签约单位id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String partyA;
    /**
     * 中介单位id 中介（乙方）id
     */
    @NotBlank(message = "中介单位id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String interId;
    /**
     * 签约 / 解约 1:签约   0:解约
     */
    @NotBlank(message = "操作类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
