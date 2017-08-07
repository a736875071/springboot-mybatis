package com.service.cm.rabbitMq.config;

import org.springframework.stereotype.Service;

/**
 * @author YangQing
 * @version 1.0.0
 */
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    //模拟业务逻辑测试
    @Override
    public void rabbitMqTest(String id) {
        try {
            Thread.sleep(1000);
            System.out.println(id + "太累了，休息一秒钟");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
