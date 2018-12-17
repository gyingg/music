package com.future_melody.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.future_melody.activity.MainNewActivity;
import com.future_melody.activity.VersionDialogActivity;
import com.future_melody.base.FutureApplication;
import com.future_melody.common.SPconst;
import com.future_melody.mode.JGResponeMode;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.SPUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Author WZL
 * Date：2018/6/27 21
 * Notes:
 */
public class JiGuangReceiver extends BroadcastReceiver {
    private static final String TAG = "JiGuangReceiver :";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e("JiGuangReceiver", "启动");
        try {
            Intent pushintent = new Intent(FutureApplication.getContext(), cn.jpush.android.service.PushService.class);//启动极光推送的服务
            context.startService(pushintent);
            Bundle bundle = intent.getExtras();
            LogUtil.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtil.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                LogUtil.e(TAG, " 异地登录信息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                SPUtils.getInstance().remove(SPconst.USER_ID);
                SPUtils.getInstance().put(SPconst.ISlogin, false);
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FutureApplication.getContext());
//                dialogBuilder.setTitle("提示");
//                dialogBuilder.setMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                dialogBuilder.setCancelable(false);
//                dialogBuilder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent loginIntent = new Intent(context, LoginActivity.class);
//                        loginIntent.putExtra(CommonConst.ISFINISH, 1);
//                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(loginIntent);
//                    }
//                });
//                AlertDialog alertDialog = dialogBuilder.create();
//                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                alertDialog.show();
                Intent sersion = new Intent(FutureApplication.getContext(), VersionDialogActivity.class);
                sersion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sersion.putExtra("msg", bundle.getString(JPushInterface.EXTRA_MESSAGE));
                context.startActivity(sersion);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtil.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);
//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FutureApplication.getContext());
//                dialogBuilder.setTitle("提示");
//                dialogBuilder.setMessage("12345678");
//                dialogBuilder.setCancelable(false);
//                dialogBuilder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent loginIntent = new Intent(context, LoginActivity.class);
//                        loginIntent.putExtra(CommonConst.ISFINISH, 1);
//                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(loginIntent);
//                    }
//                });
//                AlertDialog alertDialog = dialogBuilder.create();
//                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                alertDialog.show();

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                LogUtil.d(TAG, "用户点击打开了通知");
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Gson gson = new Gson();
                    JGResponeMode jgResponeMode = gson.fromJson(json.toString(), JGResponeMode.class);
                    //打开自定义的Activity
                    if (jgResponeMode.type.equals("1")) {
                        Intent i = new Intent(context, MainNewActivity.class);
                        i.putExtra("isMine" ,"1");
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }else {
                        Intent i = new Intent(context, MainNewActivity.class);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }
                } catch (Exception e) {

                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                LogUtil.d(TAG, " 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtil.w(TAG, "" + intent.getAction() + " connected state change to " + connected);
            } else {
                LogUtil.d(TAG, "Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtil.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    LogUtil.e(TAG, "解析：" + json.toString());

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtil.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }
}
