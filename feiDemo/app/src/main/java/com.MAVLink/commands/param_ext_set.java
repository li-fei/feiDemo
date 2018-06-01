package com.MAVLink.commands;

import java.nio.ByteBuffer;

import com.MAVLink.common.msg_param_ext_set;

import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_CUSTOM;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_INT16;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_INT32;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_INT64;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_INT8;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL32;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_REAL64;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT16;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT32;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT64;
import static com.MAVLink.enums.MAV_PARAM_EXT_TYPE.MAV_PARAM_EXT_TYPE_UINT8;

public class param_ext_set extends msg_param_ext_set {

    public param_ext_set(short target_system, short target_component) {
        this.target_component = target_component;
        this.target_system  = target_system;
        this.param_type = 0; // unknown parameter type
    }

    public void setParam_Value(Object object) {
        ByteBuffer buffer = ByteBuffer.wrap(param_value);
        if (object instanceof Byte) {
	    if (param_type != MAV_PARAM_EXT_TYPE_UINT8 ||
                param_type != MAV_PARAM_EXT_TYPE_INT8) {
            	throw new IllegalArgumentException();
	    }
            byte value = (byte) object;
            buffer.put((byte) (value >> 0));
        } else if (object instanceof Short) {
	    if (param_type != MAV_PARAM_EXT_TYPE_UINT16 ||
                param_type != MAV_PARAM_EXT_TYPE_INT16) {
            	throw new IllegalArgumentException();
	    }
            short value = (short) object;
            buffer.put((byte) (value >> 0));
            buffer.put((byte) (value >> 8));
        } else if (object instanceof Integer) {
	    if (param_type != MAV_PARAM_EXT_TYPE_UINT32 ||
                param_type != MAV_PARAM_EXT_TYPE_INT32 ||
                param_type != MAV_PARAM_EXT_TYPE_REAL32) {
            	throw new IllegalArgumentException();
	    }
            int value = (int) object;;
            buffer.put((byte) (value >> 0));
            buffer.put((byte) (value >> 8));
            buffer.put((byte) (value >> 16));
            buffer.put((byte) (value >> 24));
        } else if (object instanceof Long) {
	    if (param_type != MAV_PARAM_EXT_TYPE_UINT64 ||
                param_type != MAV_PARAM_EXT_TYPE_INT64 ||
                param_type != MAV_PARAM_EXT_TYPE_REAL64) {
            	throw new IllegalArgumentException();
	    }
            long value = (long) object;
            buffer.put((byte) (value >> 0));
            buffer.put((byte) (value >> 8));
            buffer.put((byte) (value >> 16));
            buffer.put((byte) (value >> 24));
            buffer.put((byte) (value >> 32));
            buffer.put((byte) (value >> 40));
            buffer.put((byte) (value >> 48));
            buffer.put((byte) (value >> 56));
        } else if (object instanceof Float) {
	    if (param_type != MAV_PARAM_EXT_TYPE_REAL32) {
            	throw new IllegalArgumentException();
	    }
            int v = Float.floatToIntBits((float) object);
            setParam_Value(v);
        } else if (object instanceof Double) {
	    if (param_type != MAV_PARAM_EXT_TYPE_REAL64) {
            	throw new IllegalArgumentException();
	    }
            long v = Double.doubleToLongBits((double) object);
            setParam_Value(v);
        } else if (object instanceof byte[]) {
	    if (param_type != MAV_PARAM_EXT_TYPE_CUSTOM) {
            	throw new IllegalArgumentException();
	    }
            byte[] value = (byte[]) object;
            System.arraycopy(value, 0, param_value, 0, Math.min(value.length, param_value.length));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setParam_Type(short type) {
        param_type = type;
    }
}
