package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/1.
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id号查询秒杀对象实体
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 查询秒杀对象列表
     * java中不保存形参的名字 agrs0、args1 所以用 @Param
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset ,@Param("limit") int limit);
    /**
     * 存储过程执行秒杀
     */
    void killSeckillByProcedure(Map<String,Object> map);
}
