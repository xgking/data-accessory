package com.example.demo;

import com.car.cloud.data.event.AbstractDataEvent;
import com.car.cloud.data.event.annotations.DataEvent;
import com.car.cloud.data.event.annotations.DataEventField;
import org.springframework.stereotype.Service;

/**
 * @author wxg
 */
@Service("testCase2")
@DataEvent(value = "testCase2", category = "测试", describe = "用例2")
public class TestCase2 extends AbstractDataEvent<TestCase2.InData, TestCase2.OutData> {

    /**
     * 接受数据的对象
     */
    public class InData {
        @DataEventField("入参2")
        private String inStr1;
    }

    /**
     * 返回数据对象
     */
    public class OutData {
        @DataEventField("因子2")
        private String factor2;
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
        outData.factor2 = inData.inStr1;
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
