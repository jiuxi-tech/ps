package com.jiuxi.monitor.server.core.mapper;

import com.jiuxi.monitor.server.core.bean.entity.TpSendMailRecord;
import com.jiuxi.monitor.server.core.bean.vo.TpSendMailRecordVO;
import com.jiuxi.monitor.server.core.bean.query.TpSendMailRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpSendMailRecordMapper
 * @Description: 邮件发送记录表
 * @Author yangzr
 * @Date 2024-11-20 10:36:59
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpSendMailRecordMapper {

    /**
     * 查询邮件发送记录表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.jiuxi.monitor.server.core.bean.vo.TpSendMailRecordVO>
     * @author 杨占锐
     * @date 2024/11/27 17:54
     */
    IPage<TpSendMailRecordVO> getPage(Page<TpSendMailRecordVO> page, @Param("query") TpSendMailRecordQuery query);

    /**
     * 新增邮件发送记录表
     *
     * @param bean
     * @return int
     * @author 杨占锐
     * @date 2024/11/27 17:54
     */
    int add(TpSendMailRecord bean);

    /**
     * 查看邮件发送记录表
     *
     * @param recordId
     * @return com.jiuxi.monitor.server.core.bean.vo.TpSendMailRecordVO
     * @author 杨占锐
     * @date 2024/11/27 17:54
     */
    TpSendMailRecordVO view(String recordId);

    /**
     * 删除邮件发送记录表
     *
     * @param recordId
     * @return com.jiuxi.monitor.server.core.bean.vo.TpSendMailRecordVO
     * @author 杨占锐
     * @date 2024/11/27 17:54
     */
    int delete(@Param("recordId") String recordId, @Param("updateTime") String updateTime, @Param("updator") String updator);

}
