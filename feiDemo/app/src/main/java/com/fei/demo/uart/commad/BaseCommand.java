package com.fei.demo.uart.commad;


public class BaseCommand {
    public static final int CMD_ENTER_BIND = 0x66;
    public final static int CMD_START_BIND = 2;
    public static final int CMD_BIND = 4;
    public final static int CMD_QUERY_BIND_STATE = 0x87;
    public final static int CMD_SET_TTB_STATE = 0x92;
    public static final int CMD_SET_CHANNEL_SETTING = 0x95;
    public static final int CMD_SYNC_MIXING_DATA_DELETE_ALL = 0xA3;
    public static final int CMD_SYNC_MIXING_DATA_ADD = 0x79;
    public static final int CMD_SEND_RX_RESINFO = 0x65;
    public static final int CMD_ENTER_RUN = 0x68;
    public static final int CMD_EXIT_RUN = 0x69;
    public static final int CMD_EXIT_BIND = 0x67;
    public static final int CMD_AWAIT = 0xbd;
    public static final int CMD_UNBIND = 0x86;
    public static final int CMD_RECV_BOTH_CH = 0x76;
    public static final int CMD_RECV_MIXED_CH_ONLY = 0x75;
    public static final int CMD_SET_BINDKEY_FUNCTION = 0x91;
    public static final int CMD_GET_M4_VERSION = 0x93;
    public static final int CMD_GET_RF_VERSION = 0x94;
    public static final int CMD_READY_FOR_UPDATE_TX = 0x99;
    public static final int CMD_TRANSFER_TX_FIRMWARE = 0x9A;
    public static final int CMD_UPDATE_TX_COMPLETED = 0x9b;
    public static final int CMD_UPDATE_BIN_INVALID = 0xAA;
    public static final int CMD_READY_FOR_UPDATE_RF = 0xA4;
    public static final int CMD_TRANSFER_RF_FIRMWARE = 0xA5;
    public static final int CMD_UPDATE_RF_COMPLETED	= 0xA6;
    public static final int CMD_GET_GPS_TIME = 0xB0;
    public final static int TYPE_BIND = 0;
    public final static int TYPE_CHN = 1;
    public final static int TYPE_CMD = 3;
    public final static int TYPE_RSP = 4;
    public final static int TYPE_MISSION = 5;
    public final static int COMMAND_TYPE_NORMAL = TYPE_CMD << 2;
    public final static int COMMAND_TYPE_MISSION = TYPE_MISSION << 2;
    public static final int BIND_KEY_FUNCTION_PWR = 0;
    public static final int BIND_KEY_FUNCTION_BIND = 1;
    public static final int DEFAULT_SUBCOMMAND_ID = -1;
    public int getCommandType() {
        return COMMAND_TYPE_NORMAL;
    }
    public int getSubCommandId() {
        return DEFAULT_SUBCOMMAND_ID;
    }
}
