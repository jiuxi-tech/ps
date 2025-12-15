package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.constant.enums.OpertionTypeEnum;
import com.jiuxi.admin.core.bean.entity.TpDeptBasicinfo;
import com.jiuxi.admin.core.bean.entity.TpDeptExinfo;
import com.jiuxi.admin.core.bean.entity.TpPersonDept;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpDeptExinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonDeptVO;
import com.jiuxi.admin.core.event.TpDeptBasicinfoEvent;
import com.jiuxi.admin.core.event.TpDeptBasicinfoNewEvent;
import com.jiuxi.admin.core.listener.service.TpDeptBasicinfoEventService;
import com.jiuxi.admin.core.listener.service.TpDeptBasicinfoNewEventService;
import com.jiuxi.admin.core.mapper.*;
import com.jiuxi.admin.core.service.TpCityService;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.admin.core.service.OrgTreeChangeHistoryService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.geo.GeoUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.CommonUniqueIndexUtil;
import com.jiuxi.common.util.CommonTreeUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.security.core.holder.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName: TpDeptBasicinfoServiceImpl
 * @Description: 单位/部门/网格/其他基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpDeptBasicinfoService")
public class TpDeptBasicinfoServiceImpl implements TpDeptBasicinfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpDeptBasicinfoServiceImpl.class);

    @Autowired
    private TpDeptBasicinfoMapper tpDeptBasicinfoMapper;

    @Autowired
    private TpDeptExinfoMapper tpDeptExinfoMapper;

    @Autowired
    private TpPersonDeptMapper tpPersonDeptMapper;

    @Autowired
    private TpPersonBasicinfoMapper tpPersonBasicinfoMapper;

    @Autowired
    private TpCityMapper tpCityMapper;

    @Autowired
    private TpCityService tpCityService;


    /**
     * 上下文对象
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private TpDeptBasicinfoEventService tpDeptBasicinfoEventService;


    @Autowired(required = false)
    private TpDeptBasicinfoNewEventService tpDeptBasicinfoNewEventService;

    @Autowired
    private OrgTreeChangeHistoryService orgTreeChangeHistoryService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * 机构树
     *
     * @param deptId
     * @return com.jiuxi.common.bean.TreeNode
     * @author Ypp
     * @date 2020/11/20 11:29
     */
    @Override
    public List<TreeNode> parttimeJobTree(String personId, String deptId, int category) {
        // 1、查询当前登陆人能够查询出的部门，本级及下级
        TpDeptBasicinfoVO rootDept = tpDeptBasicinfoMapper.selectDeptById(deptId);
        List<TreeNode> allList = this.deptList(rootDept, category);

        // 2、查询需要被赋予兼职部门的人已经所在的兼职部门
        List<TpPersonDeptVO> list = tpPersonDeptMapper.select("", personId, 0);

        for (TpPersonDeptVO tpPersonDeptVO : list) {
            if (StrUtil.equals(rootDept.getDeptId(), tpPersonDeptVO.getDeptId())) {
                rootDept.setChecked(true);
            }
            for (TreeNode treeNode : allList) {
                if (StrUtil.equals(treeNode.getId(), tpPersonDeptVO.getDeptId())) {
                    treeNode.setChecked(true);
                    break;
                }
            }
        }
        // 初始化根节点
        TreeNode rootNode = this.initTreeNode(rootDept);
        // 组装树
        rootNode = this.syncTree(rootNode, allList);

        return rootNode.getChildren();
    }

    /**
     * 机构树 政府
     *
     * @param deptId
     * @return com.jiuxi.common.bean.TreeNode
     * @author Ypp
     * @date 2020/11/20 11:29
     */
    @Override
    public List<TreeNode> tree(String deptId, String jwtdid, int sync, int category) {
        // 先查询本级部门信息
        TpDeptBasicinfoVO rootDept;
        if (StrUtil.isBlank(deptId)) {
            // 先查询本级部门信息
            deptId = jwtdid;
            rootDept = tpDeptBasicinfoMapper.selectDeptById(jwtdid);
        } else {
            rootDept = tpDeptBasicinfoMapper.selectDeptById(deptId);
            if (null == rootDept) {
                throw new TopinfoRuntimeException(-1, "查询部门不存在");
            }
            TpDeptBasicinfoVO jwtdeptBasicinfoVO = tpDeptBasicinfoMapper.selectDeptById(jwtdid);
            if (null == jwtdeptBasicinfoVO) {
                throw new TopinfoRuntimeException(-1, "查询不到当前登陆人所在部门");
            }
            SessionVO sessionVO = SessionHolder.get();
            if (!rootDept.getDeptLevelcode().startsWith(jwtdeptBasicinfoVO.getDeptLevelcode()) && !TpConstant.ADMIN.PERSONID.equals(sessionVO.getPersonId())) {
                throw new TopinfoRuntimeException(-1, "不能查询非本级及下级部门之外的数据。");
            }
        }

        // 初始化根节点
        TreeNode rootNode = this.initTreeNode(rootDept);
        // 菜单树d
        if (1 == sync) {
            // 同步
            // 根据部门查询本级及下级部门
            List<TreeNode> deptList = this.deptList(rootDept, category);
            rootNode = this.syncTree(rootNode, deptList);
            if (StrUtil.equals(deptId, TpConstant.NODE.TOP_NODE_ID)) {
                // 根节点不返回。
                return rootNode.getChildren();
            } else {
                // 其他账号需要返回账号所在部门。
                List<TreeNode> list = new ArrayList<>();
                list.add(rootNode);

                return list;
            }
        } else {
            // 异步
            List<TreeNode> treeNodeList = tpDeptBasicinfoMapper.selectChildren(deptId, category);
            if (StrUtil.equals(deptId, TpConstant.NODE.TOP_NODE_ID)) {
                return treeNodeList;
            } else {
                List<TreeNode> list = new ArrayList<>();
                rootNode.addChildrens(treeNodeList);
                list.add(rootNode);
                return list;
            }
        }
    }

    /**
     * 查询下一级单位或者部门，如果parentId为空，默认查询当前登陆人所在单位返回
     * deptType=SYS0501，查下级单位
     * deptType=SYS0502，查下级部门
     * deptType=null，查所有下级
     *
     * @param parentId       上级id
     * @param deptType       查询下级的类型
     * @param filterCommAscn 过滤委员会  1：过滤
     * @return com.jiuxi.common.bean.TreeNode
     * @author pand
     * @date 2023/11/20 11:29
     */
    @Override
    public List<TreeNode> getChildren(String parentId, String deptType, String filterCommAscn, Integer category) {
        SessionVO sessionVO = SessionHolder.get();
        String jwtaid = sessionVO.getAscnId();
        List<TreeNode> list = new ArrayList<>();
        if (StrUtil.isBlank(parentId)) {
            TpDeptBasicinfoVO currentAscn = tpDeptBasicinfoMapper.selectDeptById(jwtaid);
            TreeNode treeNode = this.initTreeNode(currentAscn);
            list.add(treeNode);
        } else {
            list = tpDeptBasicinfoMapper.getChildren(parentId, deptType, filterCommAscn, category);
        }
        return list;
    }

    /**
     * 当前登陆人所在单位的行政区划树 本级及下级树
     *
     * @param jwtaid: 当前登陆人所在单位id
     * @return com.jiuxi.common.bean.TreeNode
     * @author pand
     * @date 2021-03-02 16:05
     */
    @Override
    public TreeNode deptCityTree(String jwtaid, int sync, boolean expand) {
        List<TreeNode> treeNode;

        if (StrUtil.equals(jwtaid, TpConstant.ADMIN.ASCNID)) {
            // 超级管理员，查询整棵树
            treeNode = tpCityService.tree("-1", sync, expand, "on");
        } else {
            // 根据单位id查询单位信息
            TpDeptBasicinfoVO tpDeptBasicinfoVO = tpDeptBasicinfoMapper.view(jwtaid);
            // 根据cityCode查询citiId接口
            String cityId = tpCityMapper.selectCityIdByCityCode(tpDeptBasicinfoVO.getCityCode());
            // 查询本级及下级树
            treeNode = tpCityService.tree(cityId, sync, expand, "on");
        }
        // 返回完整的树
        return treeNode.get(0);
    }

    /**
     * 根据层级code查询所有下级部门
     *
     * @param deptBasicinfoVO:
     * @param category:
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO>
     * @author pand
     * @date 2021-01-06 14:48
     */
    private List<TreeNode> deptList(TpDeptBasicinfoVO deptBasicinfoVO, int category) {
        String levelCode = deptBasicinfoVO.getDeptLevelcode();
        // 根据部门层级查询所有子级部门
        List<TreeNode> deptList = tpDeptBasicinfoMapper.selectDeptListByLevelCode(levelCode, category);
        return deptList;
    }

    /**
     * 组装树结构
     *
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author Ypp
     * @date 2020/11/20 14:44
     */
    private TreeNode syncTree(TreeNode rootNode, List<TreeNode> deptList) {

        // 按pid分组
        Map<String, List<TreeNode>> map = deptList.stream().collect(Collectors.groupingBy(TreeNode::getPid, Collectors.toList()));

        // 组装树结构，避免使用递归
        deptList.forEach(treeNode -> {
            List<TreeNode> children = map.get(treeNode.getValue());
            if (null != children) {
                treeNode.setLeaf(false);
                treeNode.addChildrens(children);
            } else {
                treeNode.setLeaf(true);
            }
            if (StrUtil.equals(treeNode.getPid(), rootNode.getId())) {
                rootNode.addChildren(treeNode);
            }
        });
        return rootNode;
    }

    /**
     * 根据部门信息实例化部门treeNode
     *
     * @param vo:
     * @return com.jiuxi.common.bean.TreeNode
     * @author pand
     * @date 2021-01-06 14:52
     */
    private TreeNode initTreeNode(TpDeptBasicinfoVO vo) {
        TreeNode treeNode = new TreeNode();
        // 部门id
        treeNode.setId(vo.getDeptId());
        treeNode.setValue(vo.getDeptId());
        // 部门Pid
        treeNode.setPid(vo.getPdeptId());
        // 单位名称
        treeNode.setText(vo.getDeptFullName());
        treeNode.setLabel(vo.getDeptFullName());
        // 默认展开
        treeNode.setExpand(null == vo.getExpand() ? false : vo.getExpand());
        // 是否选中
        treeNode.setChecked(null == vo.getChecked() ? false : vo.getChecked());
        // 单位id
        treeNode.setExtend01(vo.getAscnId());
        return treeNode;
    }

    /**
     * <pre>
     * 新增部门节点
     * 添加根节点：
     * pdeptId为空，直接设置pdeptId为-1。
     * ascnId：所属单位id，获取雪花id
     * category：机构管理菜单进入，默认为政府
     * 添加子节点：
     * pdeptId为选中节点的id。
     * ascnId：所属单位id，为选中节点的单位id
     * category：机构管理菜单进入，默认为政府
     * <p>
     * 部门管理:
     * 添加根节点： (没有顶级节点按钮)
     * 添加子节点：
     * pdeptId为选中节点的id。
     * ascnId：所属单位id，为选中节点的单位id
     * category：部门管理菜单进入，默认为企业
     * </pre>
     *
     * @param vo:       部门信息
     * @param pid:      当前登陆人id
     * @param category: 0政府 1企业 2其他
     * @author pand
     * @date 2020-11-23 15:37
     */
    @Override
    public TpDeptBasicinfoVO add(TpDeptBasicinfoVO vo, String pid, int category) {
        try {
            TpDeptBasicinfo bean = new TpDeptBasicinfo();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            if (StrUtil.isBlank(vo.getDeptId())) {
                bean.setDeptId(SnowflakeIdUtil.nextIdStr());
            }
            bean.setCreator(pid);
            bean.setEnabled(Optional.ofNullable(vo.getEnabled()).orElse(TpConstant.YES));
            bean.setActived(TpConstant.YES);

            // 设置类型，区分是政府还是企业，接收前端传过来是因为可能前端需要自定义下类型。
            bean.setCategory(category);

            // root节点下只能挂单位，如果root节点下新增部门类型，强制改为单位类型，
            if (StrUtil.equals(vo.getPdeptId(), TpConstant.ADMIN.ASCNID)) {
                bean.setDeptType(TpConstant.DEPT_TYPE);
            }
            // 维护单位id
            if (StrUtil.equals(bean.getDeptType(), TpConstant.DEPT_TYPE)) {
                // ascnId的维护，单位，填充单位的id
                bean.setAscnId(bean.getDeptId());
            } else {
                // ascnId的维护，部门，网格，其他，填充父类的ascnId，前端传过来
                bean.setAscnId(bean.getAscnId());
            }
            // 添加父级id
            this.setPdeptId(bean);
            // 维护层级code
            String deptLevelCode = this.levelCode(bean.getAscnId(), bean.getPdeptId(), bean.getCategory());
            bean.setDeptLevelcode(deptLevelCode);

            String now = CommonDateUtil.now();
            bean.setCreateTime(now);
            // 修改人
            bean.setUpdator(pid);
            // 修改时间
            bean.setUpdateTime(now);

            if (StrUtil.isBlank(bean.getAscnId())) {
                throw new TopinfoRuntimeException(-1, "单位id不能为空！");
            }

            tpDeptBasicinfoMapper.save(bean);

            // 发布事件，推送部门基本信息给第三方系统
            if (null != tpDeptBasicinfoEventService) {
                applicationContext.publishEvent(new TpDeptBasicinfoEvent("部门基本信息新增同步监听", tpDeptBasicinfoEventService, bean, OpertionTypeEnum.ADD.getOpertionType()));
            }

            // 发布事件，推送部门基本信息给第三方系统 (新)
            if (null != tpDeptBasicinfoNewEventService) {
                applicationContext.publishEvent(new TpDeptBasicinfoNewEvent("部门基本信息新增同步监听", tpDeptBasicinfoNewEventService, bean, OpertionTypeEnum.ADD.getOpertionType()));
            }

            // 记录组织架构变更历史
            try {
                // 获取变更前后的完整组织树
                List<TreeNode> afterFullTree = this.getFullTree(null, category);
                
                orgTreeChangeHistoryService.recordChangeWithFullTree(
                    "CREATE",
                    Long.valueOf(pid),
                    null, // 新增操作没有旧值
                    JSONObject.toJSONString(bean),
                    null, // 新增操作没有变更前的树
                    JSONObject.toJSONString(afterFullTree) // 变更后的完整树
                );
            } catch (Exception e) {
                LOGGER.warn("记录组织架构变更历史失败: {}", e.getMessage());
            }

            TpDeptBasicinfoVO result = new TpDeptBasicinfoVO();
            BeanUtil.copyProperties(bean, result);
            return result;
        } catch (Exception e) {
            LOGGER.error("新增部门基本信息失败! vo:{}, category:{}, 错误:{}", JSONObject.toJSONString(vo), category, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增部门基本信息失败！");
        }
    }

    /**
     * 添加父级id
     * <pre>
     *     如果pdeptId为空，则将顶级节点id设置为父级id
     * </pre>
     * @param bean  部门信息
     * @return void
     * @author jiuxx
     * @date 2024/5/28 10:10
     */
    private void setPdeptId(TpDeptBasicinfo bean) {
        String pdeptId = bean.getPdeptId();
        if (StrUtil.isBlank(pdeptId)) {
            // pdeptId为空，直接设置pdeptId为 '1111111111111111111'
            bean.setPdeptId(TpConstant.NODE.TOP_NODE_ID);
        }
    }

    /**
     * 生成部门层级code
     *
     * @param ascnId   单位id
     * @param pdeptId  父级部门id
     * @param category 部门类别
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/5/28 13:37
     */
    private String levelCode(String ascnId, String pdeptId, int category) {
        String deptLevecode;
        if (StrUtil.equals(pdeptId, TpConstant.NODE.TOP_NODE_ID)) {
            if (category == TpConstant.Category.ORG) {
                // 查询顶级部门下面的所有部门
                List<TpDeptBasicinfoVO> list = tpDeptBasicinfoMapper.selectDeptListByPId(TpConstant.NODE.TOP_NODE_ID, category);
                if (list.isEmpty()) {
                    deptLevecode = TpConstant.NODE.TOP_NODE_LEVELCODE.concat(TpConstant.NODE.TOP_NODE_START);
                } else {
                    deptLevecode = this.broDeptLevecode(list.get(0));
                }
            } else {
                // 企业/中介 的层级code  = 101 + ascnId
                deptLevecode = TpConstant.NODE.TOP_NODE_LEVELCODE.concat(ascnId);
            }
        } else {
            // pdeptId不为空，非顶级部门添加，需要查询部门层级。
            List<TpDeptBasicinfoVO> list = tpDeptBasicinfoMapper.selectDeptListByPId(pdeptId, category);
            if (list.isEmpty()) {
                // 查询父级部门的层级code
                TpDeptBasicinfoVO deptBasicinfoVO = tpDeptBasicinfoMapper.selectDeptById(pdeptId);
                String pdeptLevelcode = deptBasicinfoVO.getDeptLevelcode();
                deptLevecode = pdeptLevelcode.concat(TpConstant.NODE.TOP_NODE_START);
            } else {
                // 查询兄弟节点最大的层级code,取list按层级倒叙排序取第一个
                deptLevecode = this.broDeptLevecode(list.get(0));
            }
        }
        return deptLevecode;
    }

    /**
     * 兄弟节点维护，当层级过多时，节点维护。通过截取后四位（后三位可能是00开头，如001会变成1）加1再拼接上前面的数据。如果部门数量大于999个，会出问题。因为层级code一级是3位数。
     *
     * @param vo:
     * @return java.lang.String
     * @author pand
     * @date 2021-01-04 13:46
     */
    private String broDeptLevecode(TpDeptBasicinfoVO vo) {
        int splitLength = 3;
        String broLevelcode = vo.getDeptLevelcode();
        int lenth = broLevelcode.length();
        String preLevelcode = broLevelcode.substring(0, lenth - splitLength);
        Long sufLevelcode = Long.parseLong(broLevelcode.substring(lenth - splitLength)) + 1;
        return preLevelcode + StrUtil.padPre(String.valueOf(sufLevelcode), splitLength, '0');
    }

    /**
     * 添加部门扩展信息
     *
     * @param vo: 部门信息
     * @author pand
     * @date 2020-11-23 15:37
     */
    @Override
    @CacheEvict(cacheNames = "platform.{TpDeptBasicinfoService}$[86400]", allEntries = true)
    public TpDeptExinfoVO expAdd(TpDeptExinfoVO vo) {
        try {
            TpDeptExinfo bean = new TpDeptExinfo();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            if (StrUtil.isNotBlank(vo.getLatitude()) && StrUtil.isNotBlank(vo.getLongitude())) {
                bean.setGeoCode(GeoUtils.encodeLatLon(Double.valueOf(vo.getLatitude()), Double.valueOf(vo.getLongitude()), 12));
            }

            int count = tpDeptExinfoMapper.count(vo.getDeptId());
            if (count == 0) {
                tpDeptExinfoMapper.save(bean);
            } else {
                tpDeptExinfoMapper.update(bean);
            }
            return vo;
        } catch (Exception e) {
            LOGGER.error("新增部门扩展信息失败! vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增部门扩展信息失败！");
        }
    }

    /**
     * 内部接口，查看部门/机构基本信息，由于没有校验权限，不建议直接被调用
     * 注意：已移除缓存注解，直接从数据库获取最新数据
     *
     * @param deptId: 部门id
     * @return com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO
     * @author pand
     * @date 2020-11-23 16:55
     */
    @Override
    public TpDeptBasicinfoVO view(String deptId) {

        try {
            // 当前登陆人查询的部门
            TpDeptBasicinfoVO vo = tpDeptBasicinfoMapper.view(deptId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查询部门/机构基本信息失败! deptId:{}, 错误:{}", deptId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询部门/机构基本信息失败！");
        }

    }

    /**
     * 查看部门/机构基本信息
     *
     * @param deptId: 部门id
     * @param jwtaid: 当前登陆人所在单位id
     * @return com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO
     * @author pand
     * @date 2020-11-23 16:55
     */
    @Override
    public TpDeptBasicinfoVO getById(String deptId, String jwtpid, String jwtaid) {
        // 当前登陆人所在部门
        TpDeptBasicinfoVO currentUserDeptvo;
        // 当前登陆人查询的部门
        TpDeptBasicinfoVO vo;
        try {
            currentUserDeptvo = tpDeptBasicinfoMapper.view(jwtaid);
            vo = tpDeptBasicinfoMapper.view(deptId);
        } catch (Exception e) {
            LOGGER.error("查询部门/机构基本信息失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询部门/机构基本信息失败！");
        }
        if (null == currentUserDeptvo) {
            throw new TopinfoRuntimeException(-1, "非法用户查询！");
        }

        if (null == vo) {
            throw new TopinfoRuntimeException(-1, "部门信息不存在！");
        }

        if (!vo.getDeptLevelcode().startsWith(currentUserDeptvo.getDeptLevelcode()) && !TpConstant.ADMIN.PERSONID.equals(jwtpid)) {
            throw new TopinfoRuntimeException(-1, "你没有权限访问，请与管理员联系");
        }

        return vo;
    }

    /**
     * 查看部门/机构扩展信息
     *
     * @param deptId: 部门id
     * @return com.jiuxi.admin.core.bean.vo.TpDeptExinfoVO
     * @author pand
     * @date 2020-11-23 17:13
     */
    @Override
    public TpDeptExinfoVO expView(String deptId) {
        try {
            TpDeptExinfoVO vo = tpDeptExinfoMapper.view(deptId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查询部门/机构扩展信息失败! deptId:{}, 错误:{}", deptId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询部门/机构扩展信息失败！");
        }
    }

    /**
     * 修改部门/机构基本信息
     * <p>
     * 如果机构/部门类型变化了，需要相应的修改该部门下级所有的ascnId单位id
     *
     * @param vo:  部门基本信息
     * @param pid: 操作人员id
     * @return int
     * @author pand
     * @date 2020-11-23 17:45
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    @CacheEvict(cacheNames = {"platform.{TpDeptBasicinfoService}$[86400]"}, allEntries = true)
    public TpDeptBasicinfoVO update(TpDeptBasicinfoVO vo, String pid) {
        try {
            // 在任何更新操作之前，先获取变更前的完整组织树
            List<TreeNode> beforeFullTree = null;
            try {
                beforeFullTree = this.getFullTree(null, TpConstant.Category.ORG);
            } catch (Exception e) {
                LOGGER.warn("获取变更前的完整组织树失败: {}", e.getMessage());
            }
            
            // 当前修改机构的deptId
            String deptId = vo.getDeptId();
            
            // 注意：view方法已移除缓存，无需手动清除view缓存
            // @CacheEvict注解仍会清除getById等其他方法的缓存
            // 先查询修改的数据的部门类型是否发生了变化，如果发生了变化，需要将该部门的下级部门所有的单位id进行修改
            TpDeptBasicinfoVO deptVO = tpDeptBasicinfoMapper.view(deptId);
            // 修改前的单位id
            String oldAscnId = deptVO.getAscnId();
            // 部门类型发生改变
            if (StrUtil.isNotBlank(vo.getDeptType()) && !StrUtil.equals(deptVO.getDeptType(), vo.getDeptType())) {
                // 查询当前机构的下级部门以及下下即部门（包含当前机构，排除单位）
                String newAscnId;
                List<String> list = tpDeptBasicinfoMapper.selectByDeptLevelcodeAndAscnId(deptVO.getDeptLevelcode(), oldAscnId);
                if (StrUtil.equals(vo.getDeptType(), TpConstant.DEPT_TYPE)) {
                    // 部门更改为单位类型，ascnId，填充当前单位自己的deptId，同时，下级所有的机构单位id需要变更为当前单位的id
                    newAscnId = deptId;
                } else {
                    // 企业的顶级单位不允许改为部门类型，部门可以改为单位类型
                    if (StrUtil.equals(deptVO.getPdeptId(), TpConstant.ADMIN.ASCNID)) {
                        throw new TopinfoRuntimeException(-1, "当前单位不允许修改为部门！");
                    }
                    // 单位 更改为 部门/网格/其他 类型，先查询父类ascnId，然后将本级及下级所有机构的单位id更为父类单位的id
                    TpDeptBasicinfoVO parentVO = tpDeptBasicinfoMapper.view(vo.getPdeptId());
                    if (null == parentVO) {
                        throw new TopinfoRuntimeException(500, "修改失败，当前机构的上级机构数据存在问题，请确认！");
                    }

                    newAscnId = parentVO.getAscnId();
                }
                // 修改当前机构的ascnId
                vo.setAscnId(newAscnId);
                // 更新id在list中的部门的单位id
                int count = tpDeptBasicinfoMapper.updateAscnIdByDeptIds(list, newAscnId);
                // LOGGER.info("更新了{}个部门的单位id", count);
                // 部门类型发生变更，更新部门下的人所在单位id
                tpPersonBasicinfoMapper.updateAscnIdByDeptIds(list, newAscnId, oldAscnId);
            }

            TpDeptBasicinfo bean = new TpDeptBasicinfo();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            bean.setUpdator(pid);
            bean.setUpdateTime(CommonDateUtil.now());
            // 更新操作
            tpDeptBasicinfoMapper.update(bean);

            // 在数据库更新后，获取变更后的完整组织树
            List<TreeNode> afterFullTree = null;
            try {
                afterFullTree = this.getFullTree(null, vo.getCategory());
            } catch (Exception e) {
                LOGGER.warn("获取变更后的完整组织树失败: {}", e.getMessage());
            }

            // 发布事件，推送部门基本信息给第三方系统
            if (null != tpDeptBasicinfoEventService) {
                applicationContext.publishEvent(new TpDeptBasicinfoEvent("部门基本信息新增同步监听", tpDeptBasicinfoEventService, bean, OpertionTypeEnum.UPDATE.getOpertionType()));
            }

            // 发布事件，推送部门基本信息给第三方系统 (新)
            if (null != tpDeptBasicinfoNewEventService) {
                applicationContext.publishEvent(new TpDeptBasicinfoNewEvent("部门基本信息新增同步监听", tpDeptBasicinfoNewEventService, bean, OpertionTypeEnum.UPDATE.getOpertionType()));
            }

            // 记录组织架构变更历史
            try {
                String beforeFullTreeJson = beforeFullTree != null ? JSONObject.toJSONString(beforeFullTree) : null;
                String afterFullTreeJson = afterFullTree != null ? JSONObject.toJSONString(afterFullTree) : null;
                
                orgTreeChangeHistoryService.recordChangeWithFullTree(
                    "UPDATE",
                    Long.valueOf(pid),
                    JSONObject.toJSONString(deptVO), // 修改前的数据
                    JSONObject.toJSONString(bean), // 修改后的数据
                    beforeFullTreeJson, // 变更前的完整树
                    afterFullTreeJson // 变更后的完整树
                );
            } catch (Exception e) {
                // 记录变更历史失败不应该影响主业务，只记录警告日志
                LOGGER.warn("记录组织架构变更历史失败，但不影响主业务: {}", e.getMessage(), e);
            }

            TpDeptBasicinfoVO result = new TpDeptBasicinfoVO();
            BeanUtil.copyProperties(bean, result);
            return result;
        } catch (Exception e) {
            LOGGER.error("部门基本信息修改失败! vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "部门基本信息修改失败！");
        }
    }

    /**
     * 根据部门id查询部门下绑定的人员关系，删除部门前，需要判断是否有绑定人员
     *
     * @param deptId:
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpPersonDeptVO>
     * @author pand
     * @date 2021-01-14 15:58
     */
    @Override
    public List<TpPersonDeptVO> selectBindByDeptId(String deptId) {
        try {
            List<TpPersonDeptVO> list = tpPersonDeptMapper.select(deptId, "", null);
            return list;
        } catch (Exception e) {
            LOGGER.error("部门人员绑定关系查询失败! deptId:{}, 错误:{}", deptId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "判断部门下是否有人员挂职失败！");
        }
    }

    /**
     * 删除部门信息
     * 1、删除时，需要判断是否有子节点，如果有子节点，则不能删除
     * 2、删除时，判断部门下是否有人员，如果有人要则不能删除
     * 3、删除为逻辑删除
     *
     * @param id: 部门ids
     * @return int
     * @author pand
     * @date 2020-11-23 19:39
     */
    @Override
    @CacheEvict(cacheNames = "platform.{TpDeptBasicinfoService}$[86400]", allEntries = true)
    public int removeById(String id, String jwtpid) {
        try {
            // 在删除操作之前，先获取变更前的完整组织树
            List<TreeNode> beforeFullTree = null;
            try {
                beforeFullTree = this.getFullTree(null, TpConstant.Category.ORG);
            } catch (Exception e) {
                LOGGER.warn("获取删除前的完整组织树失败: {}", e.getMessage());
            }
            
            // 查询是否有子节点部门
            int count = tpDeptBasicinfoMapper.findChildren(id);
            if (0 != count) {
                throw new TopinfoRuntimeException(-1, "存在子级部门，不能删除！");
            }
            // 部门下是否绑定人员或者兼职人员
            List<TpPersonDept> list = tpDeptBasicinfoMapper.findPersonDeptById(id, "");
            if (!list.isEmpty()) {
                throw new TopinfoRuntimeException(-1, "部门下有挂职人员，不能删除！");
            }
            TpDeptBasicinfoVO vo = tpDeptBasicinfoMapper.view(id);
            if (null == vo) {
                throw new TopinfoRuntimeException(-1, "部门信息不存在！");
            }
            TpDeptBasicinfo bean = new TpDeptBasicinfo();
            bean.setDeptId(id);
            bean.setUpdator(jwtpid);
            bean.setActived(0);
            bean.setCategory(vo.getCategory());
            bean.setDeptLevelcode(vo.getDeptLevelcode());
            bean.setUpdateTime(CommonDateUtil.now());

            // 删除时，唯一索引字段需要添加删除时间
            int updateCount = tpDeptBasicinfoMapper.deleteByDeptId(bean.getDeptId(), bean.getUpdateTime(), bean.getUpdator(), CommonUniqueIndexUtil.addDeleteTime(bean.getDeptLevelcode()));

            // 在删除操作后，获取变更后的完整组织树
            List<TreeNode> afterFullTree = null;
            try {
                afterFullTree = this.getFullTree(null, vo.getCategory());
            } catch (Exception e) {
                LOGGER.warn("获取删除后的完整组织树失败: {}", e.getMessage());
            }

            // 发布事件，推送部门基本信息给第三方系统
            if (null != tpDeptBasicinfoEventService) {
                applicationContext.publishEvent(new TpDeptBasicinfoEvent("部门基本信息新增同步监听", tpDeptBasicinfoEventService, bean, OpertionTypeEnum.DELETE.getOpertionType()));
            }

            // 发布事件，推送部门基本信息给第三方系统 (新)
            if (null != tpDeptBasicinfoNewEventService) {
                applicationContext.publishEvent(new TpDeptBasicinfoNewEvent("部门基本信息新增同步监听", tpDeptBasicinfoNewEventService, bean, OpertionTypeEnum.DELETE.getOpertionType()));
            }

            // 记录组织架构变更历史
            try {
                String beforeFullTreeJson = beforeFullTree != null ? JSONObject.toJSONString(beforeFullTree) : null;
                String afterFullTreeJson = afterFullTree != null ? JSONObject.toJSONString(afterFullTree) : null;
                
                orgTreeChangeHistoryService.recordChangeWithFullTree(
                    "DELETE",
                    Long.valueOf(jwtpid),
                    JSONObject.toJSONString(vo), // 删除前的数据
                    null, // 删除操作没有新值
                    beforeFullTreeJson, // 变更前的完整树
                    afterFullTreeJson // 变更后的完整树
                );
            } catch (Exception e) {
                LOGGER.warn("记录组织架构变更历史失败: {}", e.getMessage());
            }

            return updateCount;
        } catch (Exception e) {
            LOGGER.error("删除部门信息失败! id:{}, 错误:{}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "删除部门信息失败！" : e.getMessage());
        }
    }

    /**
     * @param deptId
     * @return java.lang.String
     * @author pand
     * @date 2021-08-24 15:41
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDeptBasicinfoService}$[86400]", key = "#root.methodName + ':' + #deptId")
    public String getLineCode(String deptId) {
        String lineCode = tpDeptExinfoMapper.getLineCode(deptId);
        return lineCode;
    }

    /**
     * 根据单位id，删除部门
     *
     * @param ascnId 单位id
     * @param jwtpid 操作人
     * @return void
     * @author jiuxx
     * @date 2024/5/30 10:09
     */
    @Override
    public void deleteDeptByAscnId(String ascnId, String jwtpid) {
        String now = CommonDateUtil.now();
        List<TpDeptBasicinfoVO> deptIds = tpDeptBasicinfoMapper.listByEntId(ascnId);
        for (TpDeptBasicinfoVO vo: deptIds) {
            tpDeptBasicinfoMapper.deleteByDeptId(vo.getDeptId(), now, jwtpid, CommonUniqueIndexUtil.addDeleteTime(vo.getDeptLevelcode()));
        }
    }

    /**
     * 获取完整的组织机构树（包含所有节点）
     * 用于记录变更历史时保存完整的组织架构快照
     *
     * @param rootId 根节点ID，如果为空则从顶级节点开始
     * @param category 类别：0政府 1企业 2其他
     * @return 完整的组织机构树结构
     * @author 系统生成
     * @date 2025-01-27
     */
    @Override
    public List<TreeNode> getFullTree(String rootId, int category) {
        try {
            // 如果没有指定根节点，使用顶级节点
            if (StrUtil.isBlank(rootId)) {
                rootId = TpConstant.NODE.TOP_NODE_ID;
            }
            
            // 使用已有的tree方法获取完整树，传入TOP_NODE_ID作为jwtdid以获取所有数据
            List<TreeNode> fullTree = this.tree(rootId, TpConstant.NODE.TOP_NODE_ID, 1, category);
            
            return fullTree;
            
        } catch (Exception e) {
            LOGGER.error("获取完整组织机构树失败! rootId:{}, category:{}, 错误:{}", rootId, category, ExceptionUtils.getStackTrace(e));
            // 如果获取失败，返回空列表而不抛出异常，避免影响主业务
            return new ArrayList<>();
        }
    }
}
