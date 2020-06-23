package com.keith.modules.service.cache;

public interface ISyncCacheService {

	Boolean getLock(String lockName, int expireTime);

	Boolean releaseLock(String lockName);
}
