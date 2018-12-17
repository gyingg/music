package com.future_melody.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.future_melody.R;

/**
 * Author WZL
 * Date：2018/6/7 42
 * Notes:
 */
public class MainFrieDialog2 {
    private FrameLayout id_main_fire;
    private Dialog alertDialog;

    public MainFrieDialog2(Context context) {
        alertDialog = new Dialog(context, R.style.no_back_dialog);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_main_fire2);
        id_main_fire = window.findViewById(R.id.id_main_fire);
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
        id_main_fire.setOnClickListener(listener);
    }

}
