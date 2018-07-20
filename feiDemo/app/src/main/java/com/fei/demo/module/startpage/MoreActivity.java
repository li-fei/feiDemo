package com.fei.demo.module.startpage;

import android.os.Bundle;

import com.fei.demo.R;
import com.fei.demo.activity.BaseActivity;

public class MoreActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        initBackClick();
    }

}
