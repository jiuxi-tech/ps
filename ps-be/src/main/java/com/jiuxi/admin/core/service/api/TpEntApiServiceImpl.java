package com.jiuxi.admin.core.service.api;

import cn.hutool.core.bean.BeanUtil;
import com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO;
import com.jiuxi.admin.core.service.TpEntBasicinfoService;
// import com.jiuxi.plugin.api.bean.dto.TpEntBasicinfoDTO;
// import com.jiuxi.plugin.api.bean.query.TpEntDeleteQuery;
// import com.jiuxi.plugin.api.interfaces.TpEntApiService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: TpEntApiServiceImpl
 * @Description: 企业api
 * @Author: Ypp
 * @Date: 2023/11/15 16:37
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpEntApiService")
public class TpEntApiServiceImpl /* implements TpEntApiService */ {

    @Autowired
    private TpEntBasicinfoService tpEntBasicinfoService;

    @Autowired
    private TpApiCommonService tpApiCommonService;


    /**
     * 根据企业id获取企业基本信息
     *
     * @param entId 企业id
     * @param clazz 返回的目标对象类型
     * @return T
     * @author Ypp
     * @date 2023/11/15 17:13
     */
    /*
    @Override
    public <T> T getBaseInfo(String entId, Class<T> clazz) {

        TpEntBasicinfoVO vo = tpEntBasicinfoService.view(entId);

        return tpApiCommonService.copy(vo, clazz);
    }
    */

    /**
     * 根据企业id获取企业基本信息
     *
     * @param entId 企业id
     * @return TpEntBasicinfoDTO
     * @author jiuxx
     * @date 2024/5/28 9:19
     */
    /*
    @Override
    public TpEntBasicinfoDTO getBaseInfo(String entId) {

        return getBaseInfo(entId, TpEntBasicinfoDTO.class);
    }
    */

    /**
     * 根据企业统一信用代码获取企业基本信息
     *
     * @param entUnifiedCode 统一信用代码
     * @param clazz          返回的目标对象类型
     * @return T
     * @author jiuxx
     * @date 2024/5/28 13:54
     */
    public <T> T getBaseInfoByEntUnifiedCode(String entUnifiedCode, Class<T> clazz) {
        TpEntBasicinfoVO vo = tpEntBasicinfoService.getBaseInfoByEntUnifiedCode(entUnifiedCode);
        return tpApiCommonService.copy(vo, clazz);
    }

    /*
     * 根据企业统一信用代码获取企业基本信息
     *
     * @param entUnifiedCode 企业统一信用代码
     * @return TpEntBasicinfoDTO
     * @author Ypp
     * @date 2024/5/28 9:19
     *
    @Override
    public TpEntBasicinfoDTO getBaseInfoByEntUnifiedCode(String entUnifiedCode) {
        return getBaseInfoByEntUnifiedCode(entUnifiedCode, TpEntBasicinfoDTO.class);
    }
    */

    /**
     * 根据企业id获取企业全称
     *
     * @param entId
     * @return java.lang.String
     * @author Ypp
     * @date 2023/11/15 14:00
     */
    public String getFullNameById(String entId) {
        TpEntBasicinfoVO basicinfoVO = tpEntBasicinfoService.view(entId);
        if (null == basicinfoVO) {
            return null;
        }
        return basicinfoVO.getEntFullName();
    }

    /**
     * 根据企业id获取企业简称
     *
     * @param entId
     * @return java.lang.String
     * @author Ypp
     * @date 2023/11/15 14:00
     */
    public String getSimpleNameById(String entId) {
        TpEntBasicinfoVO basicinfoVO = tpEntBasicinfoService.view(entId);
        if (null == basicinfoVO) {
            return null;
        }
        return basicinfoVO.getEntSimpleName();
    }

    /**
     * 新增企业基本信息
     * <pre>
     *     必填字段
     *         1. 企业名称
     *         2. 统一社会信用代码
     *         3. 注册地址行政区划
     *         4. 操作人
     *     处理逻辑
     *         1. 校验【统一社会信用代码】是否重复
     *         2. 如果经纬度不为空，则转换出geoCode并保存
     *         3. 保存企基本信息
     *         4. 保存一条部门信息（pdeptId = 1111111111111111111, deptType = SYS0501, category = 1, deptId = entId, ascnId = entId）
     * </pre>
     *
     * @param basicinfoDTO
     * @param jwtpid
     * @return java.lang.String
     * @throws com.jiuxi.core.bean.TopinfoRuntimeException 校验失败将抛出异常
     * @author jiuxx
     * @date 2024/5/28 14:21
     */
    /*
    @Override
    public String add(TpEntBasicinfoDTO basicinfoDTO, String jwtpid) {
        // 校验必填信息
        this.validateAdd(basicinfoDTO, jwtpid);

        TpEntBasicinfoVO vo = new TpEntBasicinfoVO();
        BeanUtils.copyProperties(basicinfoDTO, vo);
        String entId = tpEntBasicinfoService.add(vo, jwtpid);
        return entId;
    }
    */

    /**
     * 新增企业基本信息时校验
     * <pre>
     *     1. 企业名称
     *     2. 统一社会信用代码
     *     3. 注册地址
     * </pre>
     *
     * @param dto    企业信息
     * @param jwtpid 操作人员id
     * @return void
     * @author jiuxx
     * @date 2024/5/28 15:16
     */
    /*
    private void validateAdd(TpEntBasicinfoDTO dto, String jwtpid) {

        Validate.notBlank(jwtpid, "操作人员为空！");
        Validate.notBlank(dto.getEntFullName(), "企业全称为空！");
        Validate.notBlank(dto.getEntUnifiedCode(), "统一社会信用代码为空！");
        Validate.notBlank(dto.getProdAddrCode(), "生产地行政区划CODE为空！");

    }
    */

    /**
     * 新增企业基本信息
     * <pre>
     *     必填字段
     *         1. 企业id
     *         2. 操作人
     *     处理逻辑
     *         1. 校验【统一社会信用代码】是否重复
     *         2. 如果经纬度不为空，则转换出geoCode并保存
     *         3. 保存企基本信息
     *         4. 保存一条部门信息（pdeptId = 1111111111111111111, deptType = SYS0501, category = 1, deptId = entId, ascnId = entId）
     * </pre>
     *
     * @param basicinfoDTO 企业信息
     * @param jwtpid       操作人
     * @return java.lang.Integer 修改的行数
     * @throws com.jiuxi.core.bean.TopinfoRuntimeException 校验失败将抛出异常
     * @author jiuxx
     * @date 2024/5/28 14:21
     */
    /*
    @Override
    public int update(TpEntBasicinfoDTO basicinfoDTO, String jwtpid) {
        // 更新时校验
        this.validateUpdate(basicinfoDTO, jwtpid);

        TpEntBasicinfoVO vo = new TpEntBasicinfoVO();
        BeanUtils.copyProperties(basicinfoDTO, vo);
        int update = tpEntBasicinfoService.update(vo, jwtpid);
        return update;
    }
    */

    /**
     * 更新时校验
     *
     * @param dto
     * @param jwtpid
     * @return void
     * @author jiuxx
     * @date 2024/5/28 15:55
     */
    /*
    private void validateUpdate(TpEntBasicinfoDTO dto, String jwtpid) {

        Validate.notBlank(jwtpid, "操作人员为空！");
        Validate.notBlank(dto.getEntId(), "企业id为空！");

    }
    */

    /**
     * 删除企业信息  注意：将会删除关联的人员、账号、部门信息
     * <pre>
     *     1. 删除主部门关联关系的人员的账号
     *     2. 删除企业基本信息
     *     3. 删除所有企业部门信息
     *     4. 删除所有企业人员
     *     5. 发布删除事件
     * </pre>
     *
     * @param entId  企业第
     * @param jwtpid 操作人员id
     * @return int
     * @author jiuxx
     * @date 2024/5/28 17:02
     */
    /*
    @Override
    public int delete(String entId, String jwtpid) {

        return tpEntBasicinfoService.delete(entId, jwtpid);
    }
    */

    /**
     * 根据企业id获取企业基本信息-包含已删除的数据
     *
     * @param entId 企业id
     * @param clazz 返回的目标对象类型
     * @return T
     * @author jiuxx
     * @date 2023/11/15 17:13
     */
    public <T> T getBaseInfoIncludeNotActive(String entId, Class<T> clazz) {

        TpEntBasicinfoVO vo = tpEntBasicinfoService.getBaseInfoIncludeNotActive(entId);

        return tpApiCommonService.copy(vo, clazz);
    }

    /**
     * 根据企业id获取企业基本信息-包含已删除的数据
     *
     * @param entId 企业id
     * @return T
     * @author jiuxx
     * @date 2023/11/15 17:13
     */
    /*
    @Override
    public TpEntBasicinfoDTO getBaseInfoIncludeNotActive(String entId) {
        return getBaseInfoIncludeNotActive(entId, TpEntBasicinfoDTO.class);
    }
    */

    /**
     * 查询删除的企业信息
     *
     * @param query#prodAddrCode    生成经营地址行政区划编码，根据编码查询本级及下级
     * @param query#updateTimeStart 修改时间-开始  格式：yyyyMMddHHmmss   updateTimeStart <= updateTime <= updateTimeEnd
     * @param query#updateTimeEnd   修改时间-结束  格式：yyyyMMddHHmmss   updateTimeStart <= updateTime <= updateTimeEnd
     * @param query#current         当前页码，默认1
     * @param query#size            默认返回条数，默认10
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpEntBasicinfoDTO>
     * @author jiuxx
     * @date 2024//5 16:13
     */
    /*
    @Override
    public List<TpEntBasicinfoDTO> listDelete(TpEntDeleteQuery query) {
        // 查询
        List<TpEntBasicinfoVO> vos = tpEntBasicinfoService.listDelete(query);

        // 返回数据
        List<TpEntBasicinfoDTO> result = BeanUtil.copyToList(vos, TpEntBasicinfoDTO.class);
        return result;
    }
    */

    /**
     * 根据企业id判断数据是否存在 (忽略actived字段)
     *
     * @param entId
     * @return boolean 存在返回true, 不存在返回false
     * @author jiuxx
     * @date 2024/8/5 15:39
     */
    public boolean existsByEntId(String entId) {
        return tpEntBasicinfoService.existsByEntId(entId);
    }

    /**
     * 根据【统一信用代码】判断数据是否存在
     *
     * @param entUnifiedCode 统一信用代码
     * @return boolean 存在返回true, 不存在返回false
     * @author jiuxx
     * @date 2024/8/5 15:39
     */
    public boolean existsByEntUnifiedCode(String entUnifiedCode) {
        return tpEntBasicinfoService.existsEntUnifiedCode(entUnifiedCode, "");
    }
}
