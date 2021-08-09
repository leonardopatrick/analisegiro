package br.com.sankhya.commercial.analisegiro.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

    public static long add(long timestamp, int amount, int field) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timestamp);
        calendar.add(field, amount);

        return calendar.getTimeInMillis();
    }

    public static Timestamp dataAddDay(Timestamp data, int amount) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(data.getTime());
        cal.add(Calendar.DAY_OF_MONTH, amount);

        return new Timestamp(cal.getTimeInMillis());
    }

    }

