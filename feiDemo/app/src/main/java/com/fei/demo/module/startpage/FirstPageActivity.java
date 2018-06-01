package com.fei.demo.module.startpage;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fei.demo.R;
import com.fei.demo.activity.BaseActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FirstPageActivity extends BaseActivity implements View.OnClickListener {

    private MyHandler mHandler = new MyHandler(FirstPageActivity.this);

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSON_REQUESTCODE = 100;

    ImageView iv_more, iv_first_gallery, iv_first_qr_code;
    ImageView iv_drone_connect_status_pic;
    Button btn_drone_connect;

    private SystemStatusReceiver mSystemStatusReceiver;
    private boolean isDroneConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        initView();
        checkPermission();
        registerSystemStatusReceiver();
        setDroneConnected(isDroneConnected);
    }

    private void initView() {
        iv_more = findViewById(R.id.iv_more);
        iv_first_gallery = findViewById(R.id.iv_first_gallery);
        iv_first_qr_code = findViewById(R.id.iv_first_qr_code);
        iv_more.setOnClickListener(this);
        iv_first_gallery.setOnClickListener(this);
        iv_first_qr_code.setOnClickListener(this);
        iv_drone_connect_status_pic = findViewById(R.id.iv_drone_connect_status_pic);
        btn_drone_connect = findViewById(R.id.btn_drone_connect);
        btn_drone_connect.setOnClickListener(this);
    }

    private void registerSystemStatusReceiver() {
        mSystemStatusReceiver = new SystemStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mSystemStatusReceiver, filter);
    }

    private void unRegisterUserStatusReceiver() {
        unregisterReceiver(mSystemStatusReceiver);
    }

    private class SystemStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                // Listen Network Status Change
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        if (wifiInfo.getIpAddress() != 0) {

                        }
                    } else if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {

                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setDroneConnected(boolean isDroneConnected) {
        if (isDroneConnected) {
            iv_drone_connect_status_pic.setBackgroundResource(R.drawable.drone_connected);
            btn_drone_connect.setText(R.string.str_yuneec_drone_connect);
        } else {
            iv_drone_connect_status_pic.setBackgroundResource(R.drawable.drone_not_connect);
            btn_drone_connect.setText(R.string.str_yuneec_drone_disconnect);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                startActivity(FirstPageActivity.this, MoreActivity.class);
                break;
            case R.id.iv_first_gallery:
                setDroneConnected(false);
                break;
            case R.id.iv_first_qr_code:
//                startActivity(FirstPageActivity.this, QRcodeActivity.class);
                Intent intent = new Intent(FirstPageActivity.this, QRcodeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                break;
            case R.id.btn_drone_connect:
                if (isDroneConnected) {

                } else {
                    startActivity(FirstPageActivity.this, ConnectSelectActivity.class);
                }
                break;
        }
    }

    private class MyHandler extends Handler {
        private WeakReference<FirstPageActivity> weakReference;

        public MyHandler(FirstPageActivity activity) {
            this.weakReference = new WeakReference<FirstPageActivity>(activity);
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


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(permissions);
        } else {
            Log.e("yuneec0", "6.0以下，不需要动态权限");
        }
    }

    private void checkPermissions(List<String> permissionList) {
        if (permissionList.size() != 0) {
            requestPermission(permissionList.toArray(new String[permissionList.size()]));
        }
    }

    private void checkPermissions(String[] permissions) {
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {
            Log.e("yuneec0", "未授予的权限为空，表示都授予了");
        } else {
//            ActivityCompat.requestPermissions(this, mPermissionList.toArray(new String[mPermissionList.size()]), PERMISSON_REQUESTCODE);
            requestPermission(mPermissionList.toArray(new String[mPermissionList.size()]));
        }
    }

    private void requestPermission(String[] permission) {
        ActivityCompat.requestPermissions(this, permission, PERMISSON_REQUESTCODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSON_REQUESTCODE:
                mPermissionList.clear();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Log.e("yuneec0", "禁止权限:" + permissions[i]);
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                        if (showRequestPermission) { //重新申请权限
                            Log.e("yuneec0", "第一次禁止权限:" + permissions[i]);
//                            requestPermission(new String[]{permissions[i]});
                            mPermissionList.add(permissions[i]);
                        } else {
                            Log.e("yuneec0", "第二次勾选禁止后不再询问 :" + permissions[i]);
//                            finish();
                            toSelfSetting(this);
                        }
                    } else {
                        Log.e("yuneec0", "同意权限:" + permissions[i]);
                    }
                }
                checkPermissions(mPermissionList);
                break;
            default:
                break;
        }
    }

    public static final int REQUEST_CODE = 111;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(FirstPageActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public static void toSelfSetting(Context context) {
//        Intent mItent=new Intent(Settings.ACTION_SETTINGS);
//        startActivity(mItent);
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }

    @Override
    protected void onDestroy() {
        unRegisterUserStatusReceiver();
        super.onDestroy();
    }
}
