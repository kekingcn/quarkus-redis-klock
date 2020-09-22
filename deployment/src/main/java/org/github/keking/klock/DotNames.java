package org.github.keking.klock;

import org.jboss.jandex.DotName;

/**
 * @author kl : http://kailing.pub
 * @version 1.0
 * @date 2020/9/22
 */
public class DotNames {

    public static final DotName KLOCK = dotName(Klock.class);
    public static final DotName KLOCK_KEY = dotName(KlockKey.class);

    public static final String NAME_PARAM = "name";
    public static final String KLOCK_KEY_PARAMETER_POSITIONS_PARAM = "klockKeyParameterPositions";
    public static final String LOCK_TYPE_PARAM = "lockType";
    public static final String WAIT_TIME_PARAM = "waitTime";
    public static final String LEASE_TIME_PARAM = "leaseTime";
    public static final String KLOCK_KEY_VALUE_PARAM = "fieldName";

    private static DotName dotName(Class<?> annotationClass) {
        return DotName.createSimple(annotationClass.getName());
    }
}
