package com.controller;

import com.alibaba.fastjson.JSON;
import com.entry.KillGoods;
import com.service.KillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seckill")
public class KillGoodController {
    @Autowired
    KillGoodsService service;
    @Autowired(required = false)
    ThymeleafViewResolver viewResolver;
    @Autowired
    StringRedisTemplate template;

    //秒杀商品查询列表
    @RequestMapping("/list")
    public String list(Model model) {
        List<KillGoods> killGoodsList = service.selectAll();

        //自动渲染
        model.addAttribute("killgoods", killGoodsList);
        return "goodsList";
    }

    //thymeleaf手动渲染 最后存html源码 缓存到redis中 在返回html源码
    @ResponseBody
    @RequestMapping("/tolist")
    public String tolist(Model model, HttpServletRequest request, HttpServletResponse response) {
        String html = "";
        ValueOperations<String, String> redis = template.opsForValue();
        //获取redis中秒杀列表页的缓存 如果有 返回缓存，没有继续执行
        html = redis.get("seckill:page:list");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //取数据
        List<KillGoods> killGoodsList = service.selectAll();
        model.addAttribute("killgoods", killGoodsList);
        //手动渲染 ---->>>拿到源码和数据结合的源码 参数：模板名称 上下文
        WebContext webContext = new WebContext(request, response, request.getServletContext(), response.getLocale(), model.asMap());
        html = viewResolver.getTemplateEngine().process("goodsList", webContext);
        System.out.println("********" + html);
        if (!StringUtils.isEmpty(html)) {
            redis.set("seckill:page:list", html, 180, TimeUnit.SECONDS);
        }
        return html;

    }

    //查询商品详情
    @ResponseBody
    @RequestMapping("/view/{killid}")
    public String toView(@PathVariable("killid") Integer killid,
                         Model model, HttpServletRequest request, HttpServletResponse response) {
        String html = "";
        ValueOperations<String, String> redis = template.opsForValue();
        //获取redis中秒杀列表页的缓存 如果有 返回缓存，否则继续执行
        html = redis.get("seckill:page:goods" + killid);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //取数据
        KillGoods goods = service.selectById(killid);
        Random random = new Random();
        long notime = random.nextInt(5) + 1;
        //killid不存在 恶意高频访问 造成缓存穿透 解决 将不存在的id也存起来 设置失效时间
        if (goods == null) {
            redis.set("seckill:page:goods" + killid, "no record", notime, TimeUnit.MINUTES);
            return html; //返回一个空字符串
        }

        model.addAttribute("goods", goods);
        //秒杀倒计时
        //秒杀状态量
        int killStatus = 0;
        //开始时间倒计时
        int remainSeconds = 0;
        //获取当前时间
        long now = System.currentTimeMillis();
        long startAt = goods.getStartdate().getTime();
        long endAt = goods.getEnddate().getTime();
        if (now < startAt) {//秒杀还没开始，倒计时
            killStatus = 0;
            remainSeconds = 1;
            //(int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            killStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            killStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("killStatus", killStatus);

        //手动渲染 ---->>>拿到源码和数据结合的源码 参数：模板名称 上下文
        WebContext webContext = new WebContext(request, response, request.getServletContext(), response.getLocale(), model.asMap());
        html = viewResolver.getTemplateEngine().process("goodsView", webContext);
        System.out.println("********" + html);
        if (!StringUtils.isEmpty(html)) {
            //设置失效时间 防止失效时间相同导致雪崩

            long time = random.nextInt(5) + 1;
            //详情页缓存到redis
            redis.set("seckill:page:goods" + killid, html, time, TimeUnit.MINUTES);
            //秒杀商品的数据缓存
            redis.set("seckill:stock:" + killid, String.valueOf(goods.getStockcount()));

            redis.set("seckill:goods" + killid, JSON.toJSONString(goods));

        }
        return html;
    }

    @ResponseBody
    @RequestMapping("/getKillid")
    public int getKillid(int killid) {
        //查redis库存
        String stock = template.opsForValue().get("seckill:stock:" + killid);
        return Integer.parseInt(stock);

    }
}
