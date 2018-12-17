package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.activity.ThemeDetailsActivity;
import com.future_melody.adapter.DiscussMyAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MyInformRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MyInformRespone2;
import com.future_melody.utils.UmengUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

/**
 * Created by Y on 2018/5/18.
 * 评论我的
 */

public class DiscussMyFragment extends BaseFragment {

    private RecyclerView recycle;
    private int PageSize = 20;
    private int pageNum = 1;
    private LinearLayout no_data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discuss_my;
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
        discussmy(pageNum, PageSize, 2, userId());
    }

    private void discussmy(int pageNum, int pageSize, int type, String userId) {
        addSubscribe(apis.pinglun(new MyInformRequest(pageNum, pageSize, type, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MyInformRespone2>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MyInformRespone2>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MyInformRespone2>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<MyInformRespone2> MyInformRespone) {
                        if (pageNum == 1 && MyInformRespone.size() <= 0) {
                            no_data.setVisibility(View.VISIBLE);
                            recycle.setVisibility(View.GONE);
                        } else {
                            no_data.setVisibility(View.GONE);
                            recycle.setVisibility(View.VISIBLE);
                            if (MyInformRespone != null && MyInformRespone.size() > 0) {
                                DiscussMyAdapter adapter = new DiscussMyAdapter(mActivity, MyInformRespone);
                                adapter.setItemRec(new DiscussMyAdapter.itemRec() {
                                    @Override
                                    public void itemRec(int i) {
                                        Intent intent = new Intent(mActivity, ThemeDetailsActivity.class);
                                        intent.putExtra("SpecialId", MyInformRespone.get(i).getToThingId());
                                        startActivity(intent);
                                    }
                                });
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
        UmengUtils.onResumeToFragment("DiscussMyFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("DiscussMyFragment");
    }
}
