package com.future_melody.utils.passwordview;

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
 * Y
 */
public class GiveDecibelNullTelDialog {
    private TextView dialo_msg;
    private TextView btn_cancel;
    private TextView btn_determine;
    private Dialog alertDialog;
    private View view_bg;

    public GiveDecibelNullTelDialog(Context context) {
        alertDialog = new Dialog(context, R.style.no_back_dialog);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_common2);
        dialo_msg = window.findViewById(R.id.dialo_msg);
        btn_cancel = window.findViewById(R.id.btn_cancel);
        btn_determine = window.findViewById(R.id.btn_determine);
        view_bg = window.findViewById(R.id.view_bg);
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

    public void setRightName(String s) {
        btn_determine.setText(s);
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

    public void setCancelButton(View.OnClickListener listener) {
        btn_cancel.setOnClickListener(listener);
    }

    public void setDetermineButton(View.OnClickListener listener) {
        btn_determine.setOnClickListener(listener);
    }

    public void dissLet(int b) {
        btn_cancel.setVisibility(b);
        view_bg.setVisibility(b);
    }

}
