package com.car.cloud.data.event;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxg
 */
public abstract class AbstractDataEvent<D, R> implements DataEventDefinition {
    /**
     * 结果转换
     *
     * @param r
     * @return
     */
    private Map<String, Object> conversion(R r) {
        if (r == null) {
            return null;
        }
        Field[] fields = r.getClass().getDeclaredFields();
        int length = fields.length;
        Map<String, Object> data = new HashMap<>(length);
        int i = 0;
        while (i < length) {
            Field field = fields[i];
            i++;
            String fieldName = field.getName();
            if (fieldName.indexOf("this") == 0) {
                continue;
            }
            field.setAccessible(true);
            Object obj = null;
            try {
                obj = field.get(r);
            } catch (IllegalAccessException ignore) {
            }
            if (obj != null) {
                data.put(fieldName, obj);
            }
        }
        return data;
    }

    /**
     * @param source
     * @param defaultValue
     */
    protected <T> T assigns(T source, T defaultValue) {
        if (source != null) {
            return source;
        } else {
            return defaultValue;
        }
    }

    /**
     * 事件执行
     *
     * @return
     */
    @Override
    public ExecutionResult perform(Map<String, Object> data, Map<String, String> mapping) {
        ExecutionResult.Builder builder = ExecutionResult.builder();
        // 进行入参转换
        D d = inData(data, mapping);
        try {
            // 执行数据转换过程
            R r = practitioner(d);
            //执行完成后将数据进行转换
            builder.ok(this.conversion(r));
        } catch (DataConversionException e) {
            // 数据转换异常
            // 上游调用根据结果 做响应的处理
            builder.fail(e.getMessage());
        } catch (IllegalStateException e) {
            // 不合法的字段验证
            // 上游调用根据结果 做响应的处理
            builder.fail(e.getMessage());
        }
        return builder.build();
    }

    /**
     * 输入数据转换
     *
     * @param <D>
     * @return
     */
    protected <D> D inData(Map<String, Object> data, Map<String, String> mapping) {
        D info = data();
        if (null == info) {
            throw new RuntimeException("Initialize the object to accept the data");
        }
        Field[] fields = info.getClass().getDeclaredFields();
        int length = fields.length;
        int i = 0;
        while (i < length) {
            Field field = fields[i];
            i++;
            String fieldName = field.getName();
            if (fieldName.indexOf("this") == 0) {
                continue;
            }
            field.setAccessible(true);
            Object value = getMapValue(data, mapping, fieldName);
            if (value != null) {
                fieldSetValue(info, field, value);
            }
        }

        return info;
    }

    private Object getMapValue(Map<String, Object> data, Map<String, String> mapping, String name) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        if (mapping == null || mapping.isEmpty()) {
            return data.get(name);
        } else {

            return data.get(mapping.getOrDefault(name, name));
        }
    }

    private <D> void fieldSetValue(D aClass, Field field, Object value) {
        String name = field.getType().getSimpleName();
        try {
            String of = String.valueOf(value);
            switch (name) {
                case "Integer":
                    field.setInt(aClass, Integer.valueOf(of));
                    break;
                case "Long":
                    field.setLong(aClass, Long.valueOf(of));
                    break;
                case "Short":
                    field.setShort(aClass, Short.valueOf(of));
                    break;
                case "Double":
                    field.setDouble(aClass, Double.valueOf(of));
                    break;
                case "Float":
                    field.setFloat(aClass, Float.valueOf(of));
                    break;
                case "Boolean":
                    field.setBoolean(aClass, Boolean.valueOf(of));
                    break;
                default:
                    field.set(aClass, value);
            }
        } catch (IllegalAccessException ignore) {
        }

    }

    /**
     * 执行者
     *
     * @param info
     * @param <R>
     * @return
     * @throws DataConversionException
     */
    public abstract <R> R practitioner(D info) throws DataConversionException;

    /**
     * 接受数据对象初始化
     *
     * @param <D>
     * @return
     */
    public abstract <D> D data();

}
