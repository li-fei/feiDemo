package com.fei.demo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fei.demo.GlobalParams;
import com.fei.demo.R;
import com.fei.demo.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private RecyclerView rv_recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        checkPermission();

    }

    private void initData() {
        for (Class activity : GlobalParams.activitys) {
            mDatas.add(activity.getSimpleName());
        }
    }

    private void initView() {
        rv_recyclerView = findViewById(R.id.rv_recyclerView);
        rv_recyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_recyclerView.setLayoutManager(linerLayoutManager);
        rv_recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerViewAdapter = new RecyclerViewAdapter(this, mDatas);
        rv_recyclerView.setAdapter(recyclerViewAdapter);
    }

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION };
    List<String> mPermissionList = new ArrayList<>();

    private static final int PERMISSON_REQUESTCODE = 100;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(permissions);
        } else {
            Log.e("yuneec0", "6.0以下，不需要动态权限");
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
            Log.e("yuneec0","未授予的权限为空，表示都授予了");
        } else {
//            ActivityCompat.requestPermissions(this, mPermissionList.toArray(new String[mPermissionList.size()]), PERMISSON_REQUESTCODE);
            requestPermission(mPermissionList.toArray(new String[mPermissionList.size()]));
        }
    }

    private void requestPermission(String[] permission){
        ActivityCompat.requestPermissions(this, permission, PERMISSON_REQUESTCODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSON_REQUESTCODE:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Log.e("yuneec0", "禁止权限:" + permissions[i]);
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                        if (showRequestPermission) { //重新申请权限
                            Log.e("yuneec0", "第一次禁止权限:" + permissions[i]);
                            requestPermission(new String[]{permissions[i]});
                        } else {
                            Log.e("yuneec0", "第二次勾选禁止后不再询问 :" + permissions[i]);
                            finish();
                        }
                    }else {
                        Log.e("yuneec0", "同意权限:" + permissions[i]);
                    }
                }
                break;
            default:
                break;
        }
    }

}
