package com.d205.sdutyplus.util;

import com.d205.sdutyplus.global.enums.TimeEnum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {

    public static LocalDateTime StringToLocalDateTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeEnum.dateTimeFormat);
        LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
        return localDateTime;
    }

    public static String LocalDateTimeToString(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeEnum.dateTimeFormat);
        String now = localDateTime.format(formatter);
        return now;
    }
}
