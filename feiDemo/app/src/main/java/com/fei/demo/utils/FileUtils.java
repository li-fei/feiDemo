package com.fei.demo.utils;

import java.io.File;

/**
 * Created by Administrator on 2018/2/11/011.
 */

public class FileUtils {

    public static boolean mkDirectory(String path) {
        File file = null;
        try {
            file = new File(path);
            if (!file.exists()) {
                return file.mkdirs();
            }
            else{
                return false;
            }
        } catch (Exception e) {
        } finally {
            file = null;
        }
        return false;
    }
}
