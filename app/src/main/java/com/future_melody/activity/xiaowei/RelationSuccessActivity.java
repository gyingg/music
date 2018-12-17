package com.future_melody.activity.xiaowei;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;

/**
 * Author WZL
 * Date：2018/9/3 17
 * Notes:
 */
public class RelationSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView qr_code_next;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_relation_success;
    }

    @Override
    protected void initView() {
        qr_code_next = findViewById(R.id.qr_code_next);
    }

    @Override
    protected void initData() {
        qr_code_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.qr_code_next:
                intent = new Intent(mActivity, RelevanceXiaoWeiActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
