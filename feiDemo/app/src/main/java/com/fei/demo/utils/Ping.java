package com.fei.demo.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping {
    private static final String TAG = "Ping";
    private static CallBack callBack;

    public interface CallBack{
        void onSuccess(int count);
    }

//    public static void setCallBack(CallBack callBack) {
//        Ping.callBack = callBack;
//    }

    public static String isPingSuccess(int pingNum, String m_strForNetAddress,CallBack c) {
        callBack = c;
        StringBuffer tv_PingInfo = new StringBuffer();
        try {
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c " + pingNum + " " + m_strForNetAddress);
            int status = p.waitFor();
            Logg.loge("Ping status ---> " + status);
            String result = "";
            if (status == 0) {
                result = "success";
            } else {
                result = "failed";
            }
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = new String();
            int connectedCount = 0;
            while ((str = buf.readLine()) != null) {
                connectedCount += getCheckResult(str);
                str = str + "\r\n";
                tv_PingInfo.append(str);
            }
            callBack.onSuccess(connectedCount);
            return tv_PingInfo.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private static int getCheckResult(String line) {
        if (line.contains("ttl") && line.contains("ms")){
            return 1;
        }else {
            return 0;
        }
    }

}
