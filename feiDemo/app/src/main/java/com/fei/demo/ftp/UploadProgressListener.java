package com.fei.demo.ftp;

import java.io.File;

public interface UploadProgressListener {

    public void onUploadProgress(String currentStep, long uploadSize, File file);

}
