CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `uuid` char(64) NOT NULL DEFAULT '' COMMENT '用户唯一标识',
  `name` varchar(512) NOT NULL DEFAULT '' COMMENT '用户名称',
  `username` varchar(128) NOT NULL DEFAULT '' COMMENT '用户账号',
  `password` varchar(1024) NOT NULL DEFAULT '' COMMENT '用户密码，加密存储',
  `sex` tinyint(8) NOT NULL COMMENT '用户性别',
  `email` varchar(256) DEFAULT NULL COMMENT '用户邮箱，脱敏存储',
  `telephone` int(32) DEFAULT NULL COMMENT '用户手机号，脱敏存储',
  `delete` tinyint(1) DEFAULT '0' COMMENT '用户是否删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`)
  KEY `IDX_UUID` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户表';