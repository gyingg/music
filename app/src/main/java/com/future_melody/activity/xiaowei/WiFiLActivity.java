package com.future_melody.activity.xiaowei;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.mode.XiaoweiBleRespone;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.widget.CommonDialog;
import com.future_melody.widget.XiaoWeiWifiDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WiFiLActivity extends BaseActivity implements View.OnClickListener {

    private Animation animation;
    private ImageView back;
    private ImageView ph_title_right_img;
    private TextView et_wifi_name;
    private EditText et_wifi_psw;
    private TextView start_wifi;
    private BleDevice device;
    private int sucess = 0;
    private String realContent;
    private String serviceUUID;
    private String characteristicUUID;
    private String allString;
    private List<String> strs = new ArrayList<String>();
    private int totlaNum;
    private int isNet;
    private WifiManager wifiMgr;
    ;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_wi_fi_liens;
    }

    @Override
    protected void initView() {
        wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        et_wifi_name = findViewById(R.id.et_wifi_name);
        et_wifi_psw = findViewById(R.id.et_wifi_psw);
        start_wifi = findViewById(R.id.start_wifi);
        initAnim();
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        start_wifi.setOnClickListener(this);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        String wifiname = wifiInfo.getSSID().substring(1, (wifiInfo.getSSID().length() - 1));
        et_wifi_name.setText(wifiname + "");
    }

    private void initAnim() {
        animation = AnimationUtils.loadAnimation(this, R.anim.player);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
    }


    private void startAnmi() {
        ph_title_right_img.startAnimation(animation);
    }

    private void stoptAnmi() {
        ph_title_right_img.clearAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
        BleManager.getInstance().stopIndicate(device, serviceUUID, characteristicUUID);
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        device = intent.getParcelableExtra("bleDevice");
        realContent = intent.getStringExtra(CommonConst.XIAOWEIMACSN);
        isNet = getIntent().getIntExtra(CommonConst.XIAOWEI_IS_NET, -1);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.start_wifi:
                showLoadingDialog();
                String name = et_wifi_name.getText().toString();
                String psw = et_wifi_psw.getText().toString();
                JsonObject object = new JsonObject();
                if (!TextUtils.isEmpty(name)) {
                    if (psw.length() > 0 && psw.length() <= 7) {
                        toast("WiFi密码不能少于8位");
                    } else {
                        object.addProperty("ssid", name);
                        object.addProperty("passwd", psw);
                        Log.e("DATA：", object.toString());
                        byte[] data = CommonUtils.strToByteArray(object.toString());
                        Log.e("totla：", data.length + "总长度");
                        Math.ceil(data.length / 17);
                        double sum = (double) data.length / 17;
                        totlaNum = (int) Math.ceil(sum);
                        byte[] sumTotal = CommonUtils.toBytes(CommonUtils.numToHex8(totlaNum));
                        Log.e("totla：", totlaNum + "总包数");
                        Log.e("sumTotal：", sumTotal.length + "");
                        Log.e("总包数转String二进制：", CommonUtils.bytesToHexString(sumTotal) + "");
                        for (int i = 0; i < totlaNum; i++) {
                            byte[] packageNum = CommonUtils.toBytes(CommonUtils.numToHex8(i));
                            Log.e("第几转String：", CommonUtils.byteArrayToStr(packageNum));
                            byte[] packageBytes = new byte[20];
                            int f = i * 17;
                            packageBytes = Arrays.copyOfRange(data, f, f + 17);
                            Log.e(i + "包:", packageBytes.length + "");
                            byte[] sumByte = CommonUtils.byteMergerAll(packageNum, sumTotal, packageBytes);
                            Log.e("累积和：", (CommonUtils.all(sumByte)) + "----" + i);
//                    byte[] end = toBytes(numToHex8(all(sumByte)));
                            byte[] end = CommonUtils.toBytes(CommonUtils.byteToHex(CommonUtils.all(sumByte)));
                            Log.e("end总长度：", end.length + "");
                            byte[] sendData = CommonUtils.byteMergerAll(packageNum, sumTotal, packageBytes, end);
                            Log.e("总长度：", sendData.length + "");
                            try {
                                Thread.currentThread().sleep(300);//阻断2秒
                                sendData(sendData);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "wifi输入名字", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void sendData(byte[] data) {
        BluetoothGattService service1 = null;
        BluetoothGattCharacteristic characteristic1 = null;
        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(device);
        for (BluetoothGattService service : gatt.getServices()) {
            service1 = service;
        }
        for (BluetoothGattCharacteristic characteristic : service1.getCharacteristics()) {
            characteristic1 = characteristic;
        }
        serviceUUID = characteristic1.getService().getUuid().toString();
        characteristicUUID = characteristic1.getUuid().toString();
        Log.e("ServiceUUID：", serviceUUID + "");
        Log.e("characteristicUUID：", characteristicUUID + "");
        BleManager.getInstance().write(device, serviceUUID, characteristicUUID, data, false, new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                // 发送数据到设备成功（分包发送的情况下，可以通过方法中返回的参数可以查看发送进度）
                sucess = sucess + 1;
                Log.e("onWriteSuccess：", current + "----" + total + "sucess:" + sucess);
                Log.e("已成功发送的数据：", CommonUtils.bytesToHexString(data) + "");
                if (sucess == totlaNum) {
                    Log.e("onWriteSuccess：", "sucess:" + sucess);
//                    dialog.dismiss();
//                    getData(dialog);
                }
            }

            @Override
            public void onWriteFailure(BleException exception) {
                // 发送数据到设备失败
                dismissLoadingDialog();
                Log.e("c：", exception.toString());
            }
        });
        BleManager.getInstance().indicate(
                device,
                serviceUUID,
                characteristicUUID,
                new BleIndicateCallback() {
                    @Override
                    public void onIndicateSuccess() {
                        // 打开通知操作成功
                    }

                    @Override
                    public void onIndicateFailure(BleException exception) {
                        // 打开通知操作失败
                        LogUtil.e("onIndicateFailure", exception.toString());
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        // 打开通知后，设备发过来的数据将在这里出现
                        dismissLoadingDialog();
                        byte[] k = new byte[17];
                        System.arraycopy(data, 2, k, 0, 17);
                        LogUtil.e("新的数组：", CommonUtils.hexStr2Str(CommonUtils.bytesToHexString(k)) + "");
                        LogUtil.e("是否成功：", CommonUtils.bytesToHexString(data) + "===" + data.length);
                        LogUtil.e("是否成功String：", CommonUtils.hexStr2Str(CommonUtils.bytesToHexString(data)) + "");
                        int num = Integer.parseInt((CommonUtils.bytesToHexString(data)).substring(0, 2));
                        int allnum = Integer.parseInt((CommonUtils.bytesToHexString(data)).substring(2, 4));
                        LogUtil.e("num:", num + "");
                        strs.add(CommonUtils.bytesToHexString(data));
                        if (strs.size() == allnum) {
                            Map<Integer, String> map = new TreeMap<>(new Comparator<Integer>() {
                                @Override
                                public int compare(Integer o1, Integer o2) {
                                    return o1.compareTo(o2);
                                }
                            });
                            for (String item : strs) {
                                String head = item.substring(0, 2);
                                int headInt = Integer.parseInt(head);
                                map.put(headInt, item);
                            }
                            String str = "";
                            byte[] b = new byte[17];
//                            LogUtil.e("新的数组：", CommonUtils.hexStr2Str(CommonUtils.bytesToHexString(k)) + "");
                            for (Map.Entry<Integer, String> item : map.entrySet()) {
                                System.arraycopy(CommonUtils.hexStringToBytes(item.getValue()), 2, b, 0, 17);
                                LogUtil.e("反编译", item.getValue());
                                str += (CommonUtils.hexStr2Str(CommonUtils.bytesToHexString(b)).toString()).replace(" ", "");
                                LogUtil.e("反编译2", CommonUtils.hexStr2Str(CommonUtils.bytesToHexString(b)).toString());
                            }
                            int dex = str.indexOf("}");
                            str = str.substring(0, (dex + 1));
                            LogUtil.e("排序之后：", str + "");
                            try {
                                Gson gson = new Gson();
                                XiaoweiBleRespone respone = gson.fromJson(str, XiaoweiBleRespone.class);
                                LogUtil.e("排序之后wifi:", respone.getWifi() + "");
                                LogUtil.e("排序之后mac:", respone.getMac() + "");
                                LogUtil.e("排序之后sid:", respone.getId() + "");
                                if (!respone.getWifi().equals("failed")) {
                                    Intent intent = new Intent(mActivity, NetworkSuccessActivity.class);
                                    intent.putExtra(CommonConst.XIAOWEI_IS_NET, isNet);
                                    intent.putExtra(CommonConst.XIAOWEIMACSN, realContent);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    CommonDialog commonDialog = new CommonDialog(mActivity);
                                    commonDialog.setMsg("配网失败");
                                    commonDialog.dissLet(View.GONE);
                                    commonDialog.setRightName("重新配网");
                                    commonDialog.setDetermineButton(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(mActivity, AddXiaoweiQRcodeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                            }

                        }
                    }
                });
    }

    private void getData(XiaoWeiWifiDialog dialog) {
        BleManager.getInstance().indicate(
                device,
                serviceUUID,
                characteristicUUID,
                new BleIndicateCallback() {
                    @Override
                    public void onIndicateSuccess() {
                        // 打开通知操作成功
                    }

                    @Override
                    public void onIndicateFailure(BleException exception) {
                        // 打开通知操作失败
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        // 打开通知后，设备发过来的数据将在这里出现
                        byte[] k = new byte[17];
                        System.arraycopy(data, 2, k, 0, 17);
                        LogUtil.e("新的数组：", CommonUtils.hexStr2Str(CommonUtils.bytesToHexString(k)) + "");
                        LogUtil.e("是否成功：", CommonUtils.bytesToHexString(data) + "===" + data.length);
                        LogUtil.e("是否成功String：", CommonUtils.hexStr2Str(CommonUtils.bytesToHexString(data)) + "");
                        dialog.dismiss();
                        int num = Integer.parseInt((CommonUtils.bytesToHexString(data)).substring(0, 2));
                        int allnum = Integer.parseInt((CommonUtils.bytesToHexString(data)).substring(2, 4));
                        LogUtil.e("num:", num + "");
                    }
                });
    }

}
