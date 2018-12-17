package com.future_melody.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

import com.future_melody.utils.LogUtil;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * Author WZL
 * Date：2018/7/3 37
 * Notes:
 */
public class HeadListenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
//            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//            if (BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
//                //Bluetooth headset is now disconnected
//            }
//        } else if ("android.intent.action.HEADSET_PLUG".equals(action)) {
//            if (intent.hasExtra("state")) {
//                if (intent.getIntExtra("state", 0) == 0) {
//                    Toast.makeText(context, "拔出", Toast.LENGTH_LONG).show();
//                    if (MusicManager.isPlaying()) {
//                        MusicManager.get().pauseMusic();
//                    }else {
//                        return;
//                    }
//                } else if (intent.getIntExtra("state", 0) == 1) {
//                    Toast.makeText(context, "插入", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {

            if (MusicManager.isPlaying()) {
                MusicManager.get().pauseMusic();
            }else {
            }
        }
    }
}
