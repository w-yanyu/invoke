package cn.iyanyu.autoconfigure.mq;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ConditionalOnProperty(
    prefix = "app.mq.producer",
    name = {"enabled"},
    havingValue = "true",
    matchIfMissing = false
)
public @interface ConditionalOnProducerEnabled {
}