package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.OrgTreeChangeHistory;
import com.jiuxi.admin.core.bean.query.OrgTreeChangeHistoryQuery;
import com.jiuxi.admin.core.bean.vo.OrgTreeChangeHistoryVO;
import com.jiuxi.admin.core.service.OrgTreeChangeHistoryService;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 组织机构变更历史控制器
 * @author AI Assistant
 * @date 2024-01-26
 */
@RestController
@RequestMapping("/api/org-tree-change-history")
public class OrgTreeChangeHistoryController {

    @Autowired
    private OrgTreeChangeHistoryService orgTreeChangeHistoryService;
    
    @Autowired
    private TpDeptBasicinfoService tpDeptBasicinfoService;

    /**
     * 分页查询历史记录
     */
    @PostMapping("/list")
    public JsonResponse list(OrgTreeChangeHistoryQuery query) {
        try {
            IPage<OrgTreeChangeHistoryVO> page = orgTreeChangeHistoryService.queryPage(query);
            return JsonResponse.buildSuccess(page);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }



    /**
     * 获取最新版本
     */
    @GetMapping("/latest")
    public JsonResponse getLatest() {
        try {
            OrgTreeChangeHistory latest = orgTreeChangeHistoryService.getLatestVersion();
            return JsonResponse.buildSuccess(latest);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取最新版本失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询详情
     */
    @GetMapping("/{id}")
    public JsonResponse getById(@PathVariable String id) {
        try {
            OrgTreeChangeHistory history = orgTreeChangeHistoryService.getById(id);
            if (history != null) {
                return JsonResponse.buildSuccess(history);
            } else {
                return JsonResponse.buildFailure("记录不存在");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查看历史记录详情（用于前端历史查看页面）
     */
    @PostMapping("/view")
    public JsonResponse historyView(@RequestParam("historyId") String historyId) {
        try {
            OrgTreeChangeHistory history = orgTreeChangeHistoryService.getById(historyId);
            if (history != null) {
                // 转换为VO对象，包含orgName等字段
                OrgTreeChangeHistoryVO vo = new OrgTreeChangeHistoryVO();
                vo.setId(history.getId());
                vo.setOperationType(history.getOperationType());
                vo.setOperationTime(history.getOperationTime());
                vo.setOperatorUserId(history.getOperatorId());
                vo.setBeforeData(history.getBeforeData());
                vo.setAfterData(history.getAfterData());
                vo.setBeforeFullTree(history.getBeforeFullTree());
                vo.setAfterFullTree(history.getAfterFullTree());
                vo.setDeptId(history.getDeptId());
                
                // 设置操作类型名称
                switch (history.getOperationType()) {
                    case "CREATE":
                        vo.setOperationTypeName("创建");
                        break;
                    case "UPDATE":
                        vo.setOperationTypeName("更新");
                        break;
                    case "DELETE":
                        vo.setOperationTypeName("删除");
                        break;
                    default:
                        vo.setOperationTypeName(history.getOperationType());
                }
                
                // 通过deptId查询orgName
                String orgName = "未知节点";
                if (history.getDeptId() != null && history.getDeptId() != 0L) {
                    try {
                        TpDeptBasicinfoVO deptInfo = tpDeptBasicinfoService.view(String.valueOf(history.getDeptId()));
                        if (deptInfo != null && deptInfo.getDeptFullName() != null) {
                            orgName = deptInfo.getDeptFullName();
                        }
                    } catch (Exception e) {
                        // 查询失败时保持默认值
                    }
                }
                vo.setOrgName(orgName);
                vo.setOperatorUserName("用户ID:" + history.getOperatorId());
                
                return JsonResponse.buildSuccess(vo);
            } else {
                return JsonResponse.buildFailure("记录不存在");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }
}