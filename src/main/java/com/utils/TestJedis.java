package com.utils;

import redis.clients.jedis.Jedis;

public class TestJedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.104.62");
        //jedis.set("seckill:stock:5","3");
        String script="if redis.call('get',KEYS[1])>'0'  then  redis.call('decr',KEYS[1])   end";

        jedis.eval(script,1,"seckill:stock:"+5);
        System.out.println(jedis.get("seckill:stock:5"));
    }
}
