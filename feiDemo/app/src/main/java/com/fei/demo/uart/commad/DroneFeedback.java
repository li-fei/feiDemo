package com.fei.demo.uart.commad;


/**
 * Data in this class received from aircraft.
 *
 * @author peng.gao
 * @version 1.0
 */
public class DroneFeedback {

    /**
     * signal strength,[-100,0]
     */
    public int fskRssi;
    /**
     * voltage = 5 + 255*0.1 = 30.5V, min=5V
     */
    public float voltage;
    /**
     * 0.5A resolution
     */
    public float current;
    /**
     * 0.01m resolution, altitude (meters)
     */
    public float altitude;
    /**
     * latitude (degrees)  +/- 90 deg
     */
    public float latitude;
    /**
     * longitude (degrees)  +/- 180 deg
     */
    public float longitude;
    public float tas;
    /**
     * Plane GPS used
     */
    public boolean gpsUsed;
    public int fixType;
    /**
     * number of satellites (up to 32), fix type (2 bits), gps used 1 bit
     */
    public int satellitesNum;
    /**
     * 0.01 degree resolution
     */
    public float roll;
    /**
     * 0.01 degree resolution
     */
    public float yaw;
    /**
     * 0.01 degree resolution
     */
    public float pitch;
    /**
     * 1 bit per motor for status 1=good, 0= fail
     */
    public int motorStatus;
    /**
     * mpu6050 (FC0), mpu6050 (IMU), accelerometer (IMU)
     */
    public int imuStatus;
    /**
     * Plane GPS status
     */
    public int gpsStatus;
    /**
     * Controller GPS status
     */
    public int cgpsStatus;
    /**
     * pressure (FC0), pressure (IMU), compass1, compass2, GPS1, GPS2
     */
    public int pressCompassStatus;
    /**
     * vehicle flight mode
     */
    public int fMode;
    /**
     * Plane GPS Position used
     */
    public boolean gpsPosUsed;
    /**
     * vehicle type and additional warning flags, 1 for 350QX
     */
    public int vehicleType;
    /**
     * warning flags
     */
    public int errorFlags1;
    /**
     * gps accuracy in 0.05m resolution
     */
    public float gpsAccH;
    /**
     * vertical speed
     */
    public float vSpeed;
    /**
     * horizontal speed
     */
    public float hSpeed;

    @Override
    public String toString() {
        return "DroneFeedback{" +
                "fskRssi=" + fskRssi +
                ", voltage=" + voltage +
                ", current=" + current +
                ", altitude=" + altitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", tas=" + tas +
                ", gpsUsed=" + gpsUsed +
                ", fixType=" + fixType +
                ", satellitesNum=" + satellitesNum +
                ", roll=" + roll +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", motorStatus=" + motorStatus +
                ", imuStatus=" + imuStatus +
                ", gpsStatus=" + gpsStatus +
                ", cgpsStatus=" + cgpsStatus +
                ", pressCompassStatus=" + pressCompassStatus +
                ", fMode=" + fMode +
                ", gpsPosUsed=" + gpsPosUsed +
                ", vehicleType=" + vehicleType +
                ", errorFlags1=" + errorFlags1 +
                ", gpsAccH=" + gpsAccH +
                ", vSpeed=" + vSpeed +
                ", hSpeed=" + hSpeed +
                '}';
    }



}
