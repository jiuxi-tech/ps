package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.CJson;
import com.jiuxi.admin.core.bean.vo.CJsonVO;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoRefVO;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoVO;
import com.jiuxi.admin.core.bean.vo.TpCustomFormVO;
import com.jiuxi.admin.core.bean.vo.TpCustomModuleVO;
import com.jiuxi.admin.core.mapper.CJsonMapper;
import com.jiuxi.admin.core.mapper.TpCustomFormMapper;
import com.jiuxi.admin.core.mapper.TpCustomModuleMapper;
import com.jiuxi.admin.core.service.CJsonService;
import com.jiuxi.admin.core.service.TpAttachinfoService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName: CJsonCesServiceImpl
 * @Description: ""
 * @Author pand
 * @Date 2021-05-12 13:48:38
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("cJsonService")
public class CJsonServiceImpl implements CJsonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CJsonServiceImpl.class);

    @Autowired
    private CJsonMapper cJsonCesMapper;

    @Autowired
    private TpCustomModuleMapper tpCustomModuleMapper;

    @Autowired
    private TpCustomFormMapper tpCustomFormMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TpAttachinfoService tpAttachinfoService;

    /**
     * 前端标签
     */
    private static final String PROPS = "props";
    private static final String REFERTYPE = "referType";
    private static final String REFERTYPEPATTERN = "referTypePattern";
    private static final String SHOW_QUERY = "show_query";
    private static final String QUERY = "query";
    private static final String ROWS = "rows";
    /**
     * form属性
     */
    private static final String FORMITEMPROPS = "formItemProps";
    private static final String SHOW = "show";
    private static final String COLS = "cols";
    private static final String TYPE = "type";
    private static final String VALUE = "value";
    private static final String PARAM = "param";
    /**
     * 字段名
     */
    private static final String NAME = "name";
    /**
     * 查询条件，等于或like
     */
    private static final String SEARCHTYPE = "searchType";
    private static final String SEARCHTYPEEQU = "=";

    /**
     * 上传空间类型
     */
    private static final String TP_UPLOAD = "tp-upload";
    private static final String FB_EDITOR = "fb-editor";
    private static final String TP_DATEPICKER = "tp-datepicker";
    private static final String FB_INPUT = "fb-input";
    private static final String FB_CHECKBOX_GROUP = "fb-checkbox-group";
    private static final String FB_SELECT = "fb-select";
    private static final String FB_TREE_SELECT = "fb-tree-select";


    @Override
    public IPage<Map> queryPage(JSONObject query, String jwtaid, String jwtdid, String jwtCityCode) {

        // 列表配置信息
        TpCustomFormVO tpCustomFormVO = tpCustomFormMapper.viewByFid(query.getStr("fid"));
        if (null == tpCustomFormVO) {
            throw new TopinfoRuntimeException(-1, "请先配置列表！");
        }

        Integer pageNum = Optional.ofNullable(query.getInt("current")).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getInt("size")).orElse(10);

        JSONObject fjson = JSONUtil.parseObj(tpCustomFormVO.getFjson());
        Map<String, String> sqlMap = this.formatSql(tpCustomFormVO.getMid(), fjson, query, jwtaid, jwtdid, jwtCityCode);

        try {

            String countSql = sqlMap.get("countSql");
            String pageSql = sqlMap.get("pageSql");

            long count = jdbcTemplate.queryForObject(countSql, Long.class);

            List list = jdbcTemplate.query(pageSql, new Object[]{}, new BeanPropertyRowMapper<>(CJsonVO.class));

            Page page = new Page(pageNum, pageSize, count);
            page.setRecords(list);

            return page;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }

    /**
     * list需要拼接查询sql
     *
     * @param fjson 表单定义的具体字段
     * @param mid   模块id
     * @param query 查询条件
     * @return java.lang.String
     * @author pand
     * @date 2021-08-23 11:14
     */
    private Map<String, String> formatSql(String mid, JSONObject fjson, JSONObject query, String jwtaid, String jwtdid, String jwtCityCode) {
        Map<String, String> map = new HashMap<>(16);
        TpCustomModuleVO tpCustomModuleVO = tpCustomModuleMapper.view(mid);
        // 表名
        String mcode = tpCustomModuleVO.getMcode().toUpperCase();
        StringBuffer countSql = new StringBuffer();
        StringBuffer pageSql = new StringBuffer();
        countSql.append("select count(1) from ").append(mcode).append(" model where model.actived=1 ");
        pageSql.append("select model.id, model.refer_id, model.data_detail, model.actived, model.creator, model.create_time, model.updator, model.update_time, model.extend01, model.extend02, model.extend03 from ").append(mcode).append(" model where model.actived=1 ");

        JSONObject formQuery = fjson.getJSONObject(QUERY);
        JSONObject formProps = fjson.getJSONObject(PROPS);

        if (StrUtil.equals(formProps.getStr(REFERTYPE), "org") && StrUtil.isNotBlank(jwtaid)) {
            countSql.append("and model.refer_id").append(formProps.getStr(REFERTYPEPATTERN)).append(jwtaid);
            pageSql.append("and model.refer_id").append(formProps.getStr(REFERTYPEPATTERN)).append(jwtaid);
        } else if (StrUtil.equals(formProps.getStr(REFERTYPE), "dept") && StrUtil.isNotBlank(jwtdid)) {
            countSql.append("and model.refer_id").append(formProps.getStr(REFERTYPEPATTERN)).append(jwtdid);
            pageSql.append("and model.refer_id").append(formProps.getStr(REFERTYPEPATTERN)).append(jwtdid);
        } else if (StrUtil.equals(formProps.getStr(REFERTYPE), "city") && StrUtil.isNotBlank(jwtCityCode)) {
            countSql.append("and model.refer_id").append(formProps.getStr(REFERTYPEPATTERN)).append(jwtCityCode);
            pageSql.append("and model.refer_id").append(formProps.getStr(REFERTYPEPATTERN)).append(jwtCityCode);
        } else {
            LOGGER.info("referType类型未定义！{}", REFERTYPE);
        }

        int current = query.getInt("current");
        if (current <= 0) {
            throw new TopinfoRuntimeException(-1, "当前页不能小于0");
        }
        int size = query.getInt("size");
        int limit = (current - 1) * size;

        if (StrUtil.equals(formProps.getStr(SHOW_QUERY), "false")) {
            // 没有查询条件，直接返回
            map.put("countSql", countSql.toString());
            pageSql.append("limit ").append(limit).append(" ").append(size);
            map.put("pageSql", pageSql.toString());
            return map;
        }

        // 组装条件
        StringBuffer where = new StringBuffer();
        JSONArray rows = formQuery.getJSONArray(ROWS);
        for (int i = 0; i < rows.size(); i++) {
            JSONObject row = (JSONObject) rows.get(i);
            JSONObject formItemProps = row.getJSONObject(FORMITEMPROPS);
            // key表示字段名
            String key = formItemProps.getStr(NAME);
            // 控件类型
            String type = row.getStr(TYPE);
            // props属性
            JSONObject row_props = row.getJSONObject(PROPS);


            // 判断不同控件的查询条件
            if (StrUtil.equals(type, TP_DATEPICKER)) {
                // 时间控件，直接改成>,<查询区间
                if (StrUtil.isNotBlank(query.getStr(key + "Max")) && StrUtil.equals(formItemProps.getStr(SHOW), "true")) {
                    // and必须带，因为前面已经有了一个refer_id查询条件。json_extract(model.data_detail, '$.value.id_1632451137076_3'）< '值'
                    where.append(" and json_extract(model.data_detail, ").append("'$.value.").append(key).append("') >= '").append(query.getStr(key + "Max")).append("' ");
                }
                if (StrUtil.isNotBlank(query.getStr(key + "Min")) && StrUtil.equals(formItemProps.getStr(SHOW), "true")) {
                    // and必须带，因为前面已经有了一个refer_id查询条件。json_extract(model.data_detail, '$.value.id_1632451137076_3'）> '值'
                    where.append(" and json_extract(model.data_detail, ").append("'$.value.").append(key).append("') <= '").append(query.getStr(key + "Min")).append("' ");
                }
            } else if (StrUtil.equals(type, FB_INPUT)) {
                // 文本框控件，查询条件只有 = 或者 like
                if (StrUtil.isBlank(query.getStr(key)) || StrUtil.equals(formItemProps.getStr(SHOW), "false")) {
                    // 该参数没有传查询值，或者该属性在页面是隐藏的，不能作为查询条件拼接查询
                    continue;
                }
                if (StrUtil.equals(SEARCHTYPE, SEARCHTYPEEQU)) {
                    // and必须带，因为前面已经有了一个refer_id查询条件。json_extract(model.data_detail, '$.value.id_1632451137076_3'）= '值'
                    where.append(" and json_extract(model.data_detail, ").append("'$.value.").append(key).append("') ").append(formItemProps.getStr(SEARCHTYPE)).append(" '").append(query.getStr(key)).append("' ");
                } else {
                    // and必须带，因为前面已经有了一个refer_id查询条件。json_extract(model.data_detail, '$.value.id_1632451137076_3') like '%设备1%'
                    where.append(" and json_extract(model.data_detail, ").append("'$.value.").append(key).append("') ").append(formItemProps.getStr(SEARCHTYPE)).append(" '%").append(query.getStr(key)).append("%' ");
                }
            } else if (StrUtil.equals(type, FB_CHECKBOX_GROUP)) {
                // 复选框组fb-checkbox-group，数据库存储数据是数组，需要特殊处理
                // 获取提交的数据，逗号隔开的
                String value = query.getStr(key);
                List<String> valueList = StrUtil.split(value, ",");
                if (StrUtil.isBlank(query.getStr(key)) || StrUtil.equals(formItemProps.getStr(SHOW), "false") || StrUtil.isBlank(value)) {
                    continue;
                }
                where.append(" and ( ");
                int checkboxGroupListSize = valueList.size();
                for (int j = 0; ; ) {
                    if (j == 0) {
                        where.append("JSON_SEARCH(model.data_detail, 'all', ").append(valueList.get(j)).append(", null, '$.value.").append(key).append("') is not null");
                        j++;
                        continue;
                    } else if (j < checkboxGroupListSize) {
                        where.append(" or JSON_SEARCH(model.data_detail, 'all', ").append(valueList.get(j)).append(", null, '$.value.").append(key).append("') is not null");
                        j++;
                        continue;
                    } else {
                        where.append(")");
                        break;
                    }
                }
            } else if (StrUtil.equals(type, FB_SELECT) || StrUtil.equals(type, FB_TREE_SELECT)) {
                // 需要特殊处理，多选下拉框fb-select，多选下拉树fb-tree-select。数据库存储数据是数组
                String value = query.getStr(key);
                List<String> valueList = StrUtil.split(value, ",");
                if (StrUtil.isBlank(query.getStr(key)) || StrUtil.equals(formItemProps.getStr(SHOW), "false") || StrUtil.isBlank(value)) {
                    // 该参数没有传查询值，或者该属性在页面是隐藏的，不能作为查询条件拼接查询
                    continue;
                }

                where.append(" and ( ");
                int checkboxGroupListSize = valueList.size();
                for (int j = 0; ; ) {
                    if (j == 0) {
                        where.append("JSON_SEARCH(model.data_detail, 'all', ").append(valueList.get(j)).append(", null, '$.value.").append(key).append("') is not null");
                        j++;
                        continue;
                    } else if (j < checkboxGroupListSize) {
                        where.append(" or JSON_SEARCH(model.data_detail, 'all', ").append(valueList.get(j)).append(", null, '$.value.").append(key).append("') is not null");
                        j++;
                        continue;
                    } else {
                        where.append(")");
                        break;
                    }
                }
            } else {
                if (StrUtil.isBlank(query.getStr(key)) || StrUtil.equals(formItemProps.getStr(SHOW), "false")) {
                    // 该参数没有传查询值，或者该属性在页面是隐藏的，不能作为查询条件拼接查询
                    continue;
                }
                // 其它控件默认处理。。。。。。
                where.append(" and json_extract(model.data_detail, ").append("'$.value.").append(key).append("') ").append(formItemProps.getStr(SEARCHTYPE)).append(" '").append(query.getStr(key)).append("' ");
            }
        }

        // 有查询条件，拼接查询条件，没有不拼接
        if (StrUtil.isBlank(where.toString())) {
            pageSql.append(" limit ").append(limit).append(", ").append(size);
        } else {
            countSql.append(where);
            pageSql.append(where);
        }

        LOGGER.info("countSql：{}", countSql);
        LOGGER.info("pageSql：{}", pageSql);

        map.put("countSql", countSql.toString());
        map.put("pageSql", pageSql.toString());

        return map;
    }

    @Override
    public String add(CJsonVO vo, String jwtaid, String jwtdid, String jwtpid, String jwtCityCode) {
        String fid = vo.getFid();
        // 列表配置信息
        TpCustomFormVO tpCustomFormVO = tpCustomFormMapper.viewByFid(fid);
        if (null == tpCustomFormVO) {
            throw new TopinfoRuntimeException(-1, "请先配置列表！");
        }
        JSONObject fjson = JSONUtil.parseObj(tpCustomFormVO.getFjson());
        JSONObject formProps = fjson.getJSONObject(PROPS);
        if (StrUtil.equals(formProps.getStr(REFERTYPE), "org")) {
            vo.setReferId(jwtaid);
        } else if (StrUtil.equals(formProps.getStr(REFERTYPE), "dept")) {
            vo.setReferId(jwtdid);
        } else if (StrUtil.equals(formProps.getStr(REFERTYPE), "city")) {
            vo.setReferId(jwtCityCode);
        } else {
            throw new TopinfoRuntimeException(-1, "为定义referId类型");
        }
        String mid = tpCustomFormVO.getMid();
        TpCustomModuleVO tpCustomModuleVO = tpCustomModuleMapper.view(mid);
        if (null == tpCustomModuleVO) {
            throw new TopinfoRuntimeException(-1, "请先定义模块！");
        }

        // 生成主键
        String id = SnowflakeIdUtil.nextIdStr();

        CJson bean = new CJson();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 设置主键id
        bean.setId(id);
        bean.setMcode(tpCustomModuleVO.getMcode());
        // 创建人
        bean.setCreator(jwtpid);
        // 创建时间
        bean.setCreateTime(CommonDateUtil.now());
        // 是否有效
        bean.setActived(TpConstant.YES);

        try {
            // 附件绑定
            this.analysisFile(vo.getDataDetail(), fjson, id);
            cJsonCesMapper.add(bean);
            return id;
        } catch (Exception e) {
            LOGGER.error("新增失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "新增失败！" : e.getMessage());
        }
    }

    @Override
    public int update(CJsonVO vo, String jwtaid, String jwtdid, String jwtpid, String jwtCityCode) {

        String fid = vo.getFid();
        // 列表配置信息
        TpCustomFormVO tpCustomFormVO = tpCustomFormMapper.viewByFid(fid);
        if (null == tpCustomFormVO) {
            throw new TopinfoRuntimeException(-1, "请先配置列表！");
        }
        JSONObject fjson = JSONUtil.parseObj(tpCustomFormVO.getFjson());
        JSONObject formProps = fjson.getJSONObject(PROPS);
        if (StrUtil.equals(formProps.getStr(REFERTYPE), "org")) {
            vo.setReferId(jwtaid);
        } else if (StrUtil.equals(formProps.getStr(REFERTYPE), "dept")) {
            vo.setReferId(jwtdid);
        } else if (StrUtil.equals(formProps.getStr(REFERTYPE), "city")) {
            vo.setReferId(jwtCityCode);
        } else {
            throw new TopinfoRuntimeException(-1, "为定义referId类型");
        }
        String mid = tpCustomFormVO.getMid();
        TpCustomModuleVO tpCustomModuleVO = tpCustomModuleMapper.view(mid);
        if (null == tpCustomModuleVO) {
            throw new TopinfoRuntimeException(-1, "请先定义模块！");
        }

        CJson bean = new CJson();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        bean.setMcode(tpCustomModuleVO.getMcode());
        bean.setUpdator(jwtpid);
        bean.setUpdateTime(CommonDateUtil.now());
        // TODO

        try {
            // 附件绑定
            this.analysisFile(vo.getDataDetail(), fjson, bean.getId());
            int count = cJsonCesMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    /**
     * 新增/修改，将表单里的附件与业务数据绑定
     *
     * @param dataDetailStr 提交的业务数据
     * @param fjson         表单内容
     * @param referId       业务数据id
     * @return java.util.List<java.lang.String>
     * @author pand
     * @date 2021-09-18 15:51
     */
    public void analysisFile(String dataDetailStr, JSONObject fjson, String referId) {
        if (StrUtil.isBlank(dataDetailStr)) {
            return;
        }
        JSONObject dataDetail = JSONUtil.parseObj(dataDetailStr);

        JSONObject value = dataDetail.getJSONObject(VALUE);
        if (null == value) {
            return;
        }
        String time = CommonDateUtil.now();
        JSONArray rows = fjson.getJSONArray(ROWS);
        for (int i = 0; i < rows.size(); i++) {
            // 获取每一行数据类型
            JSONObject row = (JSONObject) rows.get(i);
            JSONArray cols = row.getJSONArray(COLS);
            for (int j = 0; j < cols.size(); j++) {
                // 获取行里面的每一列数据
                JSONObject col = (JSONObject) cols.get(j);
                String type = col.getStr(TYPE);
                if (StrUtil.equals(TP_UPLOAD, type)) {
                    // 附件类型处理
                    String name = col.getJSONObject(FORMITEMPROPS).getStr(NAME);
                    // 提取附件
                    JSONObject fileJson = value.getJSONObject(name);
                    TpAttachinfoRefVO vo = new TpAttachinfoRefVO();
                    vo.setReferType(fileJson.getStr("referType"));
                    List<TpAttachinfoVO> fileList = JSONUtil.toList(fileJson.getJSONArray("fileList"), TpAttachinfoVO.class);
                    vo.setFileList(fileList);
                    // 附件绑定
                    tpAttachinfoService.band(vo, referId, time);
                }
            }
        }
    }

    @Override
    public CJsonVO view(String fid, String id) {
        // 列表配置信息
        TpCustomFormVO tpCustomFormVO = tpCustomFormMapper.viewByFid(fid);
        if (null == tpCustomFormVO) {
            throw new TopinfoRuntimeException(-1, "请先配置列表！");
        }
        String mid = tpCustomFormVO.getMid();
        TpCustomModuleVO tpCustomModuleVO = tpCustomModuleMapper.view(mid);
        if (null == tpCustomModuleVO) {
            throw new TopinfoRuntimeException(-1, "请先定义模块！");
        }

        try {
            CJsonVO vo = cJsonCesMapper.view(tpCustomModuleVO.getMcode(), id);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int deleteByIds(String fid, List<String> ids) {
        TpCustomFormVO tpCustomFormVO = tpCustomFormMapper.viewByFid(fid);
        if (null == tpCustomFormVO) {
            throw new TopinfoRuntimeException(-1, "请先配置列表！");
        }
        String mid = tpCustomFormVO.getMid();
        TpCustomModuleVO tpCustomModuleVO = tpCustomModuleMapper.view(mid);
        if (null == tpCustomModuleVO) {
            throw new TopinfoRuntimeException(-1, "请先定义模块！");
        }

        final String tableName = tpCustomModuleVO.getMcode();
        try {
            if (null == ids || ids.isEmpty()) {
                throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
            }
            // TODO 删除也可以使用sql批量操作
            ids.forEach(id -> {
                cJsonCesMapper.delete(tableName, id);
            });
            return ids.size();
        } catch (Exception e) {
            // 手动事务回滚
            LOGGER.error("删除失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

}
