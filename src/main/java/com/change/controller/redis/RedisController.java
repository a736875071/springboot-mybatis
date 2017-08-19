package com.change.controller.redis;

import com.change.service.redis.IRedisService;
import com.change.domain.redis.ResponseModal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangQing
 * @version 1.0.0
 */
@RestController
public class RedisController {


    @Autowired
    private IRedisService redisService;


    @RequestMapping("/redis/set")
    public ResponseModal redisSet(@RequestParam("value") String value) {
        boolean isOk = redisService.set("name", value);
        System.out.println();
        return new ResponseModal(isOk ? 200 : 500, isOk, isOk ? "success" : "error", null);
    }

    @RequestMapping("/redis/get")
    public ResponseModal redisGet() {
        String name = redisService.get("name");
        return new ResponseModal(200, true, "success", name);
    }

}
