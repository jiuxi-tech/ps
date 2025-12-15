package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jiuxi.admin.core.bean.entity.TpDataPermissions;
import com.jiuxi.admin.core.bean.query.TpTreePermQuery;
import com.jiuxi.admin.core.bean.vo.TpDataPermissionsVO;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.mapper.TpDataPermissionsMapper;
import com.jiuxi.admin.core.service.TpDataPermissionsService;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonTreeUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName: TpDataPermissionsServiceImpl
 * @Description: 数据权限服务
 * @Author jiuxx
 * @Date 2023/11/1 15:39
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpDataPermissionsServiceImpl")
public class TpDataPermissionsServiceImpl implements TpDataPermissionsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpDataPermissionsServiceImpl.class);

    @Autowired
    private TpDataPermissionsMapper tpDataPermissionsMapper;

    @Autowired
    private TpDeptBasicinfoService tpDeptBasicinfoService;

    /**
     * 保存数据权限，删除原有的数据
     *
     * @author jiuxx
     * @date 2023/11/1 16:02
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "platform.{TpDataPermissionsService}$[86400]", allEntries = true)
    public void add(TpDataPermissionsVO permissionsVO) {

        if (null == permissionsVO || StrUtil.isBlank(permissionsVO.getPersonId())){
            return;
        }

        try{

            // 1. 根据人员id，删除所有权限
            tpDataPermissionsMapper.deleteByPersonId(permissionsVO.getPersonId());

            // 2. 保存权限
            if (CollectionUtil.isNotEmpty(permissionsVO.getPermList())){
                List<TpDataPermissions> list = this.createPermissions(permissionsVO);
                tpDataPermissionsMapper.batchAdd(list);
            }

        }catch (Exception e){
            LOGGER.error("保存数据权限失败，permissionsVO：{}, e: {}", JSON.toJSONString(permissionsVO), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "保存数据权限失败");
        }

    }

    /**
     * 创建数据权限集合
     *
     * @param permissionsVO  数据权限集合VO
     * @author jiuxx
     * @date 2023/11/1 15:56
     */
    private List<TpDataPermissions> createPermissions(TpDataPermissionsVO permissionsVO) {
        List<TpDataPermissions> list = new ArrayList<>();
        for (TpDataPermissionsVO vo: permissionsVO.getPermList()){
            TpDataPermissions perm = new TpDataPermissions();
            perm.setPersonId(permissionsVO.getPersonId());
            perm.setDeptId(vo.getDeptId());
            perm.setAscnId(vo.getAscnId());
            perm.setPerId(SnowflakeIdUtil.nextIdStr());
            list.add(perm);
        }
        return list;
    }

    /**
     * 查询所有数据权限
     *
     * @param personId 人员id
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpDataPermissionsVO>
     * @author jiuxx
     * @date 2023/11/2 13:59
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDataPermissionsService}$[86400]", key = "'listPerm:' + #personId")
    public List<TpDataPermissionsVO> listPerm(String personId) {
        return tpDataPermissionsMapper.listPerm(personId);
    }

    /**
     * 查询所有数据权限的部门id
     *
     * @param personId 人员id
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/2 13:53
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDataPermissionsService}$[86400]", key = "'listPermIds:' + #personId")
    public Set<String> listPermIds(String personId) {
        return tpDataPermissionsMapper.listPermIds(personId);
    }

    /**
     * 查询当前登录人的所有权限部门id，以及权限部门id的父级id
     * <pre>
     *     1. 查询当前登录人的部门
     *     2. 判断部门类型，是企业还是政府
     *     3. 企业则执行企业的查询方法
     *     4. 政府则执行政府的查询方法
     * </pre>
     * @param permQuery 当前登录人id、部门类型
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author jiuxx
     * @date 2023/11/1 17:00
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDataPermissionsService}$[86400]", key = "'treePerm:' + #permQuery.personId + ':' + #permQuery.depId + ':' + #permQuery.deptType")
    public List<TreeNode> treePerm(TpTreePermQuery permQuery) {
        String deptType = permQuery.getDeptType();
        if (StrUtil.isNotBlank(deptType)){
            String[] types = deptType.split(",");
            permQuery.setDeptTypes(Arrays.asList(types));
        }

        TpDeptBasicinfoVO deptInfo = tpDeptBasicinfoService.view(permQuery.getDepId());
        Integer category = deptInfo.getCategory();

        if (TpConstant.Category.ENT == category){
            // 企业的顶级单位编码长度 22
            List<TreeNode> result = this.treePerm(permQuery, 22);
            return CommonTreeUtil.buildTree(result);
        }

        if (TpConstant.Category.ORG == category){
            // 政府的顶级单位编码长度 6
            List<TreeNode> result = this.treePerm(permQuery, 6);
            return CommonTreeUtil.buildTree(result);
        }

        // 非企业和政府，返回数据权限的部门
        List<TreeNode> result = tpDataPermissionsMapper.treeSimplePerm(permQuery);
        return CommonTreeUtil.buildTree(result);

    }

    /**
     * 判断是否具有权限
     *
     * @param personId 人员id
     * @param deptId   部门id
     * @return 有权限返回true
     * @author jiuxx
     * @date 2023/11/2 13:09
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDataPermissionsService}$[86400]", key = "'isExistsDataPermissions:' + #personId + ':' + #depId")
    public boolean isExistsDataPermissions(String personId, String deptId) {

        if (StrUtil.isBlank(personId) || StrUtil.isBlank(deptId)){
            throw new TopinfoRuntimeException(-1, "参数为空");
        }

        String id = tpDataPermissionsMapper.isExistsDataPermissions(personId, deptId);
        return StrUtil.isNotBlank(id);
    }


    /**
     * 查询当前人员部门本级及下级所有数据权限
     *
     * @param personId 人员id
     * @param deptId   部门id
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/7 20:20
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDataPermissionsService}$[86400]", key = "'listAllDeptDataPermissions:' + #personId + ':' + #deptId")
    public Set<String> listAllDeptDataPermissions(String personId, String deptId) {
        if (StrUtil.isBlank(personId) || StrUtil.isBlank(deptId)){
            return null;
        }

        TpDeptBasicinfoVO dept = tpDeptBasicinfoService.view(deptId);
        if (null == dept){
            return null;
        }
        return tpDataPermissionsMapper.listAllDeptDataPermissions(personId, dept.getDeptLevelcode());
    }

    /**
     * 查询企业类型的 当前登录人的所有权限部门id，以及权限部门id的父级id
     * <per>
     *     1. 根据人员id，查询出所有部门信息
     *     2. 根据部门层级编码，截取出所有有效的层级编码
     *     3. 根据层级编码查询出所有部门信息
     * </per>
     * @param permQuery 人员id、部门类型
     * @param maxLength 单位编码最大长度 企业：22、政府：6
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author jiuxx
     * @date 2023/11/1 17:27
     */
    private List<TreeNode> treePerm(TpTreePermQuery permQuery, int maxLength) {

        // 1. 根据人员id，查询出所有部门信息； extend01存储部门层级编码; extend02存储单位id
        List<TreeNode> treeNodes = tpDataPermissionsMapper.treeSimplePerm(permQuery);
        if (CollectionUtil.isEmpty(treeNodes)){
            return new ArrayList<>();
        }

        // 2. 根据部门层级编码，截取出所有有效的层级编码
        Set<String> deptLevelCodes = this.createDeptLevelCodes(treeNodes, maxLength);
        if (CollectionUtil.isEmpty(deptLevelCodes)){
            return treeNodes;
        }

        // 3. 查询部门
        // 3.1 提取单位id
        Set<String> ascnIds = this.extractAscnIds(treeNodes);
        // 3.2 提取具有权限的部门id
        Set<String> permIds = this.extractPermIds(treeNodes);
        // 3.3 根据层级编码和单位id，查询出所有部门信息
        List<TreeNode> parents = tpDataPermissionsMapper.listByDeptLevelCodes(deptLevelCodes, ascnIds, permIds);
        treeNodes.addAll(parents);
        return treeNodes;
    }

    /**
     * 提取具有权限的部门id
     *
     * @param treeNodes extend02存储单位id
     * @author jiuxx
     * @date 2023/11/1 19:49
     */
    private Set<String> extractPermIds(List<TreeNode> treeNodes) {
        Set<String> permIds = new HashSet<>();
        for (TreeNode treeNode: treeNodes){
            permIds.add(treeNode.getId());
        }
        return permIds;
    }

    /**
     * 提取出单位id
     *
     * @param treeNodes extend02存储单位id
     * @author jiuxx
     * @date 2023/11/1 19:49
     */
    private Set<String> extractAscnIds(List<TreeNode> treeNodes) {
        Set<String> ascnIds = new HashSet<>();
        for (TreeNode treeNode: treeNodes){
            ascnIds.add(treeNode.getExtend02());
        }
        return ascnIds;
    }

    /**
     * 创建部门编码集合
     * <pre>
     *     层级编码
     *     单位：1011719299698012979200 长度22
     *     部门：1011719299698012979200001001001 长度 > 22; 长度 = 22 + n * 3
     * </pre>
     *
     * @param treeNodes 具有的数据权限
     * @param maxLength 单位编码最大长度 企业：22、政府：6
     * @return java.util.List<java.lang.String>
     * @author jiuxx
     * @date 2023/11/1 17:51
     */
    private Set<String> createDeptLevelCodes(List<TreeNode> treeNodes, int maxLength) {

        Set<String> parents = new HashSet<>();

        for (TreeNode treeNode: treeNodes){
            String deptLevelCode = treeNode.getExtend01();
            while (deptLevelCode.length() > maxLength){
                deptLevelCode = deptLevelCode.substring(0, deptLevelCode.length() - 3);
                if (deptLevelCode.length() >= maxLength){
                    parents.add(deptLevelCode);
                }
            }
        }

        return parents;
    }


}
