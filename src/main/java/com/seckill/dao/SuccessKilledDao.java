package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2016/9/1.
 */
public interface SuccessKilledDao {
    /**
     * 插入成功秒杀记录
     * @param seckillId
     * @param userPhone
     * @return　插入的结果行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId ,@Param("userPhone")long userPhone);

    /**
     * 根据id查询秒杀成功记录
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId ,@Param("userPhone")long userPhone);
}
