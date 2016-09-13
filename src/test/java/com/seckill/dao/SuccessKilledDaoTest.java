package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    /**
     JDBC Connection will not be managed by Spring
     Preparing: insert ignore into success_killed (seckill_id,user_phone ) values (? ,? )
     insertSuccessKilled - ==> Parameters: 1000(Long), 15895979133(Long)
     insertSuccessKilled - <==    Updates: 1
     */
    @Test
    public void testInsertSuccessKilled() throws Exception {
     int result =  successKilledDao.insertSuccessKilled(1001,15895979133L);
     System.out.print("result:" + result);
    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {

        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1001,15895979133L);
        System.out.println(successKilled);
    }
}