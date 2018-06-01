package com.fei.demo.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fei.demo.R;
import com.fei.demo.okhttp.DownloadListener;
import com.fei.demo.okhttp.DownloadService;
import com.fei.demo.okhttp.DownloadTask;
import com.fei.demo.okhttp.HttpUtils;
import com.fei.demo.okhttp.xutilsFactory;
import com.fei.demo.ota.ModuleVersion;
import com.fei.demo.ota.UpdateVersion;
import com.fei.demo.ota.VersionUtil;
import com.fei.demo.views.RoundProgressBar;

import java.lang.ref.WeakReference;

public class OtaTestActivity extends Activity implements View.OnClickListener {

    private String updateVersionUrl = "http://139.196.43.67:8080/firmwareset?typename=typhoonplus";
    private String controllerUrl = "http://47.89.40.47:8231/static/upload/H520C_OTA/TYPHOONHPLUS_ST16_A_1.0.01_BUILD305_20180117.zip";
    private String cameraUrl = "http://47.89.40.47:8231/static/upload/H520C_OTA/TYPHOONHPLUS_C23_E_1.0.01_BUILD305_20180117.yuneec";
    private String qqUrl = "https://dldir1.qq.com/qqfile/qq/QQ9.0.1/23123/QQ9.0.1.exe";
    private DownloadTask downloadTask;

    private MyHandler handler = new MyHandler(OtaTestActivity.this);

    private Button btn_start_download;
    private Button btn_pause_download;
    private Button btn_cancel_download;
    private Button btn_get_update_info;
    private TextView tv_update_version;
    private RoundProgressBar roundProgressBar;

    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ota_test);

        initView();
        bindDownLoadService();
    }

    private void bindDownLoadService() {
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void initView() {
        btn_start_download = findViewById(R.id.btn_start_download);
        btn_pause_download = findViewById(R.id.btn_pause_download);
        btn_cancel_download = findViewById(R.id.btn_cancel_download);
        btn_get_update_info = findViewById(R.id.btn_get_update_info);
        tv_update_version = findViewById(R.id.tv_update_version);
        roundProgressBar = findViewById(R.id.roundProgressBar);
        btn_start_download.setOnClickListener(this);
        btn_pause_download.setOnClickListener(this);
        btn_cancel_download.setOnClickListener(this);
        btn_get_update_info.setOnClickListener(this);
    }

    private void showUpdatePregress(long total, long current){
        if(roundProgressBar.getVisibility() != View.VISIBLE){
            roundProgressBar.setVisibility(View.VISIBLE);
        }
        roundProgressBar.setProgressFileCurrent(total,current);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_download:
                startDownLoad(qqUrl);
                break;
            case R.id.btn_pause_download:
                pauseDownLoad();
                break;
            case R.id.btn_cancel_download:
                cancelDownLoad();
                break;
            case R.id.btn_get_update_info:
                getUpdateInfo();
                break;
        }
    }

    private void startDownLoad(String url) {
        if (downloadTask == null) {
            downloadTask = new DownloadTask(new DownloadListener() {
                @Override
                public void onDownLoading(long downloadLength, long contentLength) {
                    int progress = (int) (downloadLength * 100 / contentLength);
                    Log.e("yuneec0", "onDownLoading :  contentLength: " + contentLength + "  downloadLength: " + downloadLength  + "  progress:" + progress);
                    Bundle bundle = new Bundle();
                    bundle.putLong("total",contentLength);
                    bundle.putLong("current",downloadLength);
                    Message message = Message.obtain();
                    message.what = 0 ;
                    message.setData(bundle);
                    handler.sendMessage(message);
                }

                @Override
                public void onProgress(int progress) {
                    Log.e("yuneec0", "progress :" + progress);
                }

                @Override
                public void onSuccess() {
                    Log.e("yuneec0", "onSuccess" );
                }

                @Override
                public void onFailed() {
                    Log.e("yuneec0", "onFailed" );
                }

                @Override
                public void onPaused() {
                    Log.e("yuneec0", "onPaused" );
                }

                @Override
                public void onCanceled() {
                    Log.e("yuneec0", "onCanceled" );
                }
            });
            downloadTask.execute(url);
        }
    }

    private void pauseDownLoad() {
        if (downloadTask != null) {
            downloadTask.pauseDownload();
            downloadTask = null;
        }
    }

    private void cancelDownLoad() {
        if (downloadTask != null) {
            downloadTask.cancelDownload();
            downloadTask = null;
        }
    }

    String version = "";
    private void getUpdateInfo() {

        xutilsFactory.getInstance(getApplication()).xUtils3Get(updateVersionUrl, new xutilsFactory.ParseUpdateVersionListener() {
            @Override
            public void onSuccess(UpdateVersion updateVersion) {
                ModuleVersion moduleVersion1 = VersionUtil.getInstance().getModuleVersion(updateVersion,"Controller");
                ModuleVersion moduleVersion2 = VersionUtil.getInstance().getModuleVersion(updateVersion,"APP");
                ModuleVersion moduleVersion3 = VersionUtil.getInstance().getModuleVersion(updateVersion,"OTA","A");
                ModuleVersion moduleVersion4 = VersionUtil.getInstance().getModuleVersion(updateVersion,"OTA","E");
                version = "Controller :"  + moduleVersion1.getName() + "  " + moduleVersion1.getWifiband() + "  " + moduleVersion1.getVersion() + "\n" + moduleVersion1.getUrl() + "\n"
                        + "APP :"  + moduleVersion2.getName() + "  " + moduleVersion2.getWifiband() + "  " + moduleVersion2.getVersion() + "\n" + moduleVersion2.getUrl() + "\n"
                        + "OTA :"  + moduleVersion3.getName() + "  " + moduleVersion3.getWifiband() + "  " + moduleVersion3.getVersion() + "\n" + moduleVersion3.getUrl() + "\n"
                        + "OTA :"  + moduleVersion4.getName() + "  " + moduleVersion4.getWifiband() + "  " + moduleVersion4.getVersion() + "\n" + moduleVersion4.getUrl() + "\n";
                tv_update_version.setText(version);
//                handler.sendMessage(Message.obtain(null,1));
            }
        });

//        HttpUtils.getInstance().syncGethttp(updateVersionUrl, new HttpUtils.ParseUpdateVersionListener() {
//            @Override
//            public void onSuccess(UpdateVersion updateVersion) {
//                ModuleVersion moduleVersion1 = VersionUtil.getInstance().getModuleVersion(updateVersion,"Controller");
//                ModuleVersion moduleVersion2 = VersionUtil.getInstance().getModuleVersion(updateVersion,"APP");
//                ModuleVersion moduleVersion3 = VersionUtil.getInstance().getModuleVersion(updateVersion,"OTA","A");
//                ModuleVersion moduleVersion4 = VersionUtil.getInstance().getModuleVersion(updateVersion,"OTA","E");
//                version = "Controller :"  + moduleVersion1.getName() + "  " + moduleVersion1.getWifiband() + "  " + moduleVersion1.getVersion() + "\n" + moduleVersion1.getUrl() + "\n"
//                        + "APP :"  + moduleVersion2.getName() + "  " + moduleVersion2.getWifiband() + "  " + moduleVersion2.getVersion() + "\n" + moduleVersion2.getUrl() + "\n"
//                        + "OTA :"  + moduleVersion3.getName() + "  " + moduleVersion3.getWifiband() + "  " + moduleVersion3.getVersion() + "\n" + moduleVersion3.getUrl() + "\n"
//                        + "OTA :"  + moduleVersion4.getName() + "  " + moduleVersion4.getWifiband() + "  " + moduleVersion4.getVersion() + "\n" + moduleVersion4.getUrl() + "\n";
//                handler.sendMessage(Message.obtain(null,1));
//            }
//        });
    }

    private class MyHandler extends Handler {
        private WeakReference<OtaTestActivity> weakReference;

        public MyHandler(OtaTestActivity activity) {
            this.weakReference = new WeakReference<OtaTestActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference.get() == null) {
                return;
            }
            switch (msg.what){
                case 0:
                    Bundle bundle = msg.getData();
                    showUpdatePregress(bundle.getLong("total"),bundle.getLong("current"));
                    break;
                case 1:
                    tv_update_version.setText(version);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
