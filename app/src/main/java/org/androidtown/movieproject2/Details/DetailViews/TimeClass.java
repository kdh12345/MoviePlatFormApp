package org.androidtown.movieproject2.Details.DetailViews;

import java.util.Date;

public class TimeClass {
    public static final int SEC = 60;

    public static final int MIN = 60;

    public static final int HOUR = 24;

    public static final int DAY = 30;

    public static final int MONTH=12;

    public static String formatTimeString(Date TmpDate){
        long curTime = System.currentTimeMillis();

        long regTime = TmpDate.getTime();

        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if(diffTime<SEC) {
            msg = "방금 전";
        }else if((diffTime/=SEC)<MIN) {
            msg = diffTime + "분 전";
        }else if((diffTime/=HOUR)<DAY){
            msg=diffTime+"일 전";
        }else if((diffTime/=DAY)<MONTH){
            msg=diffTime+"년 전";
        }
        return msg;
    }
}
