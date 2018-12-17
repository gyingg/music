package com.future_melody.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;

/**
 * Author WZL
 * Date：2018/6/7 42
 * Notes:
 */
public class NewUserDialog {
    private Dialog alertDialog;
    private TextView dialog_new_user_msg;
    private TextView text_btn_next;
    private ImageView img_feidie;

    public NewUserDialog(Context context) {
        alertDialog = new Dialog(context, R.style.no_back_dialog);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_new_user);
        dialog_new_user_msg = window.findViewById(R.id.dialog_new_user_msg);
        text_btn_next = window.findViewById(R.id.text_btn_next);
        img_feidie = window.findViewById(R.id.img_feidie);
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
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog.cancel();
        }
    }

    public void setCancelButton(View.OnClickListener listener) {
        if (text_btn_next != null) {
            text_btn_next.setOnClickListener(listener);
        }
    }

    public void setMsg(String s) {
        if (dialog_new_user_msg != null) {
            if (!TextUtils.isEmpty(s)) {
                dialog_new_user_msg.setText(s);
            }
        }
    }
}
