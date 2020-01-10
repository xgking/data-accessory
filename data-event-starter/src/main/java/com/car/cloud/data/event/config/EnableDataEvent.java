package com.car.cloud.data.event.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 激活数据事件装载相关信息
 *
 * @author wxg
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DataEventConfiguration.class)
public @interface EnableDataEvent {
}
