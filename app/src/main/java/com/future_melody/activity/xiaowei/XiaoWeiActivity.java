package com.future_melody.activity.xiaowei;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.net.udp.TcpClientConnector;
import com.future_melody.net.udp.UdpClientConnector;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.DatagramSocket;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author WZL
 * Date：2018/7/9 31
 * Notes:
 */
public class XiaoWeiActivity extends BaseActivity {
    @BindView(R.id.et_wifi_ip)
    EditText etWifiIp;
    @BindView(R.id.send_udp)
    Button sendUdp;
    @BindView(R.id.text_respone)
    TextView textRespone;
    @BindView(R.id.text_send)
    TextView textSend;
    @BindView(R.id.send_tcp)
    Button sendTcp;
    @BindView(R.id.send_tcp_msg)
    Button sendTcpMsg;
    @BindView(R.id.send_info)
    Button sendInfo;
    @BindView(R.id.text_xiaowei_info)
    TextView textXiaoweiInfo;
    @BindView(R.id.et_wifi)
    EditText etWifi;
    @BindView(R.id.et_wifi_psw)
    EditText etWifiPsw;
    @BindView(R.id.send_wifi)
    Button sendWifi;
    @BindView(R.id.text_wifi_info)
    TextView textWifiInfo;
    private UdpClientConnector connector = null;
    private TcpClientConnector tcpClientConnector = null;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_xiaowei;
    }

    @Override
    protected void initView() {
        setTitle("小未");
        setBlackBackble();
        setBarColor(R.color.white, true);
        setTitleLayoutColor(mActivity, R.color.white);
        setTitleColor(R.color.color_333333);
    }

    @Override
    protected void initData() {
        textSend.setText("IP地址 :" + CommonUtils.getHostIP());
        connector = UdpClientConnector.getInstance();
        tcpClientConnector = TcpClientConnector.getInstance();
    }

    @OnClick({R.id.send_udp, R.id.text_send, R.id.send_tcp, R.id.send_tcp_msg, R.id.send_info, R.id.send_wifi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_udp:
                sendUdp();
                break;
            case R.id.text_send:
                break;
            case R.id.send_tcp:
                tcpClientConnector.creatConnect("192.168.2.5", 5100);
                LogUtil.e("是否连接成功：", tcpClientConnector.isBound() + "");
                tcpClientConnector.setOnConnectLinstener(new TcpClientConnector.ConnectLinstener() {
                    @Override
                    public void onReceiveData(String data) {
                        LogUtil.e("TCP返回数据: ", data + "=");
                    }
                });
                break;
            case R.id.send_tcp_msg:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            tcpClientConnector.send(etWifiIp.getText().toString());
                            JsonObject object = new JsonObject();
                            object.addProperty("cmd", "getApList");
                            tcpClientConnector.send(object.toString());
                            tcpClientConnector.setOnConnectLinstener(new TcpClientConnector.ConnectLinstener() {
                                @Override
                                public void onReceiveData(String data) {
                                    textRespone.setText("热点信息：：" + data + "");
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.send_info:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            tcpClientConnector.send(etWifiIp.getText().toString());
                            JsonObject object = new JsonObject();
                            object.addProperty("cmd", "getDevInfo");
                            tcpClientConnector.send(object.toString());
                            tcpClientConnector.setOnConnectLinstener(new TcpClientConnector.ConnectLinstener() {
                                @Override
                                public void onReceiveData(String data) {
                                    textXiaoweiInfo.setText("小未信息：" + data + "");
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.send_wifi:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            tcpClientConnector.send(etWifiIp.getText().toString());
                            JsonObject object = new JsonObject();
                            object.addProperty("cmd", "joinWifi");
                            object.addProperty("ssid", etWifi.getText().toString());
                            object.addProperty("passwd", etWifiPsw.getText().toString());
                            tcpClientConnector.send(object.toString());
                            tcpClientConnector.setOnConnectLinstener(new TcpClientConnector.ConnectLinstener() {
                                @Override
                                public void onReceiveData(String data) {
                                    textXiaoweiInfo.setText("配网模式：" + data + "");
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    private void sendUdp() {
        connector.createConnector("172.16.66.255", 5101, 90000);
        JsonObject object = new JsonObject();
        object.addProperty("cmd", "getTcpInfo");
        LogUtil.e("object:", object.toString());
        connector.sendStr(object.toString());
        connector.setOnConnectLinstener(new UdpClientConnector.ConnectLinstener() {
            @Override
            public void onReceiveData(String data) {
                LogUtil.e("返回数据:", data);
                toast(data);
                textRespone.setText("返回数据:" + data);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            tcpClientConnector.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
