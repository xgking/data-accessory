package com.example.demo;

import com.car.cloud.data.event.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
        DataEventDefinition event = DataEventHelper.event("parseIdNumber");
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put("idNumber", "622103197309114510");
        ExecutionResult result = event.perform(map, null);

        String x = Json.toStr(result.data());
        System.out.println(x);
    }

    @Test
    public void strategy() {

        DataEventNode testCase1 = DataEventNode.builder().self("testCase1").build();
        Map<String, String> map = new HashMap<>(1);
        map.put("inStr1", "inStr");
        DataEventNode testCase2 = DataEventNode.builder().self("testCase2").superior("testCase1").mapping(map).build();

        List<DataEventNode> list = new ArrayList<>();
        list.add(testCase1);
        list.add(testCase2);

        ExecutionStrategy combination = DataEventHelper.combination(list);

        List<DataApiFieldDetails> inFields = combination.inFields();
        List<DataApiFieldDetails> outFields = combination.outFields();
        System.out.println(Json.toStr(inFields));
        System.out.println(Json.toStr(outFields));

        Map<String, Object> data = new HashMap<>(1);
        data.put("inStr", "1234");
        ExecutionResult result = combination.execution(data);

        System.out.println(Json.toStr(result.data()));

    }

}
