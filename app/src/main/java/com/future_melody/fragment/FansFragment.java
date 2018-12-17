package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.future_melody.R;
import com.future_melody.activity.GiveDecibelActivity;
import com.future_melody.adapter.FansFragmentAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MineReconmendFans;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineReconmendFansRespone;
import com.future_melody.utils.TipLinearUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Y on 2018/9/17.
 * 粉丝fragment
 */

public class FansFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    private SmartRefreshLayout refreshLayout;
    private int pageNum = 1;
    private int PageSize = 20;
    private List<MineReconmendFansRespone> list;
    private RecyclerView recycle;
    private FansFragmentAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fans;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recycle = view.findViewById(R.id.recycle);
        list = new LinkedList<>();
    }

    @Override
    protected void initData() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        recycle.setLayoutManager(new LinearLayoutManager(mActivity));
        getFansList(userId());
    }

    private void getFansList(String userId) {
        addSubscribe(apis.mineFans(new MineReconmendFans(userId, pageNum, PageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MineReconmendFansRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MineReconmendFansRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MineReconmendFansRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {


                    @Override
                    public void onNext(List<MineReconmendFansRespone> mineReconmendFansRespone) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        if (mineReconmendFansRespone != null && mineReconmendFansRespone.size() > 0) {
                            list.addAll(mineReconmendFansRespone);
                            adapter = new FansFragmentAdapter(mActivity, list);
                            adapter.setItemRec(new FansFragmentAdapter.itemRec() {
                                @Override
                                public void itemRec(int i) {
                                    MineReconmendFansRespone respone =list.get(i);
                                    Intent intent = new Intent(mActivity, GiveDecibelActivity.class);
                                    intent.putExtra("tel", respone.getUser_name());
                                    startActivity(intent);
                                    mActivity.finish();
                                }
                            });
                            recycle.setAdapter(adapter);
                        } else {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }

                    @Override
                    public void onComplete() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                }));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        list.clear();
        getFansList(userId());

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        refreshLayout.setNoMoreData(false);
        getFansList(userId());
    }

}
