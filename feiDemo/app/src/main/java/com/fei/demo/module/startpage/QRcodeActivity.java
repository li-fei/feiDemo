package com.fei.demo.module.startpage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.fei.demo.R;
import com.fei.demo.activity.BaseActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class QRcodeActivity extends BaseActivity {

    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        initBackClick();
    }

    public void initBackClick() {
        ImageView iv_gr_code_back = findViewById(R.id.iv_gr_code_back);
        iv_gr_code_back.setImageResource(R.drawable.first_qr_code_back_button_selector);
        iv_gr_code_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


        CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                bundle.putString(CodeUtils.RESULT_STRING, result);
                resultIntent.putExtras(bundle);
                QRcodeActivity.this.setResult(RESULT_OK, resultIntent);
                QRcodeActivity.this.finish();
            }

            @Override
            public void onAnalyzeFailed() {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                bundle.putString(CodeUtils.RESULT_STRING, "");
                resultIntent.putExtras(bundle);
                QRcodeActivity.this.setResult(RESULT_OK, resultIntent);
                QRcodeActivity.this.finish();
            }
    };
}
