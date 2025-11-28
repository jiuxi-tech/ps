package com.jiuxi.module.user.app.impl;

import cn.hutool.core.date.DateUtil;
import com.jiuxi.admin.core.bean.entity.*;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonDeptVO;
import com.jiuxi.admin.core.bean.vo.TpPersonExinfoVO;
import com.jiuxi.admin.core.mapper.*;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SmUtils;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.module.user.app.dto.*;
import com.jiuxi.module.user.app.service.UserImportExportService;
import com.jiuxi.shared.common.utils.ExcelUtil;
import com.jiuxi.shared.common.utils.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户导入导出应用服务实现
 * 
 * @author User Import Refactor
 * @date 2025-11-27
 */
@Service
public class UserImportExportServiceImpl implements UserImportExportService {

    private static final Logger logger = LoggerFactory.getLogger(UserImportExportServiceImpl.class);

    @Autowired
    private TpPersonBasicinfoMapper tpPersonBasicinfoMapper;

    @Autowired
    private TpPersonExinfoMapper tpPersonExinfoMapper;

    @Autowired
    private TpAccountMapper tpAccountMapper;

    @Autowired
    private TpDictionaryMapper tpDictionaryMapper;

    @Autowired
    private TpDeptBasicinfoMapper tpDeptBasicinfoMapper;

    @Autowired
    private TpPersonDeptMapper tpPersonDeptMapper;

    /**
     * 导入用户数据
     * 采用全量预验证 + 单事务提交模式
     */
    @Override
    @Transactional(timeout = 300, rollbackFor = Exception.class)
    public ImportResultDTO importUsers(MultipartFile file, String operatorId, String tenantId, String ascnId) {
        logger.info("开始导入用户数据，操作人：{}, 租户：{}", operatorId, tenantId);

        try {
            // 阶段1：读取并解析Excel文件
            List<UserImportDTO> importDataList = ExcelUtil.parseImportExcel(file.getInputStream());
            
            if (importDataList == null || importDataList.isEmpty()) {
                return ImportResultDTO.failure(0, Collections.singletonList(
                    new ImportErrorDTO(null, null, null, "Excel文件无有效数据")
                ));
            }

            int totalRows = importDataList.size();
            logger.info("成功解析Excel文件，共{}行数据", totalRows);

            // 阶段2：全量预校验（不写库）
            ImportResultDTO validationResult = validateAllData(importDataList, tenantId);
            if (!validationResult.getSuccess()) {
                logger.warn("数据校验失败，发现{}个错误", validationResult.getErrors().size());
                return validationResult;
            }

            // 阶段3：数据入库
            importDataToDB(importDataList, operatorId, tenantId, ascnId);

            logger.info("用户数据导入成功，共导入{}条记录", totalRows);
            return ImportResultDTO.success(totalRows);

        } catch (Exception e) {
            logger.error("用户数据导入失败", e);
            throw new RuntimeException("用户数据导入失败：" + e.getMessage(), e);
        }
    }

    /**
     * 阶段2：全量数据预校验
     */
    private ImportResultDTO validateAllData(List<UserImportDTO> dataList, String tenantId) {
        List<ImportErrorDTO> errors = new ArrayList<>();

        // 用于缓存校验过程中的查询结果
        Map<String, String> deptPathToDeptIdMap = new HashMap<>();
        Set<String> existingUsernames = new HashSet<>();
        Set<String> existingIdcards = new HashSet<>();
        Map<String, List<String>> userDeptMap = new HashMap<>(); // 账号 -> 部门ID列表（用于兼职重复检查）
        Map<String, Integer> usernameCountMap = new HashMap<>(); // 统计每个账号在导入数据中出现的次数

        // 第一遍：统计账号出现次数
        for (UserImportDTO dto : dataList) {
            String username = dto.getUsername() != null ? dto.getUsername().trim() : "";
            usernameCountMap.put(username, usernameCountMap.getOrDefault(username, 0) + 1);
        }

        for (UserImportDTO dto : dataList) {
            // 基础格式校验
            validateBasicFormat(dto, errors);
            
            if (errors.stream().anyMatch(e -> e.getRow().equals(dto.getRowNumber()))) {
                // 如果已有错误，跳过后续业务校验
                continue;
            }

            // 部门路径校验
            String deptId = validateDeptPath(dto, tenantId, deptPathToDeptIdMap, errors);
            
            // 账号唯一性校验
            validateUsernameUniqueness(dto, tenantId, existingUsernames, errors);
            
            // 判断是否为兼职场景
            String username = dto.getUsername().trim();
            boolean isPartTime = false;
            
            // 场景1：账号在本次导入中出现多次（兼职）
            if (usernameCountMap.getOrDefault(username, 0) > 1) {
                isPartTime = true;
            }
            
            // 场景2：账号在数据库中已存在（兼职）
            if (!isPartTime) {
                TpAccount existingAccount = tpAccountMapper.selectByUsername(username);
                if (existingAccount != null && (existingAccount.getActived() == null || existingAccount.getActived() == 1)) {
                    isPartTime = true;
                }
            }
            
            // 身份证唯一性校验（兼职时跳过）
            if (!isPartTime) {
                validateIdcardUniqueness(dto, existingIdcards, errors);
            }
            
            // 兼职重复性校验
            if (deptId != null && ValidationUtil.isNotEmpty(dto.getUsername())) {
                validatePartTimeDuplication(dto, deptId, userDeptMap, errors);
            }
        }

        if (errors.isEmpty()) {
            return ImportResultDTO.success(dataList.size());
        } else {
            return ImportResultDTO.failure(dataList.size(), errors);
        }
    }

    /**
     * 基础格式校验
     */
    private void validateBasicFormat(UserImportDTO dto, List<ImportErrorDTO> errors) {
        Integer row = dto.getRowNumber();

        // 必填字段校验
        String error = ValidationUtil.validateUsername(dto.getUsername());
        if (error != null) {
            errors.add(new ImportErrorDTO(row, "账号名", dto.getUsername(), error));
        }

        error = ValidationUtil.validatePassword(dto.getPassword());
        if (error != null) {
            errors.add(new ImportErrorDTO(row, "初始密码", dto.getPassword(), error));
        }

        error = ValidationUtil.validatePersonName(dto.getPersonName());
        if (error != null) {
            errors.add(new ImportErrorDTO(row, "姓名", dto.getPersonName(), error));
        }

        error = ValidationUtil.validateDeptPath(dto.getDeptPath());
        if (error != null) {
            errors.add(new ImportErrorDTO(row, "部门", dto.getDeptPath(), error));
        }

        // 可选字段格式校验
        error = ValidationUtil.validateSex(dto.getSex());
        if (error != null) {
            errors.add(new ImportErrorDTO(row, "性别", dto.getSex(), error));
        }

        error = ValidationUtil.validatePartWorkDate(dto.getPartWorkDate());
        if (error != null) {
            errors.add(new ImportErrorDTO(row, "参加工作时间", dto.getPartWorkDate(), error));
        }

        error = ValidationUtil.validateIdcard(dto.getIdcard());
        if (error != null) {
            errors.add(new ImportErrorDTO(row, "身份证号码", dto.getIdcard(), error));
        }

        error = ValidationUtil.validateRank(dto.getRank());
        if (error != null) {
            errors.add(new ImportErrorDTO(row, "职务职级", dto.getRank(), error));
        }

        error = ValidationUtil.validateTitleName(dto.getTitleName());
        if (error != null) {
            errors.add(new ImportErrorDTO(row, "职称", dto.getTitleName(), error));
        }
    }

    /**
     * 部门路径校验
     * 
     * @return 部门ID（校验通过时）
     */
    private String validateDeptPath(UserImportDTO dto, String tenantId, 
                                    Map<String, String> cacheMap, List<ImportErrorDTO> errors) {
        String deptPath = dto.getDeptPath().trim();
        Integer row = dto.getRowNumber();

        // 先从缓存查找
        if (cacheMap.containsKey(deptPath)) {
            return cacheMap.get(deptPath);
        }

        // 数据库查询
        TpDeptBasicinfoVO dept = tpDeptBasicinfoMapper.selectDeptByName(deptPath);
        
        if (dept == null) {
            errors.add(new ImportErrorDTO(row, "部门", deptPath, "部门路径不存在"));
            return null;
        }

        // 检查部门是否已停用（允许actived为空，视为有效）
        if (dept.getActived() != null && dept.getActived() != 1) {
            errors.add(new ImportErrorDTO(row, "部门", deptPath, "部门已停用"));
            return null;
        }

        // 加入缓存
        cacheMap.put(deptPath, dept.getDeptId());
        return dept.getDeptId();
    }

    /**
     * 账号唯一性校验
     * 注意：允许同一账号在本次导入中多次出现（处理为兼职）
     * - 如果系统中已存在，则本次导入的所有记录都作为兼职部门
     * - 如果系统中不存在，第一次出现作为主部门，其余作为兼职部门
     */
    private void validateUsernameUniqueness(UserImportDTO dto, String tenantId,
                                            Set<String> cache, List<ImportErrorDTO> errors) {
        String username = dto.getUsername().trim();
        Integer row = dto.getRowNumber();

        // 注释原有的重复检查逻辑，允许账号名在本次导入中重复（用于兼职场景）
        // if (cache.contains(username)) {
        //     errors.add(new ImportErrorDTO(row, "账号名", username, "账号名在本次导入中重复"));
        //     return;
        // }

        // 检查数据库中是否已存在
        TpAccount existingAccount = tpAccountMapper.selectByUsername(username);
        if (existingAccount != null && (existingAccount.getActived() == null || existingAccount.getActived() == 1)) {
            // 账号已存在，标记为兼职导入（不报错，后续作为兼职处理）
            logger.debug("账号{}已存在，将作为兼职部门导入", username);
        }

        cache.add(username);
    }

    /**
     * 身份证唯一性校验
     */
    private void validateIdcardUniqueness(UserImportDTO dto, Set<String> cache, List<ImportErrorDTO> errors) {
        if (ValidationUtil.isEmpty(dto.getIdcard())) {
            return; // 身份证非必填
        }

        String idcard = dto.getIdcard().trim().toUpperCase();
        Integer row = dto.getRowNumber();

        // 检查本次导入数据中是否重复
        if (cache.contains(idcard)) {
            errors.add(new ImportErrorDTO(row, "身份证号码", idcard, "身份证号码在本次导入中重复"));
            return;
        }

        // 检查数据库中是否已存在
        TpPersonBasicinfo existingPerson = tpPersonBasicinfoMapper.selectByIdCard(idcard);
        if (existingPerson != null && existingPerson.getActived() == 1) {
            errors.add(new ImportErrorDTO(row, "身份证号码", idcard, "身份证号码已存在"));
            return;
        }

        cache.add(idcard);
    }

    /**
     * 兼职重复性校验
     */
    private void validatePartTimeDuplication(UserImportDTO dto, String deptId,
                                            Map<String, List<String>> userDeptMap, List<ImportErrorDTO> errors) {
        String username = dto.getUsername().trim();
        Integer row = dto.getRowNumber();

        List<String> deptIds = userDeptMap.computeIfAbsent(username, k -> new ArrayList<>());

        // 检查是否在同一部门重复
        if (deptIds.contains(deptId)) {
            errors.add(new ImportErrorDTO(row, "部门", dto.getDeptPath(), 
                "账号[" + username + "]在部门[" + dto.getDeptPath() + "]重复兼职"));
            return;
        }

        // 如果账号已存在，检查是否与已有部门重复
        TpAccount existingAccount = tpAccountMapper.selectByUsername(username);
        if (existingAccount != null && (existingAccount.getActived() == null || existingAccount.getActived() == 1)) {
            // 查询该账号已有的所有部门
            List<TpPersonDeptVO> existingDepts = tpPersonDeptMapper.select(null, existingAccount.getPersonId(), null);
            for (TpPersonDeptVO pd : existingDepts) {
                if (pd.getDeptId().equals(deptId)) {
                    errors.add(new ImportErrorDTO(row, "部门", dto.getDeptPath(), 
                        "账号[" + username + "]已在部门[" + dto.getDeptPath() + "]中"));
                    return;
                }
            }
        }

        deptIds.add(deptId);
    }

    /**
     * 阶段3：数据入库
     */
    private void importDataToDB(List<UserImportDTO> dataList, String operatorId, String tenantId, String ascnId) {
        String now = CommonDateUtil.now();

        // 按账号分组（同一账号的多条数据表示兼职）
        Map<String, List<UserImportDTO>> userGroups = dataList.stream()
            .collect(Collectors.groupingBy(dto -> dto.getUsername().trim()));

        for (Map.Entry<String, List<UserImportDTO>> entry : userGroups.entrySet()) {
            String username = entry.getKey();
            List<UserImportDTO> userDataList = entry.getValue();

            // 检查账号是否已存在
            TpAccount existingAccount = tpAccountMapper.selectByUsername(username);
            
            if (existingAccount == null) {
                // 新用户：创建人员、账号、部门关系
                createNewUser(userDataList, operatorId, tenantId, ascnId, now);
            } else {
                // 已有用户：仅添加兼职部门
                addPartTimeDepts(existingAccount.getPersonId(), userDataList, now);
            }
        }
    }

    /**
     * 创建新用户
     */
    private void createNewUser(List<UserImportDTO> userDataList, String operatorId, 
                               String tenantId, String ascnId, String now) {
        // 使用第一条数据的信息创建人员和账号
        UserImportDTO firstData = userDataList.get(0);
        
        String personId = SnowflakeIdUtil.nextIdStr();
        String accountId = SnowflakeIdUtil.nextIdStr();

        // 1. 创建人员基本信息
        TpPersonBasicinfo person = new TpPersonBasicinfo();
        person.setPersonId(personId);
        person.setPersonName(firstData.getPersonName().trim());
        person.setSex(parseSex(firstData.getSex()));
        person.setIdcard(ValidationUtil.isNotEmpty(firstData.getIdcard()) ? firstData.getIdcard().trim() : null);
        person.setOffice(ValidationUtil.isNotEmpty(firstData.getRank()) ? firstData.getRank().trim() : null);
        person.setAscnId(ascnId);
        person.setActived(1);
        person.setCategory(0); // 默认政府人员
        person.setCreator(operatorId);
        person.setCreateTime(now);
        person.setUpdator(operatorId);
        person.setUpdateTime(now);
        person.setTenantId(tenantId);

        tpPersonBasicinfoMapper.save(person);

        // 2. 创建人员扩展信息
        TpPersonExinfo exinfo = new TpPersonExinfo();
        exinfo.setPersonId(personId);
        // 将 YYYY-MM-DD 格式转换为 YYYYMMDD 格式存储
        exinfo.setPartWorkDate(ValidationUtil.formatDateToYYYYMMDD(firstData.getPartWorkDate()));
        // 新字段：职务职级、职称
        exinfo.setZwzj(ValidationUtil.isNotEmpty(firstData.getZwzj()) ? firstData.getZwzj().trim() : null);
        exinfo.setZhicheng(ValidationUtil.isNotEmpty(firstData.getZhicheng()) ? firstData.getZhicheng().trim() : null);
        exinfo.setTenantId(tenantId);
        
        tpPersonExinfoMapper.save(exinfo);

        // 3. 创建账号
        TpAccount account = new TpAccount();
        account.setAccountId(accountId);
        account.setUsername(firstData.getUsername().trim());
        account.setUserpwd(SmUtils.digestHexSM3(firstData.getPassword().trim())); // SM3加密
        account.setPersonId(personId);
        account.setLocked(0); // 未冻结
        account.setEnabled(1); // 启用
        account.setActived(1); // 有效
        account.setCreateTime(now);
        account.setUpdateTime(now);
        account.setTenantId(tenantId);
        // 设置密码修改时间为导入时间，避免密码过期检查失败
        account.setLastPasswordChangeTime(now);

        tpAccountMapper.insert(account);

        // 4. 创建人员部门关系（第一个为主部门，其余为兼职部门）
        for (int i = 0; i < userDataList.size(); i++) {
            UserImportDTO dto = userDataList.get(i);
            String deptId = getDeptIdByPath(dto.getDeptPath().trim());

            TpPersonDept personDept = new TpPersonDept();
            personDept.setPersonId(personId);
            personDept.setDeptId(deptId);
            personDept.setDefaultDept(i == 0 ? 1 : 0); // 第一个为主部门

            tpPersonDeptMapper.save(personDept);
        }

        logger.debug("成功创建新用户：{}", firstData.getUsername());
    }

    /**
     * 为已有用户添加兼职部门
     */
    private void addPartTimeDepts(String personId, List<UserImportDTO> userDataList, String now) {
        for (UserImportDTO dto : userDataList) {
            String deptId = getDeptIdByPath(dto.getDeptPath().trim());

            TpPersonDept personDept = new TpPersonDept();
            personDept.setPersonId(personId);
            personDept.setDeptId(deptId);
            personDept.setDefaultDept(0); // 所有导入的都是兼职部门

            tpPersonDeptMapper.save(personDept);
        }

        logger.debug("为用户{}添加{}个兼职部门", personId, userDataList.size());
    }

    /**
     * 根据部门路径获取部门ID
     */
    private String getDeptIdByPath(String deptPath) {
        TpDeptBasicinfoVO dept = tpDeptBasicinfoMapper.selectDeptByName(deptPath);
        return dept != null ? dept.getDeptId() : null;
    }

    /**
     * 性别文本转数字
     */
    private Integer parseSex(String sex) {
        if (ValidationUtil.isEmpty(sex)) {
            return 0; // 保密
        }
        sex = sex.trim();
        if ("男".equals(sex)) {
            return 1;
        } else if ("女".equals(sex)) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public void exportUsers(String deptId, String deptLevelcode, List<String> selectedUserIds, 
                           String tenantId, OutputStream outputStream) throws Exception {
        logger.info("开始导出用户数据 - 部门ID：{}, 层级码：{}", deptId, deptLevelcode);

        // 查询用户数据
        List<UserExportDTO> exportDataList = queryUsersForExport(deptId, deptLevelcode, selectedUserIds, tenantId);
        
        if (exportDataList.isEmpty()) {
            logger.warn("没有可导出的用户数据");
            throw new RuntimeException("没有可导出的用户数据");
        }

        logger.info("查询到{}条用户数据，开始写入Excel", exportDataList.size());
        
        // 写入Excel文件
        ExcelUtil.writeExportExcel(exportDataList, outputStream);
        
        logger.info("用户数据导出成功");
    }

    /**
     * 查询待导出的用户数据
     */
    private List<UserExportDTO> queryUsersForExport(String deptId, String deptLevelcode, 
                                                     List<String> selectedUserIds, String tenantId) {
        List<UserExportDTO> resultList = new ArrayList<>();
        
        try {
            List<TpPersonBasicinfoVO> personList = new ArrayList<>();
            
            // 1. 如果selectedUserIds不为空，按ID查询指定用户
            if (selectedUserIds != null && !selectedUserIds.isEmpty()) {
                for (String userId : selectedUserIds) {
                    TpPersonBasicinfoVO person = tpPersonBasicinfoMapper.view(userId);
                    if (person != null && person.getActived() == 1) {
                        personList.add(person);
                    }
                }
            } else {
                // 2. 查询部门及子部门的所有用户
                // 构造查询条件
                com.jiuxi.admin.core.bean.query.TpPersonBasicQuery query = 
                    new com.jiuxi.admin.core.bean.query.TpPersonBasicQuery();
                query.setDeptId(deptId);
                query.setDeptLevelcode(deptLevelcode);
                query.setCurrent(1);
                query.setSize(Integer.MAX_VALUE);
                
                // 查询分页数据
                com.baomidou.mybatisplus.core.metadata.IPage<TpPersonBasicinfoVO> page = 
                    tpPersonBasicinfoMapper.getPage(
                        new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, Integer.MAX_VALUE),
                        query
                    );
                personList = page.getRecords();
            }
            
            // 3. 转换为UserExportDTO
            for (TpPersonBasicinfoVO person : personList) {
                UserExportDTO dto = new UserExportDTO();
                
                // 基本信息
                dto.setPersonName(person.getPersonName());
                dto.setSexName(convertSexToName(person.getSex()));
                dto.setIdcard(person.getIdcard());
                
                // 查询账号信息
                TpAccount account = tpAccountMapper.selectByPersonId(person.getPersonId());
                if (account != null) {
                    dto.setUsername(account.getUsername());
                }
                
                // 查询部门信息：查询用户的主部门（DEFAULT_DEPT=1）
                List<TpPersonDeptVO> personDepts = tpPersonDeptMapper.select(null, person.getPersonId(), 1);
                if (personDepts != null && !personDepts.isEmpty()) {
                    // 获取主部门ID
                    String personDeptId = personDepts.get(0).getDeptId();
                    TpDeptBasicinfoVO dept = tpDeptBasicinfoMapper.selectDeptById(personDeptId);
                    if (dept != null) {
                        // 直接使用DEPT_FULL_NAME
                        dto.setDeptFullPath(dept.getDeptFullName());
                    }
                }
                
                // 查询扩展信息
                TpPersonExinfoVO exinfo = tpPersonExinfoMapper.view(person.getPersonId());
                if (exinfo != null) {
                    // 将 YYYYMMDD 格式转换为 YYYY-MM-DD 格式导出
                    dto.setPartWorkDate(ValidationUtil.formatDateToYYYYMMDD_Hyphen(exinfo.getPartWorkDate()));
                    
                    // 新字段：职务职级、职称
                    dto.setZwzj(exinfo.getZwzj());
                    dto.setZhicheng(exinfo.getZhicheng());
                }
                
                resultList.add(dto);
            }
            
            logger.info("查询用户数据完成，共{}条", resultList.size());
            return resultList;
            
        } catch (Exception e) {
            logger.error("查询用户数据失败", e);
            throw new RuntimeException("查询用户数据失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 性别数字转文本
     */
    private String convertSexToName(Integer sex) {
        if (sex == null) {
            return "";
        }
        switch (sex) {
            case 1:
                return "男";
            case 2:
                return "女";
            default:
                return "";
        }
    }

    @Override
    public void downloadTemplate(OutputStream outputStream) throws Exception {
        ExcelUtil.generateImportTemplate(outputStream);
    }

    /**
     * 通过职称名称查询字典CODE
     * 
     * @param titleName 职称名称
     * @return 职称编码，找不到返回null
     */
    private String findTitleCodeByName(String titleName) {
        try {
            // 查询SYS12职称字典下的所有选项
            List<TreeNode> titleDict = tpDictionaryMapper.treeByDicCode("SYS12");
            if (titleDict != null && !titleDict.isEmpty()) {
                for (TreeNode node : titleDict) {
                    if (titleName.equals(node.getLabel())) {
                        return node.getValue();
                    }
                }
            }
            logger.warn("职称[{}]在字典中找不到对应的CODE", titleName);
            return null;
        } catch (Exception e) {
            logger.error("查询职称字典失败", e);
            return null;
        }
    }
}
