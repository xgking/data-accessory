package com.car.cloud.data.event;

import java.util.Map;

/**
 * 数据事件通用接口处理
 *
 * @author wxg
 */
public interface DataEventDefinition {

    /**
     * 事件执行
     *
     * @param mainData
     * @param mappingData
     * @return
     */
    ExecutionResult perform(Map<String, Object> mainData, Map<String, String> mappingData);
}
