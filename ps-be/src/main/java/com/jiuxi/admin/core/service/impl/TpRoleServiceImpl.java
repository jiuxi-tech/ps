package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpRole;
import com.jiuxi.admin.core.bean.entity.TpRoleMenu;
import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.admin.core.mapper.TpMenuMapper;
import com.jiuxi.admin.core.mapper.TpPersonRoleMapper;
import com.jiuxi.admin.core.mapper.TpRoleMapper;
import com.jiuxi.admin.core.mapper.TpRoleMenuMapper;
import com.jiuxi.admin.core.service.TpRoleService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.CommonTreeUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import com.jiuxi.core.core.event.TpRoleAuthorizationEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: TpRoleServiceImpl
 * @Description: 角色表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpRoleService")
public class TpRoleServiceImpl implements TpRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpRoleServiceImpl.class);

    @Autowired
    private TpRoleMapper tpRoleMapper;

    @Autowired
    private TpPersonRoleMapper tpPersonRoleMapper;

    @Autowired
    private TpRoleMenuMapper tpRoleMenuMapper;

    @Autowired
    private TpMenuMapper tpMenuMapper;

    /**
     * 上下文对象
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 授权查询，查询自己拥有的角色和自己这个角色所创建的角色
     *
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.jiuxi.admin.core.bean.vo.TpRoleVO>
     * @author pand
     * @date 2021-06-09 21:12
     */
    @Override
    public LinkedHashSet<TpRoleVO> roleAuthList(TpRoleAuthQuery query, String pid, String deptId, String rids, String jwtaid) {

        if (!StrUtil.equals(TpConstant.ADMIN.PERSONID, pid) && StrUtil.isNotBlank(rids)) {
            // 分管：查询自己的角色创建的角色并且本单位下的,和自己拥有的角色
            String[] roleIds = StringUtils.split(rids, ",");
            query.setCreateRoles(roleIds);
            query.setAscnId(jwtaid);
            query.setDeptId(deptId);
            query.setPersonId(pid);
        }

        LinkedHashSet<TpRoleVO> list = tpRoleMapper.roleAuthList(query);
        LinkedHashSet<TpRoleVO> listByCreateRole = tpRoleMapper.roleAuthListByCreateRole(query);
        list.addAll(listByCreateRole);

        return list;
    }

    /**
     * 分页查询数据
     * 超管：查询所有角色
     * 分管：查询自己拥有这个角色创建
     *
     * @param query:
     * @param pid:   当前操作人personId
     * @param rids:  当前操作人所拥有的角色
     * @return com.jiuxi.mybatis.util.PageUtils
     * @author pand
     * @date 2020-11-25 14:48
     */
    @Override
    public IPage<TpRoleVO> queryPage(TpRoleQuery query, String pid, String rids, String jwtaid) {
        try {
            if (!StrUtil.equals(TpConstant.ADMIN.PERSONID, pid)) {
                // 分管：查询自己的角色创建的角色并且本单位下的
                String[] roleIds = StringUtils.split(rids, ",");
                query.setCreateRoles(roleIds);
                query.setAscnId(jwtaid);
            }
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
            if (pageSize == -1) {
                // 不分页
                Page<TpRoleVO> page = new Page<>(pageNum, pageSize);
                IPage<TpRoleVO> iPage = tpRoleMapper.getPage(page, query);
                return iPage;
            } else {
                // 分页
                Page<TpRoleVO> page = new Page<>(pageNum, pageSize);
                IPage<TpRoleVO> iPage = tpRoleMapper.getPage(page, query);
                return iPage;
            }
        } catch (Exception e) {
            LOGGER.error("角色列表信息失败！query:{}, 错误: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "角色列表信息失败！");
        }
    }

    /**
     * 超级管理员：查询全部角色
     * 其他：根据所在单位查询单位的角色
     *
     * @param query 查询条件
     * @return
     */
    @Override
    public List<TpRoleVO> getList(TpRoleQuery query, String jwtaid) {

        List<TpRoleVO> list;
        if (!StrUtil.equals(TpConstant.ADMIN.PERSONID, jwtaid)) {
            // admin 账号查询全部
            list = tpRoleMapper.getList(query);
        } else {
            // 非admin账号，根据ascnId查询
            query.setAscnId(jwtaid);
            list = tpRoleMapper.getList(query);
        }
        return list;
    }

    /**
     * 新增角色信息，政府人员创建的category=0，企业创建的category=1，其他情况暂不考虑
     *
     * @param vo:       角色信息
     * @param pid:      当前登陆人id
     * @param aid:      当前登陆人单位id
     * @param category: 0政府 1企业 2其他
     * @author pand
     * @date 2020-11-23 15:37
     */
    @Override
    public int add(TpRoleVO vo, String pid, String aid, int category) {
        try {
            TpRole bean = new TpRole();
            String roleId = SnowflakeIdUtil.nextIdStr();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            bean.setRoleId(roleId);
            bean.setAscnId(aid);

            String now = CommonDateUtil.now();
            bean.setCreator(pid);
            bean.setCreateTime(now);
            // 修改人
            bean.setUpdator(pid);
            // 修改时间
            bean.setUpdateTime(now);

            switch (category) {
                case TpConstant.Category.ORG:
                    bean.setCategory(TpConstant.Category.ORG);
                    break;
                case TpConstant.Category.ENT:
                    bean.setRoleType(1);
                    bean.setCategory(TpConstant.Category.ENT);
                    break;
                default:
                    break;
            }

            // 新增角色
            int count = tpRoleMapper.save(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("新增角色信息失败！vo:{}, 错误: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增角色信息失败！");
        }
    }


    /**
     * 修改角色信息
     * 超级管理员可以修改所有角色，分级管理员，只能修改自己创建的角色
     *
     * @param vo:  角色信息
     * @param pid: 当前登陆人id
     * @author pand
     * @date 2020-11-23 15:37
     */
    @Override
    public int update(TpRoleVO vo, String pid) {
        if (!StrUtil.equals(TpConstant.ADMIN.PERSONID, pid) && !StrUtil.equals(vo.getCreator(), pid)) {
            // 分级管理员，只能修改自己创建的角色
            throw new TopinfoRuntimeException(-1, "无权限修改！只能修改自己创建的角色");
        }
        try {
            TpRole bean = new TpRole();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            bean.setUpdator(pid);
            bean.setUpdateTime(CommonDateUtil.now());

            // 修改角色
            int count = tpRoleMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改角色信息失败！vo:{}, 错误: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改角色信息失败！");
        }
    }

    /**
     * 查看角色信息
     *
     * @param roleId: 角色id
     * @return com.jiuxi.admin.core.bean.vo.TpRoleVO
     * @author pand
     * @date 2020-11-25 17:13
     */
    @Override
    public TpRoleVO view(String roleId) {
        try {
            TpRoleVO vo = tpRoleMapper.view(roleId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看角色信息失败！roleId:{}, 错误: {}", roleId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看角色信息失败！");
        }
    }


    /**
     * 删除前查询是否有人员绑定角色
     *
     * @param roleId:
     * @return int
     * @author pand
     * @date 2020-11-24 20:15
     */
    @Override
    public List<TpPersonRoleVO> selectByRoleId(String roleId) {
        try {
            // 1. 查询角色是否有绑定用户
            List<TpPersonRoleVO> personRoleVOS = tpPersonRoleMapper.selectByRoleId(roleId);

            return personRoleVOS;
        } catch (Exception e) {
            LOGGER.error("删除角色信息失败！roleId:{}, 错误: {}", roleId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除角色信息失败！");
        }
    }

    /**
     * 删除角色
     * 1、超管可以删除所有角色，分级管理员只能删除自己创建的角色。
     * 2、删除角色需要判断角色是否绑定用户，如果有用户绑定，需要先解除绑定才能进行删除。
     *
     * @param roleId:
     * @param creator:
     * @param pid:     当前登陆人id
     * @return int
     * @author pand
     * @date 2020-11-24 20:15
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int delete(String roleId, String creator, String pid) {
        if (!StrUtil.equals(TpConstant.ADMIN.PERSONID, pid) && !StrUtil.equals(creator, pid)) {
            // 分级管理员，只能删除自己创建的角色
            throw new TopinfoRuntimeException(-1, "无权限删除！");
        }

        try {
            LOGGER.warn("操作人：{}, 删除了：{} 角色.", pid, roleId);
            // 1、删除关联关系，tp_person_role
            tpPersonRoleMapper.deleteByRoleId(roleId);
            // 2、删除角色信息
            TpRole bean = new TpRole();
            bean.setRoleId(roleId);
            bean.setUpdateTime(CommonDateUtil.now());

            int count = tpRoleMapper.delete(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("删除角色信息失败！roleId:{}, 错误: {}", roleId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除角色信息失败！");
        }
    }

    /**
     * 授权菜单，同步获取数据，故一次性读取所有的菜单
     * 超级管理员，查询所有菜单树。默认展开
     * 分级管理员查询自己拥有的菜单，默认展开
     *
     * @param rids: jwt解析出来的roleIds
     * @param pid:  jwt解析出来的personId
     * @return com.jiuxi.common.bean.TreeNode
     * @author pand
     * @date 2020-11-25 19:43
     */
    @Override
    public List<TreeNode> authTree(String roleId, String rids, String pid) {

        // 如果是超级管理员，则查询所有菜单树; 否则查询自己拥有的菜单。
        LinkedHashSet<TreeNode> set;
        if (TpConstant.ADMIN.PERSONID.equals(pid)) {
            // 查询出所有的顶级菜单，然后递归组装成树结构
            set = tpMenuMapper.selectByCreator(null, "1");
        } else {
            // 1、根据pid查询分级管理员自己拥有角色，然后查询角色所拥有的菜单，然后递归组装成树结构
            if (StrUtil.isBlank(rids)) {
                LOGGER.error("pid = {} 未分配角色", pid);
                throw new TopinfoRuntimeException(-1, "该用户未分配角色！");
            }
            // 2、根据分级管理员拥有的角色查询所拥有的菜单
            List<String> roleIds = StrUtil.split(rids, ",");
            // 3、组装成菜单树
            set = tpMenuMapper.authSelectByRoldIds(roleIds);

            // 2、根据jwtpid查询分级管理员自己创建的菜单，然后递归组装成树结构
            LinkedHashSet<TreeNode> creatorBySelfSet = tpMenuMapper.selectByCreator(pid, "1");
            set.addAll(creatorBySelfSet);
        }

        // 根据roleId查询该角色已经拥有的权限
        LinkedHashSet<TreeNode> hasRoles = tpMenuMapper.authSelectByRoldIds(Arrays.asList(roleId));
        // 添加角色选中状态
        this.setChecked(set, hasRoles);

        // 组装数据，返回树结构
        List<TreeNode> result = new ArrayList<>(set);
        List<TreeNode> treeNodes = CommonTreeUtil.buildTree(result);
        // 过滤掉非顶级节点的菜单（存在非顶级节点的原因是它的附件设置为未启用了）
        treeNodes = treeNodes.stream().filter((treeNode)-> treeNode.getPid().equals(TpConstant.NODE.TOP_NODE_PID)).collect(Collectors.toList());
        return treeNodes;
    }

    /**
     * 添加角色选中状态
     *
     * @param set      所有角色集合
     * @param hasRoles 拥有的角色集合
     * @return void
     * @author 杨占锐
     * @date 2024/6/4 20:06
     */
    private void setChecked(LinkedHashSet<TreeNode> set, LinkedHashSet<TreeNode> hasRoles) {
        Map<String, TreeNode> map = new HashMap<>();
        for (TreeNode node: set) {
            map.put(node.getId(), node);
        }

        for (TreeNode node: hasRoles) {
            TreeNode treeNode = map.get(node.getId());
            if (treeNode != null){
                treeNode.setChecked(true);
            }
        }
    }

    /**
     * 角色授权
     *
     * @param roleId:  角色id
     * @param menuIds: 菜单id
     * @return void
     * @author pand
     * @date 2020-11-25 19:48
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int roleMenus(String roleId, String menuIds) {
        try {

            // 发布 角色授权 事件， 用于清除 角色菜单的缓存
            applicationContext.publishEvent(new TpRoleAuthorizationEvent("角色授权事件"));

            // 根据roleId先将原来的权限删除掉
            tpRoleMenuMapper.delete(roleId, "");

            String[] menuIdArr = StringUtils.split(menuIds, ",");
            // 去除重复的菜单ID，避免主键重复错误
            Set<String> uniqueMenuIds = new LinkedHashSet<>(Arrays.asList(menuIdArr));
            int len = uniqueMenuIds.size();
            
            TpRoleMenu bean = new TpRoleMenu();
            bean.setRoleId(roleId);
            // 设置权限
            if (len > 0) {
                for (String menuId : uniqueMenuIds) {
                    // 跳过空字符串或null值
                    if (StrUtil.isNotBlank(menuId)) {
                        bean.setMenuId(menuId);
                        tpRoleMenuMapper.save(bean);
                    }
                }
            }

            return len;
        } catch (Exception e) {
            LOGGER.error("角色授权失败！roleId:{}, menuIds:{}, 错误: {}", roleId, menuIds, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "角色授权失败！");
        }
    }
}
