package com.jiuxi.admin.core.controller.pc;

import com.jiuxi.admin.core.bean.vo.TpPersonTagVO;
import com.jiuxi.admin.core.service.TpPersonTagService;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.annotation.IgnoreAuthorization;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: TpPersonTagController
 * @Description: 人员标签控制器
 * @Author system
 * @Date 2025-01-28
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/person-tag")
@Authorization
public class TpPersonTagController {

    @Autowired
    private TpPersonTagService tpPersonTagService;

    /**
     * 查询人员标签列表
     *
     * @param personId 人员ID
     * @param deptId   部门ID
     * @return JsonResponse
     */
    @RequestMapping("/person-tags")
    @IgnoreAuthorization
    public JsonResponse getPersonTags(@RequestParam String personId, @RequestParam String deptId) {
        List<TpPersonTagVO> list = tpPersonTagService.getPersonTags(personId, deptId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 查询标签下的人员列表
     *
     * @param tagId 标签ID
     * @return JsonResponse
     */
    @RequestMapping("/tag-persons")
    @IgnoreAuthorization
    public JsonResponse getTagPersons(@RequestParam String tagId) {
        List<TpPersonTagVO> list = tpPersonTagService.getTagPersons(tagId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 保存人员标签关系
     *
     * @param personId 人员ID
     * @param tagIds   标签ID列表（逗号分隔）
     * @param deptId   部门ID
     * @param jwtpid   当前用户ID
     * @return JsonResponse
     */
    @RequestMapping("/save")
    @IgnoreAuthorization
    public JsonResponse savePersonTags(@RequestParam String personId, 
                                       @RequestParam String tagIds, 
                                       @RequestParam String deptId, 
                                       String jwtpid) {
        List<String> tagIdList = null;
        if (tagIds != null && !tagIds.trim().isEmpty()) {
            tagIdList = Arrays.asList(tagIds.split(","));
        }
        
        int count = tpPersonTagService.savePersonTags(personId, tagIdList, deptId, jwtpid);
        return JsonResponse.buildSuccess(count);
    }

    /**
     * 删除人员标签关系
     *
     * @param personId 人员ID
     * @param tagId    标签ID
     * @param deptId   部门ID
     * @return JsonResponse
     */
    @RequestMapping("/delete")
    @IgnoreAuthorization
    public JsonResponse deletePersonTag(@RequestParam String personId, 
                                        @RequestParam String tagId, 
                                        @RequestParam String deptId) {
        int count = tpPersonTagService.deletePersonTag(personId, tagId, deptId);
        return JsonResponse.buildSuccess(count);
    }

    /**
     * 根据标签ID删除所有关联关系
     *
     * @param tagId 标签ID
     * @return JsonResponse
     */
    @RequestMapping("/delete-by-tag")
    @IgnoreAuthorization
    public JsonResponse deleteByTagId(@RequestParam String tagId) {
        int count = tpPersonTagService.deleteByTagId(tagId);
        return JsonResponse.buildSuccess(count);
    }

}