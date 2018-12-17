package com.future_melody.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.future_melody.R;

/**
 * Author WZL
 * Date：2018/9/13 13
 * Notes:
 */
public class PearlDialog {
    private Dialog alertDialog;
    private TextView dialog_pearl_num;

    public PearlDialog(Context context) {
        alertDialog = new Dialog(context, R.style.dialogstyle);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_pearl);
        dialog_pearl_num = window.findViewById(R.id.dialog_pearl_num);
    }

    public void dismiss() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog.cancel();
        }
    }
    public void setMsg(String num){
        dialog_pearl_num.setText(num);
    }
}
