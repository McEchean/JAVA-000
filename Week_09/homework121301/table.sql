create table `t_usd` (
  `id` bigint(20) not null auto_increment comment 'id',
  `user_id` bigint(20) not null comment '用户id',
  `usd` decimal(18,2) not null default '0.00' comment '美元账户余额',
  `freeze` decimal(18,2) not null default '0.00' comment '冻结额度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


create table `t_rmb` (
  `id` bigint(20) not null auto_increment comment 'id',
  `user_id` bigint(20) not null comment '用户id',
  `rmb` decimal(18,2) not null default '0.00' comment '人民币账户余额',
  `freeze` decimal(18,2) not null default '0.00' comment '冻结额度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;