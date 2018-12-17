package com.future_melody.net.udp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.future_melody.utils.LogUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Author WZL
 * Date：2018/7/9 31
 * Notes:
 */
public class UdpClientConnector {
    private static UdpClientConnector mUdpClientConnector;
    private ConnectLinstener mListener;
    private Thread mSendThread;

    private byte receiveData[] = new byte[1024];
    private String mSendHexString;

    private boolean isSend = false;

    public interface ConnectLinstener {
        void onReceiveData(String data);
    }

    public void setOnConnectLinstener(ConnectLinstener linstener) {
        this.mListener = linstener;
    }

    public static UdpClientConnector getInstance() {
        if (mUdpClientConnector == null) {
            mUdpClientConnector = new UdpClientConnector();
        }
        return mUdpClientConnector;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    if (mListener != null) {
                        mListener.onReceiveData(msg.getData().getString("data"));
                    }
                    break;
            }
        }
    };

    /**
     * 创建udp发送连接（服务端ip地址、端口号、超时时间）
     *
     * @param ip
     * @param port
     * @param timeOut
     */
    public void createConnector(final String ip, final int port, final int timeOut) {
        if (mSendThread == null) {
            mSendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (!isSend)
                            continue;
                        DatagramSocket socket = null;
                        try {
                            socket = new DatagramSocket();
                            socket.setSoTimeout(timeOut);
                            InetAddress serverAddress = InetAddress.getByName(ip);
                            byte data[] = mSendHexString.getBytes("utf-8");
                            DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, port);
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length,serverAddress, port);
                            socket.send(sendPacket);
                            socket.receive(receivePacket);
                            LogUtil.e("接收的IP：" ,receivePacket.getSocketAddress()+"");
                            LogUtil.e("接收的端口：" ,receivePacket.getPort()+"");
                            LogUtil.e("接收的Ip：" ,receivePacket.getAddress()+"");
                            Message msg = new Message();
                            msg.what = 1000;
                            Bundle bundle = new Bundle();
                            bundle.putString("data", new String(receivePacket.getData()));
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                            socket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isSend = false;
                    }
                }
            });
            mSendThread.start();
        }
    }

    /**
     * 发送数据
     *
     * @param str
     */
    public void sendStr(final String str) {
        mSendHexString = str;
        isSend = true;
    }

}
