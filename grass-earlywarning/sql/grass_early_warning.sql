-- --------------------------------------------------------
-- 主机:                           101.132.242.90
-- 服务器版本:                        8.0.23 - MySQL Community Server - GPL
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 导出  表 grass-service-2.0.grass_early_warning 结构
DROP TABLE IF EXISTS `grass_early_warning`;
CREATE TABLE IF NOT EXISTS `grass_early_warning` (
  `rules_id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `rules_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则名称',
  `rules_state` int NOT NULL DEFAULT '0' COMMENT '规则状态',
  `rules_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预警级别',
  `trigger_relation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '触发条件关系',
  `trigger_frequency` bigint NOT NULL DEFAULT '0' COMMENT '触发条件执行频率',
  `judge_relation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '判断条件关系',
  `judge_frequency` bigint NOT NULL DEFAULT '0' COMMENT '判断条件执行频率',
  `finish_relation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结束条件关系',
  `finish_frequency` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结束条件执行频率',
  `judge_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '触发条件类型',
  `add_up_count` int NOT NULL DEFAULT '0' COMMENT '累加次数',
  `add_up_duration` bigint NOT NULL DEFAULT '0' COMMENT '累加持续时间',
  `add_up_once_max` bigint NOT NULL DEFAULT '0' COMMENT '累加单次最大时长',
  `continuous_count` int NOT NULL DEFAULT '0' COMMENT '连续次数',
  `continuous_increase` double(20,2) NOT NULL DEFAULT '0.00' COMMENT '连续增长参数',
  `silent_cycle` int NOT NULL DEFAULT '0' COMMENT '沉默周期',
  `start_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '生效开始时间',
  `end_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '生效结束时间',
  `suggest_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '处置建议ID',
  `tenant_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`rules_id`)
) ENGINE=InnoDB AUTO_INCREMENT=274 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预警规则';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_alarm_his 结构
DROP TABLE IF EXISTS `grass_early_warning_alarm_his`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_alarm_his` (
  `his_id` bigint NOT NULL AUTO_INCREMENT COMMENT '报警历史ID',
  `rules_id` bigint NOT NULL COMMENT '预警规则ID',
  `alarm_time` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预警时间',
  PRIMARY KEY (`his_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预警规则报警历史';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_carrier 结构
DROP TABLE IF EXISTS `grass_early_warning_carrier`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_carrier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `rules_id` bigint NOT NULL COMMENT '预警规则ID',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '物联设备ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=338 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预警规则运行载体';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_finish 结构
DROP TABLE IF EXISTS `grass_early_warning_finish`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_finish` (
  `rules_id` bigint NOT NULL COMMENT '预警规则ID',
  `finish_index` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件编号',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物联设备ID',
  `point_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '测点ID',
  `judge` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '比较符号',
  `point_value` double(20,2) DEFAULT NULL COMMENT '触发值',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`rules_id`,`finish_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预警规则结束条件';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_finish_his 结构
DROP TABLE IF EXISTS `grass_early_warning_finish_his`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_finish_his` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `his_id` bigint NOT NULL COMMENT '报警历史ID',
  `rules_id` bigint NOT NULL COMMENT '预警规则ID',
  `finish_index` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件编号',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物联设备ID',
  `point_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '测点ID',
  `judge` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '比较符号',
  `point_value` double(20,2) DEFAULT NULL COMMENT '触发值',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='预警规则结束条件历史';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_his 结构
DROP TABLE IF EXISTS `grass_early_warning_his`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_his` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `his_id` bigint NOT NULL COMMENT '报警历史ID',
  `rules_id` bigint NOT NULL COMMENT '预警规则ID',
  `rules_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '规则名称',
  `rules_state` int NOT NULL DEFAULT '0' COMMENT '规则状态',
  `rules_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '预警级别',
  `trigger_relation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '触发条件关系',
  `trigger_frequency` bigint NOT NULL DEFAULT '0' COMMENT '触发条件执行频率',
  `judge_relation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '判断条件关系',
  `judge_frequency` bigint NOT NULL DEFAULT '0' COMMENT '判断条件执行频率',
  `finish_relation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结束条件关系',
  `finish_frequency` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结束条件执行频率',
  `judge_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '触发条件类型',
  `add_up_count` int NOT NULL DEFAULT '0' COMMENT '累加次数',
  `add_up_duration` bigint NOT NULL DEFAULT '0' COMMENT '累加持续时间',
  `add_up_once_max` bigint NOT NULL DEFAULT '0' COMMENT '累加单次最大时长',
  `continuous_count` int NOT NULL DEFAULT '0' COMMENT '连续次数',
  `continuous_increase` double(20,2) NOT NULL DEFAULT '0.00' COMMENT '连续增长参数',
  `silent_cycle` int NOT NULL DEFAULT '0' COMMENT '沉默周期',
  `start_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '生效开始时间',
  `end_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '生效结束时间',
  `suggest_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '处置建议ID',
  `tenant_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='预警规则历史';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_judge 结构
DROP TABLE IF EXISTS `grass_early_warning_judge`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_judge` (
  `rules_id` bigint NOT NULL COMMENT '预警规则ID',
  `judge_index` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件编号',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物联设备ID',
  `point_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '测点ID',
  `judge` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '比较符号',
  `point_value` double(20,2) DEFAULT NULL COMMENT '触发值',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`rules_id`,`judge_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预警规则判断条件';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_judge_his 结构
DROP TABLE IF EXISTS `grass_early_warning_judge_his`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_judge_his` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `his_id` bigint NOT NULL COMMENT '报警历史ID',
  `rules_id` bigint NOT NULL COMMENT '预警规则ID',
  `judge_index` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件编号',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物联设备ID',
  `point_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '测点ID',
  `judge` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '比较符号',
  `point_value` double(20,2) DEFAULT NULL COMMENT '触发值',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='预警规则判断条件历史';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_notice 结构
DROP TABLE IF EXISTS `grass_early_warning_notice`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `rules_id` bigint NOT NULL COMMENT '关联规则id',
  `notice_template_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知模板id',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1555 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预警规则通知';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_suggest 结构
DROP TABLE IF EXISTS `grass_early_warning_suggest`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_suggest` (
  `suggest_id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `suggest_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '建议分类',
  `suggest_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '建议标题',
  `suggest_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '建议内容',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`suggest_id`),
  KEY `notice_id` (`suggest_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='处置建议';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_suggest_his 结构
DROP TABLE IF EXISTS `grass_early_warning_suggest_his`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_suggest_his` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `his_id` bigint NOT NULL COMMENT '报警历史ID',
  `suggest_id` bigint NOT NULL COMMENT '自增ID',
  `suggest_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '建议分类',
  `suggest_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '建议标题',
  `suggest_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '建议内容',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='处置建议历史';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_trigger 结构
DROP TABLE IF EXISTS `grass_early_warning_trigger`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_trigger` (
  `rules_id` bigint NOT NULL COMMENT '预警规则ID',
  `trigger_index` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件编号',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物联设备ID',
  `point_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '测点ID',
  `judge` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '比较符号',
  `point_value` double(20,2) DEFAULT NULL COMMENT '触发值',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`rules_id`,`trigger_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预警规则触发条件';

-- 数据导出被取消选择。

-- 导出  表 grass-service-2.0.grass_early_warning_trigger_his 结构
DROP TABLE IF EXISTS `grass_early_warning_trigger_his`;
CREATE TABLE IF NOT EXISTS `grass_early_warning_trigger_his` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `his_id` bigint NOT NULL COMMENT '报警历史ID',
  `rules_id` bigint NOT NULL COMMENT '预警规则ID',
  `trigger_index` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '条件编号',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '物联设备ID',
  `point_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '测点ID',
  `judge` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '比较符号',
  `point_value` double(20,2) DEFAULT NULL COMMENT '触发值',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='预警规则触发条件历史';

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
