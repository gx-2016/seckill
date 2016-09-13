package com.seckill.web;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExectuion;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.Seckill;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/3.
 */
@Controller
@RequestMapping("/seckill") //url:模块/资源/{id}/细分 /seckill/list
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> seckillList = seckillService.getSeckillList();
        model.addAttribute("list", seckillList);
        return "list";//WEB-INF/jsp/list.jsp
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (null == seckillId) {
            return "redirct:/seckill/list";
        }
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if (null == seckill) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId, Model model) {
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            return new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return new SeckillResult<Exposer>(false, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public SeckillResult<SeckillExectuion> execution(@PathVariable("seckillId") Long seckillId,
                                                     @PathVariable("md5") String md5,
                                                     @CookieValue(value = "killPhone", required = false) Long phone,
                                                     Model model) {
        if (phone == null) {
            return new SeckillResult<SeckillExectuion>(false, "未注册");
        }
        try {
            SeckillExectuion execution = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExectuion>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExectuion execution = new SeckillExectuion(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExectuion>(true, execution);
        } catch (SeckillCloseException e) {
            SeckillExectuion execution = new SeckillExectuion(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExectuion>(true, execution);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            SeckillExectuion execution = new SeckillExectuion(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExectuion>(true, execution);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    public SeckillResult<Long> time(Model model) {
        return new SeckillResult<Long>(true, new Date().getTime());
    }

}
