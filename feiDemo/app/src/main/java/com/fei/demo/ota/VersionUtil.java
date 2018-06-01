package com.fei.demo.ota;

/**
 * Created by Administrator on 2018/2/11/011.
 */

public class VersionUtil {

    private static VersionUtil instance;

    public static VersionUtil getInstance(){
        if (instance == null){
            instance = new VersionUtil();
        }
        return instance;
    }

    public ModuleVersion getModuleVersion(UpdateVersion updateVersion, String component){
        ModuleVersion moduleVersion = null;
        for (int i = 0; i < updateVersion.getList().size(); i++) {
            moduleVersion = updateVersion.getList().get(i);
            if(moduleVersion.getComponent().equals(component)){
                break;
            }
        }
        return moduleVersion;
    }

    public ModuleVersion getModuleVersion(UpdateVersion updateVersion, String component,String wifiband){
        ModuleVersion moduleVersion = null;
        for (int i = 0; i < updateVersion.getList().size(); i++) {
            moduleVersion = updateVersion.getList().get(i);
            if(moduleVersion.getComponent().equals(component) && moduleVersion.getWifiband().equals(wifiband)){
                break;
            }
        }
        return moduleVersion;
    }
}
