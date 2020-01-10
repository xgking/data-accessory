package com.car.cloud.data.event;

import java.io.Serializable;

/**
 * 数据接口描述信息 用于收集接口信息 便于执行策略管理
 *
 * @author wxg
 */
public class DataApiFieldDetails implements Serializable {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 字段详细
     */
    private String describe;
    /**
     *
     */
    private String detailed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }
}
