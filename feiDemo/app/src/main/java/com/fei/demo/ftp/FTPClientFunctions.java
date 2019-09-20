package com.fei.demo.ftp;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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

    /**
     * 下载单个文件，可实现断点下载.
     *
     * @param serverPath
     *            Ftp目录及文件路径
     * @param localPath
     *            本地目录
     * @param fileName
     *            下载之后的文件名称
     * @param listener
     *            监听器
     * @throws IOException
     */
    public static final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";
    public static final String FTP_DOWN_LOADING = "ftp文件正在下载";
    public static final String FTP_DOWN_SUCCESS = "ftp文件下载成功";
    public static final String FTP_DOWN_FAIL = "ftp文件下载失败";
    public static final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";
    public void downloadSingleFile(String serverPath, String localPath, String fileName, DownLoadProgressListener listener)
            throws Exception {

        // 先判断服务器文件是否存在
        FTPFile[] files = ftpClient.listFiles(serverPath);
        if (files.length == 0) {
            listener.onDownLoadProgress(FTP_FILE_NOTEXISTS, 0, null);
            return;
        }

        //创建本地文件夹
        File mkFile = new File(localPath);
        if (!mkFile.exists()) {
            mkFile.mkdirs();
        }

        localPath = localPath + fileName;
        // 接着判断下载的文件是否能断点下载
        long serverSize = files[0].getSize(); // 获取远程文件的长度
        File localFile = new File(localPath);
        long localSize = 0;
        if (localFile.exists()) {
            localSize = localFile.length(); // 如果本地文件存在，获取本地文件的长度
            if (localSize >= serverSize) {
                File file = new File(localPath);
                file.delete();
            }
        }

        // 进度
        long step = serverSize / 100;
        long process = 0;
        long currentSize = 0;
        // 开始准备下载文件
        OutputStream out = new FileOutputStream(localFile, true);
        ftpClient.setRestartOffset(localSize);
        InputStream input = ftpClient.retrieveFileStream(serverPath);
        byte[] b = new byte[1024];
        int length = 0;
        while ((length = input.read(b)) != -1) {
            out.write(b, 0, length);
            currentSize = currentSize + length;
            if (currentSize / step != process) {
                process = currentSize / step;
                if (process % 5 == 0) {  //每隔%5的进度返回一次
                    listener.onDownLoadProgress(FTP_DOWN_LOADING, process, null);
                }
            }
        }
        out.flush();
        out.close();
        input.close();

        // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
        if (ftpClient.completePendingCommand()) {
            listener.onDownLoadProgress(FTP_DOWN_SUCCESS, 0, new File(localPath));
        } else {
            listener.onDownLoadProgress(FTP_DOWN_FAIL, 0, null);
        }

        // 下载完成之后关闭连接
        this.ftpDisconnect();
        listener.onDownLoadProgress(FTP_DISCONNECT_SUCCESS, 0, null);
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
