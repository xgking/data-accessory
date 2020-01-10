package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * json处理
 *
 * @author wxg
 */
public class Json {
    static ObjectMapper objectMapper = new ObjectMapper();

    static final String REGEX = "[.]";
    static final String L = "[";
    static final String R = "]";

    public static <T> String toStr(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T getValue(String content, String nodalLine, Class<T> elementClass) {
        JsonNode node = readTree(content);
        String[] split = nodalLine.split(REGEX);
        int l = split.length;
        int i = 0;
        while (i < l) {
            node = getOne(node, split[i]);
            if (node == null) {
                break;
            }
            i++;
        }
        return objectMapper.convertValue(node, elementClass);
    }

    public static <T> List<T> getListValue(String content, String nodalLine, Class<T> elementClass) {
        JsonNode node = readTree(content);
        String[] split = nodalLine.split(REGEX);
        int l = split.length;
        int i = 0;
        while (i < l) {
            node = getOne(node, split[i]);
            i++;
        }
        return objectMapper.convertValue(node, objectMapper.getTypeFactory().constructArrayType(elementClass));
    }

    public static <T> List<T> getListValue(String content, Class<T> elementClass) {
        JsonNode node = readTree(content);
        return objectMapper.convertValue(node, objectMapper.getTypeFactory().constructArrayType(elementClass));
    }

    public static <T> T getValue(String content, Class<T> elementClass) {
        JsonNode node = readTree(content);
        return objectMapper.convertValue(node, elementClass);
    }

    private static JsonNode getOne(JsonNode node, String nodalLine) {
        //解析节点链 主要判断数组
        int a = nodalLine.indexOf(L);
        Integer index = null;
        if (a > 0) {
            int b = nodalLine.indexOf(R);
            index = Integer.parseInt(nodalLine.substring(a + 1, b));
            nodalLine = nodalLine.substring(0, a);
        }
        // 获取值
        if (node.has(nodalLine)) {
            JsonNode jsonNode = node.get(nodalLine);
            if (index != null && jsonNode != null) {
                return jsonNode.get(index);
            }
            return jsonNode;
        }
        return null;
    }

    private static JsonNode readTree(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
