package br.com.sankhya.commercial.analisegiro.util;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimeUtils {
    public static Timestamp getNow() {
        return  new Timestamp(System.currentTimeMillis());
    }

    public static int getDifference(Object now,
                                    Timestamp ultCompra) {
        return 1;
    }

    public static void clearTime(Calendar calendar) {
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.clear(Calendar.AM_PM);
    }

    }

