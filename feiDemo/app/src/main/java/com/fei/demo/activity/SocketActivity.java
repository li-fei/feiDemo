package com.fei.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.fei.demo.R;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

/*
Socket的使用类型主要有两种：
        流套接字（streamsocket） ：基于 TCP协议，采用 流的方式 提供可靠的字节流服务
        数据报套接字(datagramsocket)：基于 UDP协议，采用 数据报文 提供数据打包发送的服务
*/

public class SocketActivity extends Activity {
    TextView tv_socket_test;
    String msg ="";
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        tv_socket_test = findViewById(R.id.tv_socket_test);
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send();
                    }
                }).start();
            }
        });

        findViewById(R.id.btn_receive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        receive();
                    }
                }).start();
            }
        });

    }

    private void send() {
        count++;
        String host = "255.255.255.255";
        int port = 9998;
        try {
            DatagramSocket sendSocket = new DatagramSocket();
            String mes = " 你好！我是客户端，给你发的消息是: " + count;
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
            Log.e("yuneec0","接受服务器返回的消息：" + backMes);

            sendSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DatagramSocket receiveSocket;
    private void receive() {
        int port = 9998;
        byte[] buf = new byte[1024];
        SocketAddress sendAddress;
        try {
            receiveSocket = new DatagramSocket(port);
            DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

            while (true){
                receiveSocket.receive(receivePacket);
                String getMes = new String(buf, 0, receivePacket.getLength());
                Log.e("yuneec0","对方发送的消息：" + getMes);     msg+=getMes;
                // 通过数据报得到发送方的IP和端口号，并打印
                InetAddress sendIP = receivePacket.getAddress();
                int sendPort = receivePacket.getPort();
                Log.e("yuneec0","对方的IP地址是：" + sendIP.getHostAddress() + "  ,端口号是：" + sendPort);
                // 通过数据报得到发送方的套接字地址
                sendAddress = receivePacket.getSocketAddress();
                // 确定要反馈发送方的消息内容，并转换为字节数组
                String feedback = count + " 我收到了，这是回给你的。 ";
                byte[] backBuf = feedback.getBytes();
                // 创建发送类型的数据报
                DatagramPacket sendPacket = new DatagramPacket(backBuf, backBuf.length, sendAddress);
                // 通过套接字发送数据
                receiveSocket.send(sendPacket);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_socket_test.setText(msg);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭套接字
        receiveSocket.close();
    }
}
