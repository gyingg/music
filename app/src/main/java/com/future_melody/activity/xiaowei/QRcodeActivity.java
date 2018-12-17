package com.future_melody.activity.xiaowei;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.future_melody.R;
import com.future_melody.activity.zxing.OnScannerListener;
import com.future_melody.activity.zxing.ScanerCodeActivity;
import com.future_melody.base.BaseActivity;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.XiaoWeiQRcodeRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.XiaoWeiQRcodeRespone;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.List;
import java.util.UUID;

/**
 * Author WZL
 * Date：2018/9/3 11
 * Notes:
 */
public class QRcodeActivity extends BaseActivity implements OnScannerListener {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BleDevice device;

    @Override
    protected int getContentViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        return R.layout.activity_qr_code;
    }

    @Override
    protected void initView() {
        initBle();
        if (BleManager.getInstance().isSupportBle()) {
            if (BleManager.getInstance().isBlueEnable()) {

            } else {
                Toast.makeText(this, "请打开蓝牙", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 0x01);
            }
        } else {
            Toast.makeText(this, "您的设备暂不支持BLE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initData() {
        //二维码扫码
        ScanerCodeActivity.setScanerListener(this);
        startActivity(new Intent(this, ScanerCodeActivity.class));
    }

    //二维码扫码成功and失败
    @Override
    public void onSuccess(String type, Result result) {
        //扫描类型
        BarcodeFormat codeType = result.getBarcodeFormat();
        if (BarcodeFormat.QR_CODE.equals(codeType)) {
            System.out.println("二维码扫描结果");
        } else if (BarcodeFormat.EAN_13.equals(codeType)) {
            System.out.println("让你扫二维码,扫什么条形码 →_→ ");
        } else {
            System.out.println("扫的什么玩意儿 -_-|| ");
        }
        //扫描结果
        String realContent = result.getText();
        Toast.makeText(this, "扫描结果:" + realContent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail(String type, String message) {
        System.out.println("扫描失败原因:" + message);
    }

    //xiaoweiName
    private void getName() {
        addSubscribe(apis.getXiaoWeiInfo(new XiaoWeiQRcodeRequest("", userId(),"", 0))
                .compose(RxHttpUtil.<FutureHttpResponse<XiaoWeiQRcodeRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<XiaoWeiQRcodeRespone>handleResult())
                .subscribeWith(new HttpSubscriber<XiaoWeiQRcodeRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(XiaoWeiQRcodeRespone xiaoWeiQRcodeRespone) {

                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    private void initBle() {
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setOperateTimeout(50000);
    }

    private void initSearch(String xiaoweiName) {
        String[] uuids;
//        if (TextUtils.isEmpty(et_serevice_uuid.getText().toString())) {
//            uuids = null;
//        } else {
//            uuids = et_serevice_uuid.getText().toString().split(",");
//        }
        uuids = "0000180d-0000-1000-8000-00805f9b34fb".split(",");
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }
        Log.e("UUID:", serviceUuids.length + "");
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, xiaoweiName)         // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void search() {
        showLoadingDialog();
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                dismissLoadingDialog();
                Log.e("设备名称onScanFinished：", scanResultList.size() + "");
            }

            @Override
            public void onScanStarted(boolean success) {
                Log.e("onScanStarted：", success + "");
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                dismissLoadingDialog();
                Log.e("设备名称：", bleDevice.getName() + "");
                Log.e("设备名称getKey：", bleDevice.getKey() + "");
                Log.e("getMac：", bleDevice.getMac() + "");
                Log.e("getDevice：", bleDevice.getDevice() + "");
                Log.e("getTimestampNanos：", bleDevice.getTimestampNanos() + "");
                Log.e("getRssi：", bleDevice.getRssi() + "");
                Log.e("getScanRecord：", bleDevice.getScanRecord() + "");
                device = bleDevice;
            }
        });
    }

    private void content(BleDevice bleDevice) {
        Log.e("content：", "开始链接名称：" + device.getName());
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                Log.e("content：", "开始链接");
                showLoadingDialog();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                Log.e("连接失败：", exception.toString() + "");
                dismissLoadingDialog();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                dismissLoadingDialog();
                Log.e("content：", "连接成功");
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                Log.e("content：", "断开连接");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x01:
                if (BleManager.getInstance().isBlueEnable()) {
                    Log.e("开启成功", "打开了");
                } else {
                    Log.e("没有开启", "没有");
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }
}
