package com.keith.config.shiro.cache;

import com.keith.config.shiro.security.JwtProperties;
import com.keith.utils.JedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiroCacheManager implements CacheManager {

    @Autowired
    JedisUtils jedisUtils;

    @Autowired
    JwtProperties jwtProperties;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new ShiroCache<K,V>(jedisUtils,jwtProperties);
    }
}
