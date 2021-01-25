package com.fei.demo.wifi;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fei.demo.R;
import com.fei.demo.utils.SharedPreUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WifiConnectView extends FrameLayout {

    Context context;
    WifiManager wifiManager;
    List<ScanResult> scanResults = new ArrayList<ScanResult>();
    ScanResult connectedScanResult;
    ObjectAnimator alphaAnimator;
    View searchLoading;
    RecyclerView rv_wifi_list;
    RecyclerView.Adapter wifiListAdapter;
    String lastConnectedWifiPassword = "";
    private static final int WIFI_STRENGTH_FILTER = -100;
    private static final String[] WIFI_NAME_FILTER = {"C23", " "};

    public WifiConnectView(Context context) {
        this(context, null);
    }

    public WifiConnectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WifiConnectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View contentView = LayoutInflater.from(context).inflate(R.layout.settings_wifi_connect, null);
        addView(contentView);
        init();
    }

    private void init() {
        registerSystemStatusReceiver();
        if (wifiManager == null) {
            wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        searchLoading = findViewById(R.id.tv_search_loading);
        rv_wifi_list = findViewById(R.id.rv_wifi_list);
        rv_wifi_list.setHasFixedSize(true);
//        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        rv_wifi_list.setLayoutManager(linerLayoutManager);
        rv_wifi_list.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv_wifi_list.setItemAnimator(new DefaultItemAnimator());
        rv_wifi_list.setAdapter(wifiListAdapter = new WifiListAdapter());
        scanWifi();
    }

    private void scanWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        startSearchAnimator();
    }

    private void startSearchAnimator(){
        searchLoading.setVisibility(View.VISIBLE);
        if (alphaAnimator == null) {
            alphaAnimator = ObjectAnimator.ofFloat(searchLoading, "alpha", 0.2f, 1);
            alphaAnimator.setInterpolator(new LinearInterpolator());
            alphaAnimator.setDuration(2000);
            alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
            alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
            alphaAnimator.setAutoCancel(true);
            alphaAnimator.start();
        }
    }

    private void stopSearchAnimator(){
        searchLoading.setVisibility(View.GONE);
        if (alphaAnimator != null) {
            alphaAnimator.cancel();
            alphaAnimator = null;
        }
    }

    class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
            View connectedIcon;
            TextView wifiName;

            public MyViewHolder(View itemView) {
                super(itemView);
                connectedIcon = itemView.findViewById(R.id.connected_icon);
                wifiName = itemView.findViewById(R.id.wifi_name_text);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int selectedIndex = getAdapterPosition();
                ScanResult scanResult = scanResults.get(selectedIndex);
                String key = '"'+ scanResult.SSID+ '"';
                String password = SharedPreUtil.getString(getContext(),key);
                Log.e("yuneec0","点击item ssid :" + scanResult.SSID + "  password:" + password);
                if("".equals(password)){
                    showPasswordDialog(scanResult);
                }else {
                    WifiUtil.getInstance(getContext()).connectWifi(scanResult,password);
                }
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_wifi_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            position = holder.getAdapterPosition();
            holder.wifiName.setText(scanResults.get(position).SSID);
            if (position == 0) {
                if (connectedScanResult == null) {
                    holder.connectedIcon.setVisibility(View.INVISIBLE);
                } else {
                    boolean equal = connectedScanResult.BSSID.equals(scanResults.get(position).BSSID);
                    holder.connectedIcon.setVisibility(equal ? View.VISIBLE : View.INVISIBLE);
                }
            } else {
                holder.connectedIcon.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return scanResults.size();
        }
    }

    boolean savePassword = false;
    private void showPasswordDialog(final ScanResult scanResult) {
        InputPasswordDialog dialog = new InputPasswordDialog(getContext());
        dialog.setTitle(scanResult.SSID);
        dialog.setResultListener(new InputPasswordDialog.ResultListener() {
            @Override
            public void onOK(String password) {
                Log.e("yuneec0","点击输入框写入的密码:" + password);
                lastConnectedWifiPassword = password;
                savePassword = true;
                WifiUtil.getInstance(getContext()).connectWifi(scanResult,password);
            }
        });
        dialog.show();
    }

    MyReceiver myReceiver;
    private void registerSystemStatusReceiver() {
        if(myReceiver == null){
            myReceiver = new MyReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            getContext().registerReceiver(myReceiver, filter);
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        Log.e("yuneec0","wifi conected : SSID:" + wifiInfo.getSSID() + "  ; BSSID:" + wifiInfo.getBSSID() + "  " );
                        if(savePassword){
                            SharedPreUtil.saveString(getContext(),wifiInfo.getSSID(),lastConnectedWifiPassword);
                            savePassword = false;
                        }
                        moveTopWifi(wifiInfo.getBSSID());
                    } else if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {

                    }
                    break;
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    addWifiToScanResults();
                    stopSearchAnimator();
                    break;
            }
        }
    }

    private void moveTopWifi(String bssid) {
        for (ScanResult s : scanResults){
            if (s.BSSID.equals(bssid)) {
                scanResults.remove(s);
                scanResults.add(0,s);
                connectedScanResult = s;
                break;
            }
        }
        wifiListAdapter.notifyItemRangeInserted(0, scanResults.size());
    }

    private void addWifiToScanResults() {
        wifiListAdapter.notifyItemRangeRemoved(0, scanResults.size());
        scanResults.clear();
        List<ScanResult> results = wifiManager.getScanResults();
        String connectedBSSID = wifiManager.getConnectionInfo().getBSSID();
        for (ScanResult s : results) {
            if (!Arrays.asList(WIFI_NAME_FILTER).contains(s.SSID.trim().length() > 3 ? s.SSID.substring(0, 3) : s.SSID)) {
                if (s.BSSID.equals(connectedBSSID)) {
                    Log.e("yuneec0","scanResults ----------- :" + wifiManager.getConnectionInfo().getSSID() + "  --- >" + s.SSID);
                    connectedScanResult = s;
                    scanResults.add(s);
                }else if (s.level > WIFI_STRENGTH_FILTER) {
                    scanResults.add(s);
                }
            }
        }
        for (int i = 0; i < scanResults.size() - 1; i++) {  //去重
                for (int j = scanResults.size() - 1; j > i; j--) {
                    if (scanResults.get(j).SSID.equals(scanResults.get(i).SSID)) {
                        scanResults.remove(j);
                    }
                }
        }
        wifiListAdapter.notifyItemRangeInserted(0, scanResults.size());
        Log.e("yuneec0","scanResults :" + scanResults.size());
    }

}
