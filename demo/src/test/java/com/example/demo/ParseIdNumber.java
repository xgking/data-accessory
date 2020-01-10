package com.example.demo;

import com.car.cloud.data.event.annotations.DataEvent;
import com.car.cloud.data.event.annotations.DataEventField;
import com.car.cloud.data.event.AbstractDataEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * @author wxg
 */
@Service("parseIdNumber")
@DataEvent(value = "parseIdNumber", category = "数据解析", describe = "身份证号解析")
public class ParseIdNumber extends AbstractDataEvent<ParseIdNumber.InData, ParseIdNumber.OutData> {

    /**
     * 接受数据的对象
     */
//    @Getter
//    @Setter
    public class InData {
        @DataEventField("身份证号码")
        private String idNumber;
    }

    /**
     * 返回数据对象
     */
//    @Getter
//    @Setter
    public class OutData {
        @DataEventField("户籍")
        private String householdRegistration;
        @DataEventField("性别")
        private String gender;
        @DataEventField("生日")
        private String birthday;
        @DataEventField("年龄")
        private Integer age;
    }

    /**
     * 清洗执行
     *
     * @param inData
     * @return
     */
    @Override
    public OutData practitioner(InData inData) {
        Details parse = parse(inData.idNumber);
        OutData data = new OutData();
        data.age = parse.getAge();
        data.birthday = parse.birthday;
        data.gender = parse.gender;
        return data;
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

    private static Pattern pattern = Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
    private static String[] comparison_table = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static int[] Wi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    public Details parse(String idNumber) {
        Details details = new Details();
        if (!pattern.matcher(idNumber).matches()) {
            details.setAvailable(false);
            details.setMessage("身份证号码格式错误");
            return details;
        }

        if (!verify(idNumber)) {
            details.setAvailable(false);
            details.setMessage("身份证号码不合法");
            return details;
        }

        details.setProvincialCode(idNumber.substring(0, 2));
        details.setRegionalCode(idNumber.substring(2, 4));
        details.setCountyCode(idNumber.substring(4, 6));
        details.setYear(idNumber.substring(6, 10));
        details.setMonth(idNumber.substring(10, 12));
        details.setDay(idNumber.substring(12, 14));
        details.birthday();
        details.setOrderCode(idNumber.substring(14, 17));
        details.setCheckCode(idNumber.substring(17, 18).toUpperCase());
        details.setAge(ageCalculation(idNumber));

        details.setAvailable(true);
        details.setMessage("身份证解析成功");
        return details;

    }

    public boolean verify(String idNumber) {
        String Ai = idNumber.substring(0, 17);
        String c = idNumber.substring(17, 18).toUpperCase();
        int length = 17;
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += Integer.parseInt(String.valueOf(Ai.charAt(i))) * Wi[i];
        }
        String s = comparison_table[sum % 11];
        return s.equals(c);
    }

    public int ageCalculation(String idNumber) {
        String year = idNumber.substring(6, 10);
        String month = idNumber.substring(10, 12);
        String day = idNumber.substring(12, 14);

        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH) + 1;
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);

        int bYear = Integer.parseInt(year);
        int bMonth = Integer.parseInt(month);
        int bDay = Integer.parseInt(day);

        int yDiff = thisYear - bYear;
        int mDiff = thisMonth - bMonth;
        int dDiff = thisDay - bDay;
        // 不足年
        if (mDiff < 0) {
            yDiff -= 1;
        } else if (mDiff == 0) {
            // 不足月
            if (dDiff < 0) {
                yDiff -= 1;
            }
        }

        return yDiff;
    }

    public class Details {
        private boolean available;
        private String message;
        private String provincialCode;
        private String regionalCode;
        private String countyCode;
        private String year;
        private String month;
        private String day;
        private String birthday;
        private String orderCode;
        private String checkCode;
        private int age;
        private String gender;

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getProvincialCode() {
            return provincialCode;
        }

        public void setProvincialCode(String provincialCode) {
            this.provincialCode = provincialCode;
        }

        public String getRegionalCode() {
            return regionalCode;
        }

        public void setRegionalCode(String regionalCode) {
            this.regionalCode = regionalCode;
        }

        public String getCountyCode() {
            return countyCode;
        }

        public void setCountyCode(String countyCode) {
            this.countyCode = countyCode;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getBirthday() {
            return birthday;
        }

        public void birthday() {
            this.birthday = year + "年" + month + "月" + day + "日";
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            Integer value = Integer.valueOf(orderCode);
            this.gender = value % 2 == 1 ? "男性" : "女性";
            this.orderCode = orderCode;
        }

        public String getCheckCode() {
            return checkCode;
        }

        public void setCheckCode(String checkCode) {
            this.checkCode = checkCode;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

    }
}
