package com.MAVLink.commands;

import com.MAVLink.common.msg_command_long;

public class cmd_long extends msg_command_long {
	public static final short TARGET_SYSTEM_BROADCAST = 0;
	public static final short TARGET_SYSTEM_VEHICLE = 1;
	public static final short TARGET_SYSTEM_GCS = 255;

	public cmd_long(int command, short target_system, short target_component, float param1, float param2, float param3, float param4, float param5, float param6, float param7) {
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
		this.param6 = param6;
		this.param7 = param7;
		this.command = command;
		this.target_component = target_component;
		this.target_system = target_system;
		this.confirmation = 0;
	}

	public cmd_long(int command, short target_system, short target_component, float param1, float param2, float param3, float param4, float param5, float param6) {
		this(command, target_component, target_system, param1, param2, param3, param4, param5, param6, 0.0f);
	}

	public cmd_long(int command, short target_system, short target_component, float param1, float param2, float param3, float param4) {
		this(command, target_component, target_system, param1, param2, param3, param4, 0.0f, 0.0f, 0.0f);
	}

	public cmd_long(int command, short target_system, short target_component, float param1, float param2, float param3) {
		this(command, target_component, target_system, param1, param2, param3, 0.0f, 0.0f, 0.0f, 0.0f);
	}

	public cmd_long(int command, short target_system, short target_component, float param1, float param2) {
		this(command, target_component, target_system, param1, param2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	}

	public cmd_long(int command, short target_system, short target_component, float param1) {
		this(command, target_component, target_system, param1, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	}

	public cmd_long(int command, short target_system, short target_component) {
		this(command, target_component, target_system, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	}
}
