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
     * �����
     * @param seckillId
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * ����id�Ų�ѯ��ɱ����ʵ��
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * ��ѯ��ɱ�����б�
     * java�в������βε����� agrs0��args1 ������ @Param
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset ,@Param("limit") int limit);
    /**
     * �洢����ִ����ɱ
     */
    void killSeckillByProcedure(Map<String,Object> map);
}
