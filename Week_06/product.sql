CREATE TABLE `product` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` char(64) NOT NULL DEFAULT '',
  `name` varchar(1024) NOT NULL DEFAULT '',
  `attribute` varchar(1024) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `price` decimal(18,2) NOT NULL,
  `sales` int(11) NOT NULL DEFAULT '0',
  `state` tinyint(8) NOT NULL,
  `delete` tinyint(1) DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;