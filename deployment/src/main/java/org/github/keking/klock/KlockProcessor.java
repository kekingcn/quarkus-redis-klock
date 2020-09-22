package org.github.keking.klock;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.AnnotationsTransformerBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;

import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.github.keking.klock.lock.LockFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author kl : http://kailing.pub
 * @version 1.0
 * @date 2020/9/21
 */
public class KlockProcessor {

    private static final String FEATURE = "klock";

    private static final Logger logger = LoggerFactory.getLogger(KlockProcessor.class);

    @BuildStep
    public void feature(BuildProducer<FeatureBuildItem> feature) {
        feature.produce(new FeatureBuildItem(FEATURE));
    }

    @BuildStep
    AnnotationsTransformerBuildItem annotationsTransformer() {
        return new AnnotationsTransformerBuildItem(new KlockAnnotationsTransformer());
    }

    @Record(ExecutionTime.STATIC_INIT)
    @BuildStep
    void load(BuildProducer<AdditionalBeanBuildItem> additionalBeans,KlockConfig config,KlockRecorder recorder) {
        if (config.enabled){
            logger.info("klock distributed lock is turned on");
            additionalBeans.produce(new AdditionalBeanBuildItem(LockFactory.class));
            additionalBeans.produce(new AdditionalBeanBuildItem(RedissonDefinition.class));
            AdditionalBeanBuildItem.Builder builder = AdditionalBeanBuildItem.builder();
            builder.addBeanClass(KlockInterceptor.class);
            additionalBeans.produce(builder.build());
        }else {
            logger.warn("Klock distributed lock is not turned on, you can add [quarkus.klock=true] to enable it");
        }
    }
}
