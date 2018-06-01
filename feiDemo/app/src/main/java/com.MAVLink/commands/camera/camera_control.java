package com.MAVLink.commands.camera;

import com.MAVLink.common.msg_command_long;
import com.MAVLink.enums.MAV_CMD;

public class camera_control extends msg_command_long {
    private static final short TARGET_SYSTEM = camera.TARGET_SYSTEM; // vehicle
    private static final short TARGET_COMPONENT = camera.TARGET_COMPONENT;

    /* MAV COMMAND_LONG message       Send a command with up to seven parameters to the MAV
     * target system#                 System which should execute the command
     * target_component#              Component which should execute the command
     * command#                       Command ID, as defined by MAV_CMD enum
     * Param #1                       Parameter 1, as defined by MAV_CMD enum
     * Param #2                       Parameter 2, as defined by MAV_CMD enum
     * Param #3                       Parameter 3, as defined by MAV_CMD enum
     * Param #4                       Parameter 4, as defined by MAV_CMD enum
     * Param #5                       Parameter 5, as defined by MAV_CMD enum
     * Param #6                       Parameter 6, as defined by MAV_CMD enum
     * Param #7                       Parameter 7, as defined by MAV_CMD enum
     */
    public camera_control(int command, float param1, float param2, float param3, float param4, float param5, float param6, float param7) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
        this.param6 = param6;
        this.param7 = param7;
        this.command = command;
        this.target_component = TARGET_COMPONENT;
        this.target_system = TARGET_SYSTEM;
        this.confirmation = 0;
    }

    public camera_control(int command, float param1, float param2, float param3, float param4, float param5, float param6) {
        this(command, param1, param2, param3, param4, param5, param6, 0.0f);
    }

    public camera_control(int command, float param1, float param2, float param3, float param4) {
        this(command, param1, param2, param3, param4, 0.0f, 0.0f, 0.0f);
    }

    public camera_control(int command, float param1, float param2, float param3) {
        this(command, param1, param2, param3, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public camera_control(int command, float param1, float param2) {
        this(command, param1, param2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public camera_control(int command, float param1) {
        this(command, param1, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public camera_control(int command) {
        this(command, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    /* MAV_CMD_IMAGE_START_CAPTURE  Start image capture sequence. Sends CAMERA_IMAGE_CAPTURED after each capture. Use NAN for reserved values.
     * Mission Param #1             Reserved (Set to 0)
     * Mission Param #2             Duration between two consecutive pictures (in seconds)
     * Mission Param #3             Number of images to capture total - 0 for unlimited capture
     * Mission Param #4             Reserved (all remaining params)
     */
    public static camera_control cmd_image_start_capture(int duration, int number) {
        return new camera_control(MAV_CMD.MAV_CMD_IMAGE_START_CAPTURE, 0.0f, duration, number);
    }

    /* MAV_CMD_IMAGE_STOP_CAPTURE   Stop image capture sequence Use NAN for reserved values.
     * Mission Param #1             Reserved (Set to 0)
     * Mission Param #2             Reserved (all remaining params)
     */
    public static camera_control cmd_image_stop_capture() {
        return new camera_control(MAV_CMD.MAV_CMD_IMAGE_STOP_CAPTURE);
    }

    /* MAV_CMD_PREFLIGHT_REBOOT_SHUTDOWN      WIP: Request the reboot or shutdown of system components.
     * Mission Param #1                       0: Do nothing for autopilot, 1: Reboot autopilot, 2: Shutdown autopilot, 3: Reboot autopilot and keep it in the bootloader until upgraded.
     * Mission Param #2                       0: Do nothing for onboard computer, 1: Reboot onboard computer, 2: Shutdown onboard computer, 3: Reboot onboard computer and keep it in the bootloader until upgraded.
     * Mission Param #3                       WIP: 0: Do nothing for camera, 1: Reboot onboard camera, 2: Shutdown onboard camera, 3: Reboot onboard camera and keep it in the bootloader until upgraded
     * Mission Param #4                       WIP: 0: Do nothing for mount (e.g. gimbal), 1: Reboot mount, 2: Shutdown mount, 3: Reboot mount and keep it in the bootloader until upgraded
     * Mission Param #5                       Reserved, send 0
     * Mission Param #6                       Reserved, send 0
     * Mission Param #7                       WIP: ID (e.g. camera ID -1 for all IDs)
     */
    public static camera_control cmd_preflight_reboot() {
        return new camera_control(MAV_CMD.MAV_CMD_PREFLIGHT_REBOOT_SHUTDOWN, 1.0f);
    }

    public static camera_control cmd_preflight_shutdown() {
        return new camera_control(MAV_CMD.MAV_CMD_PREFLIGHT_REBOOT_SHUTDOWN, 2.0f);
    }

    /* MAV_CMD_REQUEST_CAMERA_CAPTURE_STATUS  WIP: Request capture status (CAMERA_CAPTURE_STATUS).
     * Mission Param #1	                      0: No action 1: Request camera capture status
     * Mission Param #2	                      Reserved (all remaining params)
     */
    public static camera_control cmd_request_camera_capture_status() {
        return new camera_control(MAV_CMD.MAV_CMD_REQUEST_CAMERA_CAPTURE_STATUS,1.0f);
    }

    /* MAV_CMD_REQUEST_CAMERA_INFORMATION  WIP: Request camera information (CAMERA_INFORMATION).
     * Mission Param #1	                   0: No action 1: Request camera capabilities
     * Mission Param #2	                   Reserved (all remaining params)
     */
    public static camera_control cmd_request_camera_information() {
        return new camera_control(MAV_CMD.MAV_CMD_REQUEST_CAMERA_INFORMATION, 1.0f);
    }

    /* MAV_CMD_REQUEST_CAMERA_SETTINGS     WIP: Request camera settings  (CAMERA_SETTINGS).
     * Mission Param #1	                   0: No action 1: Request camera settings
     * Mission Param #2	                   Reserved (all remaining params)
     */
    public static camera_control cmd_request_camera_settings() {
        return new camera_control(MAV_CMD.MAV_CMD_REQUEST_CAMERA_SETTINGS, 1.0f);
    }

    /* MAV_CMD_REQUEST_STORAGE_INFORMATION WIP: Request storage information (STORAGE_INFORMATION). Use the command's target_component to target a specific component's storage.
     * Mission Param #1                    Storage ID (0 for all, 1 for first, 2 for second, etc.)
     * Mission Param #2                    0: No Action 1: Request storage information
     * Mission Param #3                    Reserved (all remaining params)
     */
    public static camera_control cmd_request_storage_information() {
        return new camera_control(MAV_CMD.MAV_CMD_REQUEST_STORAGE_INFORMATION, 0.0f, 1.0f);
    }

    public static camera_control cmd_request_storage_information(int storageId) {
        return new camera_control(MAV_CMD.MAV_CMD_REQUEST_STORAGE_INFORMATION, storageId, 1.0f);
    }

    /* MAV_CMD_RESET_CAMERA_SETTINGS       WIP: Reset all camera settings to Factory Default
     * Mission Param #1                    0: No Action 1: Reset all settings
     * Mission Param #2                    Reserved (all remaining params)
     */
    public static camera_control cmd_reset_camera_settings() {
        return new camera_control(MAV_CMD.MAV_CMD_RESET_CAMERA_SETTINGS, 1.0f);
    }

    /* MAV_CMD_SET_CAMERA_MODE    Set camera running mode. Use NAN for reserved values.
     *  Mission Param #1          Reserved (Set to 0)
     *  Mission Param #2          Camera mode (see CAMERA_MODE enum)
     *  Mission Param #3          Reserved (all remaining params)
     */
    public static camera_control cmd_set_camera_mode(int mode) {
        return new camera_control(MAV_CMD.MAV_CMD_SET_CAMERA_MODE, 0.0f, mode);
    }

    public static camera_control cmd_storage_format() {
        return new camera_control(MAV_CMD.MAV_CMD_STORAGE_FORMAT, 0.0f, 1.0f);
    }

    /* MAV_CMD_VIDEO_START_CAPTURE Starts video capture (recording). Use NAN for reserved values.
     * Mission Param #1            Reserved (Set to 0)
     * Mission Param #2            Frequency CAMERA_CAPTURE_STATUS messages should be sent while recording (0 for no messages, otherwise frequency in Hz)
     * Mission Param #3            Reserved (all remaining params)
     */
    public static camera_control cmd_video_start_capture() {
        return new camera_control(MAV_CMD.MAV_CMD_VIDEO_START_CAPTURE);
    }

    /* MAV_CMD_VIDEO_STOP_CAPTURE  Stop the current video capture (recording). Use NAN for reserved values.
     * Mission Param #1            Reserved (Set to 0)
     * Mission Param #2            Reserved (all remaining params)
     */
    public static camera_control cmd_video_stop_capture() {
        return new camera_control(MAV_CMD.MAV_CMD_VIDEO_STOP_CAPTURE);
    }
}
