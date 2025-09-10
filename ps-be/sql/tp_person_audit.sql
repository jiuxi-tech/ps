-- 个人信息修改关键字段审核表
CREATE TABLE tp_person_audit (
    audit_id varchar(36) PRIMARY KEY  NOT NULL COMMENT '申请ID, 唯一标识ID，主键',

    person_id BIGINT(20) NOT NULL COMMENT '人员ID',
    person_name VARCHAR(100) NOT NULL COMMENT '人员姓名',
    person_phone VARCHAR(20) COMMENT '人员手机号',
    dept_id BIGINT(20) COMMENT '人员部门ID',
    dept_name VARCHAR(200) COMMENT '人员部门名称',
    
    original_data TEXT COMMENT '修改前数据(JSON格式)',
    modified_data TEXT COMMENT '修改后数据(JSON格式)',
    
    changed_fields VARCHAR(500) COMMENT '变更字段列表，逗号分隔',
    
    audit_status TINYINT(4) NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝，3-已取消',
  
    audit_reason VARCHAR(500) COMMENT '审核理由',
    audit_time DATETIME COMMENT '审核时间',
      
    auditor_id BIGINT(20) COMMENT '审核人(管理)ID',
    auditor_name VARCHAR(100) COMMENT '审核人(管理)姓名', 
    
    submit_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_person_id (person_id),
    INDEX idx_audit_status (audit_status),
    INDEX idx_submit_time (submit_time),
    INDEX idx_auditor_id (auditor_id),
    INDEX idx_dept_id (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户个人信息修改审核表';
 