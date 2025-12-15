package com.jiuxi.admin.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.service.TpDataPermissionsService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.security.core.holder.SessionHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @ClassName: DataPermissionsUtil
 * @Description:
 * @Author jiuxx
 * @Date 2023/11/2 13:43
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Component
public class CommonDataPermissionsUtil {

    private static TpDataPermissionsService tpDataPermissionsService;

    public CommonDataPermissionsUtil(TpDataPermissionsService tpDataPermissionsService) {
        CommonDataPermissionsUtil.tpDataPermissionsService = tpDataPermissionsService;
    }

    /**
     * 根据人员id查询数据权限部门id
     *
     * @param personId 人员id
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/2 13:53
     */
    public static Set<String> listDataPermPersonId(String personId) {

        return tpDataPermissionsService.listPermIds(personId);
    }

    /**
     * 查询当前登陆人的所有数据权限部门id
     *
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/2 13:53
     */
    public static Set<String> listCurrentPersonDataPerm() {
        SessionVO sessionVO = SessionHolder.get();
        if (null == sessionVO) {
            return null;
        }
        String personId = SessionHolder.get().getPersonId();
        if (StrUtil.isBlank(personId)) {
            return null;
        }
        return listDataPermPersonId(personId);
    }

    /**
     * 判断当前登陆人是否具有所有【传入部门的数据权限】
     *
     * @param deptIds 部门id
     * @return 全部有返回true，否则返回false
     * @author jiuxx
     * @date 2023/11/2 13:09
     */
    public static boolean isExistsDataPermissions(Set<String> deptIds) {
        SessionVO sessionVO = SessionHolder.get();
        if (null == sessionVO) {
            return false;
        }
        return isExistsDataPermissions(sessionVO.getPersonId(), deptIds);
    }

    /**
     * 判断当前人员是否具有所有【传入部门的数据权限】
     *
     * @param personId 人员id
     * @param deptIds  部门id
     * @return 全部有返回true，否则返回false
     * @author jiuxx
     * @date 2023/11/2 13:09
     */
    public static boolean isExistsDataPermissions(String personId, Set<String> deptIds) {
        if (StrUtil.isBlank(personId)) {
            return false;
        }

        Set<String> permIds = tpDataPermissionsService.listPermIds(personId);
        if (CollectionUtil.isEmpty(permIds)) {
            return false;
        }

        for (String deptId : deptIds) {
            if (!permIds.contains(deptId)) {
                return false;
            }
        }
        return true;
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
    public static boolean isExistsDataPermissions(String personId, String deptId) {

        return tpDataPermissionsService.isExistsDataPermissions(personId, deptId);
    }



    /**
     * 查询当前部门及下级的数据权限
     *
     * @param deptId  部门id（一般为登录人部门id）
     *               为空则返回登录人的部门id
     *               不为空则判断是否具有当前部门权限，无权限则抛出异常，有权限则返回部门id
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/2 14:34
     */
    public static Set<String> listCurrentDeptDataPermissions(String deptId) {
        Set<String> set = new LinkedHashSet();
        if (StrUtil.isNotBlank(deptId)){
            set.add(deptId);
        }
        return listCurrentDeptDataPermissions(set);
    }

    /**
     * 查询当前部门及下级的数据权限
     *
     * @param deptIds  部门id（一般为前端选择的部门id）
     *                为空则返回登录人的部门id
     *                不为空则判断是否具有当前部门权限，无权限则抛出异常，有权限则返回部门id
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/2 14:34
     */
    public static Set<String> listCurrentDeptDataPermissions(Set<String> deptIds) {
        Set<String> set = null;
        if (CollectionUtil.isEmpty(deptIds)) {
            SessionVO sessionVO = SessionHolder.get();
            if (null == sessionVO) {
                throw new TopinfoRuntimeException(-1, "登录信息过期");
            }
            set = new HashSet<>();
            set.add(sessionVO.getDeptId());
        } else {
            boolean existsDataPermissions = CommonDataPermissionsUtil.isExistsDataPermissions(deptIds);
            if (!existsDataPermissions) {
                throw new TopinfoRuntimeException(-1, "当前人员没有相关的数据权限");
            }
            set = deptIds;
        }
        return set;
    }


    /**
     * 查询当前部门的数据权限
     *
     * @param deptId 部门id，如果为空则返回登录人的所有数据权限的部门id
     *               为空则返回当前登录人所有数据权限
     *               不为空则返回当前部门或下级部门数据权限（根据includeChildren确定）
     * @param includeChildren 是否包含下级
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/2 14:34
     */
    public static Set<String> listAllDeptDataPermissions(String deptId, boolean includeChildren) {

        // 取出登录人信息
        SessionVO sessionVO = SessionHolder.get();
        if (null == sessionVO) {
            return null;
        }

        // 部门id为空时，查询当前登录人
        if (StrUtil.isBlank(deptId)){
            return CommonDataPermissionsUtil.listDataPermPersonId(sessionVO.getPersonId());
        }

        // 不包含下级的情况
        if (!includeChildren){
            // 判断是否具有权限
            boolean existsDataPermissions = CommonDataPermissionsUtil.isExistsDataPermissions(sessionVO.getPersonId(), deptId);
            if (!existsDataPermissions) {
                throw new TopinfoRuntimeException(-1, "当前人员没有相关的数据权限");
            }

            Set<String> set = new LinkedHashSet<>();
            set.add(deptId);
            return set;
        }

        // 包含下级的情况
        return tpDataPermissionsService.listAllDeptDataPermissions(sessionVO.getPersonId(), deptId);

    }


    /**
     * 查询当前部门的数据权限
     *
     * @param deptId 部门id，如果为空则返回登录人的所有数据权限的部门id
     *               为空则返回当前登录人所有数据权限
     *               不为空则返回当前部门及下级部门数据权限
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/2 14:34
     */
    public static Set<String> listAllDeptDataPermissions(String deptId) {

        return listAllDeptDataPermissions(deptId, true);
    }

    /**
     * 查询当前部门的数据权限
     *
     * @param deptIds 部门id，如果为空则返回登录人的所有数据权限的部门id
     * @return java.util.Set<java.lang.String>
     * @author jiuxx
     * @date 2023/11/2 14:34
     */
    public static Set<String> listAllDeptDataPermissions(Set<String> deptIds) {
        Set<String> set = null;
        if (CollectionUtil.isEmpty(deptIds)) {
            set = CommonDataPermissionsUtil.listCurrentPersonDataPerm();
        } else {
            boolean existsDataPermissions = CommonDataPermissionsUtil.isExistsDataPermissions(deptIds);
            if (!existsDataPermissions) {
                throw new TopinfoRuntimeException(-1, "当前人员没有相关的数据权限");
            }
            set = deptIds;
        }
        return set;
    }

}
