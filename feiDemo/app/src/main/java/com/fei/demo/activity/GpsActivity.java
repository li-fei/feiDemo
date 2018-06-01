package com.fei.demo.activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fei.demo.R;

public class GpsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        testGps1();
        testGps2();
    }

    private void testGps1() {
        TextView tv_gps1 = findViewById(R.id.tv_gps1);
        float[] result = new float[2];
        double droneLatitude = 120.9318591;
        double droneLongitude =  31.1910751;
        double controllerLatitude = 120.9318491;
        double controllerLongitude = 31.1910651;
        Location.distanceBetween(droneLatitude, droneLongitude, controllerLatitude , controllerLongitude , result);
        Log.e("yuneec0","----->" + result.length);
        for (int i=0 ;i<result.length;i++){
            Log.e("yuneec0","-----> --> " + result[i]);
        }
    }

    private void testGps2() {
        TextView tv_gps2 = findViewById(R.id.tv_gps2);
    }
}
