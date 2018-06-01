package com.fei.demo.activity;

import android.app.Activity;
import android.os.Bundle;

import com.fei.demo.R;

public class EmptyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
    }
}
