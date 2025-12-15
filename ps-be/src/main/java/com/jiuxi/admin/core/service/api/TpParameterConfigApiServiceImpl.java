package com.jiuxi.admin.core.service.api;

import com.jiuxi.admin.core.bean.vo.TpParameterConfigVO;
import com.jiuxi.admin.core.service.TpParameterConfigService;
// import com.jiuxi.plugin.api.bean.dto.TpParameterConfigDTO;
// import com.jiuxi.plugin.api.interfaces.TpParameterConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: TpParameterConfigApiServiceImpl
 * @Description:
 * @Author jiuxx
 * @Date 2024/5/27 18:03
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpParameterConfigApiServiceImpl")
public class TpParameterConfigApiServiceImpl /* implements TpParameterConfigApiService */ {

    @Autowired
    private TpParameterConfigService tpParameterConfigService;

    @Autowired
    private TpApiCommonService tpApiCommonService;

    /**
     * 根据参数配置编码，查询配置信息
     *
     * @param pmKey 配置编码
     * @param clazz 返回的对象类型
     * @return T    返回的对象
     * @author jiuxx
     * @date 2024/5/27 18:01
     */
    public <T> T getConfig(String pmKey, Class<T> clazz) {
        TpParameterConfigVO vo = tpParameterConfigService.viewByPmKey(pmKey);

        return tpApiCommonService.copy(vo, clazz);
    }

    /**
     * 根据参数配置编码，查询配置信息 (不查询无效和未启用的数据)
     *
     * @param pmKey 配置编码
     * @return TpParameterConfigDTO
     * @author jiuxx
     * @date 2024/5/27 18:01
     */
    public Object getConfig(String pmKey) {
        // return getConfig(pmKey, TpParameterConfigDTO.class); // Commented out - TpParameterConfigDTO not available
        return null;
    }
}
