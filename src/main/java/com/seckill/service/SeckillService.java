package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExectuion;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;

import java.util.List;

/**
 * 站在"使用者"的角度设计接口
 * 方法的粒度（明确），参数，返回值(return(dto vo)/exception（业务异常）)
 * Created by Administrator on 2016/9/2.
 */
public interface SeckillService {

    /**
     * 查询所有的秒杀列表
     * @return
     */
     List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(long seckillId);

    /**
     * 秒杀开始的时候，暴露秒杀接口的地址，否则输出系统时间和秒杀开始时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    SeckillExectuion executeSeckill(long seckillId ,long userPhone ,String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;

    SeckillExectuion executeSeckillByProcedure(long seckillId ,long userPhone ,String md5);



}
