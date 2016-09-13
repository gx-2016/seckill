package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExectuion;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;

import java.util.List;

/**
 * վ��"ʹ����"�ĽǶ���ƽӿ�
 * ���������ȣ���ȷ��������������ֵ(return(dto vo)/exception��ҵ���쳣��)
 * Created by Administrator on 2016/9/2.
 */
public interface SeckillService {

    /**
     * ��ѯ���е���ɱ�б�
     * @return
     */
     List<Seckill> getSeckillList();

    /**
     * ��ѯ������ɱ��¼
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(long seckillId);

    /**
     * ��ɱ��ʼ��ʱ�򣬱�¶��ɱ�ӿڵĵ�ַ���������ϵͳʱ�����ɱ��ʼʱ��
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    SeckillExectuion executeSeckill(long seckillId ,long userPhone ,String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;

    SeckillExectuion executeSeckillByProcedure(long seckillId ,long userPhone ,String md5);



}
