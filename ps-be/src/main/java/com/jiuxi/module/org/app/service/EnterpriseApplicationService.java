package com.jiuxi.module.org.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.vo.TpEntAccountVO;
import com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO;
import com.jiuxi.admin.core.bean.query.TpEntAccountQuery;
import com.jiuxi.admin.core.bean.query.TpEntBasicQuery;

/**
 * @ClassName: EnterpriseApplicationService
 * @Description: 企业应用服务接口
 * @Author DDD重构
 * @Date 2025-09-12
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface EnterpriseApplicationService {

    IPage queryPage(TpEntBasicQuery query, String ascnId);

    IPage<TpEntAccountVO> accountQueryPage(TpEntAccountQuery query, String jwtpid, String jwtaid);

    IPage<TpEntAccountVO> adminQueryPage(TpEntAccountQuery query);

    TpEntBasicinfoVO view(String entId);

    /**
     * 根据【统一社会信用代码】查询企业id
     *
     * @param entUnifiedCode
     * @return com.jiuxi.admin.core.bean.entity.TpEntBasicinfo
     * @author jiuxx
     * @date 2024/5/28 14:14
     */
    String selectByEntUnifiedCode(String entUnifiedCode);

    /**
     * 判断【统一社会信用代码】是否存在
     *
     * @param entUnifiedCode  统一社会信用代码
     * @param entId           企业id，不为空时，排除当前企业
     * @return boolean        存在时返回true
     * @author jiuxx
     * @date 2024/5/28 15:46
     */
    boolean existsEntUnifiedCode(String entUnifiedCode, String entId);

    /**
     * 新增企业信息
     *
     * @param vo
     * @param jwtpid
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/5/28 16:21
     */
    String add(TpEntBasicinfoVO vo, String jwtpid);

    /**
     * 新增企业信息 - 不校验统一信用代码
     *
     * @param vo
     * @param jwtpid
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/5/28 16:21
     */
    String addInfo(TpEntBasicinfoVO vo, String jwtpid);

    /**
     * 修改企业信息
     *
     * @param vo
     * @param jwtpid
     * @return int
     * @author jiuxx
     * @date 2024/5/28 16:23
     */
    int update(TpEntBasicinfoVO vo, String jwtpid);

    /**
     * 删除企业信息
     *
     * @param entId
     * @param jwtpid
     * @return int
     * @author jiuxx
     * @date 2024/5/28 17:14
     */
    int delete(String entId, String jwtpid);

    /**
     * 根据统一信用代码查询企业基本信息
     *
     * @param entUnifiedCode 统一信用代码
     * @return com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO
     * @author jiuxx
     * @date 2024/5/28 13:56
     */
    TpEntBasicinfoVO getBaseInfoByEntUnifiedCode(String entUnifiedCode);

    /**
     * 根据企业id获取企业基本信息-包含已删除的数据
     *
     * @param entId 企业id
     * @author jiuxx
     * @date 2023/11/15 17:13
     */
    TpEntBasicinfoVO getBaseInfoIncludeNotActive(String entId);

    /**
     * 根据企业id判断数据是否存在 (忽略actived字段)
     *
     * @param entId
     * @return boolean 存在返回true, 不存在返回false
     * @author jiuxx
     * @date 2024/8/5 15:39
     */
    boolean existsByEntId(String entId);

}