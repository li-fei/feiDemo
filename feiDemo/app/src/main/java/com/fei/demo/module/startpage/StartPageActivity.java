package com.fei.demo.module.startpage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fei.demo.R;
import com.fei.demo.activity.BaseActivity;
import com.fei.demo.activity.MainActivity;
import com.fei.demo.adapter.WelcomeAdapter;
import com.fei.demo.utils.SharedPreUtil;
import com.fei.demo.views.AlignedTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class StartPageActivity extends BaseActivity implements View.OnClickListener{

    private MyHandler mHandler = new MyHandler(StartPageActivity.this);
    private boolean isFirstStartApp = true;
    CheckBox cb_agree;
    Button btn_strat_next;
    ImageView iv_launch;
    LinearLayout ll_yuneec_protocol;
    RelativeLayout rl_agree;
    AlignedTextView tv_yuneec_app_protocol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        isFirstStartApp = SharedPreUtil.getBoolean(this,SharedPreUtil.IS_FIRST_START_APP,true);
        initView();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(iv_launch, "alpha", 1f, 0f);
                animator.setDuration(2000);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (!isFirstStartApp){
                            startFirstPageActivity();
                        }else {
                            ll_yuneec_protocol.setVisibility(View.VISIBLE);
                            rl_agree.setVisibility(View.VISIBLE);
                        }
                    }
                });
                animator.start();
            }
        },1000);
    }

    private void initView() {
        tv_yuneec_app_protocol = findViewById(R.id.tv_yuneec_app_protocol);
        ll_yuneec_protocol = findViewById(R.id.ll_yuneec_protocol);
        rl_agree = findViewById(R.id.rl_agree);
        iv_launch = findViewById(R.id.iv_launch);
        cb_agree = findViewById(R.id.cb_agree);
        btn_strat_next = findViewById(R.id.btn_strat_next);
        btn_strat_next.setOnClickListener(this);

        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    btn_strat_next.setEnabled(true);
                    btn_strat_next.setBackground(getResources().getDrawable(R.drawable.agree_button_selector));
                }else {
                    btn_strat_next.setEnabled(false);
                    btn_strat_next.setBackground(getResources().getDrawable(R.color.welcome_btn_unclick));
                }
            }
        });
    }

    private void startFirstPageActivity() {
        startActivity(StartPageActivity.this,FirstPageActivity.class);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_strat_next:
                SharedPreUtil.saveBoolean(this,SharedPreUtil.IS_FIRST_START_APP,false);
                startFirstPageActivity();
                break;
        }
    }

    private class MyHandler extends Handler {
        private WeakReference<StartPageActivity> weakReference;
        public MyHandler(StartPageActivity activity) {
            this.weakReference = new WeakReference<StartPageActivity>(activity);
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
}
