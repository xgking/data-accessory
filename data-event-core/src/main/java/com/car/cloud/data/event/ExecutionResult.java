package com.car.cloud.data.event;

import java.util.Map;

/**
 * 数据事件执行结果采集
 * 并不直接在实现类使用 在抽象执行类与数据事件节点之间进行数据及状态传输
 *
 * @author wxg
 */
public class ExecutionResult {

    private boolean available;
    private String message;
    private Map<String, Object> data;

    private ExecutionResult() {
    }

    public ExecutionResult(Builder builder) {
        this.available = builder.available;
        this.message = builder.message;
        this.data = builder.data;
    }

    public boolean available() {
        return available;
    }

    public String message() {
        return message;
    }

    public void aggregation(String message) {
        if (message == null) {
            return;
        }
        if (this.message == null) {
            this.message = message;
        } else {
            this.message.concat(";").concat(message);
        }
    }

    public void data(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> data() {
        return data;
    }

    public void put(Map<String, Object> data) {
        if (data == null) {
            return;
        }
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.putAll(data);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean available;
        private String message;
        private Map<String, Object> data;

        public Builder available(boolean available) {
            this.available = available;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder fail(String message) {
            this.available = false;
            this.message = message;
            return this;
        }

        public Builder ok(Map<String, Object> data) {
            this.available = true;
            this.data = data;
            return this;
        }

        public Builder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }


        public ExecutionResult build() {
            return new ExecutionResult(this);
        }
    }
}
