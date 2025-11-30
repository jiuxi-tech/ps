package com.jiuxi.admin.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.OpenApiUserVO;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.mapper.TpDeptBasicinfoMapper;
import com.jiuxi.admin.core.mapper.TpPersonBasicinfoMapper;
import com.jiuxi.admin.core.service.OpenApiUserService;
import com.jiuxi.admin.core.util.DataMaskUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 开放API - 用户信息查询服务实现类
 * 
 * @author system
 * @date 2025-01-30
 */
@Service
public class OpenApiUserServiceImpl implements OpenApiUserService {

    @Autowired
    private TpPersonBasicinfoMapper personMapper;

    @Autowired
    private TpDeptBasicinfoMapper deptMapper;

    /**
     * 根据人员ID查询用户信息（脱敏）
     */
    @Override
    public OpenApiUserVO getUserById(String personId) {
        if (!StringUtils.hasText(personId)) {
            return null;
        }

        // 查询用户信息
        TpPersonBasicinfoVO personVO = personMapper.view(personId);
        if (personVO == null) {
            return null;
        }

        // 转换为脱敏VO
        return convertToMaskedVO(personVO);
    }

    /**
     * 分页查询用户列表（脱敏）
     */
    @Override
    public IPage<OpenApiUserVO> getUserList(String deptId, Integer page, Integer size) {
        // 设置默认值
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 20 : size;
        
        // 限制最大每页条数
        if (size > 100) {
            size = 100;
        }

        // 构建查询条件
        TpPersonBasicQuery query = new TpPersonBasicQuery();
        query.setDeptId(deptId);
        query.setCurrent(page);
        query.setSize(size);

        // 分页查询
        Page<TpPersonBasicinfoVO> pageParam = new Page<>(page, size);
        IPage<TpPersonBasicinfoVO> personPage = personMapper.getPage(pageParam, query);

        // 转换为脱敏VO
        Page<OpenApiUserVO> result = new Page<>(page, size);
        result.setTotal(personPage.getTotal());
        
        java.util.List<OpenApiUserVO> records = new java.util.ArrayList<>();
        personPage.getRecords().forEach(personVO -> {
            records.add(convertToMaskedVO(personVO));
        });
        result.setRecords(records);

        return result;
    }

    /**
     * 搜索用户（脱敏）
     */
    @Override
    public IPage<OpenApiUserVO> searchUsers(String keyword, String deptId, Integer page, Integer size) {
        // 设置默认值
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 20 : size;
        
        // 限制最大每页条数
        if (size > 100) {
            size = 100;
        }

        // 构建查询条件
        TpPersonBasicQuery query = new TpPersonBasicQuery();
        query.setPersonName(keyword);
        query.setDeptId(deptId);
        query.setCurrent(page);
        query.setSize(size);

        // 分页查询
        Page<TpPersonBasicinfoVO> pageParam = new Page<>(page, size);
        IPage<TpPersonBasicinfoVO> personPage = personMapper.getPage(pageParam, query);

        // 转换为脱敏VO
        Page<OpenApiUserVO> result = new Page<>(page, size);
        result.setTotal(personPage.getTotal());
        
        java.util.List<OpenApiUserVO> records = new java.util.ArrayList<>();
        personPage.getRecords().forEach(personVO -> {
            records.add(convertToMaskedVO(personVO));
        });
        result.setRecords(records);

        return result;
    }

    /**
     * 将用户信息转换为脱敏VO
     * 
     * @param personVO 原始用户信息
     * @return 脱敏后的用户信息
     */
    private OpenApiUserVO convertToMaskedVO(TpPersonBasicinfoVO personVO) {
        if (personVO == null) {
            return null;
        }

        OpenApiUserVO vo = new OpenApiUserVO();
        
        // 复制基本字段
        vo.setPersonId(personVO.getPersonId());
        vo.setOffice(personVO.getOffice());
        vo.setDeptId(personVO.getDeptId());
        vo.setDeptName(personVO.getDeptSimpleName()); // 使用部门简称
        vo.setSex(personVO.getSex());
        vo.setActived(personVO.getActived());
        vo.setCreateTime(personVO.getCreateTime());

        // 性别名称
        if (personVO.getSex() != null) {
            vo.setSexName(personVO.getSex() == 1 ? "男" : "女");
        }

        // 脱敏处理
        vo.setPersonName(DataMaskUtil.maskName(personVO.getPersonName()));
        vo.setPersonNo(DataMaskUtil.maskEmployeeNo(personVO.getPersonNo()));
        vo.setPhone(DataMaskUtil.maskPhone(personVO.getPhone()));
        vo.setEmail(DataMaskUtil.maskEmail(personVO.getEmail()));

        return vo;
    }
}
