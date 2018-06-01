package com.fei.demo.module.startpage;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fei.demo.R;
import com.fei.demo.activity.BaseActivity;

public class MobilePhoneConnectActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_phone_connect);
        initBackClick();
        initView();
        initSkip(true, new CallBack() {
            @Override
            public void skipCallBack() {
//                finish();
            }
        });
    }

    private void initView() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_remote_control_connect:

                break;
        }
    }

}
