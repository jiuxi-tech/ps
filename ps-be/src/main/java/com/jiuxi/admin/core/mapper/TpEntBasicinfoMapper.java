package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpEntBasicinfo;
import com.jiuxi.admin.core.bean.query.TpEntAccountQuery;
import com.jiuxi.admin.core.bean.query.TpEntBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpEntAccountVO;
import com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO;
// import com.jiuxi.plugin.api.bean.query.TpEntDeleteQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpEntBasicinfoMapper
 * @Description: 企业基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpEntBasicinfoMapper extends BaseMapper<TpEntBasicinfo> {

    IPage<TpEntBasicinfoVO> selectPageByCityCode(Page<TpEntBasicinfoVO> page, @Param("query") TpEntBasicQuery query);

    /**
     * 2021.12.10 过期原因，企业管理 -> 账号信息，根据企业查询管理账号，只管理企业管理员账号。该接口过期，使用entAdminList接口。
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.jiuxi.admin.core.bean.vo.TpEntAccountVO>
     * @author pand
     * @date 2021-12-10 16:21
     */
    @Deprecated
    IPage<TpEntAccountVO> accountQueryPage(Page<TpEntAccountVO> page, @Param("query") TpEntAccountQuery query);

    IPage<TpEntAccountVO> adminQueryPage(Page<TpEntAccountVO> page, @Param("query") TpEntAccountQuery query);

    TpEntBasicinfoVO selectByEntId(String entId);

    void save(TpEntBasicinfo bean);

    TpEntBasicinfoVO view(String entId);

    /**
     * 根据统一信用代码查询企业基本信息
     *
     * @param entUnifiedCode 统一信用代码
     * @return com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO
     * @author 杨占锐
     * @date 2024/5/28 13:56
     */
    TpEntBasicinfoVO getBaseInfoByEntUnifiedCode(@Param("entUnifiedCode") String entUnifiedCode);

    int updateByEntId(TpEntBasicinfo bean);

    /**
     * 根据统一社会信用代码查询企业id
     *
     * @param entUnifiedCode
     * @return com.jiuxi.admin.core.bean.entity.TpEntBasicinfo
     * @author 杨占锐
     * @date 2024/5/28 14:14
     */
    String selectByEntUnifiedCode(@Param("entUnifiedCode") String entUnifiedCode);

    /**
     * 删除企业，为了防止企业删除后再次新增该企业，统一信用代码冲突，这里在删除的企业统一信用代码后面加上删除时间
     *
     * @param entId      企业id
     * @param updateTime 删除时间
     * @return
     */
    int deleteByEntId(@Param("entId") String entId, @Param("updateTime") String updateTime, @Param("entUnifiedCode") String entUnifiedCode);

    /**
     * 判断【统一社会信用代码】是否存在
     *
     * @param entUnifiedCode  统一社会信用代码
     * @param entId           企业id，不为空时，排除当前企业
     * @return boolean        存在时返回true
     * @author 杨占锐
     * @date 2024/5/28 15:46
     */
    String existsEntUnifiedCode(@Param("entUnifiedCode") String entUnifiedCode, @Param("entId") String entId);

    /**
     * 根据企业id获取企业基本信息-包含已删除的数据
     *
     * @param entId 企业id
     * @author 杨占锐
     * @date 2023/11/15 17:13
     */
    TpEntBasicinfoVO getBaseInfoIncludeNotActive(@Param("entId") String entId);

    /**
     * 查询删除的企业信息
     *
     * @param query#prodAddrCode     生成经营地址行政区划编码，根据编码查询本级及下级
     * @param query#updateTimeStart  修改时间-开始  格式：yyyyMMddHHmmss   updateTimeStart <= updateTime <= updateTimeEnd
     * @param query#updateTimeEnd    修改时间-结束  格式：yyyyMMddHHmmss   updateTimeStart <= updateTime <= updateTimeEnd
     * @param query#current          当前页码，默认1
     * @param query#size             默认返回条数，默认10
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpEntBasicinfoDTO>
     * @author 杨占锐
     * @date 2024//5 16:13
     */
    // List<TpEntBasicinfoVO> listDelete(TpEntDeleteQuery query);

    /**
     * 根据企业id判断数据是否存在 (忽略actived字段)
     *
     * @param entId
     * @return boolean 存在返回true, 不存在返回false
     * @author 杨占锐
     * @date 2024/8/5 15:39
     */
    String existsByEntId(@Param("entId") String entId);


}
