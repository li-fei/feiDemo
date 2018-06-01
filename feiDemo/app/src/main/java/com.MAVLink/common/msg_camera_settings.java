/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE CAMERA_SETTINGS PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* WIP: Settings of a camera, can be requested using MAV_CMD_REQUEST_CAMERA_SETTINGS.
*/
public class msg_camera_settings extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_CAMERA_SETTINGS = 260;
    public static final int MAVLINK_MSG_ID_CAMERA_SETTINGS_CRC = 146;
    public static final int MAVLINK_MSG_LENGTH = 5;
    private static final long serialVersionUID = MAVLINK_MSG_ID_CAMERA_SETTINGS;


      
    /**
    * Timestamp (milliseconds since system boot)
    */
    public long time_boot_ms;
      
    /**
    * Camera mode (CAMERA_MODE)
    */
    public short mode_id;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_CAMERA_SETTINGS;
        packet.crc_extra = MAVLINK_MSG_ID_CAMERA_SETTINGS_CRC;
              
        packet.payload.putUnsignedInt(time_boot_ms);
              
        packet.payload.putUnsignedByte(mode_id);
        
        return packet;
    }

    /**
    * Decode a camera_settings message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.time_boot_ms = payload.getUnsignedInt();
              
        this.mode_id = payload.getUnsignedByte();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_camera_settings(){
        msgid = MAVLINK_MSG_ID_CAMERA_SETTINGS;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_camera_settings(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_CAMERA_SETTINGS;
        unpack(mavLinkPacket.payload);
    }

        
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_CAMERA_SETTINGS - sysid:"+sysid+" compid:"+compid+" time_boot_ms:"+time_boot_ms+" mode_id:"+mode_id+"";
    }
}
        