package com.controller;

import com.alibaba.fastjson.JSON;
import com.entry.KillGoods;
import com.entry.Userinfo;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class KillController {

    private static volatile KillController INSTANCE;
    @Autowired
    StringRedisTemplate template;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmqpAdmin amqpAdmin;

    //秒杀业务
    @ResponseBody
    @RequestMapping("/seckill/kill/{killid}")
    public String kill(@PathVariable("killid") Integer killid, HttpSession session) {
        ValueOperations<String, String> opsForValue = template.opsForValue();
        SetOperations<String, String> opsForSet = template.opsForSet();
        //1、用户是否登录 没有提示登录
        Userinfo user = (Userinfo) session.getAttribute("user");
        if (user != null) {
            //2、判断商品是不是在秒杀进行中
            String json = opsForValue.get("seckill:goods" + killid);
            KillGoods killgoods = JSON.parseObject(json, KillGoods.class);
            long now = new Date().getTime();
            long startAt = killgoods.getStartdate().getTime();
            long endAt = killgoods.getEnddate().getTime();
            if (now < startAt) {
                return "秒杀未开始";
            } else if (now > endAt) {
                return "秒杀已结束";
            }
            //3、该用户是否秒杀过了这个商品
            Boolean result = opsForSet.isMember("seckill:order", user.getUserid() + ":" + killid);
            if (result) {
                return "已经秒杀过了 请把机会让给别人吧";
            }

            if (INSTANCE == null) {
                synchronized (KillController.class) {
                    if (INSTANCE == null) {
                        //4、查看库存的多少
                        int stock = Integer.parseInt(opsForValue.get("seckill:stock:" + killid));
                        if (stock <= 0) {
                            return "该商品已经售罄";
                        }
                        //5、如果有预设库存则decrement操作  抢到了该商品 库存-1
                        Long decrement = opsForValue.decrement("seckill:stock:" + killid);
                        if (decrement >= 0) {
                            System.out.println(decrement);
                            System.out.println("预设库存扣减成功");

                        }
                    }
                }
            }
            ////4、查看库存的多少
            //int stock = Integer.parseInt(opsForValue.get("seckill:stock:" + killid));
            //if (stock <= 0) {
            //    return "该商品已经售罄";
            //}
            ////5、如果有预设库存则decrement操作  抢到了该商品 库存-1
            //Long decrement = opsForValue.decrement("seckill:stock:" + killid);
            //if (decrement >= 0) {
            //    System.out.println(decrement);
            //    System.out.println("预设库存扣减成功");
            //}
            //执行luo脚本 预防并发带来的问题
            //Jedis jedis = new Jedis("192.168.104.62");
            //String script = "if redis.call('get',KEYS[1])>'0'  then  redis.call('decr',KEYS[1])   end";
            //
            //jedis.eval(script, 1, "seckill:stock:" + killid);
            //String stock = jedis.get("seckill:stock:" + killid);
            //if ("0".equals(stock)) {
            //    return "抱歉，该商品已经售罄";
            //
            //} else {
            //    Jedis jedis = new Jedis("192.168.104.62");
            //    jedis.auth("123456");
                //String script = "if redis.call('get',KEYS[1])>'0'  then  redis.call('decr',KEYS[1])   end";
                //
                //jedis.eval(script, 1, "seckill:stock:" + killid);
                //String stock = jedis.get("seckill:stock:" + killid);
                //6、将用户秒杀商品记录存进redis中
                opsForSet.add("seckill:order", user.getUserid() + ":" + killid);
                //发送下订单的消息
                Map map = new HashMap();
                map.put("userid", user.getUserid());
                map.put("killid", killid);
                //创建交换机  队列 路由键
                DirectExchange exchange = new DirectExchange("seckill-exchange", true, false);
                amqpAdmin.declareExchange(exchange);
                Queue queue = new Queue("seckill-queue", true, false, false);
                amqpAdmin.declareQueue(queue);
                Binding binding = new Binding("seckill-queue", Binding.DestinationType.QUEUE, "seckill-exchange", "seckill-queue", null);
                amqpAdmin.declareBinding(binding);
                //发送消息
                rabbitTemplate.convertAndSend("seckill-exchange", "seckill-queue", map);

                return "预设秒杀成功,正在处理你的请求，请稍等";
        }
        //}
        else {
            return "<script>location.href='/pages/login.html'</script>";
        }
    }
}