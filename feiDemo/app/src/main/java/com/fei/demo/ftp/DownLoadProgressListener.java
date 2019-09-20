package com.fei.demo.ftp;

import java.io.File;

public interface DownLoadProgressListener {
    public void onDownLoadProgress(String currentStep, long downProcess, File file);
}
