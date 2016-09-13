-- ���ݿ�ű���ʼ��

--�������ݿ�
CREATE DATABASE seckill;

--ʹ�����ݿ�
use seckill;

--������seckill
CREATE  TABLE seckill(
  'seckill_id' bigint not null  AUTO_INCREMENT COMMENT '��Ʒid',
  'name' VARCHAR (120) not null  COMMENT '��Ʒ����',
  'number' int  not null COMMENT '�������',
  'start_time' TIMESTAMP  not null  COMMENT '��ɱ����ʱ��',
  'end_time' TIMESTAMP  not null  COMMENT '��ɱ����ʱ��',
  'create_time' TIMESTAMP  not null DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  PRIMARY KEY(seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='��ɱ����'

--��ʼ������
INSERT INTO seckill('name','number','start_time','end_time')
VALUES
('1000��ɱiphone6','100','2016-9-1 00:00:00','2016-9-10 00:00:00'),
('500��ɱiphone','200','2016-9-1 00:00:00','2016-9-10 00:00:00'),
('300��ɱС��4','300','2016-9-1 00:00:00','2016-9-10 00:00:00'),
('200��ɱ����','400','2016-9-1 00:00:00','2016-9-10 00:00:00');


-- ��ɱ�ɹ���
-- �û������Ϣ
CREATE TABLE seccuss_killed(
  'seckill_id' bigint not null COMMENT '��Ʒid',
  'user_phone' bigint not null  COMMENT '�û��ֻ���',
  'state' tinyint  not null DEFAULT -1 COMMENT '-1��Ч 0�ɹ� 1�Ѹ��� 2 �ѷ���',
  'create_time'TIMESTAMP not null COMMENT '����ʱ��',
  PRIMARY KEY (seckill_id,user_phone),/*��������*/
  KEY idx_create_time(create_time)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='��ɱ�ɹ���ϸ��'

