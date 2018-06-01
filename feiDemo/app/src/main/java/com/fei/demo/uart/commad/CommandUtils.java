package com.fei.demo.uart.commad;

import java.util.Arrays;

/**
 * Util class for command.
 * This class contains some static tool methods about command.
 * <p/>
 * Below is C structure of command :
 * typedef  struct  COMMANDH{
 * 0           u16	start_str;
 * 2  0        u8   	size;   		    //len 字节，数据长度，含len的     M3生成
 * 3  1  0     u16	FCF;			    //帧控制域  	见表2
 * 5  3  2     u8    Type;				//帧类型，绑定时设为绑定帧 见表4
 * 6  4  3     u16   PANID;				//网络地址，
 * 8  6  5     u16  	NodeIDdest;    		//节点地址
 * 10 8  7     u16  	NodeIDsource;    	//节点地址
 * 12 10 9     u8	command;
 * }COMMAND_HEADER;
 *
 * @author peng.gao
 * @version 1.0
 * @see BaseCommand
 */
public class CommandUtils {
    /**
     * The length of command body exclude data
     */
    public static final int COMMAND_BODY_EXCLUDE_VALUES_LENGTH = 10;

    /**
     * Get type from command body
     *
     * @param commandBody Command body expressed as byte array
     * @return Command type
     */
    public static int getTypeFromCommandBody(byte[] commandBody) {
        return commandBody[2] & 0xff;
    }

    /**
     * Get command ID from command body
     *
     * @param commandBody Command body expressed as byte array
     * @return Command ID
     */
    public static int getCommandIdFromCommandBody(byte[] commandBody) {
        return commandBody[9] & 0xff;
    }

    /**
     * Get command ID from mission command body
     *
     * @param commandBody Command body expressed as byte array
     * @return Command ID
     */
    public static int getCommandIdFromMissionCommandBody(byte[] commandBody) {
        return commandBody[10] & 0xff;
    }

    /**
     * Get sub-id of command from mission command body
     *
     * @param commandBody Command body expressed as byte array
     * @return Sub-id of command
     */
    public static int getSubCommandIdFromMissionCommandBody(byte[] commandBody) {
        return commandBody[11] & 0xff;
    }

    /**
     * Get command values from command body
     *
     * @param commandBody Command body expressed as byte array
     * @return Command values expressed as byte array
     */
    public static byte[] getCommandValuesFromCommandBody(byte[] commandBody) {
        return Arrays.copyOfRange(commandBody, 10, commandBody.length);
    }

    /**
     * Get mixed ID of command.This ID mixed command type,command ID and sub-id of command.
     *
     * @param type         Command type
     * @param commandId    Command ID
     * @param subCommandId Sub-id of command
     * @return Mixed command ID
     * @see BaseCommand#getCommandType()
     * @see BaseCommand#getSubCommandId()
     */
    public static int getMixCommandId(int type, int commandId, int subCommandId) {
        if (type != BaseCommand.COMMAND_TYPE_MISSION) {
            return commandId;
        } else {
            return type << 16 | commandId << 8 | subCommandId;
        }
    }

    /**
     * Get mixed ID of command.This ID mixed command type,command ID and sub-id of command.
     *
     * @param commandBody Command body expressed as byte array
     * @return Mixed command ID
     * @see BaseCommand#getCommandType()
     * @see BaseCommand#getSubCommandId()
     */
    public static int getMixCommandId(byte[] commandBody) {
        int type = getTypeFromCommandBody(commandBody);
        if (type != BaseCommand.COMMAND_TYPE_MISSION) {
            return getCommandIdFromCommandBody(commandBody);
        } else {
            return getMixCommandId(type,
                    getCommandIdFromMissionCommandBody(commandBody),
                    getSubCommandIdFromMissionCommandBody(commandBody));
        }
    }

    /**
     * @param yaw original yaw ,may be negative
     * @return yaw between [0,360]
     */
    public static float normalizeYaw(float yaw) {
        float rtn = 0;

        if (yaw < 0) {
            yaw += 360;
        }

        if (yaw >= 0 && yaw <= 90) {
            rtn = 90 - yaw;
        } else if (yaw > 90 && yaw <= 360) {
            rtn = 450 - yaw;
        }
        return rtn;
    }
}
