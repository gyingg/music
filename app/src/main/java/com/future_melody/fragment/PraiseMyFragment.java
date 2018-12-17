package com.future_melody.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.adapter.PraiseMyAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MyInformRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MyInformRespone2;
import com.future_melody.net.respone.MyInformRespone3;
import com.future_melody.utils.UmengUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

/**
 * Created by Y on 2018/5/18.
 * 赞我的
 */

public class PraiseMyFragment extends BaseFragment {
    private RecyclerView recycle;
    private int PageSize = 20;
    private int pageNum = 1;
    private LinearLayout no_data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_praise_my;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        recycle = view.findViewById(R.id.recycle);
        no_data = view.findViewById(R.id.no_data);
        recycle.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    protected void initData() {
        praisemy(pageNum, PageSize, 3, userId());
    }

    private void praisemy(int pageNum, int pageSize, int type, String userId) {
        addSubscribe(apis.zan(new MyInformRequest(pageNum, pageSize, type, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MyInformRespone3>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MyInformRespone3>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MyInformRespone3>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<MyInformRespone3> MyInformRespone) {
                        if (pageNum == 1 && MyInformRespone.size() <= 0) {
                            no_data.setVisibility(View.VISIBLE);
                            recycle.setVisibility(View.GONE);
                        } else {
                            no_data.setVisibility(View.GONE);
                            recycle.setVisibility(View.VISIBLE);
                            if (MyInformRespone != null && MyInformRespone.size() > 0) {
                            PraiseMyAdapter adapter = new PraiseMyAdapter(mActivity, MyInformRespone);
                            recycle.setAdapter(adapter);
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
        UmengUtils.onResumeToFragment("PraiseMyFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("PraiseMyFragment");
    }
}
