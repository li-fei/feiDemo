package com.fei.demo.ota;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/11/011.
 */

public class UpdateVersion {
    private String aircraftName;
    private String noticeZH;
    private String noticeEN;
    private ArrayList<ModuleVersion> list;

    public String getAircraftName() {
        return aircraftName;
    }

    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    public String getNoticeZH() {
        return noticeZH;
    }

    public void setNoticeZH(String noticeZH) {
        this.noticeZH = noticeZH;
    }

    public String getNoticeEN() {
        return noticeEN;
    }

    public void setNoticeEN(String noticeEN) {
        this.noticeEN = noticeEN;
    }

    public ArrayList<ModuleVersion> getList() {
        return list;
    }

    public void setList(ArrayList<ModuleVersion> list) {
        this.list = list;
    }
}
