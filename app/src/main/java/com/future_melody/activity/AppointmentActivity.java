package com.future_melody.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.adapter.AppointmentsAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.SetAppointment;
import com.future_melody.net.request.StarAppointment;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.SetAdministrationRespone;
import com.future_melody.net.respone.StarAppointmentRespone;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author WZL
 * Date：2018/5/22 20
 * Notes: 任命
 */
public class AppointmentActivity extends BaseActivity implements OnLoadMoreListener {
    @BindView(R.id.text_title_left)
    TextView textTitleLeft;
    @BindView(R.id.ph_title_right)
    TextView phTitleRight;
    @BindView(R.id.appointment_list)
    RecyclerView appointmentList;
    @BindView(R.id.apppintment_et)
    EditText apppintmentEt;
    @BindView(R.id.ph_title_right_img)
    ImageView phTitleRightImg;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private AppointmentsAdapter appointmentAdapter;
    private List<StarAppointmentRespone> list;
    private int pageNum = 1;
    private int pageSize = 20;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_appointment;
    }

    @Override
    protected void initView() {
        setTitle("任命");
        setTitleLeft("取消");
        setTitleLeftColor(R.color.color_666666);
        setTitleLayoutColor(mActivity, R.color.white);
        setTitleColor(R.color.color_333333);
        phTitleRight.setText("确定");
        phTitleRight.setVisibility(View.VISIBLE);
        phTitleRight.setTextColor(getResources().getColor(R.color.color_666666));
        setBarColor(R.color.white, true);
        phTitleRightImg.setVisibility(View.GONE);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setEnableRefresh(false);
        appointmentList.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    protected void initData() {
        list = new LinkedList<>();
        list.clear();
        geList(pageNum, pageSize, userId(), apppintmentEt.getText().toString());
        apppintmentEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (TextUtils.isEmpty(apppintmentEt.getText().toString())) {
                        TipLinearUtil.create(mActivity).showTipMessage("搜索条件不能为空");
                    } else {
                        // 先隐藏键盘
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        list.clear();
                        geList(pageNum, pageSize, userId(), apppintmentEt.getText().toString());
                    }
                }
                return false;
            }
        });
        apppintmentEt.addTextChangedListener(new myTextWatcher());
    }

    private void geList(int pageNum, int pageSize, String userId, String text) {
        addSubscribe(apis.appointmentList(new StarAppointment(userId, text, pageNum, pageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<StarAppointmentRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<StarAppointmentRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<StarAppointmentRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                        refreshLayout.finishLoadMore(false);
                    }
                }) {
                    @Override
                    public void onNext(List<StarAppointmentRespone> starAppointmentRespones) {
                        refreshLayout.finishLoadMore();
                        if (starAppointmentRespones != null && starAppointmentRespones.size() > 0) {
                            list.addAll(starAppointmentRespones);
                            if (appointmentAdapter!=null){
                                appointmentAdapter.notifyDataSetChanged();
                            }
                            appointmentAdapter = new AppointmentsAdapter(mActivity, list);
                            appointmentList.setAdapter(appointmentAdapter);
                            appointmentAdapter.setAddClickListener(new AppointmentsAdapter.AddClickListener() {
                                @Override
                                public void onClick(int i) {
                                    StarAppointmentRespone respone = list.get(i);
                                    if (list.get(i).guardian_asteroid == 1) {
                                        addUser(userId(), 0, list.get(i).userid);
                                        respone.guardian_asteroid = 0;
                                    } else {
                                        addUser(userId(), 1, list.get(i).userid);
                                        respone.guardian_asteroid = 1;
                                    }
                                    appointmentAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @OnClick({R.id.text_title_left, R.id.ph_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_title_left:
                finish();
                break;
            case R.id.ph_title_right:
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        geList(pageNum, pageSize, userId(), apppintmentEt.getText().toString());
    }

    private class myTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            LogUtil.e("charSequence", "1");
            charSequence = charSequence;
            if (!TextUtils.isEmpty(charSequence)) {
                apppintmentEt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void addUser(String userId, int isAdd, String beUserId) {
        LogUtil.e("add", "add");
        addSubscribe(apis.setAppointment(new SetAppointment(userId, isAdd, beUserId))
                .compose(RxHttpUtil.<FutureHttpResponse<SetAdministrationRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<SetAdministrationRespone>handleResult())
                .subscribeWith(new HttpSubscriber<SetAdministrationRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        LogUtil.e("onError", exception + "");
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(SetAdministrationRespone setAdministrationRespone) {
                        TipLinearUtil.create(mActivity).showTipMessage(setAdministrationRespone.getMsg());
                        LogUtil.e("onNext", "onNext");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e("onComplete", "onComplete");
                    }
                }));
    }
}
