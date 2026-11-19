package cn.iyanyu.autoconfigure.mq;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ConditionalOnProperty(
        prefix = "app.mq.consumer",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = false
)
public @interface ConditionalOnConsumerEnabled {
}
