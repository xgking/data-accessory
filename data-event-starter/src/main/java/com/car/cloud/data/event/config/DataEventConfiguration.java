package com.car.cloud.data.event.config;

import com.car.cloud.data.event.DataEventDefinition;
import com.car.cloud.data.event.DataEventHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * 在启用 EnableDataEvent时,根据spring扫描机制,definitionMap将得到值,这将用于DataEventHelper的初始化
 *
 * @author wxg
 */
public class DataEventConfiguration {
    @Autowired
    private Map<String, DataEventDefinition> definitionMap;

    @Bean
    CommandLineRunner initData() {
        return args -> DataEventHelper.init(this.definitionMap);

    }
}
