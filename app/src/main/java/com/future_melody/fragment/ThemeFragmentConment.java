package com.future_melody.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.future_melody.R;
import com.future_melody.activity.LoginActivity;
import com.future_melody.adapter.ThemeCommendRecommendAdapter;
import com.future_melody.adapter.ThemeDetailsAdapter;
import com.future_melody.base.BaseFullBottomSheetFragment;
import com.future_melody.common.CommonConst;
import com.future_melody.common.SPconst;
import com.future_melody.mode.CommentModel;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.HttpUtil;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.CommentListRequest;
import com.future_melody.net.request.DotPraiseRequest;
import com.future_melody.net.request.ReplyCommentRequest;
import com.future_melody.net.respone.CommentListNewRespone;
import com.future_melody.net.respone.DotPraiseResponse;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.ReplyCommentResponse;
import com.future_melody.receiver.ListenXingEventBus;
import com.future_melody.receiver.SendWebEventBus;
import com.future_melody.receiver.ThemeCommendNumEventBus;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.ReplyDialog;
import com.future_melody.utils.SPUtils;
import com.future_melody.utils.TipLinearUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.future_melody.common.CommonConst.userId;

/**
 * Author WZL
 * Date：2018/8/10 08
 * Notes:
 */
public class ThemeFragmentConment extends BaseFullBottomSheetFragment implements View.OnClickListener {
    private ThemeCommendRecommendAdapter adapter;
    private RecyclerView listview_comtent;
    private View dialogView;
    private EditText et_content;
    protected CompositeDisposable mCompositeDisposable;
    private List<CommentModel> list;
    private TextView text_comtent_num;
    private ImageView btn_finsh_comtent;
    private TextView theme_details_send;
    private String parentId;
    private LinearLayout input_et;
    private String specialid;
    private int commentCount;
    private ReplyDialog replyDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogView = inflater.inflate(R.layout.popupwindow_theme_conment, container, false);
        initView();
        return dialogView;
    }

    private void initView() {
//        initImmersionBar(R.color.white ,true);
        setBarDarkFont();
        listview_comtent = dialogView.findViewById(R.id.listview_comtent);
        et_content = dialogView.findViewById(R.id.et_content);
        text_comtent_num = dialogView.findViewById(R.id.text_comtent_num);
        btn_finsh_comtent = dialogView.findViewById(R.id.btn_finsh_comtent);
        theme_details_send = dialogView.findViewById(R.id.theme_details_send);
        input_et = dialogView.findViewById(R.id.input_et);
        theme_details_send.setOnClickListener(this);
        et_content.setOnClickListener(this);
        btn_finsh_comtent.setOnClickListener(this);
        initData();
    }

    private void initData() {
        InputFilter[] emoji = {CommonUtils.enmoji(getActivity())};
        et_content.setFilters(emoji);
        Bundle bundle = getArguments();
        specialid = bundle.getString("SpecialId");
        list = new LinkedList<>();
        if (SPUtils.getInstance().getString(SPconst.USER_ID) != null) {
            getCommentList(SPUtils.getInstance().getString(SPconst.USER_ID), specialid, 1, 20);
        }
        listview_comtent.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ThemeCommendRecommendAdapter(getActivity(), list);
        listview_comtent.setAdapter(adapter);
        adapter.setMusicClickListener(new ThemeCommendRecommendAdapter.MusicClickListener() {
            @Override
            public void onClick(int i, View view) {

                if (list.get(i).parentId != null) {
                    parentId = list.get(i).commentId;
                } else {
                    parentId = "";
                }
                LogUtil.e("onItemClick", i + "");
                LogUtil.e("onItemClick---nickname", list.get(i).nickname);
                //弹出输入框
                showReplayDialog("回复: " + list.get(i).nickname);

                //评论条目定位到输入框上面
                final int[] coord = new int[2];
                if (listview_comtent != null) {
                    view.getLocationOnScreen(coord);//获取当前被点击的条目在屏幕中  左上角的坐标  x  coord[0]   ,y  coord[1]
                }
                //延时300毫秒滑动   为了使键盘完全弹出后计算滑动高度
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int[] coord1 = new int[2];
                        input_et.getLocationOnScreen(coord1);//  获取输入框的总布局
                        int span = view.getHeight();//获取当前条目的高度
                        //滑动距离= 被点击view Y值 + 自身高度 - 输入框布局的Y值
                        listview_comtent.smoothScrollBy(coord[1] + span - coord1[1], 1000);
                    }
                }, 300);

            }


        });
        adapter.setThemeClickListener(new ThemeDetailsAdapter.ThemeClickListener() {
            @Override
            public void GetInfo(int i) {
                if (SPUtils.getInstance().getBoolean(SPconst.ISlogin)) {
                    CommentModel respone = list.get(i);
                    if (respone.isLike == 0) {
                        respone.isLike = 1;
                        respone.likeCount = respone.likeCount + 1;
                    } else {
                        respone.isLike = 0;
                        respone.likeCount = respone.likeCount + -1;
                    }
                    adapter.notifyDataSetChanged();
                    themedotpraise(respone.userId, 1, respone.commentId, specialid, userId());
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
            }
        });
    }

    public void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    //弹出输入框
    private void showReplayDialog(String hint) {
        replyDialog = new ReplyDialog(getActivity());
        replyDialog.setHintText(hint)
                .setOnBtnCommitClickListener(v -> {
                    // TODO: 2018/8/15/015 调用发送评论接口 replyDialog.getContent()为输入内容
                    if (SPUtils.getInstance().getBoolean(SPconst.ISlogin)) {
                        String trim = replyDialog.getContent();
                        if (TextUtils.isEmpty(trim)) {
                            Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            replycomment(trim, parentId, specialid, SPUtils.getInstance().getString(SPconst.USER_ID));
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra(CommonConst.ISFINISH, 1);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void getCommentList(String userId, String specialid, int pageNum, int pageSize) {
        addSubscribe(HttpUtil.getPHApis().commenNewtList(new CommentListRequest(userId, specialid, pageNum, pageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<CommentListNewRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<CommentListNewRespone>handleResult())
                .subscribeWith(new HttpSubscriber<CommentListNewRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                    }
                }) {
                    @Override
                    public void onNext(CommentListNewRespone commentListRespone) {
                        sendComNum(commentListRespone.count);
                        text_comtent_num.setText(commentListRespone.count + "条评论");
                        if (commentListRespone != null && commentListRespone.commentVoList.size() > 0) {
                            list.addAll(commentListRespone.commentVoList);
                            adapter.notifyDataSetChanged();
                        } else {
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    /**
     * 回复评论
     *
     * @param commentContent
     * @param parentId
     * @param spcialId
     * @param userId
     */
    private void replycomment(final String commentContent, String parentId, String spcialId, String userId) {
        showLoadingDialog();
        addSubscribe(HttpUtil.getPHApis().replycomment(new ReplyCommentRequest(commentContent, parentId, spcialId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<ReplyCommentResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ReplyCommentResponse>handleResult())
                .subscribeWith(new HttpSubscriber<ReplyCommentResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    public void onNext(ReplyCommentResponse ReplyCommentResponse) {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        replyDialog.dismiss();
                        dismissLoadingDialog();
                        hideSoftInputView();
                        et_content.getText().clear();
                        list.clear();
                        getCommentList(userId(), spcialId, 1, 20);
                    }
                }));
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.theme_details_send:
                if (SPUtils.getInstance().getBoolean(SPconst.ISlogin)) {
                    String trim = et_content.getText().toString().trim();
                    if (TextUtils.isEmpty(trim)) {
                        Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        replycomment(trim, parentId, specialid, SPUtils.getInstance().getString(SPconst.USER_ID));
                    }
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.et_content:
                showReplayDialog("说好听点,歌手需要你的鼓励");
                break;
            case R.id.btn_finsh_comtent:
                if (getBehavior() != null) {
                    getBehavior().setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    //评论点赞
    private void themedotpraise(final String beingUserId, int flag, String commentId, String specialId, String userId) {
        showLoadingDialog();
        addSubscribe(HttpUtil.getPHApis().dotpraise(new DotPraiseRequest(beingUserId, commentId, flag, specialId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<DotPraiseResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<DotPraiseResponse>handleResult())
                .subscribeWith(new HttpSubscriber<DotPraiseResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        TipLinearUtil.create(getActivity()).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(DotPraiseResponse DotPraiseResponse) {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                }));
    }

    private AlertDialog alertDialog;

    public void showLoadingDialog() {
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK)
                    return true;
                return false;
            }
        });
        alertDialog.show();
        alertDialog.setContentView(R.layout.loading_alert);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void dismissLoadingDialog() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void sendComNum(int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new ThemeCommendNumEventBus(position));
            }
        }).start();
    }
}

