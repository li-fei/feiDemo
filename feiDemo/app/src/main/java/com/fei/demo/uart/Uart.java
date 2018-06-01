package com.fei.demo.uart;

import android.util.Log;

public class Uart {

    static {
        System.loadLibrary("uart-lib");
    }

    public native String jniTest();
    public static native boolean openDevice();
    public static native boolean closeDevice();
    public static native boolean isOpenDevice();
    public static native byte[] recv();
    public static native int send(byte[] buf);


    private static Uart instance;
    private ReadThread readThread;

    public static Uart getInstance(){
        if(instance==null){
            synchronized (Uart.class){
                if(instance==null){
                    instance=new Uart();
                }
            }
        }
        return instance;
    }

    private class ReadThread extends Thread {
        private boolean isStop;

        public ReadThread() {
            isStop = false;
        }

        @Override
        public void run() {
            while (!isStop) {
                byte[] data = Uart.recv();
                uartDateReceivedListener.onReceive(data,0,data.length);
            }
        }

        public void exit() {
            isStop = true;
            interrupt();
        }
    }

    public synchronized boolean open() {
        boolean isOpen = false;
        if(isOpenDevice()){
            isOpen = true;
        }else {
            isOpen = openDevice();
            if (isOpen) {
                readThread = new ReadThread();
                readThread.start();
            }
        }
        return isOpen;
    }

    public synchronized void close() {
        if (readThread != null) {
            readThread.exit();
        }

        if(isOpenDevice()){
            closeDevice();
        }
    }


    UartDateReceivedListener uartDateReceivedListener;
    public void setUartDateReceivedListener(UartDateReceivedListener uartDateReceivedListener) {
        this.uartDateReceivedListener = uartDateReceivedListener;
    }


}
