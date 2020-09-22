package org.github.keking.klock.lock;

import org.github.keking.klock.model.LockInfo;
import org.redisson.api.RedissonClient;
import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by kl on 2017/12/29.
 * Content :
 */
@Singleton
public class LockFactory  {

    @Inject
    RedissonClient redissonClient;

    public Lock getLock(LockInfo lockInfo){
        switch (lockInfo.getType()) {
            case NonFair:
                return new NonFairLock (redissonClient, lockInfo);
            case Read:
                return new ReadLock(redissonClient, lockInfo);
            case Write:
                return new WriteLock(redissonClient, lockInfo);
            default:
                return new FairLock(redissonClient, lockInfo);
        }
    }

}
