package com.fei.demo.module.startpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fei.demo.R;
import com.fei.demo.activity.BaseActivity;

public class ConnectSelectActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout rl_remote_control_connect,rl_mobile_phone_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_select);
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
        rl_remote_control_connect = findViewById(R.id.rl_remote_control_connect);
        rl_mobile_phone_connect = findViewById(R.id.rl_mobile_phone_connect);
        rl_remote_control_connect.setOnClickListener(this);
        rl_mobile_phone_connect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_remote_control_connect:
//                startActivity(this,RemoteControlConnectActivity.class);
                startConnectActivity(0);
                break;
            case R.id.rl_mobile_phone_connect:
//                startActivity(this,MobilePhoneConnectActivity.class);
                startConnectActivity(1);
                break;
        }
    }

    private void startConnectActivity(int flag) {
        Intent intent = new Intent(this,RemoteControlConnectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("connectFlag",flag);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }


}
