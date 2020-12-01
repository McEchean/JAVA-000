CREATE TABLE `order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `uuid` varchar(64) NOT NULL DEFAULT '' COMMENT '唯一标识',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `product_id` int(11) NOT NULL COMMENT '产品id',
  `product_count` int(11) NOT NULL COMMENT '产品数量',
  `shipping_fee` decimal(18,2) NOT NULL COMMENT '邮费',
  `sum_fee` decimal(18,2) NOT NULL COMMENT '总金额',
  `real_fee` decimal(18,2) NOT NULL COMMENT '实付金额',
  `state` tinyint(8) NOT NULL COMMENT '订单状态',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `pay_time` datetime NOT NULL COMMENT '支付时间',
  `deal_time` datetime NOT NULL COMMENT '成交时间',
  `address_id` int(11) NOT NULL COMMENT '订单邮寄地址id',
  `snapshot_id` int(11) NOT NULL COMMENT '交易快照id',
  `delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`)
  KEY `IDX_PRUID` (`product_id`)
  KEY `IDX_UUID` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单交易表';