/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : mygoschool

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 17/10/2024 20:10:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for affiche
-- ----------------------------
DROP TABLE IF EXISTS `affiche`;
CREATE TABLE `affiche`  (
  `id` bigint NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公告标题',
  `text` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公告内容',
  `createAt` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for apprise
-- ----------------------------
DROP TABLE IF EXISTS `apprise`;
CREATE TABLE `apprise`  (
  `id` bigint NOT NULL,
  `createUserId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论人',
  `receiveUserId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '被评价人id',
  `appriseData` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拼接内容',
  `type` int NULL DEFAULT NULL COMMENT '评论内容的类型  拼车 1 二手 2  ',
  `postId` bigint NULL DEFAULT NULL COMMENT '评价帖子的id',
  `createAt` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `isDelate` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for carshareorder
-- ----------------------------
DROP TABLE IF EXISTS `carshareorder`;
CREATE TABLE `carshareorder`  (
  `orderId` bigint NOT NULL AUTO_INCREMENT COMMENT '拼车订单id',
  `createUserId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户id',
  `receiveUserId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接受用户id',
  `receiveUserWechatImg` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接受用户的微信二维码图片',
  `startAddressAll` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发地点全称',
  `startAddress` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发地点简称',
  `endAddressAll` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目的地全称',
  `endAddress` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目的地简称',
  `startDateTime` datetime NULL DEFAULT NULL COMMENT '出发日期时间',
  `currentPerson` int NULL DEFAULT 1 COMMENT '当前人数',
  `lackPerson` int NOT NULL DEFAULT 1 COMMENT '缺少人数',
  `phoneNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `wechatAccount` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信账户',
  `wechatImg` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布者的微信二维码图片',
  `receiveDateTime` datetime NULL DEFAULT NULL COMMENT '订单接受时间',
  `isBefore` tinyint(1) NULL DEFAULT 0 COMMENT '是否提前',
  `beforeTime` time NULL DEFAULT '00:00:00' COMMENT '提前时间',
  `isAfter` tinyint(1) NULL DEFAULT 0 COMMENT '是否延后',
  `AfterTime` time NULL DEFAULT '00:00:00' COMMENT '延后时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `createUserAppriseId` bigint NULL DEFAULT NULL COMMENT '创建订单的用户对订单的评价id',
  `receiveUserAppriseId` bigint NULL DEFAULT NULL COMMENT '接受订单用户对订单的评价Id',
  `createAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateAt` datetime NULL DEFAULT NULL,
  `isDelete` tinyint(1) NULL DEFAULT 0,
  `status` int NULL DEFAULT 0 COMMENT '订单状态 0已发布  1已接收  2已完成 3已过期',
  PRIMARY KEY (`orderId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 188 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '拼车订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for emailaccount
-- ----------------------------
DROP TABLE IF EXISTS `emailaccount`;
CREATE TABLE `emailaccount`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `toEmailTexts` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接受者的邮箱列表 用,分割',
  `accountSetFrom` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者的邮箱',
  `accountSetUser` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名，默认为发件人邮箱前缀，可以不填，也可以填发件人邮箱全称',
  `accountEmailSendHost` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮件发送服务商',
  `accountEmailPass` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱授权码',
  `accountEmailPort` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '465' COMMENT '邮箱发送端口 默认为465',
  `sendEmailDataId` int NULL DEFAULT NULL COMMENT '要发送的邮箱的数据id',
  `sendEmailTemplateFileName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '要发送的邮箱模板的名称',
  `heFenKey` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '和风天气Key',
  `qingYanAssistantId` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '智谱清言的智能体Id',
  `isDelete` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for feekback
-- ----------------------------
DROP TABLE IF EXISTS `feekback`;
CREATE TABLE `feekback`  (
  `id` bigint NOT NULL,
  `userId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `remark` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `imgList` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NOT NULL DEFAULT 0 COMMENT '反馈的状态 未解决: 0  已解决: 1',
  `createAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reportId` bigint NULL DEFAULT 0 COMMENT '默认0就是意见反馈  不为0就是订单举报',
  `isDelete` tinyint(1) NULL DEFAULT 0,
  `reportType` tinyint(1) NULL DEFAULT 0 COMMENT '0就是意见反馈 1就是拼车投诉 2就是闲置投诉',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for girlfriend
-- ----------------------------
DROP TABLE IF EXISTS `girlfriend`;
CREATE TABLE `girlfriend`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `startOfLove` datetime NULL DEFAULT NULL COMMENT '恋爱开始时间',
  `newBirthday` datetime NULL DEFAULT NULL COMMENT '新历生日',
  `oldBirthday` datetime NULL DEFAULT NULL COMMENT '农历生日',
  `remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `randomEmailTitle` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '随机问候语句',
  `randomHtmlTitle` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '随机问候语句',
  `address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `subject` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sendEmailTemplate` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `isDelete` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for myimg
-- ----------------------------
DROP TABLE IF EXISTS `myimg`;
CREATE TABLE `myimg`  (
  `id` bigint NOT NULL,
  `imgPath` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片的全路径',
  `isUse` tinyint(1) NULL DEFAULT 0 COMMENT '是否使用默认为没有使用 定时删除没有使用的图片',
  `createAt` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用来记录上传的图片方便后期查询无用图片使用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mynsfw
-- ----------------------------
DROP TABLE IF EXISTS `mynsfw`;
CREATE TABLE `mynsfw`  (
  `id` bigint NOT NULL,
  `postUserOpenId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `imgPath` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `backupImgPath` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备份图片路径 保存原先图片的路径',
  `status` int NULL DEFAULT NULL COMMENT '0待审核可能是违规  1不是违规图片 2违规图片',
  `score` double NULL DEFAULT NULL COMMENT 'nsfw分数',
  `createAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mysensitive
-- ----------------------------
DROP TABLE IF EXISTS `mysensitive`;
CREATE TABLE `mysensitive`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `white` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `black` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int NOT NULL,
  `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `text` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
  `createAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isDelete` tinyint(1) NULL DEFAULT 0,
  `sort` tinyint(1) NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop`  (
  `id` bigint NOT NULL,
  `detail` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` double NOT NULL,
  `address` int NOT NULL COMMENT '0 濂溪校区 1鹤问湖校区 2其他',
  `imgs` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片列表 英文逗号分割',
  `status` int NOT NULL DEFAULT 0 COMMENT '0在售 1下架 ',
  `createUserId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `wechatImg` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `isDelete` tinyint(1) NOT NULL DEFAULT 0,
  `browse` int NOT NULL DEFAULT 0 COMMENT '商品浏览量',
  `cancelTime` datetime NULL DEFAULT NULL COMMENT '自动下架时间',
  `createAt` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `shop_createUserId_index`(`createUserId` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo`  (
  `userId` bigint NOT NULL AUTO_INCREMENT,
  `openId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userWx` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `userWxImg` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信二维码图片',
  `userPhone` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` int NOT NULL DEFAULT 1 COMMENT '1为男 0为女',
  `reportNum` int NOT NULL DEFAULT 0 COMMENT '举报次数',
  `isStudent` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是不是学生',
  `isBlack` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是不是被封号了',
  `createAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1795702161006600195 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
