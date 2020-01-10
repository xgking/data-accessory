package com.car.cloud.data.event;

import java.io.Serializable;
import java.util.List;

/**
 * 数据接口描述信息 用于收集接口信息 便于执行策略管理
 *
 * @author wxg
 */
public class DataApiDetails implements Serializable {
    /**
     * 接口值
     */
    private String value;
    /**
     * 分组名称
     */
    private String category;
    /**
     * 接口描述
     */
    private String describe;
    /**
     * 忽略
     */
    private Boolean ignore;
    /**
     * 接受参数
     */
    private List<DataApiFieldDetails> inFields;
    /**
     * 返回参数
     */
    private List<DataApiFieldDetails> outFields;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<DataApiFieldDetails> getInFields() {
        return inFields;
    }

    public void setInFields(List<DataApiFieldDetails> inFields) {
        this.inFields = inFields;
    }

    public List<DataApiFieldDetails> getOutFields() {
        return outFields;
    }

    public void setOutFields(List<DataApiFieldDetails> outFields) {
        this.outFields = outFields;
    }

    public Boolean getIgnore() {
        return ignore;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }
}
