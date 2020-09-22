package org.github.keking.klock;

import io.netty.channel.nio.NioEventLoopGroup;
import io.quarkus.arc.DefaultBean;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author kl : http://kailing.pub
 * @version 1.0
 * @date 2020/9/21
 */
@ApplicationScoped
public class RedissonDefinition {


   private final KlockConfig klockConfig;

    public RedissonDefinition(KlockConfig klockConfig) {
        this.klockConfig = klockConfig;
    }

    @Produces
    @ApplicationScoped
    @DefaultBean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(klockConfig.redis.address.get())
                .setDatabase(klockConfig.redis.database.getAsInt())
                .setPassword(klockConfig.redis.password.orElse(null));
        config.setCodec(new JsonJacksonCodec());
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

}
