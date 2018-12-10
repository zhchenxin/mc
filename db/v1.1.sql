ALTER TABLE `customer` ADD `is_log` TINYINT NOT NULL DEFAULT 0 COMMENT '是否记录日志' after attempts;
ALTER TABLE `customer` ADD `priority` TINYINT NOT NULL DEFAULT 50 COMMENT '优先级:0=低,50=正常,100=高' after is_log;

ALTER TABLE `message` ADD `priority` TINYINT NOT NULL DEFAULT 50 COMMENT '优先级:0=低,50=正常,100=高' after status;


CREATE TABLE IF NOT EXISTS `message_min_count` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` TINYINT NOT NULL COMMENT '统计维度:1=push,2=pop',
  `num` int(11)NOT NULL COMMENT '统计数据',
  `create_date` int(11) NOT NULL DEFAULT '0' COMMENT '消息创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci COMMENT='每分钟订单统计信息';