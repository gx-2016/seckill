-- ִ����ɱ�Ĵ洢����
-- row_count()�޸ĵ�����
DELIMITER $$ --console ���з�; ת��Ϊ$$

-- ����洢����

CREATE  PROCEDURE seckill.execute_seckill(
  in v_seckill_id bigint,in v_phone bigint,
  in v_kill_time timestamp ,
  out r_result int
)
BEGIN
    DECLARE insert_count int DEFAULT 0;
    START TRANSACTION
    insert into success_killed
     (seckill_id,user_phone,create_time)
     values (v_seckill_id,v_phone,v_kill_time);
    select row_count() into insert_count;
    IF(insert_count = 0)THEN
      ROLLBACK;
      set r_result = -1;
    ELSEIF (insert_count < 0)THEN
      ROLLBACK;
      set r_result = -2;
    ELSE
      update success_killed set number = number -1
      where seckill_id = v_seckill_id
      and start_time > v_kill_time
      and end_time < v_kill_time
      and number > 0
      select row_count() into insert_count;
      IF (insert_count = 0)THEN
        ROLLBACK;
        set r_result = 0;
      ELSEIF(insert_count < 0)THEN
        ROLLBACK;
        set r_result = -2;
      ELSE
        COMMIT ;
      ENDIF
    ENDIF
END
$$

set @r_result = -3;

--ִ�д洢����
call seckill.execute_seckill(1001,15895979133,now(),@r_result);
-- ���
SELECT @r_result;