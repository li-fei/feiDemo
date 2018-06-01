package com.fei.demo.okhttp;

/**
 * Created by Administrator on 2018/2/9/009.
 */

public interface DownloadListener {

    void onDownLoading(long total, long current);
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();

}
