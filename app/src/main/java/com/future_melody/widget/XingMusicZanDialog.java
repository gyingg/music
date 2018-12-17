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
public class XingMusicZanDialog {
    private TextView dialo_msg;
    private TextView btn_cancel;
    private Dialog alertDialog;

    public XingMusicZanDialog(Context context) {
        alertDialog = new Dialog(context, R.style.no_back_dialog);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_xing_music__zan);
        dialo_msg = window.findViewById(R.id.dialo_msg);
        btn_cancel = window.findViewById(R.id.btn_cancel);
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

    public void setMsg(String s) {
        dialo_msg.setText(s);
    }

    public void dismiss() {
        alertDialog.cancel();
        alertDialog.dismiss();
    }

    public void setCancelButton(View.OnClickListener listener) {
        btn_cancel.setOnClickListener(listener);
    }
}
