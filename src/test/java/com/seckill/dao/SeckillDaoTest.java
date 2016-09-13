package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 * 配置spring 和junit 整合 ，junit整合时自动加载sprig容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void testQueryById() throws Exception {
        long id =1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void testQueryAll() throws Exception {
        List<Seckill> seckillList = seckillDao.queryAll(0,4);
        System.out.println(seckillList.get(0).getName());
    }

    @Test
    /**
        JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@1172d60]will not be managed by Spring
        Preparing: update seckill set number = number -1 where seckill_id =? and start_time <= ? and end_time >= ? and number > 0;
        Parameters: 1000(Long), 2016-09-02 13:06:30.728(Timestamp), 2016-09-02 13:06:30.728(Timestamp)
        Updates: 1
     */
    public void testReduceNumber() throws Exception {

        int update = seckillDao.reduceNumber(1000,new Date());
        System.out.print("update:"+update);
    }

}