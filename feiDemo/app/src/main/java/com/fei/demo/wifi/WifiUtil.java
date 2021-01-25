package com.fei.demo.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Administrator on 2018/2/12/012.
 */

public class WifiUtil {

    private static WifiManager wifiManager;

    private static WifiUtil instance;

    public static WifiUtil getInstance(Context context){
        if (wifiManager == null) {
            wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        if (instance == null){
            instance = new WifiUtil();
        }
        return instance;
    }

    public void connectWifi(ScanResult scanResult, String password) {
        WifiConfiguration cameraWifiConfig = new WifiConfiguration();
        cameraWifiConfig.BSSID = scanResult.BSSID;
        cameraWifiConfig.SSID = "\"" + scanResult.SSID + "\"";
        if (scanResult.capabilities.contains("WPA")) {
            cameraWifiConfig.preSharedKey = "\"" + password + "\"";
        } else {
            cameraWifiConfig.wepKeys[0] = "\"" + password + "\"";
            cameraWifiConfig.wepTxKeyIndex = 0;
        }
        int cameraNetId = wifiManager.addNetwork(cameraWifiConfig);
        wifiManager.enableNetwork(cameraNetId, true);
        wifiManager.reconnect();
    }
}
