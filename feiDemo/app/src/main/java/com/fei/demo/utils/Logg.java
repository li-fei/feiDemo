package com.fei.demo.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Logg {

    private static boolean showLog = true;
    public static boolean showTheScreenLog = false;

    private static String TAG = "yuneec0_wel";

    public static void loge(String log) {
        if (showLog) {
            Log.e(TAG, log);
        }
    }

    public static void loge(String tag,String log) {
        if (showLog) {
            Log.e(TAG + "_" + tag, log);
        }
    }

    private static String showScreenLog = "";
    private static int maxLog = 30 ;
    private static LinkedList<String> queue = new LinkedList<String>();

    public static void writeLogToScreen(String log) {
        if (!showTheScreenLog) {
            return;
        }
        if (queue.size() >= maxLog) {
            queue.poll();
        }
        queue.offer(getCurrentTime() + log);

        showScreenLog = "";
        for (int i = 0; i < queue.size(); i++) {
            showScreenLog += (queue.get(i) + "\n");
        }

        if (loggCallBack != null) {
            loggCallBack.showLog(showScreenLog);
        }
    }

    private static String getCurrentTime() {
        String time = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        time += "" + sdf.format(date) + ": ";
        return time;
    }

    public interface LoggCallBack{
        void showLog(String showScreenLog);
    }

    static LoggCallBack loggCallBack;

    public static void setLoggCallBack(LoggCallBack callBack) {
        loggCallBack = callBack;
    }


}
