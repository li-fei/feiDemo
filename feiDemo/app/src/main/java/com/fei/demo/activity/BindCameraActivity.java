package com.fei.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
import com.MAVLink.Parser;
import com.MAVLink.commands.camera.camera_control;
import com.MAVLink.common.msg_command_ack;
import com.MAVLink.common.msg_command_long;
import com.fei.demo.R;
import com.fei.demo.utils.ByteUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BindCameraActivity extends Activity implements View.OnClickListener{

    private ScheduledExecutorService mExecutor;
    TextView tv_udp;
    Button btn_take_photo;
    Parser parser ;
    MAVLinkPacket packet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_camera);
        tv_udp = findViewById(R.id.tv_udp);
        btn_take_photo = findViewById(R.id.btn_take_photo);
        btn_take_photo.setOnClickListener(this);
        parser = new Parser();

        sendHeartbeatPacket();

        new Thread(new Runnable() {
            @Override
            public void run() {
                receive();
                send();
            }
        }).start();
    }

    private void sendHeartbeatPacket() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
            mExecutor.scheduleAtFixedRate(new mRunnable(), 0, 1, TimeUnit.SECONDS);
        }
    }

    private class mRunnable implements Runnable {
        @Override
        public void run() {

        }
    }

    private void send() {
        String host = "192.168.42.1";
        int port = 0;
        try {
            DatagramSocket sendSocket = new DatagramSocket();
            String mes = "123";
            byte[] buf = mes.getBytes();
            InetAddress ip = InetAddress.getByName(host);
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip, port);
            sendSocket.send(sendPacket);
            sendSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DatagramSocket getSocket;
    private void receive() {
        int port = 14550;
        try {
            getSocket = new DatagramSocket(port);
            while (true) {
                byte[] buf = new byte[2048];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                getSocket.receive(packet);
                byte[] receiveData = packet.getData();
//                String info = new String(buf, 0, packet.getLength());
                Log.e("yuneec0","bind receiveData: " + ByteUtils.byteArrayToHexString(receiveData,0,receiveData.length) + "  --- ");
//                setText(ByteUtils.byteArrayToHexString(receiveData,0,receiveData.length));

                parseUDP(receiveData);

                InetAddress sendIP = packet.getAddress();
                sendPort = packet.getPort();
//                Log.e("yuneec0","bind receiveData camera ip ：" + sendIP.getHostAddress() + "   port ：" + sendPort);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getReceiveAckCommandId(byte[] revBuf) {
        ByteBuffer buffer = ByteBuffer.wrap(revBuf);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort(10)&0x0FFFF;
    }

    int count = 0;
    int resultCode0 = 0;
    int resultCode5 = 0;
    private void parseUDP(byte[] receiveData) {

        int pos = 0;
        while (pos < receiveData.length){
            if(parser == null){
                parser = new Parser();
            }
            packet = parser.mavlink_parse_char(receiveData[pos]);

            if(packet != null){
                MAVLinkMessage mavLinkMessage = packet.unpack();
                int msgid = mavLinkMessage.msgid;
                Log.e("yuneec0","msgid:" + msgid);
                if(msgid == 77){
                    msg_command_ack msg = (msg_command_ack) mavLinkMessage;
                    Log.e("yuneec0","......msgid: 77777777777777777777777777 ------------------" + (count++) + " --- " + msg.result + "  ---  " + msg.command);

                    if(msg.result == 5){
                        resultCode5 ++ ;
                        Log.e("yuneec0","take pthoto resultCode5  = --- " + resultCode5 );
                    }

                    if(msg.result == 0){
                        resultCode0 ++ ;
                        Log.e("yuneec0","take pthoto resultCode0  = " + resultCode0 );
                    }
                }
            }

            pos++;
        }




//        Log.e("yuneec0","bind receiveData: " + ByteUtils.byteArrayToHexString(receiveData,0,receiveData.length));
//        int pos = 0;
//        while (pos+1 < receiveData.length){
//            //Log.e("gptest", "pos: " + pos + " length "+ receiveData.length);
//            if (receiveData[pos] == (byte) 0xfd) {
//                count++;
//                int len = receiveData[pos + 1] + 12;
//
//                byte[] msg = new byte[len];
//                System.arraycopy(receiveData, pos, msg, 0, len);
////                Log.e("yuneec0","bind receiveData: " + ByteUtils.byteArrayToHexString(msg,0,msg.length));
//                parseMav2(msg);
//
//                pos += len;
//            }
//            else {
//                break;
//            }
//        }

    }

    private void parseMav2(byte[] receiveData) {
        int len = receiveData[1];
//        byte[] data = new byte[len+10+2];
//        System.arraycopy(receiveData, 0, data, 0, data.length);
//        Log.e("yuneec0","bind receiveData: " + ByteUtils.byteArrayToHexString(receiveData,0,receiveData.length));
        setText(ByteUtils.byteArrayToHexString(receiveData,0,receiveData.length));
        byte[] msg = new byte[3];
        System.arraycopy(receiveData, 7, msg, 0, 3);
        int msgId = (0xff & msg[2]) | (0xff00 & (msg[1] << 8)) | (0xff0000 & (msg[0] << 16));
        int revCommandId = getReceiveAckCommandId(receiveData);
        int resultCode = receiveData[12]&0x0FF;
        if(msgId != 0){
            Log.e("yuneec0","receiveData msg id = " + msgId + " ***  command id =  "+revCommandId + " *** len = " + len);
            if(msgId == 77){
                if(revCommandId == 2000){
                    Log.e("yuneec0","receiveData resultCode = " + resultCode );
                    Log.e("yuneec0","bind receiveData: " + ByteUtils.byteArrayToHexString(receiveData,0,receiveData.length));
                }
            }
        }
    }


    //    DatagramSocket sendSocket;
    int sendPort = 0 ;
    private void sendMav2(final byte[] buf) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String host = "192.168.42.1";
                try {
//                    sendSocket = new DatagramSocket();
                    InetAddress ip = InetAddress.getByName(host);
                    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip, sendPort);
                    getSocket.send(sendPacket);
//                    sendSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void setText(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_udp.setText(msg);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_take_photo:
//                byte[] takephoto = new byte[]{(byte) 0xFD, 0x20, 0x00, 0x00, 0x27, 0x01, (byte) 0xBE, 0x4C, 0x00, 0x00, 0x00, 0x00,0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
//                        0x00, (byte) 0x80, 0x3F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xD0, 0x07, 0x01, 0x64, 0x7F,0x78 };
//                sendMav2(takephoto);
//                camera_control takephoto = new camera_control(2000,0);
//                MAVLinkPacket packet = takephoto.pack();
//                sendMav2(packet.encodePacket());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                            }
                            byte[] b = new byte[]{(byte) 0xFD, 0x20, 0x00, 0x00, 0x27, 0x01, (byte) 0xBE, 0x4C, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                                    0x00, (byte) 0x80, 0x3F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xD0, 0x07, 0x01, 0x64, 0x7F, 0x78};
                            sendMav2(b);
                        }
                    }
                }).start();

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(sendSocket != null){
//            sendSocket.close();
//        }
        if(getSocket != null){
            getSocket.close();
        }
        if (mExecutor != null) {
            mExecutor.shutdown();
        }
    }
}
