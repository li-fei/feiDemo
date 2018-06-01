package com.fei.demo.module.startpage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.fei.demo.R;
import com.fei.demo.activity.BaseActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MoreActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        initBackClick();
    }

}
