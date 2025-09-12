package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpAccountThirdQuery;
import com.jiuxi.admin.core.bean.vo.TpAccountThirdVO;
import com.jiuxi.admin.core.service.TpAccountThirdService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


/**
 * @ClassName: IotThreeAccountController
 * @Description: 合作方管理表
 * @Author pand
 * @Date 2022-04-20 15:02:39
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/account-third")
@Authorization
public class TpAccountThirdController {

    @Autowired
    private TpAccountThirdService tpAccountThirdService;

    /**
     * 列表
     *
     * @Author pand
     * @Date 2022-04-20 15:02:39
     */
    @RequestMapping("/list")
    public JsonResponse list(TpAccountThirdQuery query) {
        IPage<TpAccountThirdVO> page = tpAccountThirdService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }


    /**
     * 信息
     *
     * @Author pand
     * @Date 2022-04-20 15:02:39
     */
    @RequestMapping("/view")
    public JsonResponse view(String accountId) {
        TpAccountThirdVO vo = tpAccountThirdService.view(accountId);
        return JsonResponse.buildSuccess(vo);
    }



    /**
     * 保存
     *
     * @Author pand
     * @Date 2022-04-20 15:02:39
     */
    @RequestMapping("/add")
    public JsonResponse add(TpAccountThirdVO tpAccountThredVO, String jwtpid) {
        String accountId = tpAccountThirdService.add(tpAccountThredVO, jwtpid);
        return JsonResponse.buildSuccess(accountId);
    }

    /**
     * 重置secret
     *
     * @Author pand
     * @Date 2022-04-20 15:02:39
     */
    @RequestMapping("/reset")
    public JsonResponse reset(String accountId, String jwtpid) {
        tpAccountThirdService.reset(accountId, jwtpid);
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除
     *
     * @Author pand
     * @Date 2022-04-20 15:02:39
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String[] appKeys, String jwtpid) {
        tpAccountThirdService.deleteByIds(Arrays.asList(appKeys), jwtpid);
        return JsonResponse.buildSuccess();
    }

}
