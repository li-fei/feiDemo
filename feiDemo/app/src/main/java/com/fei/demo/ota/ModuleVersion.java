package com.fei.demo.ota;

/**
 * Created by Administrator on 2018/2/11/011.
 */

public class ModuleVersion {
    private String component;
    private String name;
    private String wifiband;
    private String url;
    private String version;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWifiband() {
        return wifiband;
    }

    public void setWifiband(String wifiband) {
        this.wifiband = wifiband;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
