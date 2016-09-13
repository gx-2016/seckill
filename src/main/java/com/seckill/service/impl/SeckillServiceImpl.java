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

    //md5�ص㲻���棬������ֵ���������á�
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
        //ͨ��jedis������ʣ��ŵ㣺һ���Խ����ڳ�ʱ�Ļ�����
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            seckill = seckillDao.queryById(seckillId);
            if (null == seckill) {
                //������
                return new Exposer(false, seckillId);
            } else {
                //���뻺��
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
     * 1.�����Ŷ��γ�ͳһ��Լ��
     * 2.��֤����ִ��ʱ�価���ܲ��������⴩����������� RPC��HTTP����ĵ��û�������
     * 3.�������з�������Ҫ������ƣ���ֻ��һ���޸ġ�
     */
    public SeckillExectuion executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        //�ж�md5�Ƿ�һ��
        if (null == md5 || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data was rewrited!");
        }
        try {
            //��¼������Ϊ
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                throw new RepeatKillException("�ظ���ɱ��");
            } else {
                //�����,�ȵ���Դ�������м�������
                int updateCount = seckillDao.reduceNumber(seckillId, new Date());
                if (updateCount <= 0) {
                    throw new SeckillCloseException("��ɱ�ر�!");
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
            //��־
            //logger.log(e.getMessage());
            //���б������쳣ת��Ϊ����ʱ�쳣
            throw new SeckillException("seckill exception: " + e.getMessage());
        }
    }

    @Override
    public SeckillExectuion executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
        //�ж�md5�Ƿ�һ��
        if (null == md5 || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data was rewrited!");
        }
        Date kill_time = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("userPhone", userPhone);
        map.put("kill_time", kill_time);
        map.put("result", null);
        //ִ�д洢���̣�result����ֵ
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
