package com.future_melody.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.fragment.AttentionFragment;
import com.future_melody.fragment.FansFragment;
import com.future_melody.fragment.SuchFragment;

import org.greenrobot.eventbus.EventBus;

public class GiveDecibelTelActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout star_details_fragment;
    private FragmentManager manager;
    private SuchFragment suchFragment;
    private FragmentTransaction transaction;
    private AttentionFragment attentionFragment;
    private FansFragment fansFragment;
    private TextView or_such_user;
    private TextView attention;
    private TextView fans;
    private View v1;
    private View v2;
    private View v3;
    private TextView back;
    private ImageView give_tel;
    private EditText et_tel;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_give_decibel_tel;
    }

    @Override
    protected void initView() {
        star_details_fragment = findViewById(R.id.star_details_fragment);
        or_such_user = findViewById(R.id.or_such_user);
        attention = findViewById(R.id.attention);
        give_tel = findViewById(R.id.give_tel);
        et_tel = findViewById(R.id.et_tel);
        fans = findViewById(R.id.fans);
        back = findViewById(R.id.back);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
    }

    @Override
    protected void initData() {
        star_details_fragment.setOnClickListener(this);
        or_such_user.setOnClickListener(this);
        attention.setOnClickListener(this);
        fans.setOnClickListener(this);
        back.setOnClickListener(this);
        give_tel.setOnClickListener(this);

        manager = getSupportFragmentManager();
        suchFragment = new SuchFragment();  //搜素内容
        attentionFragment = new AttentionFragment();  //关注
        fansFragment = new FansFragment();   //粉丝
        transaction = manager.beginTransaction();
        transaction.add(R.id.star_details_fragment, suchFragment);
        transaction.add(R.id.star_details_fragment, attentionFragment);
        transaction.add(R.id.star_details_fragment, fansFragment);
        transaction.hide(attentionFragment);
        transaction.hide(fansFragment);
        transaction.commit();
        updateBottomChecked(0);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.or_such_user:
                updateBottomChecked(0);
                break;
            case R.id.attention:
                updateBottomChecked(1);
                break;
            case R.id.fans:
                updateBottomChecked(2);
                break;
            case R.id.back:
                finish();
                hideSoftInputView();
                break;
            case R.id.give_tel:
                //请输入手机号
                String tel = et_tel.getText().toString().trim();
                EventBus.getDefault().post(tel);   //手机号传递到Fragement
                hideSoftInputView();
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void updateBottomChecked(int position) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(suchFragment);
        transaction.hide(attentionFragment);
        transaction.hide(fansFragment);
        switch (position) {
            case 0:
                transaction.show(suchFragment);
                updateData(0);
                break;
            case 1:
                transaction.show(attentionFragment);
                updateData(1);
                break;
            case 2:
                transaction.show(fansFragment);
                updateData(2);
                break;
        }
        transaction.commit();
    }

    private void updateData(int position) {
        switch (position) {
            case 0:
                or_such_user.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                or_such_user.setTextSize(18);
                attention.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                attention.setTextSize(15);
                fans.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                fans.setTextSize(15);
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                break;
            case 1:
                or_such_user.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                or_such_user.setTextSize(15);
                attention.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                attention.setTextSize(18);
                fans.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                fans.setTextSize(15);
                v2.setVisibility(View.VISIBLE);
                v1.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                break;
            case 2:
                or_such_user.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                or_such_user.setTextSize(15);
                attention.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                attention.setTextSize(15);
                fans.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                fans.setTextSize(18);
                v3.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
                v1.setVisibility(View.GONE);
                break;
        }
    }

      /*  Bundle传值  A-F   Bundle bundle = new Bundle();
                         bundle.putParcelableArrayList("telGiveRespone", (ArrayList<? extends Parcelable>) telGiveRespone);
                        suchFragment.setArguments(bundle);

                        Bundle bundle = getArguments();
                        if(bundle != null){
                          list = bundle.getParcelableArrayList("telGiveRespone");}
                        */
}
