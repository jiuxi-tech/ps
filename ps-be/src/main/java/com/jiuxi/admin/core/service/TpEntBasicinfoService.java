package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpEntAccountQuery;
import com.jiuxi.admin.core.bean.query.TpEntBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpEntAccountVO;
import com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO;
// import com.jiuxi.plugin.api.bean.query.TpEntDeleteQuery;

import java.util.List;


/**
 * @ClassName: TpEntBasicinfoService
 * @Description: 企业基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpEntBasicinfoService {

    IPage queryPage(TpEntBasicQuery query, String ascnId);

    IPage<TpEntAccountVO> accountQueryPage(TpEntAccountQuery query, String jwtpid, String jwtaid);

    IPage<TpEntAccountVO> adminQueryPage(TpEntAccountQuery query);

    TpEntBasicinfoVO view(String entId);

    /**
     * 根据【统一社会信用代码】查询企业id
     *
     * @param entUnifiedCode
     * @return com.jiuxi.admin.core.bean.entity.TpEntBasicinfo
     * @author 杨占锐
     * @date 2024/5/28 14:14
     */
    String selectByEntUnifiedCode(String entUnifiedCode);

    /**
     * 判断【统一社会信用代码】是否存在
     *
     * @param entUnifiedCode  统一社会信用代码
     * @param entId           企业id，不为空时，排除当前企业
     * @return boolean        存在时返回true
     * @author 杨占锐
     * @date 2024/5/28 15:46
     */
    boolean existsEntUnifiedCode(String entUnifiedCode, String entId);

    /**
     * 新增企业信息
     *
     * @param vo
     * @param jwtpid
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/5/28 16:21
     */
    String add(TpEntBasicinfoVO vo, String jwtpid);

    /**
     * 新增企业信息 - 不校验统一信用代码
     *
     * @param vo
     * @param jwtpid
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/5/28 16:21
     */
    String addInfo(TpEntBasicinfoVO vo, String jwtpid);

    /**
     * 修改企业信息
     *
     * @param vo
     * @param jwtpid
     * @return int
     * @author 杨占锐
     * @date 2024/5/28 16:23
     */
    int update(TpEntBasicinfoVO vo, String jwtpid);

    /**
     * 删除企业信息
     *
     * @param entId
     * @param jwtpid
     * @return int
     * @author 杨占锐
     * @date 2024/5/28 17:14
     */
    int delete(String entId, String jwtpid);

    /**
     * 根据统一信用代码查询企业基本信息
     *
     * @param entUnifiedCode 统一信用代码
     * @return com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO
     * @author 杨占锐
     * @date 2024/5/28 13:56
     */
    TpEntBasicinfoVO getBaseInfoByEntUnifiedCode(String entUnifiedCode);

    /**
     * 根据企业id获取企业基本信息-包含已删除的数据
     *
     * @param entId 企业id
     * @author 杨占锐
     * @date 2023/11/15 17:13
     */
    TpEntBasicinfoVO getBaseInfoIncludeNotActive(String entId);

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
    boolean existsByEntId(String entId);

}

