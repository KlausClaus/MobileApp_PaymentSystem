/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : payment

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 21/10/2024 07:05:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for notify
-- ----------------------------
DROP TABLE IF EXISTS `notify`;
CREATE TABLE `notify`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `uid` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notify
-- ----------------------------
INSERT INTO `notify` VALUES (1, 'Successfully paid tuition bill via WeChat!', '2024-10-21 07:02', 2);
INSERT INTO `notify` VALUES (2, 'Your tuition bill has been issued, please pay it on time!', '2024-10-21 05:18', NULL);
INSERT INTO `notify` VALUES (3, 'Your tuition bill has been released, please pay it in time！', '2024-10-21 06:57', NULL);

-- ----------------------------
-- Table structure for payway
-- ----------------------------
DROP TABLE IF EXISTS `payway`;
CREATE TABLE `payway`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `payway` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `is_default` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payway
-- ----------------------------
INSERT INTO `payway` VALUES (5, 'xxxx@xx.com', 'Alipay', 0);
INSERT INTO `payway` VALUES (9, 'xxxx@xx.com', 'WeChat', 1);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `grade` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `classes` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `professional` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, 'xxxx@xx.com', 'xx', 'xx', 'xx', '男', 'xx');

-- ----------------------------
-- Table structure for tuition
-- ----------------------------
DROP TABLE IF EXISTS `tuition`;
CREATE TABLE `tuition`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'Primary key, auto-increment',
  `student_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'Student Name',
  `student_email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'Student ID Email',
  `major` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'Major',
  `academic_year` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'Year of Study',
  `total_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Total fees',
  `tuition_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Tuition Fee',
  `accommodation_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Accommodation Fee',
  `book_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Book Fee',
  `material_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Material Fee',
  `activity_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Extracurricular activities fee',
  `exam_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Examination Fee',
  `payment_method` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'Payment method (e.g. bank transfer, credit card, etc.)',
  `payment_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT 'Payment Amount',
  `created_time` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'Creation time',
  `payment_time` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'Payment time (if paid)',
  `status` int(0) NULL DEFAULT NULL COMMENT 'Payment Status',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'Tuition fee form, recording students tuition fee information and payment status' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tuition
-- ----------------------------
INSERT INTO `tuition` VALUES (1, 'xx', 'xxxx@xx.com', 'xx', '2024', 255.00, 100.00, 155.00, 0.00, 0.00, 0.00, 0.00, 'WeChat', 0.00, '2024-10-21 06:57', '2024-10-21 07:02', 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `role` int(0) NULL DEFAULT NULL,
  `notify` int(0) NULL DEFAULT NULL,
  `avatar` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', 1, 0, NULL);
INSERT INTO `user` VALUES (2, 'xxxx@xx.com', '1234', 2, 1, 'http://192.168.0.105:9090/files/29aedafd6d134d8a88a0d4e38a0c91d1.jpg');

SET FOREIGN_KEY_CHECKS = 1;
