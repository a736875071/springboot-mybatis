package com.controller.cm.rabbitMq;

import com.service.cm.rabbitMq.config.Send;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangQing
 * @version 1.0.0
 */

@RestController
@RequestMapping(value = "/rabbitmq")
public class RabbitMqContrller01 {
    @Autowired
    private Send send;

    @RequestMapping(value = "contract/direct", method = RequestMethod.GET)
    public void contractDirect(String content) {
        for (int i = 0; i < 10; i++) {
            send.sendMsg("ssss" + content + i);
        }
    }

}
