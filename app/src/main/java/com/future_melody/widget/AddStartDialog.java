package com.future_melody.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.KeyBoardUtil;
import com.future_melody.utils.LogUtil;

/**
 * Author WZL
 * Date：2018/6/7 42
 * Notes:
 */
public class AddStartDialog {
    private TextView dialo_btn;
    private TextView dialo_msg;
    private EditText dialo_et;
    private Dialog alertDialog;

    public AddStartDialog(Context context) {
        alertDialog = new Dialog(context, R.style.no_back_dialog);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_add_start);
        dialo_btn = window.findViewById(R.id.dialo_btn);
        dialo_msg = window.findViewById(R.id.dialo_msg);
        dialo_et = window.findViewById(R.id.dialo_et);
      /*  dialo_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        dialo_et.setFilters(emoji);*/
        dialo_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5),CommonUtils.enmoji(context)});
      /*  InputFilter[] emoji = {CommonUtils.enmoji(context)};
        dialo_et.setFilters(emoji);*/
        dialo_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LogUtil.e("addDiaolog", charSequence + "");
                if (!TextUtils.isEmpty(charSequence)) {
                    dialo_btn.setBackground(context.getResources().getDrawable(R.drawable.text_radius_diaolog));
                    dialo_btn.setTextColor(context.getResources().getColor(R.color.color_333333));
                } else {
                    dialo_btn.setBackground(context.getResources().getDrawable(R.drawable.text_radius_un));
                    dialo_btn.setTextColor(context.getResources().getColor(R.color.huis));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void dismiss(Activity context) {
        KeyBoardUtil.hideKeyBoard(context);
        alertDialog.dismiss();
    }

    public void setNegativeButton(View.OnClickListener listener) {
        dialo_btn.setOnClickListener(listener);
    }

    public String getMsg() {
        return dialo_et.getText().toString();
    }

    public void setTitle(String s) {
        dialo_msg.setText(s);
    }
}
