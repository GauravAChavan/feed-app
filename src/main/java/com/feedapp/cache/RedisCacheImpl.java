package com.feedapp.cache;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheImpl implements Cache {


    @Value("${caching.redis.server_address}")
    private String serverAddress;

    private static RedissonClient client;

    @PostConstruct
    private void initRedisClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(serverAddress);
        client = Redisson.create(config);
    }


    @Override
    public <T> void setCache(String key, T value, long expiryTime) {
        client.getBucket(key).set(value, expiryTime, TimeUnit.MINUTES);
    }

    @Override
    public <T> T getCache(String key) {
        return (T) client.getBucket(key).get();
    }


}
