package com.fei.demo.uart;

/**
 * Created by Administrator on 2017/12/8/008.
 */

public class Jni {

    static {
        System.loadLibrary("uart-lib");
//        System.loadLibrary("native-lib");
    }

    private static Jni jni;

    public static Jni getInstance() {
        if (jni == null) {
            jni = new Jni();
        }
        return jni;
    }

    public native String stringFromJNI();
    public native String jniTest();
    public native boolean jniTestBoolean();
}
