package net.lovexq.seckill.common.utils;

import net.lovexq.seckill.common.exception.SystemException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author LuPindong
 * @time 2017-04-30 08:52
 */
public class TimeUtil {

    public static ZoneId shanghai = ZoneId.of("Asia/Shanghai");

    public static LocalDate nowDate() {
        return LocalDate.now(shanghai);
    }

    public static LocalTime nowTime() {
        return LocalTime.now(shanghai);
    }

    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(shanghai);
    }

    public static String format(LocalDateTime target) {
        return format(target, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(LocalDateTime target, String pattern) {
        if (target == null) {
            return null;
        } else {
            try {
                return target.format(DateTimeFormatter.ofPattern(pattern));
            } catch (Exception e) {
                throw new SystemException("Error formatting date with format pattern \"" + pattern + "\"", e);
            }
        }
    }
}