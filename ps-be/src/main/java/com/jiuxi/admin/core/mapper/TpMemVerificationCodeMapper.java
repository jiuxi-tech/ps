package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpMemVerificationCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpMemVerificationCodeMapper
 * @Description:
 * @Author pand
 * @Date 2021-04-22 15:23:29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpMemVerificationCodeMapper {

    int add(TpMemVerificationCode bean);

    int updateCodeByCodeId(@Param("codeId") String codeId, @Param("verificationCode") String verificationCode, @Param("sendTimeStamp") String sendTimeStamp);

    /**
     * 倒序排序取最近的一条
     *
     * @param phone:
     * @param busType:
     * @return com.jiuxi.admin.core.bean.entity.TpMemVerificationCode
     * @author pand
     * @date 2021-04-23 10:10
     */
    TpMemVerificationCode select(@Param("phone") String phone, @Param("busType") String busType);

    /**
     * 根据验证码查询验证码是否有效
     *
     * @param phone:
     * @param busType:
     * @param verificationCode: 验证码
     * @return com.jiuxi.admin.core.bean.entity.TpMemVerificationCode
     * @author pand
     * @date 2021-04-23 10:10
     */
    TpMemVerificationCode selectByVcode(@Param("phone") String phone, @Param("busType") String busType, @Param("verificationCode") String verificationCode);

    /**
     * 根据验证码id删除验证码
     *
     * @param codeId:
     * @return void
     * @author pand
     * @date 2021-04-23 13:41
     */
    void delete(String codeId);

}
