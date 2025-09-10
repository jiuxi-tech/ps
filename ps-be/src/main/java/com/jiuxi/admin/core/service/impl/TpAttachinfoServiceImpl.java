package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoVO;
import com.jiuxi.admin.core.mapper.TpAttachinfoMapper;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.admin.core.bean.entity.TpAttachinfo;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoRefVO;
import com.jiuxi.admin.core.service.TpAttachinfoService;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 附件方法
 * @ClassName: TpAttachinfoServiceImpl
 * @Author: pand
 * @Date: 2021-01-19 14:55
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
@Service("tpAttachinfoService")
public class TpAttachinfoServiceImpl implements TpAttachinfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpAttachinfoServiceImpl.class);

    @Autowired
    private TpAttachinfoMapper tpAttachinfoMapper;

    @Override
    public int save(TpAttachinfo bean) {
        try {
            // 转换成数据库对象
            bean.setAttachId(StrUtil.isBlank(bean.getAttachId()) ? SnowflakeIdUtil.nextIdStr() : bean.getAttachId());

            if (StrUtil.isBlank(bean.getCreateTime())) {
                bean.setCreateTime(CommonDateUtil.now());
                bean.setUpdateTime(bean.getCreateTime());
            }

            int count = tpAttachinfoMapper.save(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("新增附件失败！bean: {}, 错误:{}", JSONObject.toJSONString(bean), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增附件失败！");
        }
    }

    @Override
    public TpAttachinfo view(String attachId) {
        try {
            TpAttachinfo bean = tpAttachinfoMapper.view(attachId);
            return bean;
        } catch (Exception e) {
            LOGGER.error("查看附件信息失败！attachId: {}, 错误:{}", attachId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看附件信息失败！");
        }
    }

    @Override
    public List<TpAttachinfoVO> selectByReferIdAndType(String referId, String referType) {
        try {
            List<TpAttachinfoVO> list = tpAttachinfoMapper.selectByReferIdAndType(referId, referType);
            return list;
        } catch (Exception e) {
            LOGGER.error("查询附件信息失败！referId: {}, referType:{}, 错误:{}", referId, referType, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询附件信息失败！");
        }
    }

    @Override
    public int update(TpAttachinfo bean, String pid) {
        try {
            bean.setUpdator(pid);
            bean.setUpdateTime(CommonDateUtil.now());

            int count = tpAttachinfoMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("附件信息修改失败！bean: {}, 错误:{}", JSONObject.toJSONString(bean), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "附件修改失败！");
        }
    }

    /**
     * 删除附件
     * <pre>
     * 根据 referId 和 attachId 删除附件：
     *    如果 referId 为空，则只能删除当前登录人自己创建的附件，物理删除
     *    如果 referId 不为空， 则根据 数据权限 判断是否能够删除，如果权限不对，则抛出异常 逻辑删除
     * </pre>
     *
     * @param attachId:
     * @param jwtpid:
     * @return int
     * @author pand
     * @date 2021-03-15 13:25
     */
    @Override
    public int remove(String referId, String attachId, String jwtpid) {

        try {

            int count = 0;
            if (StrUtil.isBlank(referId)) {
                // 如果 referId 为空，则只能删除当前登录人自己创建的附件，物理删除
                count = tpAttachinfoMapper.physicsDelete(attachId, jwtpid);
            } else {
                // 逻辑删除
                count = tpAttachinfoMapper.logicDelete(attachId, jwtpid, CommonDateUtil.now());
            }
            return count;
        } catch (Exception e) {
            LOGGER.error("删除附件信息失败！referId: {}, attachId:{}, 错误:{}", referId, attachId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "删除附件信息失败！" : e.getMessage());
        }
    }

    /**
     * 物理删除
     *
     * @param attachId:
     * @return int
     * @author pand
     * @date 2021-03-15 13:25
     */
    @Override
    public int delete(String attachId) {
        try {
            int count = tpAttachinfoMapper.delete(attachId);
            return count;
        } catch (Exception e) {
            LOGGER.error("删除附件信息失败！attachId:{}, 错误:{}", attachId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "删除附件信息失败！" : e.getMessage());
        }
    }

    @Override
    public int deleteByReferId(TpAttachinfo bean, String pid) {
        try {
            int count = tpAttachinfoMapper.deleteByReferId(bean.getReferId(), pid, CommonDateUtil.now());
            return count;
        } catch (Exception e) {
            LOGGER.error("删除附件信息失败！referId:{}, 错误:{}", bean.getReferId(), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "删除附件信息失败！" : e.getMessage());
        }
    }

    @Override
    public int deleteByReferIdAndReferType(String referId, String referType, String pid) {
        try {
            int count = tpAttachinfoMapper.deleteByReferIdAndReferType(referId, referType, pid, CommonDateUtil.now());
            return count;
        } catch (Exception e) {
            LOGGER.error("删除附件信息失败！referId:{}, referType:{}, 错误:{}", referId, referType, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "删除附件信息失败！" : e.getMessage());
        }
    }

    /**
     * 业务数据，附件绑定
     *
     * @param list:    附件列表
     * @param referId: 业务id
     * @param pid:     操作人id
     * @return void
     * @author pand
     * @date 2021-03-05 16:24
     */
    @Override
    public void listBand(List<TpAttachinfoRefVO> list, String referId, String pid) {

        if (null == list || list.isEmpty()) {
            return;
        }

        String updateTime = CommonDateUtil.now();
        for (TpAttachinfoRefVO item : list) {
            // 根据业务id和业务附件类型，删除附件
            String referType = item.getReferType();

            tpAttachinfoMapper.deleteByReferIdAndReferType(referId, referType, pid, updateTime);

            // 重新绑定附件
            List<TpAttachinfoVO> tpAttachinfoVOS = item.getFileList();
            if (null != tpAttachinfoVOS && tpAttachinfoVOS.size() > 0) {
                // 附件绑定
                tpAttachinfoVOS.forEach(tpAttachinfo -> {
                    tpAttachinfoMapper.updateByAttachId(tpAttachinfo.getAttachId(), referId, updateTime);
                });
            }
        }
    }

    /**
     * 业务数据，附件绑定
     *
     * @param vo:      附件
     * @param referId: 业务id
     * @param pid:     操作人id
     * @return void
     * @author pand
     * @date 2021-03-05 16:24
     */
    @Override
    public void band(TpAttachinfoRefVO vo, String referId, String pid) {

        if (null == vo) {
            return;
        }

        String updateTime = CommonDateUtil.now();

        // 根据业务id和业务附件类型，删除附件
        String referType = vo.getReferType();
        tpAttachinfoMapper.deleteByReferIdAndReferType(referId, referType, pid, updateTime);

        // 重新绑定附件
        List<TpAttachinfoVO> list = vo.getFileList();
        if (null != list && list.size() > 0) {
            // 附件绑定
            list.forEach(tpAttachinfo -> {
                tpAttachinfoMapper.updateByAttachId(tpAttachinfo.getAttachId(), referId, updateTime);
            });
        }
    }

    /**
     * 修改附件的rederId
     *
     * @param attachIds 多个附件id
     * @param referId   业务id
     * @param pid       操作人员id
     * @return void
     * @author 杨占锐
     * @date 2024/6/5 15:29
     */
    @Override
    public void modifyReferId(List<String> attachIds, String referId, String pid) {
        String now = CommonDateUtil.now();
        for (String attachId: attachIds) {
            TpAttachinfo bean = new TpAttachinfo();
            bean.setAttachId(attachId);
            bean.setReferId(referId);
            bean.setUpdateTime(now);
            bean.setUpdator(pid);
            tpAttachinfoMapper.update(bean);
        }
    }

    /**
     * 逻辑删除附件
     *
     * @param attachId 附件id
     * @param pid      操作人员id
     * @return void
     * @author 杨占锐
     * @date 2024/6/5 15:34
     */
    @Override
    public void deleteByAttachId(String attachId, String pid) {

        tpAttachinfoMapper.logicDelete(attachId, pid, CommonDateUtil.now());
    }
    /**
     * 判断附件是否存在 （忽略actived字段）
     *
     * @param attachId
     * @return boolean
     * @author 杨占锐
     * @date 2024/7/22 14:51
     */
    @Override
    public boolean exists(String attachId) {
        String id = tpAttachinfoMapper.exists(attachId);
        return StrUtil.isNotBlank(id);
    }
}
