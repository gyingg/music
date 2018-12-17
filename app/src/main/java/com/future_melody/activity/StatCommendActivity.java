package com.future_melody.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.future_melody.R;
import com.future_melody.adapter.StatCommendAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.mode.XingMusicSetTomModel;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.SetMusicTop;
import com.future_melody.net.request.XingMusicSetTop;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.SetMusicTopResopone;
import com.future_melody.net.respone.XingMusicSetTopResopone;
import com.future_melody.utils.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 星歌推荐
 */
public class StatCommendActivity extends BaseActivity implements View.OnClickListener, OnLoadMoreListener {
    private ListView listView_new_top;
    private ImageView back;
    private TextView ph_title_right_img;
    private SmartRefreshLayout refreshLayout;
    private int pageNum = 1;
    private int pageSize = 20;
    private TextView xingmuxic_num;
    private List<XingMusicSetTomModel> lists;
    private StatCommendAdapter adapter;
    private int rownum;
    private int size;
    private List<XingMusicSetTomModel> checkedList;
    ;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_stat_commend;
    }

    @Override
    protected void initView() {
        listView_new_top = findViewById(R.id.listView_new_top);
        refreshLayout = findViewById(R.id.refreshLayout);
        xingmuxic_num = findViewById(R.id.xingmuxic_num);
        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
    }

    @Override
    protected void initData() {
        lists = new LinkedList<>();
        checkedList = new LinkedList<>();
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setEnableRefresh(false);
        getList(userId(), pageNum, pageSize);
    }

    private void getList(String userId, int pageNum, int pageSize) {
        addSubscribe(apis.topList(new XingMusicSetTop(userId, pageNum, pageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<XingMusicSetTopResopone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<XingMusicSetTopResopone>handleResult())
                .subscribeWith(new HttpSubscriber<XingMusicSetTopResopone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                        refreshLayout.finishLoadMore(false);
                    }
                }) {

                    @Override
                    public void onNext(XingMusicSetTopResopone xingMusicSetTopResopone) {
                        refreshLayout.finishLoadMore();
                        rownum = xingMusicSetTopResopone.rownum;
                        if (rownum == 3) {
                            xingmuxic_num.setText(0 + "/1");
                            size = 1;
                        } else if (rownum == 2) {
                            size = 2;
                            xingmuxic_num.setText(0 + "/2");
                        } else if (rownum == 1) {
                            size = 3;
                            xingmuxic_num.setText(0 + "/3");
                        }
                        if (xingMusicSetTopResopone.users != null && xingMusicSetTopResopone.users.size() > 0) {
                            lists.addAll(xingMusicSetTopResopone.users);
                            adapter = new StatCommendAdapter(mActivity, lists);
                            listView_new_top.setAdapter(adapter);
                            adapter.setcheckboxClickListener(new StatCommendAdapter.checkboxClickListener() {
                                @Override
                                public void checked(int position) {
                                    checkedList.clear();
//                                    XingMusicSetTomModel model = lists.get(i);
////                                    if (checkedList.size() == 0) {
////                                        model.isCheck = true;
////                                    } else {
////                                    }
//                                    LogUtil.e("头部checkedList", checkedList.size() + "");
//                                    if (checkedList.size() < size) {
//                                        if (model.isCheck) {
//                                            model.isCheck = false;
//                                        } else {
//                                            model.isCheck = true;
//                                        }
//                                        adapter.notifyDataSetChanged();
//                                        if (rownum == 3) {
//                                            xingmuxic_num.setText(checkedList.size() + "/1");
//                                        } else if (rownum == 2) {
//                                            xingmuxic_num.setText(checkedList.size() + "/2");
//                                        } else if (rownum == 1) {
//                                            xingmuxic_num.setText(checkedList.size() + "/3");
//                                        }
//                                        LogUtil.e("checkedList", checkedList.size() + "");
//                                    } else {
//                                        toast("最多选择" + size + "首");
//                                    }

                                    if (lists.get(position).isCheck) {
                                        lists.get(position).isCheck = false;
                                        adapter.notifyDataSetChanged();
                                        xingmuxic_num.setText(String.format("%d/%d", getCheckedSize(lists), size));
                                        return;
                                    }
                                    if (getCheckedSize(lists) >= size) {
                                        Toast.makeText(mActivity, "最多选择" + size + "个", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    XingMusicSetTomModel model = lists.get(position);
                                    model.isCheck = !model.isCheck;
                                    xingmuxic_num.setText(String.format("%d/%d", getCheckedSize(lists), size));
                                    adapter.notifyDataSetChanged();
                                    for (XingMusicSetTomModel musicSetTomModel : lists) {
                                        if (musicSetTomModel.isCheck) {
                                            checkedList.add(musicSetTomModel);
                                        }
                                    }
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

    private int getCheckedSize(List<XingMusicSetTomModel> lists) {
        int size = 0;
        for (XingMusicSetTomModel model : lists) {
            if (model.isCheck) {
                size++;
            }
        }
        return size;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ph_title_right_img:
                if (checkedList.size() == 1) {
                    set(rownum, userId(), checkedList.get(0).musicid, "", "");
                } else if (checkedList.size() == 2) {
                    set(rownum, userId(), checkedList.get(0).musicid, checkedList.get(1).musicid, "");
                } else if (checkedList.size() == 3) {
                    set(rownum, userId(), checkedList.get(0).musicid, checkedList.get(1).musicid, checkedList.get(2).musicid);
                } else {
                    return;
                }
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        getList(userId(), pageNum, pageSize);
    }

    private void set(int rownum, String userid, String music_id1, String music_id2, String music_id3) {
        addSubscribe(apis.setMusicTop(new SetMusicTop(rownum, userid, music_id1, music_id2, music_id3))
                .compose(RxHttpUtil.<FutureHttpResponse<SetMusicTopResopone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<SetMusicTopResopone>handleResult())
                .subscribeWith(new HttpSubscriber<SetMusicTopResopone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                        refreshLayout.finishLoadMore(false);
                    }
                }) {

                    @Override
                    public void onNext(SetMusicTopResopone xingMusicSetTopResopone) {
                    }

                    @Override
                    public void onComplete() {
                        toast("推荐成功");
                        finish();
                    }
                }));
    }

}
