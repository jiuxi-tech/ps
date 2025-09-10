package com.jiuxi.admin.core.controller.pc;

import com.jiuxi.admin.core.bean.vo.TpCityVO;
import com.jiuxi.admin.core.service.TpCityService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.core.core.validator.group.AddGroup;
import com.jiuxi.core.core.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName: TpCityController
 * @Description: 行政区划表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/city")
public class TpCityController {

    @Autowired
    private TpCityService tpCityService;

    /**
     * 行政区划树
     *
     * @param cityId   行政区划id
     * @param sync     1：同步 0：异步
     * @param expand   true：树展开 false：树关闭
     * @param showTop  on：返回头节点   off：不返回头节点
     * @param response
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2022-04-21 11:08
     */
    @RequestMapping("/tree")
    public JsonResponse tree(@RequestParam(value = "cityId") String cityId, int sync,
                             @RequestParam(value = "expand", required = false, defaultValue = "false") boolean expand,
                             @RequestParam(value = "showTop", required = false, defaultValue = "off") String showTop,
                             HttpServletResponse response) {

        List<TreeNode> treeNode = tpCityService.tree(cityId, sync, expand, showTop);
        return JsonResponse.buildSuccess(treeNode);
    }

    /**
     * 树
     */
    @RequestMapping("/tree-by-code")
    public JsonResponse treeByCode(@RequestParam(value = "cityId", required = false) String cityId, int sync,
                                   @RequestParam(value = "expand", required = false, defaultValue = "false") boolean expand,
                                   @RequestParam(value = "showTop", required = false, defaultValue = "off") String showTop, String jwtCityCode,
                                   HttpServletResponse response) {

        List<TreeNode> rootNode = tpCityService.treeByCode(cityId, sync, expand, showTop, jwtCityCode);
        return JsonResponse.buildSuccess(rootNode);
    }

    /**
     * 父级code，查询下一级
     *
     * @return
     */
    @RequestMapping("/select")
    public JsonResponse select(@RequestParam(value = "cityId") String cityId) {
        List<TreeNode> list = tpCityService.select(cityId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 查询本级及下级行政区划
     *
     * @return
     */
    @RequestMapping("/select-two-level")
    public JsonResponse selectTwoLevel(String cityId) {
        List<TreeNode> list = tpCityService.selectTwoLevel(cityId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 根据层级查询行政区划
     *
     * @return
     */
    @RequestMapping("/select-by-citylevel")
    public JsonResponse selectByCityLevel(String pcityId, String cityLevel) {
        List<TpCityVO> list = tpCityService.selectByCityLevel(pcityId, cityLevel);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 信息
     */
    @RequestMapping("/view")
    public JsonResponse view(String cityId) {
        TpCityVO vo = tpCityService.view(cityId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/add")
    public JsonResponse save(@Validated(value = AddGroup.class) TpCityVO vo) {
        String cityId = tpCityService.save(vo);

        return JsonResponse.buildSuccess(cityId);
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpCityVO vo) {
        int count = tpCityService.update(vo);

        return JsonResponse.buildSuccess(count);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String cityId) {
        int count = tpCityService.delete(cityId);

        return JsonResponse.buildSuccess(count);
    }


}
