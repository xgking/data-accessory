package com.car.cloud.data.event;


/**
 * 数据转换异常
 *
 * @author wxg
 */
public class DataConversionException extends Exception {
    private String message;

    public DataConversionException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
