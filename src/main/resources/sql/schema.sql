/*
 Navicat Premium Data Transfer

 Source Server         : 172.27.224.241_33061
 Source Server Type    : MySQL
 Source Server Version : 50741
 Source Host           : 10.3.120.125:33061
 Source Schema         : simple-codebase

 Target Server Type    : MySQL
 Target Server Version : 50741
 File Encoding         : 65001

 Date: 07/05/2023 19:44:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
CREATE TABLE IF NOT exists `article`  (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for snippets
-- ----------------------------
CREATE TABLE IF NOT exists `snippets`  (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `code_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `lang` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                             `create_time` varchar(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `article_id` int(11) NULL DEFAULT NULL,
                             `order` int(11) NOT NULL,
                             `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

