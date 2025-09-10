package com.jiuxi.monitor.server.core.mapper;

import com.jiuxi.monitor.server.core.bean.entity.TpMonitorClient;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO;
import com.jiuxi.monitor.server.core.bean.query.TpMonitorClientQuery;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpMonitorClientMapper
 * @Description: 客户端基本信息
 * @Author yangzr
 * @Date 2024-11-18 16:30:42
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpMonitorClientMapper {

    /**
     * 新增客户端信息
     *
     * @param bean
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 13:27
     */
    int add(TpMonitorClient bean);

    /**
     * 修改客户端信息
     *
     * @param bean
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 13:27
     */
    int update(TpMonitorClient bean);

    /**
     * 查看客户端信息
     *
     * @param clientId
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 13:27
     */
    TpMonitorClientVO view(String clientId);

    /**
     * 删除客户端信息 （物理删除）
     *
     * @param clientId
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 13:27
     */
    int delete(@Param("clientId") String clientId);

    /**
     * 查询所有客户端信息
     *
     * @return java.util.List<com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO>
     * @author 杨占锐
     * @date 2024/11/19 14:53
     */
    List<TpMonitorClientVO> listAll(TpMonitorClientQuery query);

}
