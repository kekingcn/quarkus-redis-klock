package org.github.keking.klock;

import org.github.keking.klock.exception.KlockWaitException;
import org.github.keking.klock.lock.Lock;
import org.github.keking.klock.lock.LockFactory;
import org.github.keking.klock.model.LockInfo;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;


/**
 * @author kl : http://kailing.pub
 * @version 1.0
 * @date 2020/9/21
 */
@KlockInterceptorBinding
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 2)
public class KlockInterceptor{

    @Inject
    LockFactory lockFactory;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        LockInfo lockInfo = LockInfoProviderUtils.get(context);
        Lock lock = lockFactory.getLock(lockInfo);
        boolean lockRes = lock.acquire();
        Object result;
        if(lockRes){
            result = context.proceed();
            lock.release();
        }else {
            throw new KlockWaitException("["+lockInfo.getName() + "]Lock wait["+lockInfo.getWaitTime()+"S]timeOut");
        }
        return result;
    }

}
