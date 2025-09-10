package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpPersonRole;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpPersonRoleMapper
 * @Description: 人员角色表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpPersonRoleMapper {

    int save(TpPersonRole bean);

    /**
     * 根据部门和人员id删除【人员-角色】
     *
     * @param deptId
     * @param personId
     * @return int
     * @author 杨占锐
     * @date 2024/5/30 17:55
     */
    int delete(@Param("deptId") String deptId, @Param("personId") String personId);

    /**
     * 根据角色id删除【人员-角色】
     *
     * @param roleId 角色id
     * @return int
     * @author 杨占锐
     * @date 2024/5/30 17:55
     */
    int deleteByRoleId(@Param("roleId") String roleId);

    int deleteByPersonIdAndDeptIds(@Param("personId") String personId, @Param("deptIds") List<String> deptIds);

    List<TpPersonRoleVO> selectByPersonIdAndDeptId(@Param("deptId") String deptId, @Param("personId") String personId);

    List<TpPersonRoleVO> selectByRoleId(String roleId);

}
