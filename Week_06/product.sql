CREATE TABLE `product` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '产品id',
  `uuid` char(64) NOT NULL DEFAULT '' COMMENT '产品唯一标识',
  `name` varchar(1024) NOT NULL DEFAULT '' COMMENT '产品名称',
  `attribute` varchar(1024) DEFAULT NULL COMMENT '产品属性',
  `type` int(11) NOT NULL COMMENT '产品类型',
  `price` decimal(18,2) NOT NULL COMMENT '产品单价',
  `sales` int(11) NOT NULL DEFAULT '0' COMMENT '产品销售量',
  `state` tinyint(8) NOT NULL COMMENT '产品状态',
  `delete` tinyint(1) DEFAULT '0' COMMENT '产品是否已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改时间',
  PRIMARY KEY (`id`)
  KEY `IDX_UUID` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '商品表';