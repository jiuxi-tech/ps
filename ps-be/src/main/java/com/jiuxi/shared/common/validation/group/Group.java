package com.jiuxi.shared.common.validation.group;

import javax.validation.GroupSequence;

/**
 * @description: 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 * @author Ypp
 * @date 2020/7/20 15:06
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {
}
