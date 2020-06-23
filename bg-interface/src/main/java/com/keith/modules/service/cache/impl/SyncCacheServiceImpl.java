package com.keith.modules.service.cache.impl;


import com.keith.common.Constants;
import com.keith.config.shiro.security.JwtProperties;
import com.keith.modules.service.cache.ISyncCacheService;
import com.keith.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyncCacheServiceImpl implements ISyncCacheService {

    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    JedisUtils jedisUtils;

    /**
     * 获取redis中key的锁，乐观锁实现
     * @param lockName
     * @param expireTime 锁的失效时间
     * @return
     */
    @Override
    public Boolean getLock(String lockName, int expireTime) {
        Boolean result = Boolean.FALSE;
        try {
            boolean isExist = jedisUtils.exists(lockName);
            if(!isExist){
                jedisUtils.getSeqNext(lockName,0);
                jedisUtils.expire(lockName,expireTime<=0? Constants.ExpireTime.ONE_HOUR:expireTime);
            }
            long reVal =  jedisUtils.getSeqNext(lockName,1);
            if(1l==reVal){
                //获取锁
                result = Boolean.TRUE;
            }else {
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 释放锁，直接删除key(直接删除会导致任务重复执行，所以释放锁机制设为超时30s)
     * @param lockName
     * @return
     */
    @Override
    public Boolean releaseLock(String lockName) {
        Boolean result = Boolean.FALSE;
        try {
            jedisUtils.expire(lockName, Constants.ExpireTime.TEN_SEC);

        } catch (Exception e) {

        }
        return result;
    }
}
