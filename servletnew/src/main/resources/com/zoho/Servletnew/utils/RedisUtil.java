package com.zoho.Servletnew.utils;

import redis.clients.jedis.Jedis;

public class RedisUtil {
    private static Jedis jedis;

    public static Jedis getRedisConnection() {
        if (jedis == null) {
            jedis = new Jedis("localhost", 6379); // host and port of Redis server
        }
        return jedis;
    }

    public static void closeConnection() {
        if (jedis != null) {
            jedis.close();
        }
    }
}
