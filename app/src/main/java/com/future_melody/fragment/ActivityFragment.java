package com.future_melody.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.future_melody.R;
import com.future_melody.activity.X5WebviewNoTitleActivity;
import com.future_melody.adapter.MainActivityAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.MainActivityFragmentRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MainActivityFragmentRespone;
import com.future_melody.utils.TipLinearUtil;

import java.util.List;

/**
 * Created by Y on 2018/8/20.
 */

public class ActivityFragment extends BaseFragment {


    private RecyclerView rec;
    private MainActivityAdapter mainadapter;
    private String planetId;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        rec = view.findViewById(R.id.rec);
    }


    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        planetId = bundle.getString("planetId");
        getActive(planetId, userId());
        rec.setLayoutManager(new LinearLayoutManager(mActivity));
    }


    //获取活动
    private void getActive(final String bg_userid, String g_userid) {
        addSubscribe(apis.MainActivityFragment(new MainActivityFragmentRequest(bg_userid, g_userid))
                .compose(RxHttpUtil.<FutureHttpResponse<List<MainActivityFragmentRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<MainActivityFragmentRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<MainActivityFragmentRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(List<MainActivityFragmentRespone> mainActivityFragmentRespone) {
                        mainadapter = new MainActivityAdapter(mActivity, mainActivityFragmentRespone);
                        mainadapter.setItemRec(new MainActivityAdapter.itemRec() {
                            @Override
                            public void itemRec(int i) {
                                X5WebviewNoTitleActivity.startPHLoanWebActivity(mActivity, mainActivityFragmentRespone.get(i).activeTransferUrl);
                            }
                        });
                        rec.setAdapter(mainadapter);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

}
