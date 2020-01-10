package com.example.demo;

import com.car.cloud.data.event.AbstractDataEvent;
import com.car.cloud.data.event.annotations.DataEvent;
import com.car.cloud.data.event.annotations.DataEventField;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * @author wxg
 */
@Service("testCase1")
@DataEvent(value = "testCase1", category = "测试", describe = "用例1")
public class TestCase1 extends AbstractDataEvent<TestCase1.InData, TestCase1.OutData> {

    /**
     * 接受数据的对象
     */
    public class InData {
        @DataEventField("入参")
        private String inStr;
    }

    /**
     * 返回数据对象
     */
    public class OutData {
        @DataEventField("因子1")
        private String factor1;
    }

    /**
     * 清洗执行
     *
     * @param inData
     * @return
     */
    @Override
    public OutData practitioner(InData inData) {

        OutData outData = new OutData();
        outData.factor1 = inData.inStr;
        return outData;
    }

    /**
     * 初始化接受对象
     *
     * @return
     */
    @Override
    public InData data() {
        return new InData();
    }

}
