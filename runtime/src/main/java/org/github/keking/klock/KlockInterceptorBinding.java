package org.github.keking.klock;

import org.github.keking.klock.model.LockType;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kl : http://kailing.pub
 * @version 1.0
 * @date 2020/9/22
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface KlockInterceptorBinding {

    @Nonbinding
    String name() default "";

    @Nonbinding
    String[] klockKeyParameterPositions() default {};

    @Nonbinding
    LockType lockType() default LockType.Fair;

    @Nonbinding
    long waitTime() default 60;

    @Nonbinding
    long leaseTime() default 60;
}
