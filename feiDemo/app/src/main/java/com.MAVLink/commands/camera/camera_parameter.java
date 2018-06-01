package com.MAVLink.commands.camera;

import com.MAVLink.commands.param_ext_set;
import com.MAVLink.enums.MAV_PARAM_EXT_TYPE;
import com.MAVLink.enums.MAV_TYPE;

/* http://gerrit.yuneec.com/redmine/projects/ob/wiki/YuneecMavlinkManual */

public class camera_parameter extends param_ext_set {
    private static final short TARGET_SYSTEM = camera.TARGET_SYSTEM;
    private static final short TARGET_COMPONENT = camera.TARGET_COMPONENT;

    public static class Parameter {
        public String id;
        public short type;

        Parameter(String id, int type) {
            this.id = id;
            this.type = (short) (type & 0xffff);
        }
    }

    /* Aspect radio */
    public static final Parameter CAM_ASPECTRATIO = new Parameter("CAM_ASPECTRATIO", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL32);

    /* Camera mode */
    public static final Parameter CAM_MODE = new Parameter("CAM_MODE", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Infrared palette */
    public static final Parameter CAM_IPRALETTE = new Parameter("CAM_IRPALETTE", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT32);

    /* IR atmosphere parameters */
    public static final Parameter CAM_IRATMO = new Parameter("CAM_IRATMO", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* IR scene emissivity */
    public static final Parameter CAM_IREMISS = new Parameter("CAM_IRPALETTE", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL32);

    /* IR conversion ratio */
    public static final Parameter CAM_CONVRATIO = new Parameter("CAM_CONVRATIO", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL32);

    /* IR atmosphere temperature */
    public static final Parameter CAM_IRATMOTEMP = new Parameter("CAM_IRATMOTEMP", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL32);

    /* IR Temperature range */
    public static final Parameter CAM_IRTEMPRENA = new Parameter("CAM_IRTEMPRENA", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* IR Temperature max range */
    public static final Parameter CAM_IRTEMPMAX = new Parameter("CAM_IRTEMPMAX", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL32);

    /* IR Temperature min range */
    public static final Parameter CAM_IRTEMPMIN = new Parameter("CAM_IRTEMPMIN", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL32);

    /* IR lock status */
    public static final Parameter CAM_IRLOCKST = new Parameter("CAM_IRLOCKST", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* IR FFC mode */
    public static final Parameter CAM_IRFFCMODE = new Parameter("CAM_IRFFCMODE", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT32);

    /* IR Temperature status */
    public static final Parameter CAM_TEMPSTATUS = new Parameter("CAM_TEMPSTATUS", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_CUSTOM);

    /* Flicker mode */
    public static final Parameter CAM_FLICKER = new Parameter("CAM_FLICKER", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Image quality */
    public static final Parameter CAM_PHOTOQUAL = new Parameter("CAM_PHOTOQUAL", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* video encoder format */
    public static final Parameter CAM_VIDFMT = new Parameter("CAM_VIDFMT", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Audio recorder */
    public static final Parameter CAM_AUDIOREC = new Parameter("CAM_AUDIOREC", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Color mode */
    public static final Parameter CAM_COLORMODE = new Parameter("CAM_COLORMODE", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* EV value */
    public static final Parameter CAM_EV = new Parameter("CAM_EV", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL32);

    /* EXP mode */
    public static final Parameter CAM_EXPMODE = new Parameter("CAM_EXPMODE", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Shutter ISO */
    public static final Parameter CAM_ISO = new Parameter("CAM_ISO", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT32);

    /* Metering mode */
    public static final Parameter CAM_METERING = new Parameter("CAM_METERING", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Image format */
    public static final Parameter CAM_PHOTOFMT = new Parameter("CAM_PHOTOFMT", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Shutter speed */
    public static final Parameter CAM_SHUTTERSPD = new Parameter("CAM_SHUTTERSPD", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL32);

    /* Spot area */
    public static final Parameter CAM_SPOTAREA = new Parameter("CAM_SPOTAREA", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT16);

    /* White Blance mode */
    public static final Parameter CAM_WBMODE = new Parameter("CAM_WBMODE", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Video resolution */
    public static final Parameter CAM_VIDRES = new Parameter("CAM_VIDRES", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Photo radio */
    public static final Parameter CAM_PHOTORATIO = new Parameter("CAM_PHOTORATIO", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* File format */
    public static final Parameter CAM_FILEFMT = new Parameter("CAM_FILEFMT", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Image flip */
    public static final Parameter CAM_IMAGEFLIP = new Parameter("CAM_IMAGEFLIP", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8);

    /* Photo mode */
    public static final Parameter CAM_PHOTOMODE = new Parameter("CAM_PHOTOMODE", MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_CUSTOM);

    public camera_parameter(Parameter parameter, Object value) {
        super(TARGET_SYSTEM, TARGET_COMPONENT);
        setParam_Id(parameter.id);
        setParam_Type(parameter.type);
        setParam_Value(value);
    }

    /************************************************************************
     * Available for ALL
     ***********************************************************************/
    public static camera_parameter set_aspect_radio(float value) {
        return new camera_parameter(CAM_ASPECTRATIO, value);
    }

    public static camera_parameter set_mode(byte value) {
        return new camera_parameter(CAM_MODE, value);
    }

    /************************************************************************
     * Available for CGOET && E10T
     ***********************************************************************/
    public static camera_parameter set_ir_infrared_palette(int value) {
        return new camera_parameter(CAM_IPRALETTE, value);
    }

    public static camera_parameter set_ir_atmosphere_parameter(byte value) {
        return new camera_parameter(CAM_IRATMO, value);
    }

    public static camera_parameter set_ir_scence_emissivity(float value) {
        return new camera_parameter(CAM_IREMISS, value);
    }

    public static camera_parameter set_ir_conversion_radio(float value) {
        return new camera_parameter(CAM_CONVRATIO, value);
    }

    public static camera_parameter set_ir_atmosphere_temperature(float value) {
        return new camera_parameter(CAM_IRATMOTEMP, value);
    }

    public static camera_parameter set_ir_temperature_range(byte value) {
        return new camera_parameter(CAM_IRTEMPRENA, value);
    }

    public static camera_parameter set_ir_temperature_max(float value) {
        return new camera_parameter(CAM_IRTEMPMAX, value);
    }

    public static camera_parameter set_ir_temperature_min(float value) {
        return new camera_parameter(CAM_IRTEMPMIN, value);
    }

    public static camera_parameter set_ir_lock_status(byte value) {
        return new camera_parameter(CAM_IRLOCKST, value);
    }

    public static camera_parameter set_ir_fcc_mode(int value) {
        return new camera_parameter(CAM_IRFFCMODE, value);
    }

    public static camera_parameter set_ir_temperature_status(byte[] value) {
        return new camera_parameter(CAM_TEMPSTATUS, value);
    }

    /************************************************************************
     * Available for E90 OB FIREBIRD HDRACER IMX117
     ***********************************************************************/

    public static camera_parameter set_flicker(byte value) {
        return new camera_parameter(CAM_FLICKER, value);
    }

    public static camera_parameter set_photo_quality(byte value) {
        return new camera_parameter(CAM_PHOTOQUAL, value);
    }

    public static camera_parameter set_video_format(byte value) {
        return new camera_parameter(CAM_VIDFMT, value);
    }

    public static camera_parameter set_audio_record(byte value) {
        return new camera_parameter(CAM_AUDIOREC, value);
    }

    /************************************************************************
     * Available for ALL except CGOET E10T
     ***********************************************************************/
    public static camera_parameter set_color_mode(byte value) {
        return new camera_parameter(CAM_COLORMODE, value);
    }

    public static camera_parameter set_ev(float value) {
        return new camera_parameter(CAM_EV, value);
    }

    public static camera_parameter set_exp_mode(byte value) {
        return new camera_parameter(CAM_EXPMODE, value);
    }

    public static camera_parameter set_iso(int value) {
        return new camera_parameter(CAM_ISO, value);
    }

    public static camera_parameter set_metering(byte value) {
        return new camera_parameter(CAM_METERING, value);
    }

    public static camera_parameter set_photo_format(byte value) {
        return new camera_parameter(CAM_PHOTOFMT, value);
    }

    public static camera_parameter set_shutter_speed(float value) {
        return new camera_parameter(CAM_SHUTTERSPD, value);
    }

    public static camera_parameter set_spot_area(short value) {
        return new camera_parameter(CAM_SPOTAREA, value);
    }

    public static camera_parameter set_white_balance(byte value) {
        return new camera_parameter(CAM_WBMODE, value);
    }

    public static camera_parameter set_video_resolution(byte value) {
        return new camera_parameter(CAM_VIDRES, value);
    }

    /************************************************************************
     * Available for OB FIREBIRD HDRACER IMX117
     ***********************************************************************/
    public static camera_parameter set_photo_radio(byte value) {
        return new camera_parameter(CAM_PHOTORATIO, value);
    }

    public static camera_parameter set_file_format(byte value) {
        return new camera_parameter(CAM_FILEFMT, value);
    }

    public static camera_parameter set_image_flip(byte value) {
        return new camera_parameter(CAM_IMAGEFLIP, value);
    }

    public static camera_parameter set_photo_mode(byte[] value) {
        return new camera_parameter(CAM_PHOTOMODE, value);
    }
}

