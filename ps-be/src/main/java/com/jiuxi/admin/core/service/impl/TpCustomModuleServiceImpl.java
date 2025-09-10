package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpCustomModule;
import com.jiuxi.admin.core.bean.query.TpCustomModuleQuery;
import com.jiuxi.admin.core.bean.vo.TpCustomModuleVO;
import com.jiuxi.admin.core.mapper.TpCustomModuleMapper;
import com.jiuxi.admin.core.service.TpCustomModuleService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName: TpCustomModuleServiceImpl
 * @Description: 自定义模块信息表 存储模块的信息，按钮信息、路由信息
 * @Author pand
 * @Date 2021-05-11 11:22:40
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpCustomModuleService")
public class TpCustomModuleServiceImpl implements TpCustomModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpCustomModuleServiceImpl.class);

    @Autowired
    private TpCustomModuleMapper tpCustomModuleMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询当前链接的数据库名称
     */
    private static final String dbNameSql = "select database();";

    private static String CHECK_TABLENAME_SQL = "SELECT TABLE_NAME FROM information_schema.TABLES where TABLE_SCHEMA = '{}' and table_name = '{}}'";

    private static String MCODE_SQL = "select * from {} where actived = 1 limit 1";

    @Override
    public IPage<TpCustomModuleVO> queryPage(TpCustomModuleQuery query) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
            String mcode = query.getMcode();
            if (StrUtil.isNotBlank(mcode)) {
                query.setMcode(mcode.toUpperCase());
            }

            Page<TpCustomModuleVO> page = new Page<>(pageNum, pageSize);
            IPage<TpCustomModuleVO> iPage = tpCustomModuleMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！query:{}, 错误:{}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }

    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public String add(TpCustomModuleVO vo, String jwtpid) {
        // 表名
        String mcode = TpConstant.PRO_MCODE.concat(vo.getMcode().toUpperCase());
        // 库名
        String dbnames = jdbcTemplate.queryForObject(dbNameSql, String.class);

        // 判断数据库表名是否存在
        // String tableName = "SELECT TABLE_NAME FROM information_schema.TABLES where TABLE_SCHEMA = '" + dbnames + "' and table_name = '" + mcode + "'";

        // 判断数据库表名是否存在
        String checkTableNameSql = StrUtil.format(CHECK_TABLENAME_SQL, dbnames, mcode);

        List<Map<String, Object>> list = jdbcTemplate.queryForList(checkTableNameSql);
        if (list.size() >= 1) {
            throw new TopinfoRuntimeException(-1, "表名已存在！");
        }

        // 生成主键
        String id = SnowflakeIdUtil.nextIdStr();

        TpCustomModule bean = new TpCustomModule();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 设置主键id
        bean.setMid(id);
        bean.setMcode(mcode);
        String now = CommonDateUtil.now();
        // 创建人
        bean.setCreator(jwtpid);
        // 创建时间
        bean.setCreateTime(now);
        // 修改人
        bean.setUpdator(jwtpid);
        // 修改时间
        bean.setUpdateTime(now);
        // 是否有效
        bean.setActived(TpConstant.YES);

        try {
            // 动态创建表
            String createTableSQL = "CREATE TABLE `" + mcode + "` (" +
                    "  `id` varchar(19) COLLATE utf8mb4_bin NOT NULL COMMENT '主键', " +
                    "  `refer_id` varchar(19) COLLATE utf8mb4_bin NOT NULL COMMENT '模块id,业务主键,如针对企业新增一个黑名单模块。refer_id为使用该模块的id', " +
                    "  `data_detail` longtext COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务数据,json格式' CHECK (json_valid(`data_detail`)), " +
                    "  `ACTIVED` int(11) DEFAULT NULL COMMENT '有效标志'," +
                    "  `CREATOR` varchar(19) COLLATE utf8mb4_bin NOT NULL COMMENT '创建人'," +
                    "  `CREATE_TIME` varchar(14) COLLATE utf8mb4_bin NOT NULL COMMENT '创建时间'," +
                    "  `UPDATOR` varchar(19) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新人'," +
                    "  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新时间'," +
                    "  `EXTEND01` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段01'," +
                    "  `EXTEND02` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段02'," +
                    "  `EXTEND03` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段03'," +
                    "  PRIMARY KEY (`id`) " +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT '" + (StrUtil.isBlank(vo.getMdesc()) ? "\"\"" : vo.getMdesc()) + "';";
            jdbcTemplate.execute(createTableSQL);

            // 给新建的表外健自动创建索引
            String createIndex = "CREATE INDEX IDX_REFER_ID USING BTREE ON " + mcode + " (refer_id);";
            jdbcTemplate.execute(createIndex);

            tpCustomModuleMapper.add(bean);
            return id;
        } catch (Exception e) {
            LOGGER.error("新增失败！query:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增失败！");
        }
    }

    @Override
    public TpCustomModuleVO view(String id) {
        try {

            TpCustomModuleVO tpCustomModuleVO = tpCustomModuleMapper.view(id);
            if (null == tpCustomModuleVO) {
                throw new TopinfoRuntimeException(-1, "数据不存在！");
            }
            return tpCustomModuleVO;
        } catch (Exception e) {
            LOGGER.error("查看失败！id:{}, 错误:{}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    @Override
    public int update(TpCustomModuleVO vo, String jwtpid) {

        TpCustomModule bean = new TpCustomModule();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        bean.setUpdator(jwtpid);
        bean.setUpdateTime(CommonDateUtil.now());
        // TODO

        try {
            int count = tpCustomModuleMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public void deleteByIds(String mids) {

        List<String> ids = StrUtil.split(mids, ",");

        ids.forEach(id -> {
            // 根据id查询表数据
            TpCustomModuleVO tpCustomModuleVO = tpCustomModuleMapper.view(id);
            if (null == tpCustomModuleVO) {
                throw new TopinfoRuntimeException(-1, "数据不存在！");
            }

            String mcode = tpCustomModuleVO.getMcode();

            // 查看业务表是否有数据存在，已存在数据的表不能删除
            String mcodeSql = StrUtil.format(MCODE_SQL, mcode);
            List<Map<String, Object>> list = jdbcTemplate.queryForList(mcodeSql);
            if (list.size() == 1) {
                throw new TopinfoRuntimeException(-1, "业务数据已存在，不能删除表结构！");
            }

            // 删除表结构 -- 偷懒先不改，嘎嘎嘎
            String dropTable = "DROP TABLE " + mcode;

            try {
                jdbcTemplate.execute(dropTable);

                tpCustomModuleMapper.delete(id);
            } catch (Exception e) {
                LOGGER.error("删除失败！mids:{}, 错误:{}", mids, ExceptionUtils.getStackTrace(e));
                throw new TopinfoRuntimeException(-1, "删除失败！");
            }
        });
    }
}
