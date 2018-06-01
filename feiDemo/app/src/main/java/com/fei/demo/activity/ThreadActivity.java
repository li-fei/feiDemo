package com.fei.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fei.demo.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadActivity extends Activity {

    private MyHandler handler = new MyHandler(ThreadActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);


        sendControllerGpsToFC();

    }

    private ScheduledExecutorService mSendGpsExecutor;
    private boolean canSendRcGps = true;
    private void sendControllerGpsToFC() {
        if (mSendGpsExecutor == null) {
            Log.e("yuneec0","gps :" + "--------11111111111111111111111111111111111111111--------");
            mSendGpsExecutor = Executors.newSingleThreadScheduledExecutor();
            mSendGpsExecutor.scheduleAtFixedRate(new SendGpsTask(), 0, 200, TimeUnit.MILLISECONDS);
        }
//        if (canSendRcGps){
//            Log.e("yuneec0","gps :" + "--------11111111111111111111111111111111111111111--------");
//            canSendRcGps = false;
//            handler.postDelayed(new SendGpsTask(),200);
//        }
    }

    private void stopsendControllerGpsToFC() {
        if (mSendGpsExecutor != null) {
            mSendGpsExecutor.shutdown();
            mSendGpsExecutor = null;
        }
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("yuneec0","gps :" + "************************");
        }
    };

    private class SendGpsTask implements Runnable {
        @Override
        public void run() {
//            handler.postDelayed(this ,200);
            Log.e("yuneec0","gps :" + "----------------");
        }
    }


    private class MyHandler extends Handler {
        private WeakReference<ThreadActivity> weakReference;

        public MyHandler(ThreadActivity activity) {
            this.weakReference = new WeakReference<ThreadActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference.get() == null) {
                return;
            }
            switch (msg.what){
                case 0:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopsendControllerGpsToFC();
    }
}
