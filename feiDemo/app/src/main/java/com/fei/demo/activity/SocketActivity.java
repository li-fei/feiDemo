package com.fei.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fei.demo.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

public class SocketActivity extends Activity {
    TextView tv_socket_test;
    String msg ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        tv_socket_test = findViewById(R.id.tv_socket_test);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                udpTest();
                receive();
            }
        }).start();
    }

    private void udpTest() {
        String host = "255.255.255.255";// 广播地址
        int port = 9999;// 广播的目的端口
        String message = "test yuneec 192.168.42.155";// 用于发送的字符串
        try {
            InetAddress adds = InetAddress.getByName(host);
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), adds, port);
            while (true) {
                ds.send(dp);
                Log.e("yuneec0","send");
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send() {
        String host = "255.255.255.255";
        int port = 9998;
        try {
            DatagramSocket sendSocket = new DatagramSocket();
            String mes = "你好！接收方！";
            byte[] buf = mes.getBytes();
//            InetAddress ip = InetAddress.getLocalHost();
            InetAddress ip = InetAddress.getByName(host);
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip, port);

            sendSocket.send(sendPacket);

            //-----------------------------------------------------------------------
            byte[] getBuf = new byte[1024];
            DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);
            sendSocket.receive(getPacket);

            String backMes = new String(getBuf, 0, getPacket.getLength());
            Log.e("yuneec0","接受方返回的消息：" + backMes);

            sendSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receive() {
        int port = 9998;
        try {
            DatagramSocket getSocket = new DatagramSocket(port);
            byte[] buf = new byte[1024];
            DatagramPacket getPacket = new DatagramPacket(buf, buf.length);

            getSocket.receive(getPacket);

            String getMes = new String(buf, 0, getPacket.getLength());
            Log.e("yuneec0","对方发送的消息：" + getMes);     msg+=getMes;

            // 通过数据报得到发送方的IP和端口号，并打印
            InetAddress sendIP = getPacket.getAddress();
            int sendPort = getPacket.getPort();
            Log.e("yuneec0","对方的IP地址是：" + sendIP.getHostAddress()); msg+=sendIP.getHostAddress();
            Log.e("yuneec0","对方的端口号是：" + sendPort); msg+=sendPort;

            // 通过数据报得到发送方的套接字地址
            SocketAddress sendAddress = getPacket.getSocketAddress();

            // 确定要反馈发送方的消息内容，并转换为字节数组
            String feedback = "接收方说：我收到了！";
            byte[] backBuf = feedback.getBytes();

            // 创建发送类型的数据报
            DatagramPacket sendPacket = new DatagramPacket(backBuf,
                    backBuf.length, sendAddress);

            // 通过套接字发送数据
            getSocket.send(sendPacket);

            // 关闭套接字
            getSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_socket_test.setText(msg);
            }
        });
    }




}
