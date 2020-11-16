package com.service;

import com.entry.KillGoods;
import com.entry.KillOrderInfo;
import com.entry.OrderInfo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
/*
* 使用mq的作用
* 系统之间的解耦
* 异步通信
* 高并发 减少消费者和数据库的压力
* */
@Service
public class RabbitConsumer {
    @Autowired
    KillGoodsService service;
    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    KillOrderInfoService killOrderInfoService;

    //下订单的消费者
    @Transactional
    //创建交换机队列以及绑定
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("seckill-queue"),exchange = @Exchange("seckill-exchange"),key ="seckill-queue" ))
    //如果没有队列 自动创建队列并监听
    //@RabbitListener(queuesToDeclare=@Queue("seckill-queue"))
        //只是监听
    @RabbitListener(queues = "seckill-queue")
    public void doOrder(Map map, Channel channel, Message message) {
        System.out.println("这是map里的数据------" + map);
        int userid = (int) map.get("userid");
        int killid = (int) map.get("killid");
        //查看商品信息
        KillGoods killGoods = service.selectById(killid);
        //处理真是库存 和下订单
        int result = service.killByKillid(killid);
        if (result != 1) {
            System.out.println("库存扣减失败");
        } else {
            System.out.println("库存扣减成功");
            //创建订单和秒杀订单
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setUserid(userid); //用户id
            orderInfo.setGoodsid(killGoods.getGoodsid()); //商品id
            orderInfo.setGoodsname(killGoods.getGoodsname());//商品名
            orderInfo.setGoodscount(1); //商品数量
            orderInfo.setGoodsprice(killGoods.getKillprice());//秒杀价
            orderInfo.setCreatedate(new Date());//订单日期
            orderInfo.setOrderstatus(0);//状态
            orderInfoService.save(orderInfo);//保存订单
            System.out.println("这是订单信息****" + orderInfo);
            //创建秒杀订单
            KillOrderInfo killOrderInfo = new KillOrderInfo();
            killOrderInfo.setOrderid(orderInfo.getOrderid());
            killOrderInfo.setGoodsid(killGoods.getGoodsid()); //商品id
            killOrderInfo.setUserid(userid);//用户iD
            killOrderInfoService.save(killOrderInfo);//保存秒杀订单

            //手动进行签收
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            try {
                channel.basicAck(deliveryTag,false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("创建订单成功");
        }

        //现获取库存数，减-1，最后更新

    }
}
