-- --------------------------------------------------------
-- 主机:                           106.12.7.187
-- 服务器版本:                        5.7.23 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  9.5.0.5249
-- --------------------------------------------------------

-- 导出  表 message.cron 结构
CREATE TABLE IF NOT EXISTS `cron` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `topic_id` int(11) NOT NULL COMMENT 'topic id',
  `name` varchar(50) COLLATE utf8mb4_german2_ci NOT NULL COMMENT '消费者名称',
  `description` varchar(200) COLLATE utf8mb4_german2_ci NOT NULL COMMENT '描述',
  `spec` varchar(50) COLLATE utf8mb4_german2_ci NOT NULL COMMENT '任务配置',
  `status` int(11) NOT NULL COMMENT '状态:1=正常,2=暂停',
  `create_date` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_date` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_date` int(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_topic_id` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci COMMENT='定时任务表';

-- 数据导出被取消选择。
-- 导出  表 message.customer 结构
CREATE TABLE IF NOT EXISTS `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `topic_id` int(11) NOT NULL COMMENT 'topic id',
  `name` varchar(50) NOT NULL COMMENT '消费者名称',
  `api` varchar(200) NOT NULL COMMENT '接口地址',
  `timeout` int(11) NOT NULL COMMENT '超时时间',
  `attempts` int(11) NOT NULL COMMENT '重试次数',
  `create_date` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_date` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_date` int(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_topic_id` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci COMMENT='消费者';

-- 数据导出被取消选择。
-- 导出  表 message.failed_message 结构
CREATE TABLE IF NOT EXISTS `failed_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `topic_id` int(11) NOT NULL COMMENT 'topic id',
  `customer_id` int(11) NOT NULL COMMENT '消费者 id',
  `message` text COLLATE utf8mb4_german2_ci NOT NULL COMMENT '消息内容',
  `error` text COLLATE utf8mb4_german2_ci NOT NULL COMMENT '错误信息',
  `attempts` int(11) NOT NULL COMMENT '重试次数',
  `create_date` int(11) NOT NULL DEFAULT '0' COMMENT '消息创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci COMMENT='失败的消息';

-- 数据导出被取消选择。
-- 导出  表 message.message 结构
CREATE TABLE IF NOT EXISTS `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `topic_id` int(11) NOT NULL COMMENT 'topic id',
  `customer_id` int(11) NOT NULL COMMENT '消费者 id',
  `message` text NOT NULL COMMENT '消息内容',
  `attempts` int(11) NOT NULL COMMENT '重试次数',
  `status` tinyint(4) NOT NULL COMMENT '状态,0=等待中;1=执行中;2=成功;3=失败',
  `available_date` int(11) NOT NULL DEFAULT '0' COMMENT '延迟消息执行时间',
  `create_date` int(11) NOT NULL DEFAULT '0' COMMENT '消息创建时间',
  `timeout_date` int(11) NOT NULL DEFAULT '0' COMMENT '消息超时时间(执行中的消息的字段)',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_status_available_date` (`status`,`available_date`) USING BTREE,
  KEY `idx_timeout_date` (`timeout_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci COMMENT='消息';

-- 数据导出被取消选择。
-- 导出  表 message.message_log 结构
CREATE TABLE IF NOT EXISTS `message_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `message_id` bigint(20) NOT NULL COMMENT '消息 id',
  `topic_id` int(11) NOT NULL COMMENT 'topic id',
  `customer_id` int(11) NOT NULL COMMENT '消费者id',
  `request` text NOT NULL COMMENT '请求内容',
  `response` text NOT NULL COMMENT '请求返回值',
  `error` text NOT NULL COMMENT '错误信息',
  `time` int(11) NOT NULL COMMENT '任务执行时间',
  `create_date` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci COMMENT='消息执行日志';

-- 数据导出被取消选择。
-- 导出  表 message.topic 结构
CREATE TABLE IF NOT EXISTS `topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) NOT NULL COMMENT '描述',
  `create_date` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_date` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  `delete_date` int(11) NOT NULL DEFAULT '0' COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci COMMENT='topic';
