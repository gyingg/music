package com.future_melody.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.future_melody.R;
import com.future_melody.adapter.SystemMessagesAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MyInformRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MyInformRespone1;
import com.future_melody.utils.UmengUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

/**
 * Created by Y on 2018/5/18.
 * 系统消息
 */

public class SystemMessagesFragment extends BaseFragment {
    private RecyclerView recycle;
    private LinearLayout no_data;
    private int PageSize = 20;
    private int pageNum = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_system_messages;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        recycle = view.findViewById(R.id.recycle);
        no_data = view.findViewById(R.id.no_data);
    }

    @Override
    protected void initData() {
        SystemMessages(pageNum, PageSize, 1, userId());
        recycle.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    private void SystemMessages(int pageNum, int pageSize, int type, String userId) {
        addSubscribe(apis.myinform(new MyInformRequest(pageNum, pageSize, type, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MyInformRespone1>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MyInformRespone1>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MyInformRespone1>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<MyInformRespone1> MyInformRespone1) {
                        if (pageNum == 1 && MyInformRespone1.size() <= 0) {
                            no_data.setVisibility(View.VISIBLE);
                            recycle.setVisibility(View.GONE);
                        } else {
                            no_data.setVisibility(View.GONE);
                            recycle.setVisibility(View.VISIBLE);
                            if (MyInformRespone1 != null && MyInformRespone1.size() > 0) {
                                SystemMessagesAdapter SystemMessagesAdapter = new SystemMessagesAdapter(mActivity, MyInformRespone1);
                                recycle.setAdapter(SystemMessagesAdapter);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("SystemMessagesFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("SystemMessagesFragment");
    }
}
