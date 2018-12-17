package com.future_melody.utils.passwordview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.future_melody.R;

/**
 * .
 * Created by Administrator on 2018/9/17/017.
 */
public class PassWordDialog extends Dialog {

    private PasswordView passwordView;

    public PassWordDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public PassWordDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }


    protected PassWordDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        // 设置点击外围解散.
        setContentView(R.layout.layout_sexselection);

        setCanceledOnTouchOutside(true);

        passwordView = (PasswordView) findViewById(R.id.passwordView);
    }

    public PassWordDialog setPasswordListener(PasswordView.PasswordListener passwordListener) {
        passwordView.setPasswordListener(passwordListener);
        return this;
    }
}
