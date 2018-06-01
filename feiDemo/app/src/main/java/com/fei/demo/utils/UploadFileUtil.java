package com.fei.demo.utils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/12/25/025.
 */

public class UploadFileUtil {

    String url = "http://192.168.42.1/cgi-bin/upload";

    private static UploadFileUtil instance;

    public static UploadFileUtil getInstance(){
        if (instance == null){
            instance = new UploadFileUtil();
        }
        return instance;
    }

    public interface UploadListener{
        void onSuccess();
        void onFailure(int statusCode);
        void onProgress(int per);
    }

    public void start(String fileName, final UploadListener uploadListener){
        File file = new File(fileName);
        String fileSize = String.valueOf(file.length());
        Log.e("yuneec0",fileSize);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("ContentType", "multipart/form-data");
        client.addHeader("File-Size",fileSize);
        client.addHeader("Expect","");
        if (file.exists() && file.length() > 0) {
            RequestParams params = new RequestParams();
            try {
                params.put("image", file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            client.post(url,params,new AsyncHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    uploadListener.onSuccess();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if(statusCode == 0){
                        uploadListener.onSuccess();
                    }else {
                        uploadListener.onFailure(statusCode);
                    }
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    double pro = (double) bytesWritten / (double)totalSize;
                    uploadListener.onProgress((int) (pro*100));
                }
            });
        }
    }
}
