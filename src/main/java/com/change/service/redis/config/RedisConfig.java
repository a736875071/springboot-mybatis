package com.change.service.redis.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author YangQing
 * @version 1.0.0
 */
@Configuration
@EnableAutoConfiguration
public class RedisConfig {

    private static Logger logger = Logger.getLogger(RedisConfig.class);
    private static JedisPoolConfig config;//Jedis客户端池配置

    static {
        config = new JedisPoolConfig();
        config.setMaxTotal(60000);//设置最大连接数
        config.setMaxIdle(1000); //设置最大空闲数
        config.setMaxWaitMillis(3000);//设置超时时间
        config.setTestOnBorrow(true);
    }

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public JedisConnectionFactory getConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPoolConfig(config);
        factory.setPort(port);
        logger.info("JedisConnectionFactory bean init success.");
        return factory;
    }


    @Bean
    public RedisTemplate<?, ?> getRedisTemplate() {
        RedisTemplate<?, ?> template = new StringRedisTemplate(getConnectionFactory());
        return template;
    }
}
