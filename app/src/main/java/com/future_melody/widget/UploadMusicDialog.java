package com.future_melody.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.future_melody.R;

/**
 * Author WZL
 * Date：2018/6/20 08
 * Notes:
 */
public class UploadMusicDialog {
    private Dialog alertDialog;
    private TextView btn_clancle;
    private TextView upload_num;
    private ProgressBar progressBarHorizontal;

    public UploadMusicDialog(Context context) {
        alertDialog = new Dialog(context, R.style.no_back_dialog);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_upload_music);
        btn_clancle = window.findViewById(R.id.btn_clancle);
        upload_num = window.findViewById(R.id.upload_num);
        progressBarHorizontal = window.findViewById(R.id.progressBarHorizontal);
    }

    public void setMsg(String s) {
        upload_num.setText(s);
    }

    public void setProgress(int i) {
        progressBarHorizontal.setProgress(i);
    }

    public void setProgressMax(int i) {
        progressBarHorizontal.setMax(i);
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

    public void setCancelButton(View.OnClickListener listener) {
        btn_clancle.setOnClickListener(listener);
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
}
