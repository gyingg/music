package com.future_melody.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.adapter.TodaySuperuAdapter;
import com.future_melody.adapter.WeekSuperuAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.common.SPconst;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.AddFollowRequest;
import com.future_melody.net.request.CancelFollow;
import com.future_melody.net.request.WeekSuperuRequest;
import com.future_melody.net.respone.AddFollowRespone;
import com.future_melody.net.respone.CancelFollowRespone;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.WeekSuperuRespone;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.UmengUtils;

import java.util.List;

/**
 * Author WZL
 * Date：2018/5/15 32
 * Notes:一周最佳
 */
public class WeekSuperuFragment extends BaseFragment {

    private RecyclerView rec;
    private WeekSuperuAdapter weekSuperuAdapter;
    private ImageView img_today;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_week_super;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        rec = view.findViewById(R.id.rec);
        img_today = view.findViewById(R.id.img_today);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        String planetId = bundle.getString("planetId");
        weeksuperu(2, planetId, userId());
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void weeksuperu(final Integer category, String planetId, String userId) {
        addSubscribe(apis.weeksuperu(new WeekSuperuRequest(category, planetId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<List<WeekSuperuRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<WeekSuperuRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<WeekSuperuRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(List<WeekSuperuRespone> WeekSuperuRespone) {
                        weekSuperuAdapter = new WeekSuperuAdapter(mActivity, WeekSuperuRespone);
                        RequestOptions RequestOption = new RequestOptions();
                        RequestOption.placeholder(R.mipmap.moren);
                        Glide.with(mActivity)
                                .load(WeekSuperuRespone.get(0).headUrl)
                                .apply(RequestOption)
                                .into(img_today);
                        weekSuperuAdapter.setAttention(new WeekSuperuAdapter.attention() {
                            @Override
                            public void attention(boolean b, int i) {
                                if (b == true) {
                                    addFollowList(WeekSuperuRespone.get(i).userId, userId);  //添加关注
                                } else {
                                    offtFollowList(WeekSuperuRespone.get(i).userId, userId);   //取消关注
                                }
                            }
                        });
                        rec.setAdapter(weekSuperuAdapter);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }


    //添加关注
    private void addFollowList(final String bg_userid, String g_userid) {
        addSubscribe(apis.addlFollow(new AddFollowRequest(bg_userid, g_userid))
                .compose(RxHttpUtil.<FutureHttpResponse<AddFollowRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<AddFollowRespone>handleResult())
                .subscribeWith(new HttpSubscriber<AddFollowRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(AddFollowRespone AddFollowRespone) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    //取消关注
    private void offtFollowList(final String bg_userid, String g_userid) {
        addSubscribe(apis.cancelFollow(new CancelFollow(bg_userid, g_userid))
                .compose(RxHttpUtil.<FutureHttpResponse<CancelFollowRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<CancelFollowRespone>handleResult())
                .subscribeWith(new HttpSubscriber<CancelFollowRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(CancelFollowRespone CancelFollowRespone) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToFragment("WeekSuperuFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment("WeekSuperuFragment");
    }
}
