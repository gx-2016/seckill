-- 数据库脚本初始化

--创建数据库
CREATE DATABASE seckill;

--使用数据库
use seckill;

--创建表seckill
CREATE  TABLE seckill(
  'seckill_id' bigint not null  AUTO_INCREMENT COMMENT '商品id',
  'name' VARCHAR (120) not null  COMMENT '商品名称',
  'number' int  not null COMMENT '库存数量',
  'start_time' TIMESTAMP  not null  COMMENT '秒杀开启时间',
  'end_time' TIMESTAMP  not null  COMMENT '秒杀结束时间',
  'create_time' TIMESTAMP  not null DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表'

--初始化数据
INSERT INTO seckill('name','number','start_time','end_time')
VALUES
('1000秒杀iphone6','100','2016-9-1 00:00:00','2016-9-10 00:00:00'),
('500秒杀iphone','200','2016-9-1 00:00:00','2016-9-10 00:00:00'),
('300秒杀小米4','300','2016-9-1 00:00:00','2016-9-10 00:00:00'),
('200秒杀红米','400','2016-9-1 00:00:00','2016-9-10 00:00:00');


-- 秒杀成功表
-- 用户身份信息
CREATE TABLE seccuss_killed(
  'seckill_id' bigint not null COMMENT '商品id',
  'user_phone' bigint not null  COMMENT '用户手机号',
  'state' tinyint  not null DEFAULT -1 COMMENT '-1无效 0成功 1已付款 2 已发货',
  'create_time'TIMESTAMP not null COMMENT '创建时间',
  PRIMARY KEY (seckill_id,user_phone),/*联合主键*/
  KEY idx_create_time(create_time)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表'

