package org.github.keking.klock;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.arc.runtime.InterceptorBindings;
import jodd.util.StringUtil;
import org.github.keking.klock.model.LockInfo;
import org.github.keking.klock.model.LockType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by kl on 2017/12/29.
 */

public final class LockInfoProviderUtils {

    private static final String LOCK_NAME_PREFIX = "klock";
    public static final String LOCK_NAME_SEPARATOR = "#";

    private static final Logger logger = LoggerFactory.getLogger(LockInfoProviderUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private LockInfoProviderUtils() {
    }

    public static LockInfo get(InvocationContext invoca) {
        KlockInterceptorBinding klock = LockInfoProviderUtils.getKlock(invoca);
        if (klock == null) {
            throw new NullPointerException("@Klock lock exception, no lock instance is obtained");
        }
        LockType type = klock.lockType();
        //锁的名字，锁的粒度就是这里控制的
        String lockName = LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR + getName(klock.name(), invoca) + buildBusinessKey(klock, invoca);
        long waitTime = klock.waitTime();
        long leaseTime = klock.leaseTime();
        //如果占用锁的时间设计不合理，则打印相应的警告提示
        if (leaseTime == -1 && logger.isWarnEnabled()) {
            logger.warn("Trying to acquire Lock({}) with no expiration, " +
                    "Klock will keep prolong the lock expiration while the lock is still holding by current thread. " +
                    "This may cause dead lock in some circumstances.", lockName);
        }
        return new LockInfo(type, lockName, waitTime, leaseTime);
    }

    /**
     * 获取业务Key
     */
    private static String buildBusinessKey(KlockInterceptorBinding klock, InvocationContext invoca) {
        String[] keyParameterPositions = klock.klockKeyParameterPositions();
        Object[] methodParameterValues = invoca.getParameters();
        if (methodParameterValues.length == 0) {
            return "";
        } else {
            List<Object> keyElements = new ArrayList<>();
            if (keyParameterPositions.length > 0) {
                for (String keyParameterPosition : keyParameterPositions) {
                    String[] keyParamets = keyParameterPosition.split(LOCK_NAME_SEPARATOR);
                    int methodParameterIndex = Integer.parseInt(keyParamets[0]);
                    Object parame = methodParameterValues[methodParameterIndex];
                    if (keyParamets.length == 2) {
                        try {
                            String fieldName = keyParameterPosition.split(LOCK_NAME_SEPARATOR)[1];
                            JsonNode node = mapper.readTree(mapper.writeValueAsString(parame)).get(fieldName);
                            if (node != null) {
                                keyElements.add(node.asText());
                            }
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        keyElements.add(parame);
                    }
                }
            }

            return StringUtil.join(keyElements, LOCK_NAME_SEPARATOR);
        }
    }

    /**
     * 获取锁的name，如果没有指定，则按全类名拼接方法名处理
     */
    private static String getName(String annotationName, InvocationContext invoca) {
        if (annotationName.isEmpty()) {
            return String.format("%s$%s", invoca.getTarget().getClass().getName(), invoca.getMethod().getName());
        } else {
            return annotationName;
        }
    }

    private static KlockInterceptorBinding getKlock(InvocationContext invoca) {
        Set<Annotation> annotations = InterceptorBindings.getInterceptorBindings(invoca);
        for (Annotation annotation : annotations) {
            if (annotation instanceof KlockInterceptorBinding) {
                return (KlockInterceptorBinding) annotation;
            }
        }
        return null;
    }
}
