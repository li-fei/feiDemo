package com.fei.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fei.demo.R;
import com.fei.demo.utils.UploadFileUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class AsyncHttpActivity extends Activity {

    Button btn_upload;
    Button btn_upload2;
    Button btn_upload3;
    TextView tv_progress;
    private ProgressBar pb_progress;

    String path1 = "/storage/sdcard0/TYPHOONHPLUS_C23_C_1.0.02_BUILD350_20180127.yuneec";
    String url = "http://192.168.42.1/cgi-bin/upload";
    //    String path = "/storage/sdcard0/123.rar";
    String path = "/storage/sdcard0/11.yuneec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_http);
        x.Ext.init(getApplication());

        btn_upload = findViewById(R.id.btn_upload);
        btn_upload2 = findViewById(R.id.btn_upload2);
        btn_upload3 = findViewById(R.id.btn_upload3);
        tv_progress = findViewById(R.id.tv_progress);
        pb_progress = findViewById(R.id.pb_progress);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

        btn_upload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload2();
            }
        });

        btn_upload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        upload3();
                    }
                }).start();
            }
        });

    }

    private void upload3(){
        File file1 = new File(path);
        String fileSize = String.valueOf(file1.length());
        HttpURLConnection connection = null;
        try {
            URL url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            connection.setChunkedStreamingMode(51200); // 128K
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "******");
            connection.setRequestProperty("File-Size",fileSize);
            connection.setRequestProperty("Expect","");

            // 上传文件
            FileInputStream file = new FileInputStream(path1);
            OutputStream os = connection.getOutputStream();
            byte[] b = new byte[1024];
            int count = 0;
            while((count = file.read(b)) != -1){
                os.write(b, 0, count);
                Log.e("yuneec0","---------->" + count);
            }
            os.flush();
            os.close();

            // 获取返回数据
            if(connection.getResponseCode() == 200){
                InputStream is = connection.getInputStream();
                int ch;
                StringBuffer buffer = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    buffer.append((char) ch);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(connection != null){
                connection.disconnect();
            }
        }
    }

        private void upload2() {
            File file = new File(path1);
            String fileSize = String.valueOf(file.length());
            org.xutils.http.RequestParams params = new org.xutils.http.RequestParams(url);
            params.setMultipart(true);
            params.addHeader("File-Size",fileSize);
            params.addHeader("Expect","");
            params.addBodyParameter("image", file);
            Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    tv_progress.setText("onSuccess");
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    tv_progress.setText("onError");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    tv_progress.setText("onCancelled");
                }

                @Override
                public void onFinished() {
                    tv_progress.setText("onFinished");
                }
            });
        }

    private void upload() {
        File file = new File(path);
        String fileSize = String.valueOf(file.length());
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("ContentType", "multipart/form-data");
        client.addHeader("File-Size",fileSize);
        client.addHeader("Expect","");
        if (file.exists() && file.length() > 0) {
            RequestParams params = new RequestParams();
            params.setForceMultipartEntityContentType(true);
            try {
                params.put("image", file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            client.post(url,params,new AsyncHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    tv_progress.setText("onSuccess , statusCode:" + statusCode);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    tv_progress.setText("onFailure , statusCode:" + statusCode +" "+ error.toString());
                    if(statusCode == 0){
                        tv_progress.setText("onSuccess , statusCode:" + statusCode);
                    }
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    tv_progress.setText("totalSize：" + totalSize + "  bytesWritten:" + bytesWritten);

                    double pro = (double) bytesWritten / (double)totalSize;
                    pb_progress.setProgress((int) (pro*100));
                }
            });
        }
    }


}
