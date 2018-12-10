ALTER TABLE `customer` ADD `is_log` TINYINT NOT NULL DEFAULT 0 COMMENT '是否记录日志' after attempts;
ALTER TABLE `customer` ADD `priority` TINYINT NOT NULL DEFAULT 50 COMMENT '优先级:0=低,50=正常,100=高' after is_log;

ALTER TABLE `message` ADD `priority` TINYINT NOT NULL DEFAULT 50 COMMENT '优先级:0=低,50=正常,100=高' after status;