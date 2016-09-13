package com.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.seckill.dao.SeckillDao;
import com.seckill.entity.Seckill;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Administrator on 2016/9/8.
 */
public class RedisDao {
    private final JedisPool jedisPool;
    private SeckillDao seckillDao;
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String host,int port ) {
        jedisPool = new JedisPool(host, port);
    }

    public Seckill getSeckill(Long seckillId) {
        //�ӻ����л�ȡObject(seckill)
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                /**Jedis�ڲ���û��ʵ�����л�
                 * ���Զ�������л���prostuff
                 *  class pojo����
                 */
                byte[] btyes = jedis.get(key.getBytes());
                if (btyes != null) {
                    //�ն���
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(btyes, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        //��Object��seckill�����뻺��
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60 * 60;
                String result = jedis.setex(key, timeout, bytes.toString());
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
