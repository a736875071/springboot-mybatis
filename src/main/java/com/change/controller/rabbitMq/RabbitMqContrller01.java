package com.change.controller.rabbitMq;

import com.change.service.rabbitMq.config.Send;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangQing
 * @version 1.0.0
 */

@RestController
public class RabbitMqContrller01 {
    @Autowired
    private Send send;

    @RequestMapping(value = "rabbitmq/contract/direct", method = RequestMethod.GET)
    public void contractDirect(String content) {
        for (int i = 0; i < 10; i++) {
            send.sendMsg( content + i);
        }
    }

}
