package com.fei.demo.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fei.demo.R;
import com.fei.demo.adapter.WelcomeAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {

    private MyHandler mHandler = new MyHandler(WelcomeActivity.this);
    ViewPager vp_welcome;
    LinearLayout ll_show_enter;
    Button btn_strat_main;
    ImageView iv_launch;

    private int[] imageIdArray = new int[]{R.color.red,R.color.yellow,R.color.green};
    private List<View> viewList = new ArrayList<>();

    private LinearLayout vg;
//    private ImageView iv_point;
    private ImageView[] ivPointArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initView();
        initViewPager();
        initPoint();
        startMainActivity();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(iv_launch, "alpha", 1f, 0f);
                animator.setDuration(2000);
                animator.start();
//                iv_launch.setVisibility(View.GONE);
            }
        },2000);
    }

    private void initView() {
        iv_launch = findViewById(R.id.iv_launch);
        vp_welcome = findViewById(R.id.vp_welcome);
        vg = findViewById(R.id.guide_ll_point);
        ll_show_enter = findViewById(R.id.ll_show_enter);
        btn_strat_main = findViewById(R.id.btn_strat_main);
    }

    private void initViewPager() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        int len = imageIdArray.length;
        for (int i = 0;i<len;i++){
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundColor((getResources().getColor(imageIdArray[i])));
            viewList.add(imageView);
        }
        vp_welcome.setAdapter(new WelcomeAdapter(viewList));
        vp_welcome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == imageIdArray.length - 1){
                    ll_show_enter.setVisibility(View.VISIBLE);
                }else {
                    ll_show_enter.setVisibility(View.GONE);
                }

                for (int i = 0;i<imageIdArray.length;i++){
                    ivPointArray[position].setBackgroundColor(getResources().getColor(R.color.blue));
                    if (position != i){
                        ivPointArray[i].setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPoint() {
        ivPointArray = new ImageView[viewList.size()];
        int size = viewList.size();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);
        params.leftMargin = 10;
        for (int i = 0; i < size; i++) {
            ImageView iv_point = new ImageView(this);
            iv_point.setLayoutParams(params);
            iv_point.setPadding(30, 0, 30, 0);//left,top,right,bottom
            ivPointArray[i] = iv_point;
            if (i == 0) {
                iv_point.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                iv_point.setBackgroundColor(getResources().getColor(R.color.white));
            }
            vg.addView(ivPointArray[i]);
        }
    }

    private void startMainActivity() {
        btn_strat_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private class MyHandler extends Handler {
        private WeakReference<WelcomeActivity> weakReference;

        public MyHandler(WelcomeActivity activity) {
            this.weakReference = new WeakReference<WelcomeActivity>(activity);
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
                case 1:
                    break;
            }
        }
    }
}
