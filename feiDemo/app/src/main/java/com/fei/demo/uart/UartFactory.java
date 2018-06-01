package com.fei.demo.uart;

/**
 * Created by Administrator on 2017/12/8/008.
 */

public class UartFactory {

    private static UartFactory instance;

    public static UartFactory getInstance(){
        if(instance==null){
            instance=new UartFactory();
        }
        return instance;
    }

    public void startGetUartDate(){
        Uart.getInstance().open();
        St16Controller controller = new St16Controller();
        new UartDateProtocol(controller);
    }
}
