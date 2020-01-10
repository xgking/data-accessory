package com.car.cloud.data.event.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据指端注解
 *
 * @author wxg
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataEventField {
    /**
     * 字段描述
     *
     * @return
     */
    String value() default "";

    /**
     * 字段详细
     *
     * @return
     */
    String detailed() default "";
}
