package com.fei.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fei.demo.R;

public class BaseActivity extends AppCompatActivity {

    ImageView iv_gr_code_back;
    TextView tv_skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.setContentView(R.layout.activity_app_title);
    }

    public void initBackClick(){
        iv_gr_code_back = findViewById(R.id.iv_gr_code_back);
        iv_gr_code_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initSkip(boolean show, final CallBack callBack){
        if (show){
            tv_skip = findViewById(R.id.tv_skip);
            tv_skip.setVisibility(View.VISIBLE);
            tv_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.skipCallBack();
                    Toast.makeText(BaseActivity.this, "跳转图传首页!!!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public interface CallBack{
        void skipCallBack();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.alpha_out);
    }

    public void startActivity(Context context,Class c) {
        startActivity(new Intent(context,c));
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

}
