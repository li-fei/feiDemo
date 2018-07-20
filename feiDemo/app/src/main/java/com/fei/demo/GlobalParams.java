package com.fei.demo;

import com.fei.demo.activity.AsyncHttpActivity;
import com.fei.demo.activity.BindCameraActivity;
import com.fei.demo.activity.CmdActivity;
import com.fei.demo.activity.GMapActivity;
import com.fei.demo.activity.GpsActivity;
import com.fei.demo.activity.LangActivity;
import com.fei.demo.activity.OtaTestActivity;
import com.fei.demo.module.startpage.QRcodeActivity;
import com.fei.demo.activity.SeekBarActivity;
import com.fei.demo.activity.SocketActivity;
import com.fei.demo.activity.St16UpdateActivity;
import com.fei.demo.activity.ThreadActivity;
import com.fei.demo.activity.UartActivity;
import com.fei.demo.module.startpage.StartPageActivity;

/**
 * Created by Administrator on 2017/12/18/018.
 */

public class GlobalParams {

    public static Class[] activitys = new Class[]{
            UartActivity.class ,
            SocketActivity.class ,
            AsyncHttpActivity.class ,
            CmdActivity.class ,
            SeekBarActivity.class ,
            GpsActivity.class ,
            LangActivity.class ,
            St16UpdateActivity.class ,
            BindCameraActivity.class ,
            OtaTestActivity.class ,
            ThreadActivity.class ,
            StartPageActivity.class ,
            GMapActivity.class
    };

}
