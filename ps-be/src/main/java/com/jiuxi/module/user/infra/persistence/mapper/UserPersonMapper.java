package com.jiuxi.module.user.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpPersonBasicinfoMapper
 * @Description: 人员基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface UserPersonMapper {

    IPage<TpPersonBasicinfoVO> getPage(Page<TpPersonBasicinfoVO> page, @Param("query") TpPersonBasicQuery query);

    int save(TpPersonBasicinfo bean);

    TpPersonBasicinfoVO view(@Param("personId") String personId);

    TpPersonBasicinfo selectByPhone(@Param("phone") String phone);

    TpPersonBasicinfo selectByPhoneAndPersonId(@Param("phone") String phone, @Param("personId") String personId);

    TpPersonBasicinfo selectByEmail(@Param("email") String email);

    /**
     * 查询出在该单位所有部门下的所有主部门关联关系的人员id
     *
     * @param ascnId 单位id / 企业id
     * @return java.util.List<java.lang.String>
     * @author 杨占锐
     * @date 2024/6/4 19:12
     */
    List<String> selectPersonIdByAscnId(@Param("ascnId") String ascnId);

    int update(TpPersonBasicinfo bean);

    /**
     * 批量更新用户的单位id
     *
     * @param deptIds
     * @param ascnId
     * @return
     */
    int updateAscnIdByDeptIds(@Param("deptIds") List<String> deptIds, @Param("ascnId") String ascnId, @Param("oldAscnId") String oldAscnId);

    /**
     * 根据单位id，删除人员信息
     *
     * @param ascnId     单位id
     * @param updator    操作人
     * @param updateTime 修改时间
     * @return int
     * @author 杨占锐
     * @date 2024/5/28 17:20
     */
    int deletePersonDeptByAscnId(@Param("ascnId") String ascnId, @Param("updator") String updator, @Param("updateTime") String updateTime);

    int deleteByPersonId(TpPersonBasicinfo bean);

    List<TpPersonBasicinfoVO> getBaseInfoByIdCard(@Param("idcard") String idcard);
    
}
