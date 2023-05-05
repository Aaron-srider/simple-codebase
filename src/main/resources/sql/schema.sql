/*
 Navicat Premium Data Transfer

 Source Server         : 172.27.128.180_33061
 Source Server Type    : MySQL
 Source Server Version : 50741
 Source Host           : 172.27.128.180:33061
 Source Schema         : simple-codebase

 Target Server Type    : MySQL
 Target Server Version : 50741
 File Encoding         : 65001

 Date: 26/04/2023 19:54:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE if not exists `article`  (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `createTime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for snippets
-- ----------------------------
CREATE TABLE IF NOT EXISTS `snippets`  (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `code_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
     `lang` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
     `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
     `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
     `create_time` varchar(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `article_id` int(11) NOT NULL,
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for snippets_tag
-- ----------------------------
CREATE TABLE IF NOT EXISTS `snippets_tag`  (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `snippets_id` int(11) NOT NULL,
                               `tag_id` int(11) NOT NULL,
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE INDEX `snippets_tag_pk`(`snippets_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tag`  (
                      `id` int(11) NOT NULL AUTO_INCREMENT,
                      `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;