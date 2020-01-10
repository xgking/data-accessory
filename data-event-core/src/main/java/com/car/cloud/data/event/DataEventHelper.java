package com.car.cloud.data.event;

import com.car.cloud.data.event.annotations.DataEvent;
import com.car.cloud.data.event.annotations.DataEventField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口事件帮助类 对外使用时由该类提供完整服务
 *
 * @author wxg
 */
public class DataEventHelper {
    /**
     * 可用的数据接口集合
     */
    private static Map<String, DataEventDefinition> definitionMap;
    /**
     * 采集到的数据接口信息
     */
    private static List<DataApiDetails> apiDetailsList;
    private static Map<String, DataApiDetails> apiDetailsMap;

    /**
     * 私有化无参构造方法
     */
    private DataEventHelper() {
    }

    /**
     * 组件化时装载可用接口信息
     *
     * @param definitionMap
     */
    public static void init(Map<String, DataEventDefinition> definitionMap) {
        DataEventHelper.definitionMap = definitionMap;
        apiDetailsList = new ArrayList<>();
        apiDetailsMap = new HashMap<>(16);
        DataEventHelper.definitionMap.forEach((s, definition) -> {
            DataApiDetails details = details(definition);
            if (!details.getIgnore()) {
                apiDetailsList.add(details);
            }
            apiDetailsMap.put(s, details);
        });
    }

    /**
     * 可用资源集合
     *
     * @return
     */
    public static List<DataApiDetails> resources() {
        return DataEventHelper.apiDetailsList;
    }

    /**
     * 获取单个可用资源信息
     *
     * @param name
     * @return
     */
    public static DataApiDetails resource(String name) {
        return DataEventHelper.apiDetailsMap.get(name);
    }


    /**
     * 根据接口值获取接口
     *
     * @param key
     * @return
     */
    public static DataEventDefinition event(String key) {
        return DataEventHelper.definitionMap.get(key);
    }

    /**
     * 组合调用链
     * DataEventInformation 可直接用于缓存
     * 推荐使用
     *
     * @param list
     * @return
     */
    public static ExecutionStrategy informations(List<DataEventInformation> list) {
        Map<String, DataEventNode> nodeMap = new HashMap<>(list.size());
        list.forEach(node -> nodeMap.put(node.getEventName(), DataEventNode.builder().info(node).build()));
        return combination(nodeMap);
    }

    /**
     * 无序节点组合调用链
     *
     * @param nodes
     * @return
     */
    public static ExecutionStrategy combination(List<DataEventNode> nodes) {
        Map<String, DataEventNode> nodeMap = new HashMap<>(nodes.size());
        nodes.forEach(node -> nodeMap.put(node.getSelf(), node));
        return combination(nodeMap);
    }

    /**
     * 组合调用链
     *
     * @param nodeMap
     * @return
     */
    private static ExecutionStrategy combination(Map<String, DataEventNode> nodeMap) {
        return ExecutionStrategy.builder().nodes(nodeMap).build();
    }

    /**
     * 获取接口描述信息
     *
     * @param definition
     * @return
     */
    private static DataApiDetails details(DataEventDefinition definition) {
        // 获取接口注解
        DataEvent de = definition.getClass().getAnnotation(DataEvent.class);
        DataApiDetails details = new DataApiDetails();
        details.setIgnore(de.ignore());
        details.setValue(de.value());
        details.setCategory(de.category());
        details.setDescribe(de.describe());
        contractMethod(details, definition);
        return details;
    }

    /**
     * 获取接口约定的方法信息
     *
     * @param details
     * @param o
     */
    private static void contractMethod(DataApiDetails details, DataEventDefinition o) {
        Method[] methods = o.getClass().getDeclaredMethods();
        int i = 0;
        while (i < methods.length) {
            Method method = methods[i];
            i++;
            String simpleName = method.getReturnType().getSimpleName();
            if (simpleName.equals(Object.class.getSimpleName())) {
                continue;
            }
            String name = method.getName();
            if ("practitioner".equals(name)) {
                details.setOutFields(returnType(method.getReturnType()));
            }
            if ("data".equals(name)) {
                details.setInFields(returnType(method.getReturnType()));
            }
        }
    }

    /**
     * 获取实体类中的字段信息
     *
     * @param aClass
     * @return
     */
    private static List<DataApiFieldDetails> returnType(Class aClass) {
        Field[] fields = aClass.getDeclaredFields();
        int i = 0;
        List<DataApiFieldDetails> list = new ArrayList<>();
        while (i < fields.length) {
            Field field = fields[i];
            i++;
            // 获取字段注解
            DataEventField def = field.getAnnotation(DataEventField.class);
            if (def != null) {
                DataApiFieldDetails details = new DataApiFieldDetails();
                details.setName(field.getName());
                details.setType(field.getType().getSimpleName());
                details.setDescribe(def.value());
                details.setDetailed(def.detailed());
                list.add(details);
            }
        }
        return list;
    }
}
