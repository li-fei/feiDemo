package com.fei.demo.uart.commad;


import com.fei.demo.utils.ByteUtils;

/**
 * Created by gjw on 2016/12/28.
 */

public class HardwareData {
    private int[] angleData;
    private int[] switchData;
    private int[] totalData;

    public static HardwareData parserHardwareData(byte[] commandValues){
        HardwareData hardwareData=new HardwareData();
        int analogChannelCount=7;
        int switchChannelCount=42;
        int trimChannelCount=4;
        hardwareData.angleData=new int[analogChannelCount];
        hardwareData.switchData=new int[switchChannelCount];
        hardwareData.totalData=new int[analogChannelCount+switchChannelCount];
        StringBuffer sb=new StringBuffer();
        for (byte b : commandValues) {
            sb.append(ByteUtils.byte2bits(b));
        }
        for (int i = 0; i <analogChannelCount ; i++) {
            hardwareData.angleData[i]= Integer.valueOf(sb.substring(i*12,(i+1)*12),2);
            hardwareData.totalData[i]=hardwareData.angleData[i];

        }
        int skip=analogChannelCount*12+trimChannelCount*6;
        for (int i = 0; i < switchChannelCount; i++) {
            hardwareData.switchData[i]= Integer.valueOf(sb.substring(skip+i*2,skip+(i+1)*2),2);
            hardwareData.totalData[analogChannelCount+i]=hardwareData.switchData[i];
        }
        hardwareData.angleData[1] = 4095-hardwareData.angleData[1];
        hardwareData.angleData[3] = 4095-hardwareData.angleData[3];
        return hardwareData;
    }

    public int[] getAngleData() {
        return angleData;
    }

    public int[] getSwitchData() {
        return switchData;
    }

    public int[] getTotalData() {
        return totalData;
    }
}
