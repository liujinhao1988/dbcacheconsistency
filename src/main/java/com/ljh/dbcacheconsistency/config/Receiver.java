package com.ljh.dbcacheconsistency.config;

import com.alibaba.fastjson.JSON;
import com.ljh.dbcacheconsistency.entity.PhoneInfo;
import com.ljh.dbcacheconsistency.repository.PhoneInfoRepository;
import com.ljh.dbcacheconsistency.util.RedisUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component

public class Receiver {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    PhoneInfoRepository phoneInfoRepository;

    // queues是指要监听的队列的名字
    @RabbitListener(queues = "direct_queue")
    public void receiverDirectQueue(String phoneInfoJsonSend) {
        PhoneInfo phoneInfo=JSON.parseObject(phoneInfoJsonSend,PhoneInfo.class);
        Jedis jedis=redisUtil.getJedis();
        Integer phoneId = phoneInfo.getPhoneId();


        Integer phoneStock = phoneInfo.getPhoneStock();
        if(phoneStock==null){

            PhoneInfo phoneInfoNew = phoneInfoRepository.findByPhoneId(phoneId);
            jedis.set("sku:" + phoneId + ":info", JSON.toJSONString(phoneInfoNew));


        }else{
            jedis.set("sku:" + phoneId + ":info", JSON.toJSONString(phoneInfo));
        }



    }

}