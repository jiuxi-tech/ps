package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpPersonDept;
import com.jiuxi.admin.core.bean.vo.TpPersonDeptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpPersonDeptMapper
 * @Description: 人员部门表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpPersonDeptMapper {

    int save(TpPersonDept bean);

    List<TpPersonDeptVO> select(@Param("deptId") String deptId, @Param("personId") String personId, @Param("defaultDept") Integer defaultDept);
    int selectByDeptIdAndPersonId(@Param("deptId") String deptId, @Param("personId") String personId);

    /**
     * 根据人员id删除部门
     *
     * @param personId    人员id
     * @param defaultDept 兼职/主部门
     * @return int
     * @author jiuxx
     * @date 2024/6/3 11:22
     */
    int deleteByPersonId(@Param("personId") String personId, @Param("defaultDept") Integer defaultDept);

    /**
     * 根据部门id和人员id删除部门
     *
     * @param personId    人员id
     * @param personId 兼职/主部门
     * @return int
     * @author jiuxx
     * @date 2024/6/3 11:22
     */
    int deleteByDeptIdPersonId(@Param("deptId") String deptId, @Param("personId") String personId);


    /**
     * 物理删除，比如取消兼职部门的时候
     *
     * @param deptId:
     * @param personId:
     * @return int
     * @author pand
     * @date 2020-11-27 13:18
     */
    int delete(@Param("deptId") String deptId, @Param("personId") String personId, @Param("defaultDept") Integer defaultDept);


    int update(TpPersonDept bean);

}
