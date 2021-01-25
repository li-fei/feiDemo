package com.fei.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fei.demo.R;
import com.fei.demo.utils.Logg;
import com.fei.demo.utils.NetworkUtils;
import com.fei.demo.utils.Ping;

public class CmdActivity extends Activity {

    Button btn_ofdm;
    private final int PINGNUM = 4;
    private final String PINGIP = "192.168.241.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmd);

        btn_ofdm = findViewById(R.id.btn_ofdm);
        btn_ofdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = System.currentTimeMillis();
                try {
//                    Ping.setCallBack(new Ping.CallBack() {
//                        @Override
//                        public void onSuccess(int count) {
//                            Logg.loge("Ping successed  ******************  " + count );
//                        }
//                    });
//                    String re = Ping.isPingSuccess(4,"192.168.241.1");
                    String re = Ping.isPingSuccess(PINGNUM, PINGIP, new Ping.CallBack() {
                        @Override
                        public void onSuccess(int count) {
                            Logg.loge("Ping successed  ******************  " + count );
                            if (count == PINGNUM){
                                Logg.loge("Ping successed  OFDM   " );
                            }
                        }
                    });
                    Logg.loge("Ping ---> " + re);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;
                Logg.loge("Ping runTime  ******************  " + runTime );
            }
        });

    }

}
