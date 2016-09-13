package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExectuion;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/2.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    //    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private RedisDao redisDao;

    //md5特点不可逆，加入盐值，混淆作用。
    private String salt = "JLHBJKDWS^(**(ng(&*&*5";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //通过jedis缓存访问：优点：一致性建立在超时的基础上
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            seckill = seckillDao.queryById(seckillId);
            if (null == seckill) {
                //不存在
                return new Exposer(false, seckillId);
            } else {
                //放入缓存
                redisDao.putSeckill(seckill);
            }
        }
        Date nowTime = new Date();
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        if (nowTime.getTime() > endTime.getTime() || nowTime.getTime() < startTime.getTime()) {
            return new Exposer(false, nowTime, startTime, endTime);
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    /**
     * 1.开发团队形成统一的约定
     * 2.保证方法执行时间尽可能操作，避免穿插网络操作、 RPC、HTTP请求的调用或剥离出来
     * 3.不是所有方法都需要事务控制，如只有一行修改。
     */
    public SeckillExectuion executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        //判断md5是否一致
        if (null == md5 || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data was rewrited!");
        }
        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                throw new RepeatKillException("重复秒杀！");
            } else {
                //减库存,热点资源竞争，行级锁开启
                int updateCount = seckillDao.reduceNumber(seckillId, new Date());
                if (updateCount <= 0) {
                    throw new SeckillCloseException("秒杀关闭!");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExectuion(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            //日志
            //logger.log(e.getMessage());
            //所有编译期异常转换为运行时异常
            throw new SeckillException("seckill exception: " + e.getMessage());
        }
    }

    @Override
    public SeckillExectuion executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
        //判断md5是否一致
        if (null == md5 || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data was rewrited!");
        }
        Date kill_time = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("userPhone", userPhone);
        map.put("kill_time", kill_time);
        map.put("result", null);
        //执行存储过程，result被赋值
        try {
            seckillDao.killSeckillByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExectuion(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExectuion(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            e.getMessage();
            return new SeckillExectuion(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}
