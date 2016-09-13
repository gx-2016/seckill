package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2016/9/1.
 */
public interface SuccessKilledDao {
    /**
     * ����ɹ���ɱ��¼
     * @param seckillId
     * @param userPhone
     * @return������Ľ������
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId ,@Param("userPhone")long userPhone);

    /**
     * ����id��ѯ��ɱ�ɹ���¼
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId ,@Param("userPhone")long userPhone);
}
