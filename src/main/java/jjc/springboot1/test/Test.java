package jjc.springboot1.test;

import org.joda.time.DateTime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Test {
    public static void main(String[] args){
        LocalDateTime now = LocalDateTime.now();
        String createTime = "{ts '%d-%d-%d %d:%d:%d'}";
        createTime = String.format(createTime, now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                now.getHour(), now.getMinute(), now.getSecond());
        System.out.println(createTime);
        System.out.println(new Date());
    }
}
