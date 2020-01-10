package com.car.cloud.data.event;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 事件节点
 *
 * @author wxg
 */
public class DataEventNode {
    /**
     * 当前事件名称
     */
    private String self;
    /**
     * 上级
     */
    private String superior;
    /**
     * 下级节点
     */
    private List<DataEventNode> lowers;
    /**
     * 当前事件
     */
    private DataEventDefinition event;
    /**
     * 映射数据
     */
    private Map<String, String> mapping;
    /**
     * 事件内数据
     */
    private Map<String, Object> data;
    /**
     * 事件详情
     */
    private DataApiDetails details;
    /**
     * 接受参数
     */
    private List<DataApiFieldDetails> inFields;
    /**
     * 返回参数
     */
    private List<DataApiFieldDetails> outFields;

    /**
     * 无参构造方法
     */
    private DataEventNode() {
    }

    public String getSelf() {
        return self;
    }

    public String getSuperior() {
        return superior;
    }

    /**
     * 接受参数
     *
     * @return
     */
    public List<DataApiFieldDetails> inFields() {
        return inFields;
    }

    /**
     * 返回参数
     *
     * @return
     */
    public List<DataApiFieldDetails> outFields() {
        return outFields;
    }

    /**
     * 构建者
     *
     * @param builder
     */
    public DataEventNode(Builder builder) {
        this.self = builder.self;
        this.event = builder.event;
        this.details = builder.details;
        this.superior = builder.superior;
        this.mapping = builder.mapping;
        // 收集输入输出参数 便于策略层直接获取
        if (this.details != null) {
            this.inFields = this.details.getInFields();
            this.outFields = this.details.getOutFields();
        }
        // 映射字段不作为输入参数返回
        if (this.mapping != null && this.inFields != null) {
            this.inFields = this.inFields.stream().filter(field -> {
                String name = field.getName();
                return !this.mapping.containsKey(name);
            }).collect(Collectors.toList());
        }

    }

    /**
     * 添加下级节点
     *
     * @param node
     */
    public void lower(DataEventNode node) {
        if (node == null) {
            return;
        }
        if (this.lowers == null) {
            this.lowers = new ArrayList<>();
        }
        this.lowers.add(node);
    }

    /**
     * 事件执行
     *
     * @return
     */
    public ExecutionResult perform(Map<String, Object> info) {
        this.data = info;
        // 执行节点自身数据事件
        ExecutionResult er = null;
        if (event != null) {
            er = event.perform(this.data, this.mapping);
            // 事件不可用
            if (!er.available()) {
                er.aggregation(this.self);
                return er;
            }
        }
        // 完善数据 下游可能会用到
        this.data.putAll(er.data());
        if (lowers != null) {
            AtomicReference<String> message = new AtomicReference<>();
            lowers.stream().parallel().map(
                    node -> node.perform(this.data)
            ).forEach(result -> {
                // 成功 完善数据
                if (result.available()) {
                    this.data.putAll(result.data());
                }
                if (message.get() == null) {
                    message.set(result.message());
                } else {
                    message.set(message.get().concat(";").concat(result.message()));
                }
            });
            er.aggregation(message.get());
        }
        //覆盖数据
        er.data(this.data);
        return er;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String self;
        private String superior;
        private DataEventDefinition event;
        private Map<String, String> mapping;
        private DataApiDetails details;

        public Builder self(String self) {
            this.self = self;
            this.event = DataEventHelper.event(self);
            this.details = DataEventHelper.resource(self);
            return this;
        }

        public Builder superior(String superior) {
            this.superior = superior;
            return this;
        }

        public Builder mapping(Map<String, String> data) {
            this.mapping = data;
            return this;
        }

        public Builder info(DataEventInformation info) {
            this.mapping = info.getMapping();
            this.self = info.getEventName();
            this.superior = info.getSuperior();
            this.details = DataEventHelper.resource(info.getEventName());
            this.event = DataEventHelper.event(info.getEventName());
            return this;
        }

        public DataEventNode build() {
            return new DataEventNode(this);
        }
    }
}
