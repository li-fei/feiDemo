package com.fei.demo.ftp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fei.demo.R;

import java.io.File;
import java.lang.ref.WeakReference;

public class FTPActivity extends AppCompatActivity implements View.OnClickListener {

    private MyHandler handler = new MyHandler(FTPActivity.this);

    private Button btn_ofdm_upload_st16;
    private Button btn_ofdm_upload_drone;
    private TextView tv_up_status;
    private LoadProgressDialog loadProgressDialog;

    private FTPClientFunctions ftpClient;
    private final String sourceFilePath = "/storage/sdcard0/";
    private final String descFileName = "wireless_ofdm_lc6500.zip";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);
        initView();
    }

    private void initView() {
        btn_ofdm_upload_st16 = findViewById(R.id.btn_ofdm_upload_st16);
        btn_ofdm_upload_st16.setOnClickListener(this);
        btn_ofdm_upload_drone = findViewById(R.id.btn_ofdm_upload_drone);
        btn_ofdm_upload_drone.setOnClickListener(this);
        tv_up_status = findViewById(R.id.tv_up_status);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ofdm_upload_st16:
                startUpLoad(FTPParameter.FTP_SERVER_ST16);
                break;
            case R.id.btn_ofdm_upload_drone:
                startUpLoad(FTPParameter.FTP_SERVER_DRONE);
                break;
            default:
                break;
        }
    }

    private void startUpLoad(final String host) {
        tv_up_status.setText("");
        showLoading("upLoading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                ftpClient = new FTPClientFunctions();
                boolean connectResult = ftpClient.ftpConnect(host, FTPParameter.FTP_PORT, FTPParameter.FTP_USERNAME, FTPParameter.FTP_PASSWORD);
                if (connectResult) {
                    boolean uploadResult = ftpClient.uploadingSingle(new File(sourceFilePath + descFileName));
                    if (uploadResult) {
                        dismissLoading(3000, "upLoad Success !");
                        boolean disConnectResult = ftpClient.ftpDisconnect();
                    } else {
                        dismissLoading(3000, "upLoad Fail !");
                    }
                } else {
                    dismissLoading(3000, "connect FTP server Failed !");
                }
                */
                ftpClient = new FTPClientFunctions();
                boolean connectResult = ftpClient.ftpConnect(host, FTPParameter.FTP_PORT, FTPParameter.FTP_USERNAME, FTPParameter.FTP_PASSWORD);
                if (connectResult) {
                    final boolean uploadResult = ftpClient.uploadingSingle(new File(sourceFilePath + descFileName), new UploadProgressListener() {
                        @Override
                        public void onUploadProgress(String currentStep, long uploadSize, File file) {
                            showInfo(file.length() + currentStep + uploadSize);
                        }
                    });
                    if (uploadResult) {
                        dismissLoading(3000, "upLoad Success !");
                        boolean disConnectResult = ftpClient.ftpDisconnect();
                    } else {
                        dismissLoading(3000, "upLoad Fail !");
                    }
                } else {
                    dismissLoading(3000, "connect FTP server Failed !");
                }
            }
        }).start();
    }

    private void showLoading(String info) {
        loadProgressDialog = new LoadProgressDialog(FTPActivity.this, info);
        loadProgressDialog.show();
    }

    private void dismissLoading(int delayTime, final String msg) {
        if (delayTime == 0) {
            loadProgressDialog.dismiss();
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadProgressDialog.dismiss();
                    showInfo(msg);
                }
            }, delayTime);
        }
    }

    private void showInfo(final String info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_up_status.setText(info);
            }
        });
    }

    private class MyHandler extends Handler {
        private WeakReference<FTPActivity> weakReference;

        public MyHandler(FTPActivity activity) {
            this.weakReference = new WeakReference<FTPActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference.get() == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    break;
            }
        }
    }
}
