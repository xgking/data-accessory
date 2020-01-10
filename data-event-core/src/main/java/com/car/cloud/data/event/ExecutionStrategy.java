package com.car.cloud.data.event;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 策略层 数据事件
 *
 * @author wxg
 */
public class ExecutionStrategy {
    /**
     * 最上层的数据事件
     */
    private List<DataEventNode> lowers;
    /**
     * 事件节点集合
     */
    private Map<String, DataEventNode> nodeMap;
    /**
     * 汇总接受参数
     */
    private List<DataApiFieldDetails> inFields;
    /**
     * 汇总返回参数
     */
    private List<DataApiFieldDetails> outFields;

    public static Builder builder() {
        return new Builder();
    }

    public ExecutionStrategy(Builder builder) {
        this.nodeMap = builder.nodeMap;
        this.inFields = new ArrayList<>();
        this.outFields = new ArrayList<>();
        // 组建数据事件执行层级 这里无法保证执行顺序
        nodeMap.forEach((s, node) -> {
            if (nodeMap.containsKey(node.getSuperior())) {
                // 添加节点到父层
                nodeMap.get(node.getSuperior()).lower(node);
            } else {
                // 添加节点到顶层(策略层)
                this.lower(node);
            }
            // 采集节点输入输出字段到策略层 (在执行构建节点时已将输入输出字段收集完成,这里直接提取)
            if (node.inFields() != null) {
                this.inFields.addAll(node.inFields());
            }
            if (node.outFields() != null) {
                this.outFields.addAll(node.outFields());
            }

        });
    }

    public Map<String, DataEventNode> nodes() {
        return this.nodeMap;
    }

    /**
     * 接受参数
     *
     * @return
     */
    public List<DataApiFieldDetails> inFields() {
        if (inFields != null) {
            // 去除重复
            inFields = inFields.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(DataApiFieldDetails::getName))
            ), ArrayList::new));
        }
        return this.inFields;
    }

    /**
     * 返回参数
     *
     * @return
     */
    public List<DataApiFieldDetails> outFields() {
        if (outFields != null) {
            // 去除重复
            outFields = outFields.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(DataApiFieldDetails::getName))
            ), ArrayList::new));
        }
        return this.outFields;
    }

    /**
     * 数据事件开始执行
     *
     * @param info
     * @return
     */
    public ExecutionResult execution(Map<String, Object> info) {
        ExecutionResult.Builder builder = ExecutionResult.builder();
        if (this.lowers != null) {
            // 用于采集异常信息
            AtomicReference<String> message = new AtomicReference<>();
            this.lowers.stream().parallel().map(
                    // 异步执行数据事件
                    node -> node.perform(info)
            ).forEach(result -> {
                // 对执行结果进行处理 子级的结果由父级汇总
                // 成功 完善数据
                if (result.available()) {
                    info.putAll(result.data());
                }
                // 采集执行过程中出现的错误消息
                if (message.get() == null) {
                    message.set(result.message());
                } else {
                    message.set(message.get().concat(";").concat(result.message()));
                }
            });
            builder.message(message.get());
            builder.ok(info);
        }
        return builder.build();
    }

    /**
     * 添加下级节点
     *
     * @param node
     */
    private void lower(DataEventNode node) {
        if (node == null) {
            return;
        }
        if (this.lowers == null) {
            this.lowers = new ArrayList<>();
        }
        this.lowers.add(node);
    }

    /**
     * 不希望外部以new的形式使用 且方便在构建时执行其他操作
     */
    public static class Builder {
        private Map<String, DataEventNode> nodeMap;

        /**
         * 添加节点时,节点名称作为key,自身作为value,层级关系由策略层组合
         *
         * @param nodes
         * @return
         */
        public Builder nodes(Map<String, DataEventNode> nodes) {
            this.nodeMap = nodes;
            return this;
        }

        public ExecutionStrategy build() {
            return new ExecutionStrategy(this);
        }
    }

}
