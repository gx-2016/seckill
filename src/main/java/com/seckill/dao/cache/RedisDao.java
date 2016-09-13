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
        //从缓存中获取Object(seckill)
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                /**Jedis内部并没有实现序列化
                 * 用自定义的序列化：prostuff
                 *  class pojo类型
                 */
                byte[] btyes = jedis.get(key.getBytes());
                if (btyes != null) {
                    //空对象
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
        //把Object（seckill）放入缓存
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
