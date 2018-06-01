package com.fei.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fei.demo.R;
import com.fei.demo.uart.Jni;
import com.fei.demo.uart.Uart;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uart);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.tv_uart);
        tv.setText(Uart.getInstance().jniTest());
//        Log.e("yuneec0","--->" +  Jni.getInstance().jniTest());
//        Log.e("yuneec0","--->" +  Jni.getInstance().jniTestBoolean());
//        Log.e("yuneec0","--->" +  Uart.getInstance().jniTest());
//        UartFactory.getInstance().startGetUartDate();

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
}
