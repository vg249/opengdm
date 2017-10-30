package org.gobiiproject.gobiimodel.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Phil on 5/19/2016.
 */
public class DateUtils {


    public static String makeDateYYYYMMDD() {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        String returnVal =
                String.format("%02d", calendar.get(Calendar.YEAR)) +
                        "-" +
                        String.format("%02d", calendar.get(Calendar.MONTH) + 1) +
                        "-" +
                        String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));

        return returnVal;

    }

    public static String makeDateIdString() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String returnVal =
                String.format("%02d", calendar.get(Calendar.YEAR)) +
                        "-" +
                        String.format("%02d", calendar.get(Calendar.MONTH) + 1) +
                        "-" +
                        String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) +
                        "_" +
                        String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) +
                        "-" +
                        String.format("%02d", calendar.get(Calendar.MINUTE)) +
                        "-" +
                        String.format("%02d", calendar.get(Calendar.SECOND)) +
                        "-" +
                        String.format("%02d", calendar.get(Calendar.MILLISECOND));

        return returnVal;
    }

    public static boolean dayOfMonthOfYearIsEqual(Date dateOne, Date dateTwo) {

        Calendar calendarOne = Calendar.getInstance();
        calendarOne.setTime(dateOne);
        int dateOneYear = calendarOne.get(Calendar.YEAR);
        int dateOneMonth = calendarOne.get(Calendar.MONTH);
        int dateOneday = calendarOne.get(Calendar.DAY_OF_MONTH);

        Calendar calendarTwo = Calendar.getInstance();
        calendarTwo.setTime(dateTwo);
        int dateTwoYear = calendarTwo.get(Calendar.YEAR);
        int dateTwoMonth = calendarTwo.get(Calendar.MONTH);
        int dateTwoday = calendarTwo.get(Calendar.DAY_OF_MONTH);

        return (dateOneYear == dateTwoYear) && (dateOneMonth == dateTwoMonth) && (dateOneday == dateTwoday);
    }
}
