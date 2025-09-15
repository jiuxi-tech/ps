package com.jiuxi.module.role.app.orchestrator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.module.role.app.assembler.RoleCommandAssembler;
import com.jiuxi.module.role.app.assembler.RoleQueryAssembler;
import com.jiuxi.module.role.app.command.dto.*;
import com.jiuxi.module.role.app.command.handler.*;
import com.jiuxi.module.role.app.query.dto.*;
import com.jiuxi.module.role.app.query.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 角色业务流程编排器
 * 负责协调复杂的业务流程，整合命令和查询处理器
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@Service
public class RoleOrchestrator {
    
    // 命令处理器
    @Autowired
    private CreateRoleCommandHandler createRoleCommandHandler;
    @Autowired
    private UpdateRoleCommandHandler updateRoleCommandHandler;
    @Autowired
    private DeleteRoleCommandHandler deleteRoleCommandHandler;
    @Autowired
    private AssignPermissionsCommandHandler assignPermissionsCommandHandler;
    
    // 查询处理器
    @Autowired
    private RolePageQueryHandler rolePageQueryHandler;
    @Autowired
    private RoleDetailQueryHandler roleDetailQueryHandler;
    @Autowired
    private PermissionTreeQueryHandler permissionTreeQueryHandler;
    
    // 数据转换器
    @Autowired
    private RoleCommandAssembler roleCommandAssembler;
    @Autowired
    private RoleQueryAssembler roleQueryAssembler;
    
    /**
     * 创建角色业务流程
     * 1. 数据转换和验证
     * 2. 执行创建命令
     * 3. 返回结果
     */
    public int createRole(TpRoleVO vo, String operatorId, String applicationId, int category) {
        // 转换为命令DTO
        CreateRoleCommand command = roleCommandAssembler.toCreateCommand(vo, operatorId, applicationId);
        command.setCategory(category);
        
        // 验证命令
        roleCommandAssembler.validateCreateCommand(command);
        
        // 执行命令
        String roleId = createRoleCommandHandler.handle(command);
        
        // 返回成功标识
        return 1;
    }
    
    /**
     * 更新角色业务流程
     * 1. 数据转换和验证
     * 2. 执行更新命令
     * 3. 返回结果
     */
    public int updateRole(TpRoleVO vo, String operatorId) {
        // 转换为命令DTO
        UpdateRoleCommand command = roleCommandAssembler.toUpdateCommand(vo, operatorId);
        
        // 验证命令
        roleCommandAssembler.validateUpdateCommand(command);
        
        // 执行命令
        boolean success = updateRoleCommandHandler.handle(command);
        
        return success ? 1 : 0;
    }
    
    /**
     * 删除角色业务流程
     * 1. 构建删除命令
     * 2. 执行删除命令
     * 3. 返回结果
     */
    public int deleteRole(String roleId, String creator, String operatorId) {
        // 构建删除命令
        DeleteRoleCommand command = new DeleteRoleCommand(roleId, operatorId, creator);
        
        // 执行命令
        boolean success = deleteRoleCommandHandler.handle(command);
        
        return success ? 1 : 0;
    }
    
    /**
     * 分配角色权限业务流程
     * 1. 构建权限分配命令
     * 2. 执行权限分配
     * 3. 返回结果
     */
    public int assignRolePermissions(String roleId, String menuIds, String operatorId) {
        // 解析菜单ID列表
        List<String> menuIdList = Arrays.asList(menuIds.split(","));
        
        // 构建命令
        AssignPermissionsCommand command = new AssignPermissionsCommand(roleId, menuIdList, operatorId);
        
        // 执行命令
        boolean success = assignPermissionsCommandHandler.handle(command);
        
        return success ? 1 : 0;
    }
    
    /**
     * 角色分页查询业务流程
     * 1. 构建查询条件
     * 2. 执行查询
     * 3. 返回结果
     */
    public IPage<TpRoleVO> queryRolePage(com.jiuxi.admin.core.bean.query.TpRoleQuery legacyQuery, 
                                        String operatorId, String operatorRoleIds, String applicationId) {
        // 转换查询条件
        RolePageQuery query = roleQueryAssembler.toPageQuery(legacyQuery, operatorId, operatorRoleIds, applicationId);
        
        // 验证查询条件
        roleQueryAssembler.validatePageQuery(query);
        
        // 执行查询
        return rolePageQueryHandler.handle(query);
    }
    
    /**
     * 角色授权查询业务流程
     * 1. 构建查询条件
     * 2. 执行查询
     * 3. 返回结果
     */
    public LinkedHashSet<TpRoleVO> queryRoleAuthList(com.jiuxi.admin.core.bean.query.TpRoleAuthQuery legacyQuery,
                                                    String operatorId, String deptId, String operatorRoleIds, String applicationId) {
        // 转换查询条件
        RoleAuthQuery query = roleQueryAssembler.toAuthQuery(legacyQuery, operatorId, deptId, operatorRoleIds, applicationId);
        
        // 验证查询条件
        roleQueryAssembler.validateAuthQuery(query);
        
        // 执行查询（这里需要实现RoleAuthQueryHandler）
        // 暂时返回空结果
        return new LinkedHashSet<>();
    }
    
    /**
     * 角色详情查询业务流程
     * 1. 构建查询条件
     * 2. 执行查询
     * 3. 返回结果
     */
    public TpRoleVO getRoleDetail(String roleId, String operatorId, String operatorRoleIds) {
        // 构建查询条件
        RoleDetailQuery query = roleQueryAssembler.toDetailQuery(roleId, operatorId, operatorRoleIds);
        
        // 执行查询
        return roleDetailQueryHandler.handle(query);
    }
    
    /**
     * 权限树查询业务流程
     * 1. 构建查询条件
     * 2. 执行查询
     * 3. 返回结果
     */
    public List<TreeNode> getPermissionTree(String roleId, String operatorId, String operatorRoleIds) {
        // 构建查询条件
        PermissionTreeQuery query = roleQueryAssembler.toPermissionTreeQuery(roleId, operatorId, operatorRoleIds);
        
        // 执行查询
        return permissionTreeQueryHandler.handle(query);
    }
    
    /**
     * 根据角色ID查询关联人员业务流程
     * 这是一个复杂的查询，需要跨模块协作
     */
    public List<TpPersonRoleVO> getRolePersonList(String roleId) {
        // 这里需要调用用户模块的服务
        // 暂时返回空列表，实际实现需要在基础设施层完成
        return Arrays.asList();
    }
    
    /**
     * 获取角色列表（不分页）
     */
    public List<TpRoleVO> getRoleList(com.jiuxi.admin.core.bean.query.TpRoleQuery legacyQuery, String applicationId) {
        // 构建分页查询（设置大页面）
        RolePageQuery query = roleQueryAssembler.toPageQuery(legacyQuery, null, null, applicationId);
        query.setCurrent(1);
        query.setSize(1000); // 获取前1000条
        
        // 执行查询
        IPage<TpRoleVO> page = rolePageQueryHandler.handle(query);
        
        return page.getRecords();
    }
}