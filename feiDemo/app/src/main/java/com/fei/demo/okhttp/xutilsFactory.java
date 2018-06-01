package com.fei.demo.okhttp;

import android.app.Application;
import android.util.Log;

import com.fei.demo.ota.ModuleVersion;
import com.fei.demo.ota.UpdateVersion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/11/011.
 */

public class xutilsFactory {

    private DownloadListener listener;
    Callback.Cancelable cancelable;
    private static xutilsFactory instance;

    public static xutilsFactory getInstance(Application application){
        if (instance == null){
            instance = new xutilsFactory();
        }
        x.Ext.init(application);
        return instance;
    }

    public void setDownloadListener(DownloadListener listener) {
        this.listener = listener;
    }

    public void cancelDown(){
        cancelable.cancel();
    }

    public void startDown(String url){
        String fileName = url.substring(url.lastIndexOf("/")); //下载文件的名称
        RequestParams params = new RequestParams(url);
        params.setAutoResume(true);//设置是否在下载是自动断点续传
        params.setAutoRename(false);//设置是否根据头信息自动命名文件
        params.setSaveFilePath("/storage/sdcard0/" + fileName);
        params.setExecutor(new PriorityExecutor(2, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
        params.setCancelFast(true);//是否可以被立即停止.
        //下面的回调都是在主线程中运行的,这里设置的带进度的回调
        cancelable =  x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                Log.i("tag", "取消"+Thread.currentThread().getName());
                listener.onCanceled();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.i("tag", "onError: 失败"+Thread.currentThread().getName());
                listener.onFailed();
            }

            @Override
            public void onFinished() {
                Log.i("tag", "完成,每次取消下载也会执行该方法"+Thread.currentThread().getName());
            }

            @Override
            public void onSuccess(File arg0) {
                Log.i("tag", "下载成功的时候执行"+Thread.currentThread().getName());
                listener.onSuccess();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (isDownloading) {
                    int progress = (int) (current * 100 / total);
                    Log.i("tag", "下载中,会不断的进行回调:"+Thread.currentThread().getName() + "  total:" + total + "  current:" + current + "  progress:" + progress);
                    listener.onDownLoading(total,current);
                    listener.onProgress(progress);
                }
            }

            @Override
            public void onStarted() {
                Log.i("tag", "开始下载的时候执行"+Thread.currentThread().getName());
            }

            @Override
            public void onWaiting() {
                Log.i("tag", "等待,在onStarted方法之前执行"+Thread.currentThread().getName());
            }

        });
    }

    public void xUtils3Get(String url, final ParseUpdateVersionListener parseUpdateVersionListener){
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("limit","1");
        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("tag", "请求成功 "+Thread.currentThread().getName());
                Log.i("tag", "请求成功: "+result);
                UpdateVersion updateVersion = parseUpdateVersion(result);
                parseUpdateVersionListener.onSuccess(updateVersion);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("tag", "请求异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i("tag", "取消请求的回调方法");
            }

            @Override
            public void onFinished() {
                Log.i("tag", "请求完成,并不一定是请求成功,断开了连接就会执行该方法");
            }
        });
    }

    ParseUpdateVersionListener parseUpdateVersionListener;
    public interface ParseUpdateVersionListener {
        void onSuccess(UpdateVersion updateVersion);
    }

    private UpdateVersion parseUpdateVersion(String version) {
        UpdateVersion updateVersion = new UpdateVersion();
        try {
            ArrayList<ModuleVersion> list = new ArrayList<ModuleVersion>();
            JSONObject jsonObject = new JSONObject(version);
            String data = jsonObject.getString("data");
            JSONObject datajsonObject = new JSONObject(data);
            JSONObject firmwareset = datajsonObject.getJSONArray("firmwareset").getJSONObject(0);
            updateVersion.setAircraftName(firmwareset.getString("name"));
            JSONArray edititionJsonArray = firmwareset.getJSONArray("editition");

            JSONArray firmwareJsonArray = edititionJsonArray.getJSONObject(0).getJSONArray("firmware");
            JSONArray noticeJsonArray = edititionJsonArray.getJSONObject(0).getJSONArray("notice");

            updateVersion.setNoticeZH(noticeJsonArray.getJSONObject(0).getString("zh"));
            updateVersion.setNoticeEN(noticeJsonArray.getJSONObject(1).getString("en"));

            for (int i=0;i<firmwareJsonArray.length();i++){
                ModuleVersion moduleVersion = new ModuleVersion();
                moduleVersion.setComponent(firmwareJsonArray.getJSONObject(i).getString("component"));
                moduleVersion.setName(firmwareJsonArray.getJSONObject(i).getString("name"));
                moduleVersion.setWifiband(firmwareJsonArray.getJSONObject(i).getString("wifiband"));
                moduleVersion.setUrl(firmwareJsonArray.getJSONObject(i).getString("url"));
                moduleVersion.setVersion(firmwareJsonArray.getJSONObject(i).getString("version"));
                list.add(moduleVersion);
            }
            updateVersion.setList(list);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return updateVersion;
    }
}
