package org.github.keking.klock.model;

/**
 * Created by kl on 2017/12/29.
 * Content :锁类型
 */
public enum LockType {
    NonFair,
    Fair,
    Read,
    Write;

    LockType() {
    }

}
