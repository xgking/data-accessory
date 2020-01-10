package com.car.cloud.data.event;

import java.io.Serializable;
import java.util.Map;

/**
 * 事件信息 用于存储事件关系信息 便于快速组装执行策略
 *
 * @author wxg
 */
public class DataEventInformation implements Serializable {
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 父级名称
     */
    private String superior;
    /**
     * 映射数据
     */
    private Map<String, String> mapping;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }
}
