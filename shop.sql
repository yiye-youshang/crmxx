/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : shop

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 16/11/2020 15:56:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `goodsid` int(11) NOT NULL AUTO_INCREMENT,
  `goodsname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goodsimg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goodscontent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goodsprice` int(11) NULL DEFAULT NULL,
  `goodsstock` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`goodsid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, '租借女友', '../img/m1.jpg', '<<租借女友>>', 30, 1000);
INSERT INTO `goods` VALUES (2, '总之就是非常可爱', '../img/m2.jpg', '<<总之就是非常可爱>>', 20, 2000);
INSERT INTO `goods` VALUES (3, '鬼灭之刃', '../img/m3.jpg', '<<鬼灭之刃>>', 40, 3000);
INSERT INTO `goods` VALUES (4, '虚构推理', '../img/m4.jpg', '<<虚构推理>>', 40, 3000);
INSERT INTO `goods` VALUES (5, '在魔王城说晚安', '../img/m5.jpg', '<<在魔王城说晚安>>', 10, 2000);
INSERT INTO `goods` VALUES (6, '咒术回战', '../img/m6.jpg', '<<咒术回战>>', 20, 3000);

-- ----------------------------
-- Table structure for killgoods
-- ----------------------------
DROP TABLE IF EXISTS `killgoods`;
CREATE TABLE `killgoods`  (
  `killid` int(11) NOT NULL AUTO_INCREMENT,
  `goodsid` int(11) NULL DEFAULT NULL,
  `stockcount` int(11) NULL DEFAULT NULL,
  `startdate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `enddate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `killprice` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`killid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of killgoods
-- ----------------------------
INSERT INTO `killgoods` VALUES (1, 1, 100, '2020-11-15 18:41:52', '2020-11-17 11:11:11', 3);
INSERT INTO `killgoods` VALUES (3, 2, 96, '2020-11-12 20:15:33', '2020-11-12 20:15:33', 1);
INSERT INTO `killgoods` VALUES (4, 5, 95, '2020-11-12 15:33:40', '2020-11-20 15:33:59', NULL);

-- ----------------------------
-- Table structure for killorderinfo
-- ----------------------------
DROP TABLE IF EXISTS `killorderinfo`;
CREATE TABLE `killorderinfo`  (
  `killorderid` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NULL DEFAULT NULL,
  `goodsid` int(11) NULL DEFAULT NULL,
  `orderid` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`killorderid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of killorderinfo
-- ----------------------------
INSERT INTO `killorderinfo` VALUES (3, 2, 5, 43);
INSERT INTO `killorderinfo` VALUES (4, 1, 5, 44);

-- ----------------------------
-- Table structure for orderinfo
-- ----------------------------
DROP TABLE IF EXISTS `orderinfo`;
CREATE TABLE `orderinfo`  (
  `orderid` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NULL DEFAULT NULL,
  `goodsid` int(11) NULL DEFAULT NULL,
  `goodsname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goodscount` int(11) NULL DEFAULT NULL,
  `goodsprice` int(11) NULL DEFAULT NULL,
  `orderstatus` int(11) NULL DEFAULT NULL,
  `createdate` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `paydate` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`orderid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orderinfo
-- ----------------------------
INSERT INTO `orderinfo` VALUES (43, 2, 5, '在魔王城说晚安', 1, 0, 0, '2020-11-14 12:23:39', NULL);
INSERT INTO `orderinfo` VALUES (44, 1, 5, '在魔王城说晚安', 1, 0, 0, '2020-11-14 12:26:54', NULL);

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo`  (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `realname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES (1, 'admin', '123456', '小范');
INSERT INTO `userinfo` VALUES (2, 'test', '123', 'test');
INSERT INTO `userinfo` VALUES (3, 'test1', '123', 'test1');
INSERT INTO `userinfo` VALUES (13, '1', '1', '1');
INSERT INTO `userinfo` VALUES (14, '1233ttt', '123', 'ttttt');

SET FOREIGN_KEY_CHECKS = 1;
