package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.entity.TpAttachinfo;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoRefVO;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoVO;

import java.util.List;

/**
 * @ClassName: TpAttachinfoService
 * @Description: 附件表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpAttachinfoService {

    int save(TpAttachinfo bean);

    TpAttachinfo view(String attachId);

    List<TpAttachinfoVO> selectByReferIdAndType(String referId, String referType);

    int update(TpAttachinfo bean, String pid);

    /**
     * 删除附件
     * <pre>
     * 根据 referId 和 attachId 删除附件：
     *    如果 referId 为空，则只能删除当前登录人自己创建的附件，物理删除
     *    如果 referId 不为空， 则根据 数据权限 判断是否能够删除，如果权限不对，则抛出异常 逻辑删除
     *</pre>
     *
     * @param attachId:
     * @param jwtpid:
     * @return int
     * @author pand
     * @date 2021-03-15 13:22
     */
    int remove(String referId, String attachId, String jwtpid);

    /**
     * 物理删除
     *
     * @param attachId:
     * @return int
     * @author pand
     * @date 2021-03-15 13:22
     */
    int delete(String attachId);

    int deleteByReferId(TpAttachinfo bean, String pid);

    /**
     * 根据 referId 删除附件
     * @author 杨攀
     * @date 2022/11/25 16:49
     * @param referId
     * @param referType
     * @param pid
     * @return int
     */
    int deleteByReferIdAndReferType(String referId, String referType, String pid);

    /**
     * 批量附件绑定
     * @param list
     * @param referId
     * @param pid
     */
    void listBand(List<TpAttachinfoRefVO> list, String referId, String pid);

    /**
     * 单类型附件绑定
     * @param vo
     * @param referId
     * @param pid
     */
    void band(TpAttachinfoRefVO vo, String referId, String pid);

    /**
     * 修改附件的rederId
     *
     * @param attachIds 多个附件id
     * @param referId   业务id
     * @param pid       操作人员id
     * @return void
     * @author 杨占锐
     * @date 2024/6/5 15:29
     */
    void modifyReferId(List<String> attachIds, String referId, String pid);

    /**
     * 逻辑删除附件
     *
     * @param attachId 附件id
     * @param pid      操作人员id
     * @return void
     * @author 杨占锐
     * @date 2024/6/5 15:34
     */
    void deleteByAttachId(String attachId, String pid);
    /**
     * 判断附件是否存在 （忽略actived字段）
     *
     * @param attachId
     * @return boolean
     * @author 杨占锐
     * @date 2024/7/22 14:51
     */
    boolean exists(String attachId);

}
