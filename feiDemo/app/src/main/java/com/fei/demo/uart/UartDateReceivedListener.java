package com.fei.demo.uart;

/**
 * Created by Administrator on 2017/12/8/008.
 */

public interface UartDateReceivedListener {

    void onReceive(byte[] data, int offset, int length);

}
