package com.car.cloud.data.event.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据事件注解
 *
 * @author wxg
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataEvent {
    /**
     * 事件名称
     *
     * @return
     */
    String value() default "";

    /**
     * 事件类别
     *
     * @return
     */
    String category() default "";

    /**
     * 事件描述
     *
     * @return
     */
    String describe() default "";

    /**
     * 忽略
     *
     * @return
     */
    boolean ignore() default false;
}
