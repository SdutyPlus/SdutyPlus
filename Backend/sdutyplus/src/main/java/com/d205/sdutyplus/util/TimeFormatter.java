package com.d205.sdutyplus.util;

import com.d205.sdutyplus.global.enums.TimeEnum;
import com.d205.sdutyplus.global.error.exception.InvalidTimeFormatException;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {

    public static LocalDateTime StringToLocalDateTime(String time){
        try{
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeEnum.dateTimeFormat);
            final LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
            return localDateTime;
        }
        catch(Exception e){
            throw new InvalidTimeFormatException();
        }
    }

    public static String LocalDateTimeToString(LocalDateTime localDateTime){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeEnum.dateTimeFormat);
        final String now = localDateTime.format(formatter);
        return now;
    }

    public static String msToTime(int ms){
        ms/=1000;
        final int hour = ms / 3600;
        final int minute = ms % 3600 / 60;
        final int sec = ms % 3600 % 60;
        final String totalTime = String.format("%02d:%02d:%02d", hour, minute, sec);
        return totalTime;
    }

    public static int getDurationTime(LocalDateTime startTime, LocalDateTime endTime){
        final int second = (int) Duration.between(startTime, endTime).getSeconds();
        return second*1000;
    }

    public static String LocalDateToString(LocalDate localDate){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeEnum.dateFormat);
        final String now = localDate.format(formatter);
        return now;
    }

    public static LocalDateTime getTodayDateTime(){
        final LocalDateTime localDateTime = getKoreaLocalDateTime().toLocalDateTime();
        return localDateTime;
    }

    public static LocalDate getTodayDate(){
        final LocalDate localDate = getKoreaLocalDateTime().toLocalDate();
        return localDate;
    }

    private static ZonedDateTime getKoreaLocalDateTime(){
        final ZoneId zoneId = ZoneId.of("Asia/Seoul");
        final ZonedDateTime zonedDateTime = ZonedDateTime.now( zoneId );
        return zonedDateTime;
    }
}
