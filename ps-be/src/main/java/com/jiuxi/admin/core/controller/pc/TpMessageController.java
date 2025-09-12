package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpMessageQuery;
import com.jiuxi.admin.core.bean.vo.TpMessageReadVO;
import com.jiuxi.admin.core.bean.vo.TpMessageVO;
import com.jiuxi.admin.core.service.TpMessageService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


/**
 * @ClassName: TpMessageController
 * @Description: 消息管理
 * @Author yangp
 * @Date 2021-03-24 16:04:29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/message")
public class TpMessageController {

    @Autowired
    private TpMessageService tpMessageService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpMessageQuery query, String jwtpid) {
        IPage<TpMessageVO> page = tpMessageService.queryPage(query, jwtpid);
        return JsonResponse.buildSuccess(page);
    }


    /**
     * 消息发送
     *
     * @param tpMessage
     * @param jwtpid    当前登录人id
     * @return com.jiuxi.common.bean.JsonResponse
     * @author 杨攀
     * @date 2021/3/25 20:32
     */
    @RequestMapping("/send")
    public JsonResponse send(@Validated(value = AddGroup.class) @RequestBody TpMessageVO tpMessage, String jwtpid) {

        // 创建人id
        tpMessage.setCreator(jwtpid);

        boolean bool = tpMessageService.send(tpMessage);

        if (bool) {
            return JsonResponse.buildSuccess();
        } else {
            return JsonResponse.buildFailure("消息发送失败");
        }
    }


    /**
     * 消息提醒
     */
    @RequestMapping("/remindlist")
    public JsonResponse remindlist(String jwtaid, String jwtdid, String jwtpid) {

        List<TpMessageVO> list = tpMessageService.remindlist(jwtaid, jwtdid, jwtpid);

        return JsonResponse.buildSuccess(list);
    }


    /**
     * 点击消息查看后，把消息内容和人员id保存到消息已读报
     */
    @RequestMapping("/remind")
    public JsonResponse remind(String msgId, String jwtpid) {
        TpMessageReadVO vo = tpMessageService.remind(msgId, jwtpid);
        return JsonResponse.buildSuccess(vo);
    }


    /**
     * 修改
     */
    // @RequestMapping("/update")
    // public JsonResponse update(TpMessageVO tpMessage, String jwtpid) {
    //     tpMessageService.update(tpMessage, jwtpid);
    //     return JsonResponse.buildSuccess();
    // }

    /**
     * 删除
     */
    // @RequestMapping("/delete")
    // public JsonResponse delete(String[] msgIds) {
    //     tpMessageService.deleteByIds(Arrays.asList(msgIds));
    //     return JsonResponse.buildSuccess();
    // }

}
