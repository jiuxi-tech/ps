package com.jiuxi.admin.core.service.api;

import com.jiuxi.admin.core.bean.vo.TpPersonExinfoVO;
import com.jiuxi.module.user.app.service.UserPersonService;
// import com.jiuxi.plugin.api.bean.dto.TpPersonExinfoDTO;
// import com.jiuxi.plugin.api.interfaces.TpPersonExtApiService; // Commented out - interface not available
import org.apache.commons.lang3.Validate;
import com.jiuxi.admin.core.service.TpPersonBasicinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: TpPersonExtApiServiceImpl
 * @Description: 人员扩展信息服务
 * @Author jiuxx
 * @Date 2024/5/30 17:32
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpPersonExtApiServiceImpl")
public class TpPersonExtApiServiceImpl {

    @Autowired
    private TpPersonBasicinfoService tpPersonBasicinfoService;

    @Autowired
    private TpApiCommonService tpApiCommonService;

    public <T> T getExtInfo(String personId, Class<T> clazz) {
        TpPersonExinfoVO vo = tpPersonBasicinfoService.expView(personId);

        return tpApiCommonService.copy(vo, clazz);
    }

    public Object getExtInfo(String personId) {
        // return getExtInfo(personId, TpPersonExinfoDTO.class); // Commented out - TpPersonExinfoDTO not available
        return null;
    }

    /**
     * 新增/修改人员扩展信息
     * <pre>
     *     处理逻辑
     *         1. 校验 人员id不能为空
     *         2. 存在信息则执行修改，不存在则执行新增
     * </pre>
     * @param dto   人员扩展信息
     * @return void
     * @author jiuxx
     * @date 2024/5/30 17:35
     */
    public void edit(Object dto) {

        // Validate.notBlank(dto.getPersonId(), "人员id不能为空！"); // Commented out - TpPersonExinfoDTO not available

        // TpPersonExinfoVO vo = tpApiCommonService.copy(dto, TpPersonExinfoVO.class);
        // tpPersonBasicinfoService.expAdd(vo); // Commented out - TpPersonExinfoDTO not available
    }
}
