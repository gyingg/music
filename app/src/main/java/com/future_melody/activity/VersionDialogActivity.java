package com.future_melody.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.common.CommonConst;

/**
 * 退出登录DialogA
 */
public class VersionDialogActivity extends Activity {
    private TextView dialo_msg;
    private TextView btn_determine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_dialog);
        dialo_msg = findViewById(R.id.dialo_msg);
        btn_determine = findViewById(R.id.btn_determine);
        dialo_msg.setText(getIntent().getStringExtra("msg")+"");
        btn_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(VersionDialogActivity.this, LoginActivity.class);
                loginIntent.putExtra(CommonConst.ISFINISH, 1);
                startActivity(loginIntent);
                finish();
            }
        });

    }
}
