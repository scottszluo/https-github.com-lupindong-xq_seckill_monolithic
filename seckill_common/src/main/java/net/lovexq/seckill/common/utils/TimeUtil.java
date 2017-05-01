package net.lovexq.seckill.common.utils;

import net.lovexq.seckill.common.exception.SystemException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author LuPindong
 * @time 2017-04-30 08:52
 */
public class TimeUtil {

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