package com.jiuxi.monitor.server.core.mapper;

import com.jiuxi.monitor.server.core.bean.entity.TpMonitorConfig;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;
import com.jiuxi.monitor.server.core.bean.query.TpMonitorConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpMonitorConfigMapper
 * @Description: 监控配置
 * @Author yangzr
 * @Date 2024-11-18 16:30:42
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpMonitorConfigMapper {

    /**
     * 新增配置信息
     *
     * @param bean
     * @return int
     * @author 杨占锐
     * @date 2024/11/27 17:53
     */
    int add(TpMonitorConfig bean);

    /**
     * 修改配置信息
     *
     * @param bean
     * @return int
     * @author 杨占锐
     * @date 2024/11/27 17:53
     */
    int update(TpMonitorConfig bean);

    /**
     * 查询唯一一条的配置信息
     *
     * @return com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO
     * @author 杨占锐
     * @date 2024/11/19 10:29
     */
    TpMonitorConfigVO getOne();

}
