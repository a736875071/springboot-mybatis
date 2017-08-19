package com.change.test;

import redis.clients.jedis.Jedis;

/**
 * @author YangQing
 * @version 1.0.0
 */

public class redisConnection {
    static String constr = "127.0.0.1" ;
    public static void main(String[] args){
        Jedis jedis = new Jedis(constr,6379) ;
        System.out.println(jedis);
        String output ;
        jedis.set( "hello", "world" ) ;
        output = jedis.get( "hello") ;
        System. out.println(output) ;
    }
}