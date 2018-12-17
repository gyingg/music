package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.activity.GiveDecibelActivity;
import com.future_melody.activity.ThemeDetailsActivity;
import com.future_melody.adapter.AttentionFragmentAdapter;
import com.future_melody.adapter.DiscussMyAdapter;
import com.future_melody.adapter.MineRecommendAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MineReconmendFollow;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineReconmendFansRespone;
import com.future_melody.net.respone.MineReconmendFollowRespone;
import com.future_melody.utils.TipLinearUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Y on 2018/9/17.
 * 关注fragment
 */

public class AttentionFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    private SmartRefreshLayout refreshLayout;
    private int PageSize = 20;
    private List<MineReconmendFollowRespone> list;
    private RecyclerView recycle;
    private int pageNum = 1;
    private AttentionFragmentAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attention;
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
        getFollowList(userId(), pageNum, PageSize);
    }


    private void getFollowList(String userId, int startRecord, int pageSize) {
        addSubscribe(apis.mineFollow(new MineReconmendFollow(userId, startRecord, pageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MineReconmendFollowRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MineReconmendFollowRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MineReconmendFollowRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        if (exception.getMessage() != null) {
                            TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                        }
                    }
                }) {
                    @Override
                    public void onNext(final List<MineReconmendFollowRespone> mineReconmendFollowRespone) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        if (mineReconmendFollowRespone != null && mineReconmendFollowRespone.size() > 0) {
                            list.addAll(mineReconmendFollowRespone);
                            adapter = new AttentionFragmentAdapter(mActivity, list);
                            adapter.setItemRec(new AttentionFragmentAdapter.itemRec() {
                                @Override
                                public void itemRec(int i) {
                                    MineReconmendFollowRespone respone =list.get(i);
                                    Intent intent = new Intent(mActivity, GiveDecibelActivity.class);
                                    intent.putExtra("tel",respone.getUser_name());
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
        getFollowList(userId(), pageNum, PageSize);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        refreshLayout.setNoMoreData(false);
        getFollowList(userId(), pageNum, PageSize);
    }

}
