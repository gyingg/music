package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.future_melody.R;
import com.future_melody.activity.GiveDecibelActivity;
import com.future_melody.adapter.SuchFragmentAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.TelGiveRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.TelGiveRespone;
import com.future_melody.utils.passwordview.GiveDecibelNullTelDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Y on 2018/9/17.
 * 搜素全部用户
 */

public class SuchFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recycle;
    private int startRecord = 1;
    private int pageSize = 20;
    private List<TelGiveRespone> list;
    private SuchFragmentAdapter adapter;
    private String tel = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_such;
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
    }

    //EventBus获取Activity传递过来的内容
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        tel = event;
        telgive(tel, startRecord, pageSize);
    }

    //EventBus订阅和取消订阅
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void telgive(final String user_name, int startRecord, int pageSize) {
        addSubscribe(apis.inputpassword(new TelGiveRequest(user_name, startRecord, pageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<TelGiveRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<TelGiveRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<TelGiveRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        GiveDecibelNullTelDialog dialog = new GiveDecibelNullTelDialog(mActivity);
                        dialog.setMsg(exception.getMessage());
                        dialog.setCancelButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setDetermineButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }) {

                    @Override
                    public void onNext(List<TelGiveRespone> telGiveRespone) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        if (telGiveRespone != null && telGiveRespone.size() > 0) {
                            list.addAll(telGiveRespone);
                            adapter = new SuchFragmentAdapter(mActivity, telGiveRespone);
                            adapter.setItemRec(new SuchFragmentAdapter.itemRec() {
                                @Override
                                public void itemRec(int i) {
                                    TelGiveRespone respone = list.get(i);
                                    Intent intent = new Intent(mActivity, GiveDecibelActivity.class);
                                    intent.putExtra("tel", respone.user_name);
                                    startActivity(intent);
                                    mActivity.finish();
                                }
                            });
                            recycle.setAdapter(adapter);
                        } else {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            toast("请检查手机号是否正确呦~");
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
        startRecord = 1;
        list.clear();
        telgive(tel, startRecord, pageSize);

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        startRecord++;
        refreshLayout.setNoMoreData(false);
        telgive(tel, startRecord, pageSize);
    }
}
