package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpAttachinfo;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpAttachinfoMapper
 * @Description: 附件表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpAttachinfoMapper {

    int save(TpAttachinfo bean);

    TpAttachinfo view(String attachId);

    List<TpAttachinfoVO> selectByReferIdAndType(@Param("referId") String referId, @Param("referType") String referType);

    int update(TpAttachinfo bean);

    int updateByAttachId(@Param("attachId") String attachId, @Param("referId") String referId, @Param("updateTime") String updateTime);

    /**
     * 物理删除附件信息
     *
     * @param attachId
     * @return int
     * @author jiuxx
     * @date 2024/6/5 16:00
     */
    int delete(@Param("attachId") String attachId);

    int deleteByReferId(@Param("referId") String referId, @Param("updator") String updator, @Param("updateTime") String updateTime);

    int deleteByReferIdAndReferType(@Param("referId") String referId, @Param("referType") String referType, @Param("updator") String updator, @Param("updateTime") String updateTime);

    /**
     * 根据创建人和附件id物理删除
     *
     * @param attachId
     * @param jwtpid
     * @return int
     * @author jiuxx
     * @date 2024/6/5 16:00
     */
    int physicsDelete(@Param("attachId") String attachId, @Param("jwtpid") String jwtpid);

    /**
     * 逻辑删除
     *
     * @param attachId
     * @param jwtpid
     * @param updateTime
     * @return int
     * @author jiuxx
     * @date 2024/6/5 16:01
     */
    int logicDelete(@Param("attachId") String attachId, @Param("jwtpid")String jwtpid, @Param("updateTime")String updateTime);

    /**
     * 判断附件是否存在 （忽略actived字段）
     *
     * @param attachId
     * @return boolean
     * @author jiuxx
     * @date 2024/7/22 14:51
     */
    String exists(@Param("attachId") String attachId);

}
