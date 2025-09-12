package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.admin.bean.MenuTreeNode;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpMenu;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;
import com.jiuxi.admin.core.enums.MenuTypeEnum;
import com.jiuxi.admin.core.mapper.TpMenuMapper;
import com.jiuxi.admin.core.service.TpMenuService;
import com.jiuxi.admin.core.service.TpMenuHistoryService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.CommonTreeUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName: TpMenuServiceImpl
 * @Description: 菜单表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpMenuService")
public class TpMenuServiceImpl implements TpMenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpMenuServiceImpl.class);

    @Autowired
    private TpMenuMapper tpMenuMapper;

    @Autowired
    private TpMenuHistoryService tpMenuHistoryService;

    /**
     * 菜单树
     * 只有超级管理员具有操作菜单的权限，查询所有菜单树。默认展开
     * 进入该功能，menuId传空，获取全部菜单树。menuId不为空，查询下级菜单树。
     *
     * @param menuId:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2020-11-26 14:12
     */
    @Override
    public List<TreeNode> tree(String menuId) {

        // 超管查询所有菜单
        List<TreeNode> set = tpMenuMapper.listAll(menuId);

        return CommonTreeUtil.buildTree(set);

    }


    /**
     * 同步查询下两级，通过第二级判断下一级是否有叶子结点
     *
     * @param menuId:
     * @param jwtpid:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2020-12-01 17:48
     */
    @Override
    public List<TreeNode> asyncTree(String menuId, String jwtpid) {
        List<TreeNode> list;
        if (TpConstant.ADMIN.PERSONID.equals(jwtpid)) {
            // 根据父id查询子节点
            list = tpMenuMapper.selectChildren(menuId);
        } else {
            // 根据父id和jwtpid查询分级管理员拥有的菜单
            list = tpMenuMapper.selectChildren(menuId);
        }
        return list;
    }

    /**
     * 根据人员查询用户拥有的菜单
     *
     * @param pid:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2020-12-07 16:58
     */
    @Override
    public List<MenuTreeNode> mainTree(String pid, String rids) {
        Set<MenuTreeNode> set;
        if (StrUtil.equals(pid, TpConstant.ADMIN.PERSONID)) {
            // 1、查询超级管理员的
            set = tpMenuMapper.selectAll(1);
        } else {
            // 1、根据用户查询用户拥有的角色
            if (StrUtil.isBlank(rids)) {
                LOGGER.error("pid = {} 未分配角色", pid);
                throw new TopinfoRuntimeException(-1, "该用户未分配角色！");
            }
            // 2、根据角色查询角色拥有的菜单
            List<String> roleIds = StrUtil.split(rids, ",");
            // 3、组装成菜单树，admin组件只查询pc端的菜单
            set = tpMenuMapper.selectByRoldIds(1, roleIds);
        }

        // 解码路径
        this.decodePath(set);

        // 按pid分组
        List<MenuTreeNode> result = new ArrayList<>(set);
        List<MenuTreeNode> treeNodes = CommonTreeUtil.buildTree(result);
        // 过滤掉非顶级节点的菜单（存在非顶级节点的原因是它的父级设置为未启用了）
        treeNodes = treeNodes.stream().filter((treeNode)-> treeNode.getPid().equals(TpConstant.NODE.TOP_NODE_PID)).collect(Collectors.toList());
        return treeNodes;
    }

    private void decodePath(Set<MenuTreeNode> set) {
        set.forEach(tree -> {
            try {
                // 将路径path解码
                String path = tree.getPath();
                if (StrUtil.isNotBlank(path)) {
                    tree.setPath(URLDecoder.decode(path, "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 根据人员查询用户拥有的菜单
     *
     * @param pid:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2020-12-07 16:58
     */
    @Override
    public Set<MenuTreeNode> mainAppTree(String pid, String rids) {
        Set<MenuTreeNode> set;
        if (StrUtil.equals(pid, TpConstant.ADMIN.PERSONID)) {
            // 1、查询超级管理员的
            set = tpMenuMapper.selectAll(2);
        } else {
            // 1、根据用户查询用户拥有的角色
            if (StrUtil.isBlank(rids)) {
                LOGGER.error("pid = {} 未分配角色", pid);
                throw new TopinfoRuntimeException(-1, "该用户未分配角色！");
            }
            // 2、根据角色查询角色拥有的菜单
            List<String> roleIds = StrUtil.split(rids, ",");
            // 3、将code格式化成逗号隔开的字符串，app组件只查询app端的菜单
            set = tpMenuMapper.selectByRoldIds(2, roleIds);
        }

        return set;
    }

    /**
     * 递归组装按钮树结构
     *
     * @param set:
     * @param rootNode:
     * @return void
     * @author pand
     * @date 2020-12-01 17:29
     */
    private void convertMenuTreeNode(Set<TpMenuVO> set, MenuTreeNode rootNode) {
        try {
            for (TpMenuVO vo : set) {
                if (StrUtil.equals(vo.getMenuPid(), rootNode.getId())) {
                    MenuTreeNode treeNode = new MenuTreeNode();
                    treeNode.setId(vo.getMenuId());
                    treeNode.setValue(vo.getMenuId());
                    treeNode.setText(vo.getMenuName());
                    treeNode.setLabel(vo.getMenuName());
                    treeNode.setPid(vo.getMenuPid());
                    treeNode.setCode(vo.getMenuCode());
                    treeNode.setIcon(vo.getMenuIcon());
                    treeNode.setType(vo.getMenuType());
                    treeNode.setPath(URLDecoder.decode(vo.getMenuUri(), "UTF-8"));

                    rootNode.addChildren(treeNode);
                    convertMenuTreeNode(set, treeNode);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增菜单信息
     * 1。判断新增的节点是否是根节点，如果不是根节点，需要将新增节点的父节点变更为非叶子结点。
     * 2。新增节点是根节点，设置menuPid为-1
     *
     * @param vo: 菜单信息
     * @return com.jiuxi.admin.core.bean.vo.TpMenuVO
     * @author pand
     * @date 2020-11-26 14:48
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public TpMenuVO save(TpMenuVO vo, String pid) {

        // 父级节点为空则添加顶级节点id
        if (StrUtil.isBlank(vo.getMenuTreePid())) {
            vo.setMenuTreePid(TpConstant.NODE.TOP_NODE_PID);
        }

        try {
            // 添加菜单父级id
            vo.setMenuPid(this.recursionGetMenuPid(vo.getMenuTreePid()));
        } catch (Exception e){
            LOGGER.error("新增菜单失败, 递归查找菜单父级id时发送异常！vo：{}, 错误：{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增菜单失败！");
        }

        try {

            // 校验
            this.validate(vo);

            TpMenu bean = new TpMenu();
            vo.setMenuId(SnowflakeIdUtil.nextIdStr());
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            String now = CommonDateUtil.now();
            bean.setCreator(pid);
            bean.setCreateTime(now);
            // 修改人
            bean.setUpdator(pid);
            // 修改时间
            bean.setUpdateTime(now);
            tpMenuMapper.save(bean);
            
            // 记录新增历史
            try {
                tpMenuHistoryService.recordAddHistory(vo, pid);
            } catch (Exception e) {
                LOGGER.warn("记录菜单新增历史失败，但不影响主流程，菜单ID：{}, 错误：{}", vo.getMenuId(), e.getMessage());
            }
            
            return vo;
        } catch (TopinfoRuntimeException e) {
            LOGGER.error("新增菜单失败, message: {}, vo：{}, 错误：{}", e.getMessage(), JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("新增菜单失败！vo：{}, 错误：{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增菜单失败！");
        }
    }

    /**
     * 校验信息
     * <pre>
     *     1. 菜单归属必须相同，pc下面只能挂pc, app下面只能挂app
     *     2. 菜单类型中，“菜单”下面不能挂“大屏菜单”， “大屏菜单”下面不能挂“菜单”
     * </pre>
     * @param bean
     * @return void
     * @author 杨占锐
     * @date 2024/5/30 8:55
     */
    private void validate(TpMenuVO bean) {
        String menuPid = bean.getMenuPid();
        // 如果父级节点是-1，说明当前是顶级节点，无需限制
        if (TpConstant.NODE.TOP_NODE_PID.equals(menuPid)){
            return;
        }
        TpMenuVO parent = tpMenuMapper.view(menuPid);

        // 菜单归属不同
        if (!bean.getMenuSource().equals(parent.getMenuSource())) {
            throw new TopinfoRuntimeException(-1, "菜单归属必须与父级菜单相同");
        }

        // 菜单类型
        String parentMenuType = parent.getMenuType();
        String menuType = bean.getMenuType();
        if (MenuTypeEnum.SYS1901.getCode().equals(parentMenuType) && MenuTypeEnum.SYS1906.getCode().equals(menuType)){
            throw new TopinfoRuntimeException(-1, "父级菜单类型是“菜单”, 当前菜单不能选择“大屏菜单”");
        }
        if (MenuTypeEnum.SYS1906.getCode().equals(parentMenuType) && MenuTypeEnum.SYS1901.getCode().equals(menuType)){
            throw new TopinfoRuntimeException(-1, "父级菜单类型是“大屏菜单”, 当前菜单不能选择“菜单”");
        }
    }

    /**
     * 递归查询menuPid
     *
     * @param menuTreePid
     * @return void
     * @author 杨占锐
     * @date 2024/5/16 13:59
     */
    private String recursionGetMenuPid(String menuTreePid) {

        // 父级已经是顶级节点，则返回 -1
        if (TpConstant.NODE.TOP_NODE_PID.equals(menuTreePid)){
            return TpConstant.NODE.TOP_NODE_PID;
        }

        // 查询父级信息
        TpMenuVO view = this.view(menuTreePid);
        if (MenuTypeEnum.SYS1907.getCode().equals(view.getMenuType())){
            return recursionGetMenuPid(view.getMenuTreePid());
        } else {
            return view.getMenuId();
        }
    }

    /**
     * 更新菜单信息
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2020-11-24 20:13
     */
    @Override
    public TpMenuVO update(TpMenuVO vo, String pid) {

        try {
            // 校验
            this.validateUpdate(vo);
            
            // 获取修改前的数据
            TpMenuVO beforeMenuVO = this.view(vo.getMenuId());

            TpMenu bean = new TpMenu();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            bean.setUpdator(pid);
            bean.setUpdateTime(CommonDateUtil.now());

            tpMenuMapper.update(bean);
            
            // 记录修改历史
            try {
                tpMenuHistoryService.recordUpdateHistory(beforeMenuVO, vo, pid);
            } catch (Exception e) {
                LOGGER.warn("记录菜单修改历史失败，但不影响主流程，菜单ID：{}, 错误：{}", vo.getMenuId(), e.getMessage());
            }
            
            return vo;
        } catch (TopinfoRuntimeException e) {
            LOGGER.error("菜单信息修改失败, message: {}, vo：{}, 错误：{}", e.getMessage(), JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, e.getMessage());
        }  catch (Exception e) {
            LOGGER.error("菜单信息修改失败！{}, vo: {}", ExceptionUtils.getStackTrace(e), JSONObject.toJSONString(vo));
            throw new TopinfoRuntimeException(-1, "菜单修改失败！");
        }
    }

    /**
     * 修改时校验
     *
     * @param vo
     * @return void
     * @author 杨占锐
     * @date 2024/5/30 9:20
     */
    private void validateUpdate(TpMenuVO vo) {
        TpMenuVO view = this.view(vo.getMenuId());
        boolean isTypeNodeNew = MenuTypeEnum.SYS1907.getCode().equals(vo.getMenuType());
        boolean isTypeNodeOld = MenuTypeEnum.SYS1907.getCode().equals(view.getMenuType());
        if (isTypeNodeNew != isTypeNodeOld){
            if (isTypeNodeNew) {
                // 修改为了非分类节点
                throw new RuntimeException("不能修改为分类节点类型");
            } else {
                throw new RuntimeException("不能修节点类型");
            }
        }

        // 校验
        this.validate(vo);
    }


    /**
     * 查看菜单信息
     *
     * @param menuId:
     * @return com.jiuxi.admin.core.bean.vo.TpMenuVO
     * @author pand
     * @date 2020-11-26 14:48
     */
    @Override
    public TpMenuVO view(String menuId) {
        try {
            TpMenuVO vo = tpMenuMapper.view(menuId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看菜单信息失败！menuId:{}, 错误:{}", menuId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看菜单信息失败！");
        }
    }

    /**
     * 逻辑删除
     *
     * @param menuId:
     * @param pid:
     * @return int
     * @author pand
     * @date 2020-11-26 15:22
     */
    @Override
    public int delete(String menuId, String pid) {
        try {
            int countChildren = tpMenuMapper.countChildren(menuId);
            if (countChildren > 0) {
                throw new TopinfoRuntimeException(-1, "请先删除子菜单！");
            }
            
            // 获取删除前的数据
            TpMenuVO beforeMenuVO = this.view(menuId);

            TpMenu bean = new TpMenu();
            bean.setMenuId(menuId);
            bean.setActived(0);
            bean.setUpdator(pid);
            bean.setUpdateTime(CommonDateUtil.now());

            int count = tpMenuMapper.deleteByMenuId(bean);
            
            // 记录删除历史
            try {
                tpMenuHistoryService.recordDeleteHistory(beforeMenuVO, pid);
            } catch (Exception e) {
                LOGGER.warn("记录菜单删除历史失败，但不影响主流程，菜单ID：{}, 错误：{}", menuId, e.getMessage());
            }
            
            return count;
        } catch (Exception e) {
            LOGGER.error("删除角色信息失败！menuId:{}, 错误:{}", menuId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "删除角色信息失败！" : e.getMessage());
        }
    }

}
