package com.fei.demo.ftp;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FTPClientFunctions {

    private static final String TAG = "FTPClientFunctions";

    private FTPClient ftpClient = null;

    private double response;

    public boolean ftpConnect(String host, int port, String username, String password) {
        try {
            int reply;
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("UTF-8");
            Log.e(TAG, "connecting to the ftp server " + host + ":" + port);
            ftpClient.connect(host, port);
            reply = ftpClient.getReplyCode();
            Log.e(TAG, "connecting ReplyCode : " + reply);
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                Log.e(TAG, "login to the ftp server");
                boolean status = ftpClient.login(username, password);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                reply = ftpClient.getReplyCode();
                Log.e(TAG, "login ReplyCode : " + reply);
                return status;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error: could not connect to host " + host);
        }
        return false;
    }

    public boolean ftpDisconnect() {
        if (ftpClient == null) {
            return true;
        }
        try {
            ftpClient.logout();
            ftpClient.disconnect();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
        }
        return false;
    }

    public boolean uploadingSingle(File localFile) {
        boolean flag = true;
        try {
            InputStream inputStream = new FileInputStream(localFile);
            response += (double) inputStream.available() / 1;
            Log.e(TAG, "upload response: " + response);
            flag = ftpClient.storeFile(localFile.getName(), inputStream);
            inputStream.close();
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean uploadingSingle(File localFile, UploadProgressListener listener) {
        boolean flag = true;
        try {
            BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(localFile));
            ProgressInputStream progressInput = new ProgressInputStream(buffIn, listener, localFile);
            flag = ftpClient.storeFile(localFile.getName(), progressInput);
            buffIn.close();
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    public boolean ftpChangeDir(String path) {
        boolean status = false;
        try {
            status = ftpClient.changeWorkingDirectory(path);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "change directory failed: " + e.getLocalizedMessage());
        }
        return status;
    }

}
