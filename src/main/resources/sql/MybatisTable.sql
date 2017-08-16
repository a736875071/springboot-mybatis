/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : test3

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-07-25 09:47:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cm_auto_deduction
-- ----------------------------
DROP TABLE IF EXISTS `cm_auto_deduction`;
CREATE TABLE `cm_auto_deduction` (
  `id` bigint(20) NOT NULL COMMENT '自动扣费参数配置表主键',
  `orgId` bigint(20) DEFAULT NULL COMMENT '组织机构ID',
  `deduCycle` char(2) DEFAULT NULL COMMENT '循环扣费周期',
  `beginTime` datetime DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '结束时间',
  `portProcedure` char(64) DEFAULT NULL COMMENT '接口程序(从sm_parameter_info中取paramValue值)',
  `taskStatus` char(2) DEFAULT NULL COMMENT '启用状态',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `bankno_consCode_idx` (`orgId`),
  KEY `bankno_bnNo_idx` (`beginTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自动扣费参数配置表';

-- ----------------------------
-- Records of cm_auto_deduction
-- ----------------------------
INSERT INTO `cm_auto_deduction` VALUES ('1', '1526001', '01', '2017-05-15 17:05:57', '2017-06-28 15:00:36', '1', '01', '2017-06-28 15:00:36');
INSERT INTO `cm_auto_deduction` VALUES ('2', '1526002', '01', '2017-05-15 17:05:57', '2017-06-02 08:45:40', '1', '02', '2017-06-02 08:51:36');
INSERT INTO `cm_auto_deduction` VALUES ('3', '1526003', '01', '2017-05-15 17:05:57', '2017-06-02 08:45:42', '1', '02', '2017-06-02 08:51:36');

-- ----------------------------
-- Table structure for cm_auto_deduction_record
-- ----------------------------
DROP TABLE IF EXISTS `cm_auto_deduction_record`;
CREATE TABLE `cm_auto_deduction_record` (
  `id` bigint(20) NOT NULL COMMENT '自动扣费记录表主键',
  `deduId` bigint(20) NOT NULL COMMENT '扣费id',
  `executeStatus` char(2) DEFAULT NULL COMMENT '执行状态',
  `executeTime` datetime NOT NULL COMMENT '执行时间',
  `deduCycle` char(2) DEFAULT NULL COMMENT '扣费周期',
  `orgId` bigint(20) NOT NULL COMMENT '组织机构ID',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自动扣费记录表';

-- ----------------------------
-- Records of cm_auto_deduction_record
-- ----------------------------
INSERT INTO `cm_auto_deduction_record` VALUES ('6285723351130308608', '1', '01', '2017-06-28 15:00:30', '01', '1526001', null);
INSERT INTO `cm_auto_deduction_record` VALUES ('6285723376291938304', '1', '01', '2017-06-28 15:00:36', '01', '1526001', null);
