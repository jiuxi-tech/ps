package com.jiuxi.shared.common.constants.enums;

/**
 * @Description: 操作系统类型
 * @ClassName: OSEnum
 * @Author: pand
 * @Date: 2020-10-14 14:53
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public enum OpertionTypeEnum {

    // 新增
    ADD(1),
    // 修改
    UPDATE(2),
    // 删除
    DELETE(3),
    // 账号冻结/解冻
    LOCKED(4),
    // 启用/禁用
    ENABLED(5);

    OpertionTypeEnum(Integer opertionType) {
        this.opertionType = opertionType;
    }

    private Integer opertionType;

    public Integer getOpertionType() {
        return opertionType;
    }

    public void setOpertionType(Integer opertionType) {
        this.opertionType = opertionType;
    }
}
