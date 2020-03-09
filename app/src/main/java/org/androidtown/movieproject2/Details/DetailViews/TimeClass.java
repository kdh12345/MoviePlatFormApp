package org.androidtown.movieproject2.Details.DetailViews;

import java.util.Calendar;
import java.util.Date;

public class TimeClass {
    public static final int SEC = 60;

    public static final int MIN = 60;

    public static final int HOUR = 24;

    public static final int DAY = 30;

    public static final int MONTH=12;

    public static String formatTimeString(Date date){
        Calendar c = Calendar.getInstance();

        long now = c.getTimeInMillis();
        long dateM = date.getTime();

        long gap = now - dateM;

        String ret;

        gap = (gap/1000);

        if(gap < SEC){
            ret = "방금 전";
        }else if((gap /= SEC) < MIN){
            ret = gap + "분 전";
        }else if((gap /= MIN) < HOUR){
            ret = gap + "시간 전";
        }else if((gap /= HOUR) < DAY){
            ret = gap + "일 전";
        }else if((gap /= DAY) < MONTH){
            ret = gap + "달 전";
        }else{
            gap /= MONTH;
            ret = gap + "년 전";
        }

        return ret;
    }
}
