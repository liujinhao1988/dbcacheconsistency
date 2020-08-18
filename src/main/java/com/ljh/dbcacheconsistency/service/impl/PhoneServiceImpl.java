package com.ljh.dbcacheconsistency.service.impl;

import com.ljh.dbcacheconsistency.config.RabbitMQConfig;
import com.ljh.dbcacheconsistency.entity.PhoneInfo;
import com.ljh.dbcacheconsistency.repository.PhoneInfoRepository;
import com.ljh.dbcacheconsistency.service.PhoneService;
import com.ljh.dbcacheconsistency.util.RedisUtil;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;


@Service
public class PhoneServiceImpl implements PhoneService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    PhoneInfoRepository phoneInfoRepository;
    @Autowired
    AmqpTemplate amqpTemplate;


    @Override
    public void buyByPhoneId(Integer phoneId) {
        //连接缓存
        Jedis jedis = redisUtil.getJedis();
        String phoneKey = "sku:" + phoneId + ":info";
        //先删缓存再删数据库
        jedis.del(phoneKey);
        PhoneInfo phoneInfo = phoneInfoRepository.findByPhoneId(phoneId);
        Integer phoneStock=phoneInfo.getPhoneStock();
        Integer newPhoneStock=phoneStock-1;
        phoneInfo.setPhoneStock(newPhoneStock);
        phoneInfoRepository.save(phoneInfo);

//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        //jedis.set("sku:" + phoneId + ":info", JSON.toJSONString(phoneInfo));
        //然后把更新缓存的命令发送到rabbitmq消息队列中
        String phoneInfoJsonSend = JSON.toJSONString(phoneInfo);
        amqpTemplate.convertAndSend("direct_queue",phoneInfoJsonSend);




    }

    @Override
    public PhoneInfo findSpecsByPhoneId(Integer phoneId) {
        PhoneInfo phoneInfo=new PhoneInfo();
        Jedis jedis = redisUtil.getJedis();
        String phoneKey = "sku:" + phoneId + ":info";
        String phoneInfoJson = jedis.get(phoneKey);
        if (StringUtils.isNotBlank(phoneInfoJson)) {

            phoneInfo = JSON.parseObject(phoneInfoJson, PhoneInfo.class);
            return phoneInfo;
        }else{
            //若没有该缓存
            phoneInfo.setPhoneId(phoneId);
            //然后把更新缓存的命令发送到rabbitmq消息队列中
            String phoneInfoJsonSend = JSON.toJSONString(phoneInfo);
            amqpTemplate.convertAndSend("direct_queue",phoneInfoJsonSend);


            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return findSpecsByPhoneId(phoneId);
        }

    }
}
