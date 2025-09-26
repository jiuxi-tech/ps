---
trigger: model_decision
description: 需要了解数据库表结构的时候
---
-- MySQL dump 10.13  Distrib 5.7.26, for Win64 (x86_64)
--
-- Host: alilaoba.cn    Database: ps-bmp
-- ------------------------------------------------------
-- Server version	5.5.5-10.3.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `ps-bmp`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `ps-bmp` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `ps-bmp`;

--
-- Table structure for table `ai_person_chat`
--

DROP TABLE IF EXISTS `ai_person_chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ai_person_chat` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `person_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `app_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chat_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `first_question` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `start_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actived` int(11) DEFAULT NULL,
  `creator` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updator` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `top` int(11) DEFAULT NULL,
  `top_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ai_person_chat_child`
--

DROP TABLE IF EXISTS `ai_person_chat_child`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ai_person_chat_child` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parent_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prompt` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `question` varchar(1500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `q_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `think` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `model_answer` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `answer` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `a_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actived` int(1) DEFAULT NULL,
  `creator` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updator` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `liked` int(11) DEFAULT NULL,
  `liked_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `top` int(1) DEFAULT NULL,
  `top_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `c_json_222`
--

DROP TABLE IF EXISTS `c_json_222`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_json_222` (
  `id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `refer_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_detail` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `CREATOR` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `org_tree_change_history`
--

DROP TABLE IF EXISTS `org_tree_change_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_tree_change_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` bigint(20) DEFAULT NULL COMMENT '版本号',
  `operation_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型：CREATE, UPDATE, DELETE, MOVE',
  `operation_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人姓名',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `dept_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父部门ID',
  `old_parent_id` bigint(20) DEFAULT NULL COMMENT '原父部门ID（移动操作时使用）',
  `dept_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门编码',
  `old_dept_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原部门编码（更新操作时使用）',
  `dept_level` int(11) DEFAULT NULL COMMENT '部门层级',
  `old_dept_level` int(11) DEFAULT NULL COMMENT '原部门层级（移动操作时使用）',
  `sort_order` int(11) DEFAULT NULL COMMENT '排序号',
  `old_sort_order` int(11) DEFAULT NULL COMMENT '原排序号（更新操作时使用）',
  `description` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更描述',
  `before_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '变更前数据（JSON格式）',
  `after_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '变更后数据（JSON格式）',
  `before_full_tree` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更前的完整组织机构节点树JSON',
  `after_full_tree` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更后的完整组织机构节点树JSON',
  `created_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '记录创建时间',
  `updated_time` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_version` (`version`),
  KEY `idx_operation_time` (`operation_time`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织机构树变更历史记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `org_tree_version_sequence`
--

DROP TABLE IF EXISTS `org_tree_version_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_tree_version_sequence` (
  `id` int(11) NOT NULL DEFAULT 1,
  `current_version` bigint(20) NOT NULL DEFAULT 0,
  `updated_time` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织机构版本号序列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `s_hd_hiddendangerinfo`
--

DROP TABLE IF EXISTS `s_hd_hiddendangerinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_hd_hiddendangerinfo` (
  `hd_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_item_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chktempl_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ent_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ent_full_name` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_desc` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_level` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_location` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_source` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `neate_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `neaten_limit_date` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `checker_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prod_addr_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `industry_type_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `domain` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_kind` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_ascn_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_dept_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `checkdept_city_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hire_unit_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tenantry_ent_id` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `neaten_situation` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `neaten_comp_date` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `neaten_principal_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `neaten_measure` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recheck_result` tinyint(1) DEFAULT NULL,
  `recheck_info` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recheck_psn_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recheck_date` char(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_supervise_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `riskpoint_id` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `riskpoint_name` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `neaten_amount` decimal(12,2) DEFAULT NULL,
  `multiple_rent` tinyint(1) DEFAULT NULL,
  `actived` tinyint(1) DEFAULT NULL,
  `creator` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updator` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `src_update_timestamp` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `src_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_source` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_class` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_ascn_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_level_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_source_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_type_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_class_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `neate_type_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prod_addr_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `industry_type_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `domain_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `s_si_check_rut`
--

DROP TABLE IF EXISTS `s_si_check_rut`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_si_check_rut` (
  `check_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_kind` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chktempl_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ent_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ent_full_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prod_addr_code` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `industry_type_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `domain` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_psn_name` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_locale` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_desc` varchar(3000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_ascn_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_ascn_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_dept_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_dept_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hire_unit_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tenantry_ent_id` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ent_longitude` decimal(18,10) DEFAULT NULL,
  `ent_latitude` decimal(18,10) DEFAULT NULL,
  `data_source` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hd_num` int(11) DEFAULT NULL,
  `greathd_num` int(11) DEFAULT NULL,
  `bighd_num` int(11) DEFAULT NULL,
  `simplehd_num` int(11) DEFAULT NULL,
  `recheck_date` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recheck_psn_name` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recheck_info` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `check_status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actived` tinyint(1) DEFAULT NULL,
  `creator` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updator` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contract_id` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contract_no` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `src_update_timestamp` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SRC_ID` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_id` char(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_kind` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_member` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `group_no` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_account`
--

DROP TABLE IF EXISTS `tp_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_account` (
  `ACCOUNT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `USERNAME` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `USERPWD` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PHONE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXPIRED_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LOCKED` int(11) DEFAULT NULL,
  `ENABLED` int(11) DEFAULT NULL,
  `PERSON_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `WEIXIN` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DINGDING` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ZHELIBAN` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `THREE_ID` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `keycloak_id` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `VERIFICATION_CODE` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_password_change_time` datetime DEFAULT NULL COMMENT '上次密码修改时间',
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `ACCOUNT_ID` (`ACCOUNT_ID`),
  KEY `idx_last_password_change_time` (`last_password_change_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_account_exinfo`
--

DROP TABLE IF EXISTS `tp_account_exinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_account_exinfo` (
  `ACCOUNT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ERR_COUNT` int(11) DEFAULT NULL,
  `LAST_ERR_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LAST_LOGIN_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_account_third`
--

DROP TABLE IF EXISTS `tp_account_third`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_account_third` (
  `APP_KEY` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `APP_SECRET` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PARTNER_NAME` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PARTNER_DESC` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_agent`
--

DROP TABLE IF EXISTS `tp_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_agent` (
  `AG_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_TYPE` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_SCOPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_FROM` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_FROM_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_TO` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_TO_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_CONTEXT` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MODULE_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MODULE_TYPE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_agent_deal`
--

DROP TABLE IF EXISTS `tp_agent_deal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_agent_deal` (
  `PERSON_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DEAL_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_TYPE` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_SCOPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_FROM` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_FROM_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_TO` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_TO_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AG_CONTEXT` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MODULE_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MODULE_TYPE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_attachinfo`
--

DROP TABLE IF EXISTS `tp_attachinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_attachinfo` (
  `ATTACH_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ATTACH_TITLE` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ATTACH_NAME` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `REFER_TYPE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `REFER_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ATTACH_TYPE_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SAVE_PATH` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SAVE_PATH_BAK1` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SAVE_PATH_BAK2` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ATTACH_SIZE` decimal(12,0) DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `ORDER_INDEX` double(10,2) DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `ATTACH_ID` (`ATTACH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_city`
--

DROP TABLE IF EXISTS `tp_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_city` (
  `CITY_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CITY_CODE` varchar(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CITY_NAME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CITY_FULL_NAME` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CITY_SIMPLE_NAME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PCITY_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `STAT_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ORDER_INDEX` double(10,2) DEFAULT NULL,
  `CITY_LEVEL` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENABLED` int(11) DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND04` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND05` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `CITY_ID` (`CITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_custom_form`
--

DROP TABLE IF EXISTS `tp_custom_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_custom_form` (
  `FID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `F_SOURCE` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `FTYPE` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `FJSON` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_custom_module`
--

DROP TABLE IF EXISTS `tp_custom_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_custom_module` (
  `MID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MNAME` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MCODE` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MDESC` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_data_permission_dept`
--

DROP TABLE IF EXISTS `tp_data_permission_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_data_permission_dept` (
  `id` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '物理主键',
  `perm_id` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '数据权限id',
  `dept_id` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '指定的部门id',
  `tenant_id` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_perm_dept_id` (`perm_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='数据权限部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_data_permission_scope`
--

DROP TABLE IF EXISTS `tp_data_permission_scope`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_data_permission_scope` (
  `perm_id` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '物理主键',
  `person_id` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '人员id',
  `dept_id` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '授权时部门id',
  `data_scope` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '数据范围',
  `tenant_id` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户id',
  `creator` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(14) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  `updator` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
  `update_time` varchar(14) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改时间',
  `extend01` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段01',
  `extend02` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段02',
  `extend03` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段03',
  PRIMARY KEY (`perm_id`),
  KEY `idx_psn_dept_id` (`person_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='数据权限范围表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_data_permissions`
--

DROP TABLE IF EXISTS `tp_data_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_data_permissions` (
  `PER_ID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `PERSON_ID` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员ID',
  `DEPT_ID` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门ID',
  `ASCN_ID` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单位ID',
  `EXTEND_01` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用字段1',
  `EXTEND_02` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用字段2',
  `EXTEND_03` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用字段3',
  PRIMARY KEY (`PER_ID`),
  KEY `idx_person_id` (`PERSON_ID`),
  KEY `idx_dept_id` (`DEPT_ID`),
  KEY `idx_ascn_id` (`ASCN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='人员数据权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_database_backup_log`
--

DROP TABLE IF EXISTS `tp_database_backup_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_database_backup_log` (
  `backup_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '备份记录ID',
  `backup_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '备份名称',
  `backup_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '备份类型 1:自动备份 2:手动备份',
  `backup_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '备份状态 1:进行中 2:成功 3:失败',
  `database_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '备份的数据库名称',
  `backup_file_path` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备份文件存储路径',
  `backup_file_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备份文件名称',
  `backup_file_size` bigint(20) DEFAULT NULL COMMENT '备份文件大小(字节)',
  `backup_start_time` datetime NOT NULL COMMENT '备份开始时间',
  `backup_end_time` datetime DEFAULT NULL COMMENT '备份结束时间',
  `backup_duration` int(11) DEFAULT NULL COMMENT '备份耗时(秒)',
  `backup_command` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '执行的备份命令',
  `error_message` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误信息',
  `actived` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效 1:有效 0:无效',
  `tenant_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `creator` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updator` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '修改时间',
  `extend01` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段01',
  `extend02` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段02',
  `extend03` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段03',
  PRIMARY KEY (`backup_id`),
  KEY `idx_backup_status` (`backup_status`),
  KEY `idx_backup_type` (`backup_type`),
  KEY `idx_backup_start_time` (`backup_start_time`),
  KEY `idx_database_name` (`database_name`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_actived` (`actived`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据库备份记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_dept_basicinfo`
--

DROP TABLE IF EXISTS `tp_dept_basicinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_dept_basicinfo` (
  `DEPT_ID` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门ID，主键，唯一标识部门记录',
  `PDEPT_ID` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父部门ID，用于构建部门层级树形结构，根部门为NULL',
  `DEPT_LEVELCODE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门层级编码，三位数字递增自动生成，用于统计本级及下级部门',
  `DEPT_NO` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门编号，部门的唯一业务编码标识',
  `DEPT_FULL_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门全称，部门的完整正式名称',
  `DEPT_SIMPLE_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门简称，部门的简化显示名称',
  `DEPT_TYPE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门类型，单位/部门/网格/其他，用于区分不同性质的组织单元',
  `DEPT_DESC` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门简介，部门职能和业务范围的详细描述',
  `ORDER_INDEX` double DEFAULT NULL COMMENT '部门排序，用于控制部门在列表中的显示顺序，数值越小越靠前',
  `CATEGORY` int(11) DEFAULT NULL COMMENT '部门类别，0-政府部门，1-企业部门，2-其他类型',
  `CITY_CODE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '行政区划代码，标识部门所属的行政区域',
  `PRINCIPAL_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负责人姓名，部门主要负责人的姓名',
  `PRINCIPAL_TEL` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负责人电话，部门主要负责人的联系电话',
  `ASCN_ID` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属机构ID，政府存机构ID，企业存单位ID，分公司存所在分公司单位ID',
  `ENABLED` int(11) DEFAULT NULL COMMENT '是否启用，1-启用，0-禁用，控制部门是否可用',
  `ACTIVED` int(11) DEFAULT NULL COMMENT '是否有效，1-有效，0-无效，标识部门记录的有效性状态',
  `CREATOR` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID，记录创建该部门信息的用户标识',
  `CREATE_TIME` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建时间，部门信息首次创建的时间戳',
  `UPDATOR` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人ID，记录最后修改该部门信息的用户标识',
  `UPDATE_TIME` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改时间，部门信息最后修改的时间戳',
  `TENANT_ID` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID，多租户系统中标识数据所属租户',
  `EXTEND01` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段01，预留的业务扩展字段，可根据具体需求使用',
  `EXTEND02` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段02，预留的业务扩展字段，可根据具体需求使用',
  `EXTEND03` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段03，预留的业务扩展字段，可根据具体需求使用',
  `EXTEND04` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段04，预留的业务扩展字段，可根据具体需求使用',
  `EXTEND05` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段05，预留的业务扩展字段，可根据具体需求使用',
  `LEAF` int(11) DEFAULT NULL COMMENT '是否叶子节点，1-叶子节点（无下级），0-非叶子节点（有下级），用于树形结构优化',
  UNIQUE KEY `DEPT_ID` (`DEPT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_dept_exinfo`
--

DROP TABLE IF EXISTS `tp_dept_exinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_dept_exinfo` (
  `DEPT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SUPDEPT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LINE_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LONGITUDE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LATITUDE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GEO_CODE` varchar(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ADDRESS` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PRINCIPAL_NAME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PRINCIPAL_TEL` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EMAIL` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DIREIN_DUSTRY_CODE` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DIREIN_DUSTRY_NAME` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND04` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND05` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_dictionary`
--

DROP TABLE IF EXISTS `tp_dictionary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_dictionary` (
  `DIC_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DIC_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DIC_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DIC_TYPE` int(11) DEFAULT NULL,
  `PDIC_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DIC_DESC` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ORDER_INDEX` double(10,2) DEFAULT NULL,
  `ENABLED` int(11) DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATEOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATEOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `DIC_ID` (`DIC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_ent_basicinfo`
--

DROP TABLE IF EXISTS `tp_ent_basicinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_ent_basicinfo` (
  `ENT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENT_FULL_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENT_SIMPLE_NAME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENT_UNIFIED_CODE` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENT_TYPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENT_DESC` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LEGAL_REPR` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LEGAL_REPR_TEL` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LINK_PSN_NAME` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LINK_PSN_TEL` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `REG_FUND` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENT_ADDR_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENT_ADDR` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LONGITUDE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LATITUDE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GEO_CODE` varchar(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PROD_ADDR_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PROD_ADDR` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `INDUSTRY_TYPE_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LINE_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SCALE_TYPE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENABLED` int(11) DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend04` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend05` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CERT_CODE_TYPE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SAFETY_DIRECTOR_NAME` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SAFETY_DIRECTOR_TEL` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ent_domain` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ent_label` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `production_mode` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `administrative_sub_ordination` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `registration_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `op_scope` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `ENT_ID` (`ENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_ip_access_log`
--

DROP TABLE IF EXISTS `tp_ip_access_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_ip_access_log` (
  `LOG_ID` varchar(32) NOT NULL COMMENT '日志ID',
  `CLIENT_IP` varchar(50) NOT NULL COMMENT '客户端IP地址',
  `ACCESS_TIME` datetime NOT NULL COMMENT '访问时间',
  `ACCESS_RESULT` varchar(20) NOT NULL COMMENT '访问结果(ALLOWED:允许, DENIED:拒绝)',
  `DENY_REASON` varchar(100) DEFAULT NULL COMMENT '拒绝原因(BLACKLIST:黑名单, NOT_IN_WHITELIST:不在白名单)',
  `REQUEST_URI` varchar(500) DEFAULT NULL COMMENT '请求URI',
  `REQUEST_METHOD` varchar(10) DEFAULT NULL COMMENT '请求方法(GET,POST等)',
  `USER_AGENT` varchar(1000) DEFAULT NULL COMMENT '用户代理信息',
  `USERNAME` varchar(100) DEFAULT NULL COMMENT '用户名(如果已登录)',
  `MATCHED_RULE` varchar(200) DEFAULT NULL COMMENT '匹配的规则(具体的IP规则)',
  `RULE_TYPE` varchar(20) DEFAULT NULL COMMENT '规则类型(WHITELIST:白名单, BLACKLIST:黑名单)',
  `CITY_CODE` varchar(20) DEFAULT NULL COMMENT '城市代码',
  `ACTIVED` varchar(1) DEFAULT '1' COMMENT '是否激活(1:激活 0:未激活)',
  `CREATE_TIME` datetime DEFAULT current_timestamp() COMMENT '创建时间',
  `EXTEND01` varchar(200) DEFAULT NULL COMMENT '扩展字段1',
  `EXTEND02` varchar(200) DEFAULT NULL COMMENT '扩展字段2',
  `EXTEND03` varchar(200) DEFAULT NULL COMMENT '扩展字段3',
  PRIMARY KEY (`LOG_ID`),
  KEY `idx_client_ip` (`CLIENT_IP`),
  KEY `idx_access_time` (`ACCESS_TIME`),
  KEY `idx_access_result` (`ACCESS_RESULT`),
  KEY `idx_username` (`USERNAME`),
  KEY `idx_city_code` (`CITY_CODE`),
  KEY `idx_ip_time` (`CLIENT_IP`,`ACCESS_TIME`),
  KEY `idx_result_time` (`ACCESS_RESULT`,`ACCESS_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IP访问控制日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_keycloak_account`
--

DROP TABLE IF EXISTS `tp_keycloak_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_keycloak_account` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `account_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联的账号ID（tp_account.ACCOUNT_ID）',
  `kc_client_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Keycloak 客户端ID',
  `kc_client_secret` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Keycloak 客户端密钥（加密存储）',
  `kc_grant_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'password' COMMENT 'OAuth2 授权类型（password/authorization_code/client_credentials等）',
  `kc_username` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Keycloak 用户名',
  `kc_user_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Keycloak用户ID',
  `kc_password` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Keycloak 密码（加密存储）',
  `kc_refresh_token` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Keycloak 刷新令牌',
  `kc_access_token` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Keycloak 访问令牌',
  `kc_token_expires_at` datetime DEFAULT NULL COMMENT '访问令牌过期时间',
  `kc_refresh_expires_at` datetime DEFAULT NULL COMMENT '刷新令牌过期时间',
  `kc_post_logout_redirect_uri` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登出后重定向URI',
  `kc_realm` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT 'master' COMMENT 'Keycloak Realm名称',
  `kc_server_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Keycloak 服务器地址',
  `enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用（0:禁用, 1:启用）',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_token_refresh_time` datetime DEFAULT NULL COMMENT '最后令牌刷新时间',
  `create_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建时间（YYYYMMDDHHMMSS）',
  `update_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新时间（YYYYMMDDHHMMSS）',
  `creator` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `updater` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `tenant_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID（多租户支持）',
  `extend01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段1',
  `extend02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段2',
  `extend03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段3',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account_id` (`account_id`),
  KEY `idx_kc_client_id` (`kc_client_id`),
  KEY `idx_kc_username` (`kc_username`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_enabled` (`enabled`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_tp_keycloak_account_account_id` FOREIGN KEY (`account_id`) REFERENCES `tp_account` (`ACCOUNT_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Keycloak账号关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_licence`
--

DROP TABLE IF EXISTS `tp_licence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_licence` (
  `LICENCE_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXPIRING_DATE` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SYSTEM_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HASH_CODE` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `LICENCE_ID` (`LICENCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_mediorg`
--

DROP TABLE IF EXISTS `tp_mediorg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_mediorg` (
  `MEDIORG_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MEDIORG_FULL_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MEDIORG_SIMPLE_NAME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MEDIORG_TYPE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MEDIORG_UNIFIED_CODE` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CITY_CODE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `REG_ADDR` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ORG_ADDR` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ESTABLISH_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MEDIORG_DESC` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LEGAL_REPR` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LEGAL_REPR_TEL` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LINK_PSN_NAME` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LINK_PSN_TEL` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `REG_FUND` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LONGITUDE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LATITUDE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GEO_CODE` varchar(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SERVICE_AREA` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SERVICE_TYPE` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SERVICE_INDUSTRY` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `INFORMATION_PUB_WEB` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SCALE_TYPE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENABLED` int(11) DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `MEDIORG_ID` (`MEDIORG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_mem_verification_code`
--

DROP TABLE IF EXISTS `tp_mem_verification_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_mem_verification_code` (
  `CODE_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `BUS_TYPE` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `VERIFICATION_CODE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SEND_TIME_STAMP` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PHONE` varchar(12) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_menu`
--

DROP TABLE IF EXISTS `tp_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_menu` (
  `MENU_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MENU_NAME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MENU_CODE` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MENU_URI` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MENU_PID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `menu_tree_pid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MENU_SOURCE` int(11) DEFAULT NULL,
  `MENU_TYPE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MENU_ICON` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MENU_DESC` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ORDER_INDEX` double(10,2) DEFAULT NULL,
  `ENABLED` int(11) DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LEAF` int(11) DEFAULT NULL,
  UNIQUE KEY `MENU_ID` (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_menu_history`
--

DROP TABLE IF EXISTS `tp_menu_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_menu_history` (
  `HISTORY_ID` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '历史记录ID（主键）',
  `MENU_ID` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单ID',
  `MENU_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单名称',
  `OPERATION_TYPE` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型（INSERT：新增，UPDATE：修改，DELETE：删除）',
  `NODE_DATA_BEFORE` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '节点修改前数据（JSON格式）',
  `NODE_DATA_AFTER` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '节点修改后数据（JSON格式）',
  `FULL_TREE_BEFORE` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '全表修改前数据（JSON格式）',
  `FULL_TREE_AFTER` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '全表修改后数据（JSON格式）',
  `OPERATOR_ID` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作人ID',
  `OPERATOR_NAME` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作人姓名',
  `OPERATION_TIME` varchar(14) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作时间（yyyyMMddHHmmss）',
  `TENANT_ID` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `ASCN_ID` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属机构ID',
  `ACTIVED` int(11) DEFAULT 1 COMMENT '是否有效（1：有效，0：无效）',
  `CREATOR` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建时间（yyyyMMddHHmmss）',
  `UPDATOR` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改时间（yyyyMMddHHmmss）',
  `EXTEND01` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段01',
  `EXTEND02` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段02',
  `EXTEND03` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段03',
  PRIMARY KEY (`HISTORY_ID`),
  KEY `idx_menu_id` (`MENU_ID`),
  KEY `idx_operation_type` (`OPERATION_TYPE`),
  KEY `idx_operation_time` (`OPERATION_TIME`),
  KEY `idx_operator_id` (`OPERATOR_ID`),
  KEY `idx_tenant_id` (`TENANT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单修改历史记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_message`
--

DROP TABLE IF EXISTS `tp_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_message` (
  `MSG_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_TYPE` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_SCOPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_GROUP` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_FROM` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_FROM_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_TO` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_TO_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LEVELCODE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_CONTEXT` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_message_read`
--

DROP TABLE IF EXISTS `tp_message_read`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_message_read` (
  `PERSON_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `READ_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_TYPE` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_SCOPE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_GROUP` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_FROM` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_FROM_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_TO` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_TO_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LEVELCODE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MSG_CONTEXT` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_monitor_client`
--

DROP TABLE IF EXISTS `tp_monitor_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_monitor_client` (
  `client_id` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '客户端id',
  `application_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统名称',
  `mac_addr` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'mac地址',
  `absolute_path` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统部署的绝对路径',
  `system_desc` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统描述',
  `ip` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ip链路',
  `remark` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `system_url` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统访问地址',
  `tenant_id` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户id',
  `actived` int(11) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `creator` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(14) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  `updator` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
  `update_time` varchar(14) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改时间',
  `extend01` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段01',
  `extend02` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段02',
  `extend03` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段03',
  `extend04` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段04',
  `extend05` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段05',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='客户端基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_monitor_config`
--

DROP TABLE IF EXISTS `tp_monitor_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_monitor_config` (
  `config_id` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `cpu_threshold` decimal(12,2) DEFAULT NULL COMMENT 'cpu报警阈值',
  `memory_threshold` decimal(12,2) DEFAULT NULL COMMENT '内存报警阈值',
  `disk_threshold` decimal(12,2) DEFAULT NULL COMMENT '磁盘报警阈值',
  `send_mail` int(11) DEFAULT 0 COMMENT '是否发送邮件（1：是，0：否）',
  `principal` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统负责人（多个逗号分隔）',
  `mobile` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '负责人手机号',
  `email` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '负责人邮箱（多个逗号分隔）',
  `tenant_id` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户id',
  `actived` int(11) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `creator` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(14) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  `updator` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
  `update_time` varchar(14) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改时间',
  `extend01` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段01',
  `extend02` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段02',
  `extend03` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段03',
  `extend04` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段04',
  `extend05` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段05',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='监控配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_operate_log`
--

DROP TABLE IF EXISTS `tp_operate_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_operate_log` (
  `LOG_ID` varchar(32) NOT NULL COMMENT '日志ID',
  `PERSON_ID` varchar(32) DEFAULT NULL COMMENT '人员ID',
  `PERSON_NAME` varchar(100) DEFAULT NULL,
  `MODULE_CODE` varchar(50) DEFAULT NULL COMMENT '模块代码',
  `OPERTER_TIME` datetime DEFAULT NULL COMMENT '操作时间',
  `OPERTER_TYPE` varchar(50) DEFAULT NULL COMMENT '操作类型',
  `OPERTER_RID` varchar(100) DEFAULT NULL COMMENT '操作资源ID',
  `OPERTER_IP` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `OPERTER_BROWSER` varchar(500) DEFAULT NULL COMMENT '操作浏览器',
  `USERNAME` varchar(100) DEFAULT NULL COMMENT '用户名',
  `ASCN_ID` varchar(32) DEFAULT NULL COMMENT '归属ID',
  `CATEGORY` varchar(20) DEFAULT NULL COMMENT '用户类别',
  `CITY_CODE` varchar(20) DEFAULT NULL COMMENT '城市代码',
  `ACTIVED` varchar(1) DEFAULT '1' COMMENT '是否激活(1:激活 0:未激活)',
  `CREATOR` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATOR` varchar(32) DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `EXTEND01` varchar(200) DEFAULT NULL COMMENT '扩展字段1',
  `EXTEND02` varchar(200) DEFAULT NULL COMMENT '扩展字段2',
  `EXTEND03` varchar(200) DEFAULT NULL COMMENT '扩展字段3',
  PRIMARY KEY (`LOG_ID`),
  KEY `idx_operter_time` (`OPERTER_TIME`),
  KEY `idx_username` (`USERNAME`),
  KEY `idx_module_code` (`MODULE_CODE`),
  KEY `idx_operter_type` (`OPERTER_TYPE`),
  KEY `idx_operter_ip` (`OPERTER_IP`),
  KEY `idx_category` (`CATEGORY`),
  KEY `idx_city_code` (`CITY_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_parameter_config`
--

DROP TABLE IF EXISTS `tp_parameter_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_parameter_config` (
  `PM_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PM_KEY` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PM_NAME` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PM_VAL` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PM_DESC` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ORDER_INDEX` double(10,2) DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `ENABLED` int(11) DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `PM_ID` (`PM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_person_audit`
--

DROP TABLE IF EXISTS `tp_person_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_person_audit` (
  `audit_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请ID, 唯一标识ID，主键',
  `person_id` bigint(20) NOT NULL COMMENT '人员ID',
  `person_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '人员姓名',
  `person_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员手机号',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '人员部门ID',
  `dept_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员部门名称',
  `original_data` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改前数据(JSON格式)',
  `modified_data` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改后数据(JSON格式)',
  `changed_fields` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更字段列表，逗号分隔',
  `audit_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝，3-已取消',
  `audit_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核理由',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `auditor_id` bigint(20) DEFAULT NULL COMMENT '审核人(管理)ID',
  `auditor_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核人(管理)姓名',
  `submit_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '提交时间',
  `created_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`audit_id`),
  KEY `idx_person_id` (`person_id`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_submit_time` (`submit_time`),
  KEY `idx_auditor_id` (`auditor_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户个人信息修改审核表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_person_audit_request`
--

DROP TABLE IF EXISTS `tp_person_audit_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_person_audit_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `applicant_id` bigint(20) NOT NULL COMMENT '申请人ID',
  `applicant_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请人姓名',
  `applicant_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '申请人手机号',
  `applicant_dept_id` bigint(20) DEFAULT NULL COMMENT '申请人部门ID',
  `applicant_dept_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '申请人部门名称',
  `original_data` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改前数据(JSON格式)',
  `modified_data` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改后数据(JSON格式)',
  `changed_fields` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更字段列表，逗号分隔',
  `audit_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝，3-已取消',
  `auditor_id` bigint(20) DEFAULT NULL COMMENT '审核人ID',
  `auditor_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核人姓名',
  `audit_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核理由',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `submit_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '提交时间',
  `created_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_applicant_id` (`applicant_id`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_submit_time` (`submit_time`),
  KEY `idx_auditor_id` (`auditor_id`),
  KEY `idx_applicant_dept_id` (`applicant_dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='个人信息修改审核申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_person_basicinfo`
--

DROP TABLE IF EXISTS `tp_person_basicinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_person_basicinfo` (
  `PERSON_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员唯一标识ID，雪花算法生成的主键，全局唯一',
  `PERSON_NAME` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员姓名，必填字段，用于显示和查询',
  `PROFILE_PHOTO` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像照片存储路径，支持相对路径或URL地址',
  `PERSON_NO` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员编号，系统内部使用的人员工号或编码',
  `SEX` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别，存储值：男/女/未知，对应字典编码',
  `IDTYPE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证件类型，如：身份证/护照/军官证等，对应字典编码H38',
  `IDCARD` varchar(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证件号码，主要存储身份证号，支持15位和18位格式',
  `NATIVE_PLACE` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '籍贯信息，存储人员出生地或户籍所在地',
  `SAFEPRIN_NATION` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '民族信息，对应字典编码H51，如：汉族/回族等',
  `RESUME` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '个人简历或简介，存储人员基本履历信息',
  `BIRTHDAY` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '出生日期，格式：YYYYMMDDHHMMSS，通常只使用年月日部分',
  `PHONE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号码，加密存储，用于登录和联系，具有唯一性约束',
  `TEL` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '固定电话号码，办公室或家庭电话',
  `EMAIL` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电子邮箱地址，用于邮件通知和联系',
  `OFFICE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职位或职务名称，如：科长/主任/经理等',
  `ACTIVED` int(11) DEFAULT NULL COMMENT '账户状态，1-启用/0-禁用，控制人员是否可以正常使用系统',
  `CATEGORY` int(11) DEFAULT NULL COMMENT '人员类别，0-政府人员/1-企业人员/2-其他，用于区分不同类型用户',
  `ASCN_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属机构单位ID，政府存机构ID，企业存单位ID，分公司存分公司单位ID',
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID，记录该人员信息的创建者',
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建时间，格式：YYYYMMDDHHMMSS，记录数据创建的时间戳',
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最后更新人ID，记录最后一次修改该人员信息的用户',
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最后更新时间，格式：YYYYMMDDHHMMSS，记录数据最后修改时间',
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID，用于多租户系统的数据隔离',
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段1，预留字段，可根据业务需要存储额外信息',
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段2，预留字段，可根据业务需要存储额外信息',
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段3，预留字段，可根据业务需要存储额外信息',
  UNIQUE KEY `PERSON_ID` (`PERSON_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='人员基本信息表，存储系统中所有人员的基础信息，包括政府人员、企业人员等';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_person_dept`
--

DROP TABLE IF EXISTS `tp_person_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_person_dept` (
  `PERSON_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DEPT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DEFAULT_DEPT` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_person_exinfo`
--

DROP TABLE IF EXISTS `tp_person_exinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_person_exinfo` (
  `PERSON_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员ID',
  `TITLE_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职称代码',
  `MADDRESS` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通讯地址',
  `GRID_DUTY` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '网格职责',
  `GRID_BURDEN` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '网格负担',
  `CHECKCARD_NO` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '检查卡号',
  `CHECKCARD_LIMITDATE` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '检查卡有效期',
  `SAFETY_ENGINEER` int(11) DEFAULT NULL COMMENT '安全工程师',
  `SAFETY_ENGINEER_DATE` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '安全工程师日期',
  `SAFETY_DUTY_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '安全职责代码',
  `IS_FULL_JOB` int(11) DEFAULT NULL COMMENT '是否全职',
  `JOB_NUMBER` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工号',
  `POLITICS_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '政治面貌代码',
  `SCHOOL` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '毕业学校',
  `SEPC_SUBJECT` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '专业',
  `DIPLOMA_CODE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学历代码',
  `DEGREE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学位',
  `POSITION` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职位',
  `PART_WORK_DATE` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参加工作日期',
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `SOLDIER_URL` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '军人照片URL',
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段1',
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段2',
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段3',
  `EXTEND04` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段4',
  `EXTEND05` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段5'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='人员扩展信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_person_role`
--

DROP TABLE IF EXISTS `tp_person_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_person_role` (
  `PERSON_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ROLE_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DEPT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_person_tag`
--

DROP TABLE IF EXISTS `tp_person_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_person_tag` (
  `person_id` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '人员ID',
  `tag_id` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签ID',
  `creator` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `create_time` varchar(14) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建时间',
  `dept_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`person_id`,`tag_id`),
  KEY `idx_person_tag_person` (`person_id`),
  KEY `idx_person_tag_tag` (`tag_id`),
  KEY `idx_person_tag_creator` (`creator`),
  CONSTRAINT `tp_person_tag_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `tp_person_basicinfo` (`PERSON_ID`) ON DELETE CASCADE,
  CONSTRAINT `tp_person_tag_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tp_tag` (`tag_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='人员标签关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_richtext`
--

DROP TABLE IF EXISTS `tp_richtext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_richtext` (
  `TXT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `REFER_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CONTENT` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TXT_TYPE` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `TXT_ID` (`TXT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_role`
--

DROP TABLE IF EXISTS `tp_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_role` (
  `ROLE_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ROLE_NAME` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ROLE_TYPE` int(11) DEFAULT NULL,
  `ROLE_DESC` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ORDER_INDEX` double(10,2) DEFAULT NULL,
  `CATEGORY` int(11) DEFAULT NULL,
  `ASCN_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_ROLE` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_role_menu`
--

DROP TABLE IF EXISTS `tp_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_role_menu` (
  `ROLE_ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MENU_ID` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_scheduled_task`
--

DROP TABLE IF EXISTS `tp_scheduled_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_scheduled_task` (
  `task_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `app_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `task_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `class_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `method_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cron_expression` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remark` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `next_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `now_executed` int(11) DEFAULT NULL,
  `serial_executed` int(11) DEFAULT NULL,
  `serial_threshold` int(11) DEFAULT NULL,
  `last_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_result` int(11) DEFAULT NULL,
  `status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `enabled` int(11) DEFAULT NULL,
  `actived` int(11) DEFAULT NULL,
  `creator` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updator` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `del_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend01` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend02` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend03` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_send_mail_record`
--

DROP TABLE IF EXISTS `tp_send_mail_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_send_mail_record` (
  `record_id` varchar(36) COLLATE utf8mb4_bin NOT NULL COMMENT '记录id',
  `person_name` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收件人姓名',
  `email` varchar(500) COLLATE utf8mb4_bin NOT NULL COMMENT '电子邮箱',
  `send_time` char(14) COLLATE utf8mb4_bin NOT NULL COMMENT '发送时间',
  `message` varchar(5000) COLLATE utf8mb4_bin NOT NULL COMMENT '发送内容',
  `status` int(11) DEFAULT NULL COMMENT '发送状态（1:成功，0：失败）',
  `tenant_id` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户id',
  `actived` int(11) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `creator` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(14) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  `updator` varchar(36) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
  `update_time` varchar(14) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改时间',
  `extend01` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段01',
  `extend02` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段02',
  `extend03` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段03',
  `extend04` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段04',
  `extend05` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '扩展字段05',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='邮件发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_signup`
--

DROP TABLE IF EXISTS `tp_signup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_signup` (
  `PARTY_A` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `INTER_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_system_config`
--

DROP TABLE IF EXISTS `tp_system_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_system_config` (
  `config_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键',
  `config_value` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置值',
  `config_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'string' COMMENT '配置项类型：string-字符串，number-数字，boolean-布尔值，json-JSON对象，array-数组',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime DEFAULT current_timestamp() COMMENT '创建时间',
  `update_time` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_tag`
--

DROP TABLE IF EXISTS `tp_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_tag` (
  `tag_id` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签ID（主键）',
  `tag_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名称',
  `tag_desc` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签描述',
  `tag_color` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '#1890ff' COMMENT '标签颜色',
  `order_index` double DEFAULT 0 COMMENT '排序号',
  `tenant_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `ascn_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属机构ID',
  `log_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除',
  `creator` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `create_time` varchar(14) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建时间',
  `updator` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
  `update_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改时间',
  `extend01` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段01',
  `extend02` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段02',
  `extend03` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段03',
  PRIMARY KEY (`tag_id`),
  KEY `idx_tag_name` (`tag_name`),
  KEY `idx_tag_creator` (`creator`),
  KEY `idx_tag_create_time` (`create_time`),
  KEY `idx_tp_tag_log_delete` (`log_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_tenant`
--

DROP TABLE IF EXISTS `tp_tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_tenant` (
  `TENANT_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TENANT_NAME` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENABLED` int(11) DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND01` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND02` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `EXTEND03` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `TENANT_ID` (`TENANT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_time_rule`
--

DROP TABLE IF EXISTS `tp_time_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_time_rule` (
  `id` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规则ID（主键）',
  `rule_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规则名称',
  `start_time` varchar(14) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规则启动时间（yyyyMMddHHmmss格式）',
  `end_time` varchar(14) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规则结束时间（yyyyMMddHHmmss格式）',
  `status` int(11) DEFAULT 1 COMMENT '规则状态（0：禁用，1：启用）',
  `allow_login` int(11) DEFAULT 1 COMMENT '是否允许登录：1-允许，0-拒绝',
  `role_ids` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色ID串（逗号分隔）',
  `role_names` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色名称串（逗号分隔）',
  `user_ids` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员ID串（逗号分隔）',
  `user_names` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '人员名称串（逗号分隔）',
  `creator_id` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人ID',
  `create_time` varchar(14) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建时间（yyyyMMddHHmmss格式）',
  `modifier_id` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人ID',
  `modify_time` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改时间（yyyyMMddHHmmss格式）',
  `actived` int(11) DEFAULT 1 COMMENT '是否有效（1：有效，0：无效）',
  `log_delete` int(11) DEFAULT 0 COMMENT '逻辑删除（0：未删除，1：已删除）',
  `extend01` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段01',
  `extend02` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段02',
  `extend03` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展字段03',
  `extend04` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extend05` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_rule_name` (`rule_name`),
  KEY `idx_status` (`status`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_actived` (`actived`),
  KEY `idx_log_delete` (`log_delete`),
  KEY `idx_time_range` (`start_time`,`end_time`),
  KEY `idx_status_time` (`status`,`start_time`,`end_time`),
  KEY `idx_active_status_time` (`actived`,`log_delete`,`status`,`start_time`,`end_time`),
  KEY `idx_allow_login` (`allow_login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录时间段控制规则表：用于控制特定用户或角色在指定时间段的登录权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tp_trace`
--

DROP TABLE IF EXISTS `tp_trace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tp_trace` (
  `ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MODULE_TYPE` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `RECORD_ID` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_BEFORE` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UPDATE_AFTER` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ACTIVED` int(11) DEFAULT NULL,
  `CREATOR` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATOR_NAME` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATE_TIME` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `v_org_tree_latest_changes`
--

DROP TABLE IF EXISTS `v_org_tree_latest_changes`;
/*!50001 DROP VIEW IF EXISTS `v_org_tree_latest_changes`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_org_tree_latest_changes` AS SELECT 
 1 AS `id`,
 1 AS `version`,
 1 AS `operation_type`,
 1 AS `operation_time`,
 1 AS `operator_id`,
 1 AS `operator_name`,
 1 AS `dept_id`,
 1 AS `dept_name`,
 1 AS `parent_id`,
 1 AS `old_parent_id`,
 1 AS `dept_code`,
 1 AS `old_dept_code`,
 1 AS `dept_level`,
 1 AS `old_dept_level`,
 1 AS `sort_order`,
 1 AS `old_sort_order`,
 1 AS `description`,
 1 AS `before_data`,
 1 AS `after_data`,
 1 AS `created_time`,
 1 AS `updated_time`,
 1 AS `rn`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `v_org_tree_operation_stats`
--

DROP TABLE IF EXISTS `v_org_tree_operation_stats`;
/*!50001 DROP VIEW IF EXISTS `v_org_tree_operation_stats`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `v_org_tree_operation_stats` AS SELECT 
 1 AS `operation_type`,
 1 AS `operation_count`,
 1 AS `operation_date`,
 1 AS `operator_name`,
 1 AS `affected_dept_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'ps-bmp'
--
/*!50003 DROP FUNCTION IF EXISTS `get_next_org_version` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `get_next_org_version`() RETURNS bigint(20)
    READS SQL DATA
    DETERMINISTIC
BEGIN
    DECLARE next_version BIGINT;
    UPDATE org_tree_version_sequence SET current_version = current_version + 1 WHERE id = 1;
    SELECT current_version INTO next_version FROM org_tree_version_sequence WHERE id = 1;
    RETURN next_version;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `record_org_tree_change` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `record_org_tree_change`(
    IN p_operation_type VARCHAR(20),
    IN p_operator_id BIGINT,
    IN p_operator_name VARCHAR(100),
    IN p_dept_id BIGINT,
    IN p_dept_name VARCHAR(200),
    IN p_parent_id BIGINT,
    IN p_old_parent_id BIGINT,
    IN p_dept_code VARCHAR(50),
    IN p_old_dept_code VARCHAR(50),
    IN p_dept_level INT,
    IN p_old_dept_level INT,
    IN p_sort_order INT,
    IN p_old_sort_order INT,
    IN p_description TEXT,
    IN p_before_data JSON,
    IN p_after_data JSON
)
BEGIN
    DECLARE new_version BIGINT;
    SET new_version = get_next_org_version();
    
    INSERT INTO org_tree_change_history (
        version, operation_type, operator_id, operator_name,
        dept_id, dept_name, parent_id, old_parent_id,
        dept_code, old_dept_code, dept_level, old_dept_level,
        sort_order, old_sort_order, description,
        before_data, after_data
    ) VALUES (
        new_version, p_operation_type, p_operator_id, p_operator_name,
        p_dept_id, p_dept_name, p_parent_id, p_old_parent_id,
        p_dept_code, p_old_dept_code, p_dept_level, p_old_dept_level,
        p_sort_order, p_old_sort_order, p_description,
        p_before_data, p_after_data
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Current Database: `ps-bmp`
--

USE `ps-bmp`;

--
-- Final view structure for view `v_org_tree_latest_changes`
--

/*!50001 DROP VIEW IF EXISTS `v_org_tree_latest_changes`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_org_tree_latest_changes` AS select `h`.`id` AS `id`,`h`.`version` AS `version`,`h`.`operation_type` AS `operation_type`,`h`.`operation_time` AS `operation_time`,`h`.`operator_id` AS `operator_id`,`h`.`operator_name` AS `operator_name`,`h`.`dept_id` AS `dept_id`,`h`.`dept_name` AS `dept_name`,`h`.`parent_id` AS `parent_id`,`h`.`old_parent_id` AS `old_parent_id`,`h`.`dept_code` AS `dept_code`,`h`.`old_dept_code` AS `old_dept_code`,`h`.`dept_level` AS `dept_level`,`h`.`old_dept_level` AS `old_dept_level`,`h`.`sort_order` AS `sort_order`,`h`.`old_sort_order` AS `old_sort_order`,`h`.`description` AS `description`,`h`.`before_data` AS `before_data`,`h`.`after_data` AS `after_data`,`h`.`created_time` AS `created_time`,`h`.`updated_time` AS `updated_time`,row_number() over ( partition by `h`.`dept_id` order by `h`.`version` desc) AS `rn` from `org_tree_change_history` `h` where `h`.`version` = (select max(`org_tree_change_history`.`version`) from `org_tree_change_history`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_org_tree_operation_stats`
--

/*!50001 DROP VIEW IF EXISTS `v_org_tree_operation_stats`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `v_org_tree_operation_stats` AS select `org_tree_change_history`.`operation_type` AS `operation_type`,count(0) AS `operation_count`,cast(`org_tree_change_history`.`operation_time` as date) AS `operation_date`,`org_tree_change_history`.`operator_name` AS `operator_name`,count(distinct `org_tree_change_history`.`dept_id`) AS `affected_dept_count` from `org_tree_change_history` group by `org_tree_change_history`.`operation_type`,cast(`org_tree_change_history`.`operation_time` as date),`org_tree_change_history`.`operator_name` order by cast(`org_tree_change_history`.`operation_time` as date) desc,count(0) desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-25  3:06:01
