package com.future_melody.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.future_melody.R;

/**
 * Author WZL
 * Date：2018/6/7 42
 * Notes:
 */
public class IsWifiDialog {
    private TextView btn_open;
    private Dialog alertDialog;

    public IsWifiDialog(Context context) {
        alertDialog = new Dialog(context, R.style.no_back_dialog);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_is_wifi);
        btn_open = window.findViewById(R.id.btn_open);
    }

    public void setListener(Activity activity) {
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    activity.finish();
                }
                return false;
            }
        });
    }


    public void dismiss() {
        if (alertDialog!=null) {
            alertDialog.dismiss();
            alertDialog.cancel();
        }
    }

    public void setCancelButton(View.OnClickListener listener) {
        btn_open.setOnClickListener(listener);
    }

}
