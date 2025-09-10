package com.jiuxi.admin.core.service.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.jiuxi.admin.core.bean.entity.TpAttachinfo;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoRefVO;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoVO;
import com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO;
import com.jiuxi.admin.core.service.TpAttachinfoService;
// import com.jiuxi.plugin.api.bean.dto.TpAttachinfoDTO;
// import com.jiuxi.plugin.api.bean.dto.TpAttachinfoRefDTO;
// import com.jiuxi.plugin.api.interfaces.TpAttachinfoApiService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TpAttachinfoApiServiceImpl
 * @Description: 附件接口
 * @Author 杨占锐
 * @Date 2024/6/5 15:06
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service
public class TpAttachinfoApiServiceImpl /* implements TpAttachinfoApiService */ {

    @Autowired
    private TpAttachinfoService tpAttachinfoService;

    @Autowired
    private TpApiCommonService tpApiCommonService;

    /**
     * 查询附件信息
     *
     * @param referId    业务id
     * @param referType  附件类型
     * @return TpAttachinfoDTO
     * @author 杨占锐
     * @date 2024/6/5 15:32
     */
    public List<Object> selectByReferIdAndType(String referId, String referType) {

        Validate.notBlank(referId, "referId不能为空！");
        Validate.notBlank(referType, "referType不能为空！");

        // 查询附件
        List<TpAttachinfoVO> vos = tpAttachinfoService.selectByReferIdAndType(referId, referType);

        // 返回
        // List<TpAttachinfoDTO> result = BeanUtil.copyToList(vos, TpAttachinfoDTO.class); // Commented out - TpAttachinfoDTO not available
        return null;
    }

    /**
     * 附件绑定
     *
     * @param vo      附件信息
     * @param referId 业务id
     * @param pid     操作人员id
     * @return void
     * @author 杨占锐
     * @date 2024/6/5 15:20
     */
    // public void band(TpAttachinfoRefDTO vo, String referId, String pid) {
    //     Validate.notBlank(referId, "referId不能为空！");
    //     Validate.notBlank(pid, "操作人id不能为空！");
    //     Validate.notNull(vo, "附件信息不能为空！");
    //
    //     tpAttachinfoService.band(tpApiCommonService.copy(vo, TpAttachinfoRefVO.class), referId, pid);
    // }

    /**
     * 删除附件信息
     *
     * @param referId   业务id
     * @param referType 附件类型
     * @param pid       操作人员id
     * @return int
     * @author 杨占锐
     * @date 2024/6/5 15:21
     */
    public int deleteByReferIdAndReferType(String referId, String referType, String pid) {

        Validate.notBlank(referId, "referId不能为空！");
        Validate.notBlank(referType, "referType不能为空！");
        Validate.notBlank(pid, "操作人id不能为空！");

        return tpAttachinfoService.deleteByReferIdAndReferType(referId, referType, pid);
    }

    /**
     * 逻辑删除附件
     *
     * @param attachId 附件id
     * @param pid      操作人员id
     * @return void
     * @author 杨占锐
     * @date 2024/6/5 15:34
     */
    public void deleteByAttachId(String attachId, String pid) {
        Validate.notBlank(attachId, "attachId不能为空！");
        Validate.notBlank(pid, "操作人id不能为空！");

        tpAttachinfoService.deleteByAttachId(attachId, pid);
    }

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
    public void modifyReferId(List<String> attachIds, String referId, String pid) {
        if (CollectionUtil.isEmpty(attachIds)) {
            return;
        }
        Validate.notBlank(referId, "referId不能为空！");
        Validate.notBlank(pid, "操作人id不能为空！");
        tpAttachinfoService.modifyReferId(attachIds, referId, pid);
    }

    /**
     * 保存
     *
     * @param attachinfoDTO
     * @return int
     * @author 杨占锐
     * @date 2024/6/21 8:50
     */
    /*@Override
    public int save(TpAttachinfoDTO attachinfoDTO) {
        TpAttachinfo tpAttachinfo = BeanUtil.copyProperties(attachinfoDTO, TpAttachinfo.class);
        return tpAttachinfoService.save(tpAttachinfo);
    }*/

    /**
     * 根据主键查看
     *
     * @param attachId 附件id
     * @return com.jiuxi.plugin.api.bean.dto.TpAttachinfoDTO
     * @author 杨占锐
     * @date 2024/6/24 16:35
     */
    /*@Override
    public TpAttachinfoDTO view(String attachId) {
        TpAttachinfo view = tpAttachinfoService.view(attachId);
        return tpApiCommonService.copy(view, TpAttachinfoDTO.class);
    }*/
    /**
     * 判断附件是否存在 （忽略actived字段）
     *
     * @param attachId
     * @return boolean
     * @author 杨占锐
     * @date 2024/7/22 14:51
     */
    public boolean exists(String attachId) {
        return tpAttachinfoService.exists(attachId);
    }

    /**
     * 修改附件
     *
     * @param attachinfoDTO
     * @return int
     * @author 杨占锐
     * @date 2024/7/22 14:57
     */
    /*@Override
    public int update(TpAttachinfoDTO attachinfoDTO) {
        TpAttachinfo tpAttachinfo = BeanUtil.copyProperties(attachinfoDTO, TpAttachinfo.class);
        return tpAttachinfoService.update(tpAttachinfo, tpAttachinfo.getUpdator());
    }*/
}
