package org.github.keking.klock;


import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Created by kl on 2017/12/29.
 */
@ConfigRoot(name = "klock", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class KlockConfig {

    /**
     * 是否开启
     */
    @ConfigItem(name = ConfigItem.PARENT, defaultValue = "false")
    public boolean enabled;

    /**
     * redis配置
     */
    public RedisConfig redis;

    @ConfigGroup
    public static class RedisConfig {

        /**
         * 链接地址
         */
        @ConfigItem
        public Optional<String> address;

        /**
         * 链接密码
         */
        @ConfigItem
        public Optional<String> password;

        /**
         * db index
         */
        @ConfigItem(defaultValue = "0")
        public OptionalInt database;

    }
}
