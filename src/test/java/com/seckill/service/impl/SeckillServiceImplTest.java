package com.seckill.service.impl;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExectuion;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Administrator on 2016/9/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    //    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        for (Seckill seckill : seckillList)
            System.out.println(seckill);
    }

    @Test
    public void testGetSeckillById() throws Exception {
        Seckill seckill = seckillService.getSeckillById(1000);
        System.out.println(seckill);
    }

    @Test
    public void testExecuteSeckill() throws Exception {
        long seckillId = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        try {
            if (exposer.isExposed()) {
                String MD5 = exposer.getMd5();
                long userPhone = 13813930047L;
                SeckillExectuion seckillExectuion = seckillService.executeSeckill(seckillId, userPhone, MD5);
                System.out.println(seckillExectuion);
            } else {
                System.out.println(exposer);
            }
        } catch (RepeatKillException e) {
            System.out.printf(e.getMessage());
        } catch (SeckillCloseException e) {
            System.out.printf(e.getMessage());
        }

    }
}