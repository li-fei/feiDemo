package com.fei.demo.uart;
import android.util.Log;

import com.fei.demo.uart.commad.BaseCommand;
import com.fei.demo.uart.commad.CommandUtils;
import com.fei.demo.uart.commad.DroneFeedback;
import com.fei.demo.uart.commad.HardwareData;
import com.fei.demo.utils.ByteUtils;

import java.util.Arrays;


/**
 * Created by lk.sun on 3/8/2016.
 */
public class St16Controller implements ControllerParseInterface{
    public final static int CMD_TX_CHANNEL_DATA_RAW = 0x77;
    public final static int CMD_TX_CHANNEL_DATA_MIXED = 0x78;
    public final static int CMD_TX_STATE_MACHINE = 0x83;
    public final static int CMD_RX_FEEDBACK_DATA = 0x88;
    public final static int CMD_TX_SWITCH_CHANGED = 0x89;
    public final static int COMMAND_M4_SEND_GPS_DATA_TO_PA = 0xB6;
    public final static int ACTION_TYPE_RESPONSE = 1;
    public final static int ACTION_TYPE_FEEDBACK = 3;
    public final static byte ACTION_TYPE_ONEKEY_TAKEOFF = 6;

    private final boolean isBigEndian = false;


    @Override
    public byte[] toCommandData(byte[] data, int offset, int length) {
        if (length <= 0) {
            return null;
        }

        int type =data[2] & 0xff;
        type = (type & 0x1c) >> 2;
        if (handleNonTypePacket(data)) {
            return null;
        }
        switch (type) {
            case BaseCommand.TYPE_BIND:
                switch (data[3]) {
                    case 2:
                        handleRxBindInfo(data);
                        break;
                    case 4:
//                        Log.e("yuneec0","type bind : "+ ByteUtils.byteArrayToHexString(data,0,data.length));
                        break;
                    case 12:
                        break;
                    default:
                        break;
                }
                break;
            case BaseCommand.TYPE_CHN:
                handleChannel(data);
                return null;
            case BaseCommand.TYPE_CMD:
                if (!handleCommand(data)) {
                    return data;
                }
                break;
            case BaseCommand.TYPE_RSP:
                int command = data[9] & 0xff;
//                LogUtils.e("receive command "+command);
                switch (command) {
                    case BaseCommand.CMD_QUERY_BIND_STATE:
                        int rxAddress = (data[10] & 0xff) | (data[11] << 8 & 0xff00);
                        break;
                    case BaseCommand.CMD_GET_GPS_TIME:

                        break;
                    case BaseCommand.CMD_GET_M4_VERSION:
                        break;
                    case BaseCommand.CMD_GET_RF_VERSION:
                        break;
                    default:
                        break;
                }
                return data;
        }

        return null;
    }


    private boolean handleNonTypePacket(byte[] commandData) {
        int commandId = commandData[9] & 0xff;
        switch (commandId) {
            case COMMAND_M4_SEND_GPS_DATA_TO_PA:
                handRcGps(commandData);
                return true;

        }
        return false;
    }

    private void handleChannel(byte[] commandData) {
        int commandId = commandData[9] & 0xff;
        switch (commandId) {
            case CMD_TX_CHANNEL_DATA_MIXED:
                handleMixedChannelData(commandData);
                break;
            case CMD_TX_CHANNEL_DATA_RAW:
                handleHardwareData(commandData);
                break;
            case CMD_RX_FEEDBACK_DATA:
                handleDroneFeedback(commandData);
                break;
        }
    }

    private void handleDroneFeedback(byte[] commandData) {
        try {
            byte[] commandValues = CommandUtils.getCommandValuesFromCommandBody(commandData);
            String s = ByteUtils.byteArrayToHexString(commandValues,0,commandValues.length);
//            Log.e("yuneec0","handleDroneFeedback: " + s);
            DroneFeedback droneFeedback = new DroneFeedback();
            droneFeedback.fskRssi = commandValues[36];
            droneFeedback.voltage = (commandValues[21]&0xff) * 0.1f + 5;
            droneFeedback.current = (commandValues[22]&0xff)  * 0.5f;
            droneFeedback.altitude = ByteUtils.byteArrayToInt(Arrays.copyOfRange(commandValues, 10, 14), isBigEndian) * 0.01f;
            droneFeedback.latitude = ByteUtils.byteArrayToInt(Arrays.copyOfRange(commandValues, 2, 6), isBigEndian) * 1e-7f;
            droneFeedback.longitude = ByteUtils.byteArrayToInt(Arrays.copyOfRange(commandValues, 6, 10), isBigEndian) * 1e-7f;
            float vx = ByteUtils.byteArrayToShort(Arrays.copyOfRange(commandValues, 14, 16), isBigEndian) * 0.01f;
            float vy = ByteUtils.byteArrayToShort(Arrays.copyOfRange(commandValues, 16, 18), isBigEndian) * 0.01f;
            float vz = ByteUtils.byteArrayToShort(Arrays.copyOfRange(commandValues, 18, 20), isBigEndian) * 0.01f;
            droneFeedback.tas = (float) Math.sqrt(vx * vx + vy * vy + vz * vz);
            droneFeedback.vSpeed = vz;
            droneFeedback.hSpeed = (float) Math.sqrt(vx * vx + vy * vy);
            droneFeedback.gpsUsed = ((commandValues[20]& 0x80)>> 7) > 0;
            droneFeedback.fixType = ((commandValues[20]& 0x60))>> 5;
            droneFeedback.satellitesNum = commandValues[20]& 0x1f;
            droneFeedback.roll = ByteUtils.byteArrayToShort(Arrays.copyOfRange(commandValues, 23, 25), isBigEndian) * 0.01f;
            droneFeedback.yaw = CommandUtils.normalizeYaw(ByteUtils.byteArrayToShort(Arrays.copyOfRange(commandValues, 27, 29), isBigEndian) * 0.01f);
            droneFeedback.pitch = ByteUtils.byteArrayToShort(Arrays.copyOfRange(commandValues, 25, 27), isBigEndian) * 0.01f;
            droneFeedback.motorStatus = commandValues[29] & 0xff;
            droneFeedback.imuStatus = commandValues[30] & 0xff;
            droneFeedback.vehicleType = commandValues[33] & 0xff;
//            if (droneFeedback.vehicleType != 1) {
            droneFeedback.gpsStatus = (commandValues[30] & 0x20) >> 5;
            droneFeedback.cgpsStatus = droneFeedback.imuStatus >> 6;
//            }
            droneFeedback.pressCompassStatus = commandValues[31] & 0xff;
            droneFeedback.fMode = commandValues[32]&0xff & 0x7f;
//            if (droneFeedback.vehicleType == 1) {
//                droneFeedback.gpsStatus = droneFeedback.fMode >> 7;
//            } else {
            droneFeedback.gpsPosUsed = (droneFeedback.fMode >> 7) > 0;
//            }
            droneFeedback.errorFlags1 = commandValues[34]&0xff;
            droneFeedback.gpsAccH = (commandValues[35] & 0xff) * 0.05f;
//            Log.e("yuneec0",droneFeedback.toString());
//            try {
//                droneFeedbackListener.onReceiveDroneInfo(droneFeedback);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMixedChannelData(byte[] commandData) {
//        controllerFeedbackListener.onReceiveChannelData(ChannelData.parserChannelData(CommandUtils.getCommandValuesFromCommandBody(commandData)));
//        DataPool.getPool().setChannelData(ChannelData.parserChannelData(CommandUtils.getCommandValuesFromCommandBody(commandData)));
    }

    private void handleHardwareData(byte[] commandData) {
//        controllerFeedbackListener.onReceiveHandleHardwareData(HardwareData.parserHardwareData(CommandUtils.getCommandValuesFromCommandBody(commandData)));
//        DataPool.getPool().setHardwareData(HardwareData.parserHardwareData(CommandUtils.getCommandValuesFromCommandBody(commandData)));
        HardwareData hardwareData = HardwareData.parserHardwareData(CommandUtils.getCommandValuesFromCommandBody(commandData));
        int[] switchData = hardwareData.getSwitchData();
        String s = "switchData:";
        for (int i = 0 ; i<switchData.length;i++){
            s += switchData[i];
        }
        Log.e("lifeiDemo",s);
    }

    private boolean handleCommand(byte[] commandData) {
        int commandId = CommandUtils.getCommandIdFromCommandBody(commandData);
        switch (commandId) {
            case CMD_TX_STATE_MACHINE:

                return true;
            case CMD_TX_SWITCH_CHANGED:

                return true;
        }
        return false;
    }

    private void switchChanged(byte[] commandData) {
        byte[] commandValues = CommandUtils.getCommandValuesFromCommandBody(commandData);
//        SwitchChanged switchChanged = new SwitchChanged();
//        switchChanged.hwId = commandValues[0];
//        switchChanged.oldState = commandValues[1];
//        switchChanged.newState = commandValues[2];
//        if(controllerFeedbackListener!=null){
//            controllerFeedbackListener.onSwitchChanged(switchChanged);
//        }
//        DataPool.getPool().setSwitchChanged(switchChanged);
    }

    private void handRcGps(byte[] commandData) {
        byte[] commandValues = CommandUtils.getCommandValuesFromCommandBody(commandData);
//        controllerFeedbackListener.onReceiveYuneecRCGps(YuneecRCGps.parserRCGps(commandValues));
//        DataPool.getPool().setYuneecRCGps(YuneecRCGps.parserRCGps(commandValues));
    }

    private void handleRxBindInfo(byte[] data) {

    }
}
