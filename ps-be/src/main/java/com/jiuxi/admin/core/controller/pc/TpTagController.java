package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpTagQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonTagVO;
import com.jiuxi.admin.core.bean.vo.TpTagVO;
import com.jiuxi.admin.core.service.TpTagService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: TpTagController
 * @Description: 标签管理
 * @Author system
 * @Date 2025-01-28
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/tag")
@Authorization
public class TpTagController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "tagId";

    @Autowired
    private TpTagService tpTagService;

    /**
     * 标签列表（分页查询）
     */
    @RequestMapping("/list")
    public JsonResponse list(TpTagQuery query, String jwtpid, String jwtaid) {
        // jwtpid作为租户ID，jwtaid作为机构ID
        IPage<TpTagVO> page = tpTagService.queryPage(query, jwtpid, jwtaid);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
    }

    /**
     * 标签列表（不分页）
     */
    @RequestMapping("/all-list")
    @IgnoreAuthorization
    public JsonResponse allList(TpTagQuery query, String jwtaid) {
        List<TpTagVO> list = tpTagService.getList(query, jwtaid, jwtaid);
        return JsonResponse.buildSuccess(list);
    }



    /**
     * 新增标签
     */
    @RequestMapping(value = "/add")
    public JsonResponse add(@Validated(value = AddGroup.class) TpTagVO vo, String jwtpid, String jwtaid) {
        int count = tpTagService.add(vo, jwtpid, jwtaid, jwtaid);
        return JsonResponse.buildSuccess(count);
    }

    /**
     * 查看标签信息
     */
    @RequestMapping(value = "/view")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse view(String tagId) {
        TpTagVO vo = tpTagService.view(tagId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 修改标签
     */
    @RequestMapping(value = "/update")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpTagVO vo, String jwtpid) {
        int count = tpTagService.update(vo, jwtpid);
        return JsonResponse.buildSuccess(count);
    }

    /**
     * 删除标签
     */
    @RequestMapping("/delete")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse delete(String tagId, String jwtpid) {
        tpTagService.delete(tagId, jwtpid, jwtpid);
        return JsonResponse.buildSuccess();
    }

    /**
     * 为人员分配标签
     */
    @RequestMapping("/assign-person-tags")
    public JsonResponse assignPersonTags(String personId, String tagIds, String jwtpid) {
        // 将逗号分隔的字符串转换为List
        List<String> tagIdList = java.util.Arrays.asList(tagIds.split(","));
        tpTagService.assignPersonTags(personId, tagIdList, jwtpid);
        return JsonResponse.buildSuccess();
    }

    /**
     * 查询人员标签列表
     */
    @RequestMapping("/person-tags")
    @IgnoreAuthorization
    public JsonResponse personTags(String personId) {
        List<TpPersonTagVO> list = tpTagService.getPersonTags(personId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 移除人员指定标签
     */
    @RequestMapping("/remove-person-tag")
    public JsonResponse removePersonTag(String personId, String tagId, String jwtpid) {
        // 将单个tagId转换为List
        List<String> tagIdList = java.util.Arrays.asList(tagId);
        tpTagService.removePersonTags(personId, tagIdList);
        return JsonResponse.buildSuccess();
    }
}