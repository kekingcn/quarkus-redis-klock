package org.github.keking.klock;

import org.github.keking.klock.model.LockType;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author kl
 * @date 2017/12/29
 * Content :加锁注解
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@InterceptorBinding
public @interface Klock {
    /**
     * 锁的名称
     * @return name
     */
    String name() default "";
    /**
     * 锁类型，默认公平锁
     * @return lockType
     */
    LockType lockType() default LockType.Fair;
    /**
     * 尝试加锁，最多等待时间
     * @return waitTime
     */
    long waitTime() default 60;
    /**
     *上锁以后xxx秒自动解锁
     * @return leaseTime
     */
    long leaseTime() default 60;

}
