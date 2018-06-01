package com.fei.demo.okhttp;

import android.util.Log;

import com.fei.demo.ota.ModuleVersion;
import com.fei.demo.ota.UpdateVersion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/11/011.
 */

public class HttpUtils {


    private static HttpUtils instance;

    public static HttpUtils getInstance(){
        if (instance == null){
            instance = new HttpUtils();
        }
        return instance;
    }

    ParseUpdateVersionListener parseUpdateVersionListener;
    public interface ParseUpdateVersionListener {
        void onSuccess(UpdateVersion updateVersion);
    }

    public void syncGethttp(String url, final ParseUpdateVersionListener parseUpdateVersionListener) {
        this.parseUpdateVersionListener = parseUpdateVersionListener;

        OkHttpClient mHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("limit","1").build();
        Request request = new Request.Builder()
                .post(body)
                .url(url).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("yuneec0", json);
                UpdateVersion updateVersion = parseUpdateVersion(json);
                parseUpdateVersionListener.onSuccess(updateVersion);
            }
        });
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
