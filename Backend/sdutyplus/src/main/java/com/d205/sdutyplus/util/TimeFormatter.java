package com.d205.sdutyplus.util;

import com.d205.sdutyplus.global.enums.TimeEnum;
import com.d205.sdutyplus.global.error.exception.InvalidTimeFormatException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {

    public static LocalDateTime StringToLocalDateTime(String time){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeEnum.dateTimeFormat);
            LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
            return localDateTime;
        }
        catch(IllegalArgumentException e){
            throw new InvalidTimeFormatException();
        }
    }

    public static String LocalDateTimeToString(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeEnum.dateTimeFormat);
        String now = localDateTime.format(formatter);
        return now;
    }

    public static String msToTime(int ms){
        ms/=1000;
        int hour = ms / 3600;
        int minute = ms % 3600 / 60;
        int sec = ms % 3600 % 60;
        String totalTime = String.format("%02d:%02d:%02d", hour, minute, sec);
        return totalTime;
    }

    public static int getDurationTime(LocalDateTime startTime, LocalDateTime endTime){
        int second = (int) Duration.between(startTime, endTime).getSeconds();
        return second*1000;
    }
}
