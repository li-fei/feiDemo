package com.fei.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.fei.demo.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SeekBarActivity extends Activity {

    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);
        seekBar = findViewById(R.id.sb_seekbar);
        test();
    }

    int per = 0;
    private void test() {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        per++;
                        Log.e("yuneec0","*********" + per);
                        seekBar.setProgress(per);
                    }
                });
                if(per == 87){
                    executor.shutdown();
                }
            }
        }, 5, 1, TimeUnit.SECONDS);
    }
}
