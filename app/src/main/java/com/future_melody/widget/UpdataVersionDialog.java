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
 * Date：2018/6/12 42
 * Notes:
 */
public class UpdataVersionDialog {
    private TextView dialo_version;
    private TextView text_content;
    private TextView btn_dowload;
    private Dialog alertDialog;

    public UpdataVersionDialog(Context context) {
        alertDialog = new Dialog(context, R.style.no_back_dialog);
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_version);
        dialo_version = window.findViewById(R.id.dialo_version);
        text_content = window.findViewById(R.id.text_content);
        btn_dowload = window.findViewById(R.id.btn_dowload);
    }

    public void setListener(Activity activity) {
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    activity.finish();
                    if (alertDialog!=null){
                        alertDialog.cancel();
                        alertDialog.dismiss();
                    }
                }
                return false;
            }
        });
    }

    public void dismiss() {
        alertDialog.dismiss();
    }
    public void show() {
        alertDialog.show();
    }

    public void cancel() {
        alertDialog.cancel();
    }

    public void setDetermineButton(View.OnClickListener listener) {
        btn_dowload.setOnClickListener(listener);
    }

    public void setTextNum(String s) {
        if (btn_dowload!=null) {
            btn_dowload.setText(s);
        }
    }

    public void setTextVersion(String s) {
        dialo_version.setText(s);
    }

    public void setTextContext(String s) {
        text_content.setText(s);
    }

}
