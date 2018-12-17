package com.future_melody.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.adapter.ThemeDetailsAdapter;
import com.future_melody.adapter.ThemeDetailsMusicAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.mode.RecommendSpecialVoListBean;
import com.future_melody.mode.Recommend_Theme_Bean;
import com.future_melody.mode.ThemeDetailsMusicBean;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.AddFollowRequest;
import com.future_melody.net.request.CancelFollow;
import com.future_melody.net.request.CommentList;
import com.future_melody.net.request.DotPraiseRequest;
import com.future_melody.net.request.ReplyCommentRequest;
import com.future_melody.net.request.ShareTheme;
import com.future_melody.net.request.ThemeDetails;
import com.future_melody.net.respone.AddFollowRespone;
import com.future_melody.net.respone.AttentionThemeRespone;
import com.future_melody.net.respone.CancelFollowRespone;
import com.future_melody.net.respone.CommentListRespone;
import com.future_melody.net.respone.DotPraiseResponse;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.MineReconmendThemeRespone;
import com.future_melody.net.respone.ReplyCommentResponse;
import com.future_melody.net.respone.ShareThemeRespone;
import com.future_melody.net.respone.ThemeDetailsRespone;
import com.future_melody.receiver.FollowsThemeEventBus;
import com.future_melody.receiver.ListenXingMusicEventBus;
import com.future_melody.receiver.MusicIsLikeEventBus;
import com.future_melody.receiver.ThemeEventBus;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author WZL
 * Date：2018/5/10 05
 * Notes: 动态详情
 */
public class ThemeDetailsActivity extends BaseActivity implements View.OnFocusChangeListener,
        AdapterView.OnItemClickListener, TextWatcher, View.OnLayoutChangeListener,
        View.OnClickListener, OnLoadMoreListener, OnRefreshListener {

    private String TAG = "ThemeDetailsActivity";
    @BindView(R.id.ph_title_right)
    TextView phTitleRight;
    @BindView(R.id.ph_title_right_img)
    ImageView phTitleRightImg;
    @BindView(R.id.listview_commend)
    ListView listviewCommend;
    @BindView(R.id.theme_details_share)
    ImageView themeDetailsShare;
    @BindView(R.id.input_et)
    LinearLayout inputEt;
    @BindView(R.id.theme_details_send)
    TextView themeDetailsSend;
    @BindView(R.id.et_content)
    EditText etContent;

    private SmartRefreshLayout refreshLayout;
    private ImageView details_bg;
    private TextView details_title;
    private TextView details_context;

    private CircleImageView theme_details_userimg;
    private TextView details_username;
    private TextView details_userfrom;
    private TextView theme_follow;
    private RecyclerView details_music_list;
    private ThemeDetailsAdapter conmendAdapter;
    private ThemeDetailsMusicAdapter musicAdapter;
    private View headview;
    private String SpecialId;
    private String parentId = "";
    private List<ThemeDetailsMusicBean> musicBeanList;
    private List<CommentListRespone> list;
    private int keywordHeight;
    private CharSequence content;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private String beUserId;
    private boolean isFollows;
    private int pageNum = 1;
    private int pageSize = 20;
    private MineReconmendThemeRespone mineThemeRespone;
    private RecommendSpecialVoListBean Findbean;
    private AttentionThemeRespone attentionThemeRespone;
    private Recommend_Theme_Bean useerTheme;
    private int position;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_theme_details;
    }

    @Override
    protected void initView() {
        setBarColor(R.color.white, true);
        setBlackBackble();
        setTitle("详情");
        setTitleColor(R.color.color_333333);
        setTitleLayoutColor(mActivity, R.color.white);
        headview = View.inflate(mActivity, R.layout.headview_theme_details, null);
        keywordHeight = CommonUtils.getWindowHeight(mActivity) / 4;
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        SpecialId = intent.getStringExtra("SpecialId");
        mineThemeRespone = intent.getParcelableExtra(CommonConst.MINE_THEME);
        Findbean = intent.getParcelableExtra(CommonConst.FIND_THEME);
        attentionThemeRespone = intent.getParcelableExtra(CommonConst.FOLLOWS_THEME);
        useerTheme = intent.getParcelableExtra(CommonConst.USER_THEME);
        position = intent.getIntExtra("position", -1);
        musicBeanList = new LinkedList<>();
        list = new LinkedList<>();
//        getDeatails(userId, SpecialId);
//        getCommentList(userId, SpecialId, pageNum, pageSize);
        conmendAdapter = new ThemeDetailsAdapter(mActivity, list);
        listviewCommend.setAdapter(conmendAdapter);
        listviewCommend.addHeaderView(headview, null, false);
        refreshLayout = findViewById(R.id.refreshLayout);
        details_bg = headview.findViewById(R.id.details_bg);
        details_title = headview.findViewById(R.id.details_title);
        details_context = headview.findViewById(R.id.details_context);
        theme_details_userimg = headview.findViewById(R.id.theme_details_userimg);
        details_username = headview.findViewById(R.id.details_username);
        details_userfrom = headview.findViewById(R.id.details_userfrom);
        theme_follow = headview.findViewById(R.id.theme_follow);
        details_music_list = headview.findViewById(R.id.details_music_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        details_music_list.setLayoutManager(layoutManager);
        details_music_list.setNestedScrollingEnabled(true);
        listviewCommend.setOnItemClickListener(this);
        listviewCommend.addOnLayoutChangeListener(this);
        etContent.addTextChangedListener(this);
        phTitleRightImg.setOnClickListener(this);
        themeDetailsShare.setOnClickListener(this);
        themeDetailsSend.setOnClickListener(this);
        theme_follow.setOnClickListener(this);
        theme_details_userimg.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        InputFilter[] emoji = {CommonUtils.enmoji(mActivity)};
        etContent.setFilters(emoji);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ListenXingMusicEventBus even) {
        if (musicAdapter != null) {
            musicAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void showReply() {
        InputMethodManager manager = ((InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void getDeatails(String userId, String specialid) {
        showLoadingDialog();
        addSubscribe(apis.themeDetails(new ThemeDetails(specialid, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<ThemeDetailsRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ThemeDetailsRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ThemeDetailsRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(ThemeDetailsRespone themeDetailsRespone) {
                        musicBeanList.clear();
                        Glide.with(mActivity).load(themeDetailsRespone.specialPictureUrl).into(details_bg);
                        details_title.setText(themeDetailsRespone.specialTitle);
                        details_context.setText(themeDetailsRespone.specialDescription);
                        Glide.with(mActivity).load(themeDetailsRespone.userHeadUrl).into(theme_details_userimg);
                        details_username.setText(themeDetailsRespone.nickname);
                        details_userfrom.setText(themeDetailsRespone.planetName);
                        beUserId = themeDetailsRespone.userId;
                        if (userId().equals(beUserId)) {
                            theme_follow.setVisibility(View.GONE);
                        } else {
                            theme_follow.setVisibility(View.VISIBLE);
                        }
                        if (themeDetailsRespone.isAttention == 0) {
                            theme_follow.setText("关注");
                            isFollows = false;
                        } else {
                            theme_follow.setText("已关注");
                            isFollows = true;
                        }
                        if (themeDetailsRespone.musicVoList != null) {
                            songInfos.clear();
                            for (ThemeDetailsMusicBean respone : themeDetailsRespone.musicVoList) {
                                SongInfo model = new SongInfo();
                                TempInfo tempInfo = new TempInfo();
                                LogUtil.e("lyrics", respone.lyrics);
                                if (respone.lyrics != null && !TextUtils.isEmpty(respone.lyrics)) {
                                    tempInfo.setTemp_1(respone.lyrics);
                                }
                                LogUtil.e("url", respone.musicName);
                                tempInfo.setTemp_2(respone.isLike + "");
                                tempInfo.setTemp_3(respone.userId);
                                model.setTempInfo(tempInfo);
                                model.setSongUrl(respone.musicPath);
                                model.setSongCover(respone.coverUrl);
                                model.setSongId(respone.musicId);
                                model.setArtist(respone.singerName);
                                if (TextUtils.isEmpty(respone.musicName)) {
                                    model.setSongName("<未知>");
                                } else {
                                    model.setSongName(respone.musicName);
                                }
                                songInfos.add(model);
                            }
                            LogUtil.e("musicVoList", themeDetailsRespone.musicVoList.size() + "");
                            musicBeanList.addAll(themeDetailsRespone.musicVoList);
                            musicAdapter = new ThemeDetailsMusicAdapter(mActivity, themeDetailsRespone.musicVoList, songInfos);
                            details_music_list.setAdapter(musicAdapter);
                            musicAdapter.setMusicClickListener(new ThemeDetailsMusicAdapter.MusicClickListener() {
                                @Override
                                public void onClick(int i) {
                                    if (isLogin()) {
                                        ThemeDetailsMusicBean bean = musicBeanList.get(i);
                                        LogUtil.e("bean.isLike0", bean.isLike + "");
                                        if (bean.isLike == 0) {
                                            bean.isLike = 1;
                                            sendIsLike("1");
                                            bean.likeCount = bean.likeCount + 1;
                                            if (mineThemeRespone != null) {
                                                mineThemeRespone.likesnumber = mineThemeRespone.likesnumber + 1;
                                                sendEvent(mineThemeRespone, position);
                                            }
                                            if (Findbean != null) {
                                                Findbean.setLikeCount(Findbean.getLikeCount() + 1);
                                                sendEvent(Findbean, position);
                                            }
                                            if (attentionThemeRespone != null) {
                                                attentionThemeRespone.likeCount = attentionThemeRespone.likeCount + 1;
                                                sendFollowsEvent(attentionThemeRespone, position);
                                            }
                                            if (useerTheme != null) {
                                                useerTheme.likeCount = useerTheme.likeCount + 1;
                                                sendFollowsEvent(useerTheme, position);
                                            }
                                        } else {
                                            bean.isLike = 0;
                                            sendIsLike("0");
                                            bean.likeCount = bean.likeCount + -1;
                                            if (mineThemeRespone != null) {
                                                mineThemeRespone.likesnumber = mineThemeRespone.likesnumber - 1;
                                                sendEvent(mineThemeRespone, position);
                                            }
                                            if (Findbean != null) {
                                                Findbean.setLikeCount(Findbean.getLikeCount() - 1);
                                                sendEvent(Findbean, position);
                                            }
                                            if (attentionThemeRespone != null) {
                                                attentionThemeRespone.likeCount = attentionThemeRespone.likeCount - 1;
                                                sendFollowsEvent(attentionThemeRespone, position);
                                            }

                                        }
                                        musicAdapter.notifyDataSetChanged();
                                        dotpraise(beUserId, 0, bean.musicId, bean.musicName, bean.coverUrl, SpecialId, userId());
                                    } else {
                                        Intent intent = new Intent(mActivity, LoginActivity.class);
                                        intent.putExtra(CommonConst.ISFINISH, 1);
                                        startActivity(intent);
                                    }
                                }
                            });
                            musicAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                }));
    }

    private void sendIsLike(String isLike) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isLike != null) {
                    EventBus.getDefault().postSticky(new MusicIsLikeEventBus(isLike));
                }
            }
        }).start();
    }

    private void getCommentList(String userId, String specialid, int pageNum, int pageSize) {
        addSubscribe(apis.commentList(new CommentList(userId, specialid, pageNum, pageSize))
                .compose(RxHttpUtil.<FutureHttpResponse<List<CommentListRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<CommentListRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<CommentListRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        refreshLayout.finishRefresh(false);
                        refreshLayout.finishLoadMore(false);
                        toast(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(List<CommentListRespone> commentListRespone) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        if (commentListRespone != null && commentListRespone.size() > 0) {
                            list.addAll(commentListRespone);
                            conmendAdapter.notifyDataSetChanged();
                        } else {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }
                        conmendAdapter.setThemeClickListener(new ThemeDetailsAdapter.ThemeClickListener() {
                            @Override
                            public void GetInfo(int i) {
                                if (isLogin()) {
                                    CommentListRespone respone = list.get(i);
                                    if (respone.isLike == 0) {
                                        respone.isLike = 1;
                                        respone.likeCount = respone.likeCount + 1;
                                    } else {
                                        respone.isLike = 0;
                                        respone.likeCount = respone.likeCount + -1;
                                    }
                                    conmendAdapter.notifyDataSetChanged();
                                    themedotpraise(respone.userId, 1, respone.commentId, SpecialId, userId());
                                } else {
                                    Intent intent = new Intent(mActivity, LoginActivity.class);
                                    intent.putExtra(CommonConst.ISFINISH, 1);
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    private void getShareInfo() {
        addSubscribe(apis.sharTheme(new ShareTheme(SpecialId))
                .compose(RxHttpUtil.<FutureHttpResponse<ShareThemeRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ShareThemeRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ShareThemeRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ShareThemeRespone shareThemeRespone) {
                        share(shareThemeRespone.specialTitle, shareThemeRespone.specialContent, shareThemeRespone.specialShareUrl, shareThemeRespone.pictureUrl);
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        LogUtil.e("onFocusChange", b + "");
        if (b) {
            showReply();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        etContent.setFocusable(true);
        etContent.setFocusableInTouchMode(true);
        etContent.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) etContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(etContent, 0);
        if (list.get(i - 1).parentId != null) {
            parentId = list.get(i - 1).commentId;
        } else {
            parentId = "";
        }
        LogUtil.e("onItemClick", i + "");
        LogUtil.e("onItemClick---nickname", list.get(i - 1).nickname);
        etContent.setHint("回复: " + list.get(i - 1).nickname);


        //评论条目定位到输入框上面
        final int[] coord = new int[2];
        if (listviewCommend != null) {
            view.getLocationOnScreen(coord);//获取当前被点击的条目在屏幕中  左上角的坐标  x  coord[0]   ,y  coord[1]
        }
        //延时300毫秒滑动   为了使键盘完全弹出后计算滑动高度
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] coord1 = new int[2];
                inputEt.getLocationOnScreen(coord1);//  获取输入框的总布局
                int span = view.getHeight();//获取当前条目的高度
                //滑动距离= 被点击view Y值 + 自身高度 - 输入框布局的Y值
                listviewCommend.smoothScrollBy(coord[1] + span - coord1[1], 1000);
            }
        }, 300);

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
        addSubscribe(apis.replycomment(new ReplyCommentRequest(commentContent, parentId, spcialId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<ReplyCommentResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ReplyCommentResponse>handleResult())
                .subscribeWith(new HttpSubscriber<ReplyCommentResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog();
                        toast(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ReplyCommentResponse ReplyCommentResponse) {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        toast("评论成功");
                        hideSoftInputView();
                        etContent.getText().clear();
                        list.clear();
                        getCommentList(userId(), SpecialId, 1, 20);
                        if (mineThemeRespone != null) {
                            mineThemeRespone.commentsnumber = mineThemeRespone.commentsnumber + 1;
                            sendEvent(mineThemeRespone, position);
                        }
                        if (Findbean != null) {
                            Findbean.setCommentCount(Findbean.getCommentCount() + 1);
                            sendEvent(Findbean, position);
                        }
                        if (attentionThemeRespone != null) {
                            attentionThemeRespone.commentCount = attentionThemeRespone.commentCount + 1;
                            sendFollowsEvent(attentionThemeRespone, position);
                        }
                        if (useerTheme != null) {
                            useerTheme.commentCount = useerTheme.commentCount + 1;
                            sendFollowsEvent(useerTheme, position);
                        }
                    }
                }));
    }

    private void sendEvent(Object respone, int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (respone != null) {
                    EventBus.getDefault().post(new ThemeEventBus(respone, position));
                }
            }
        }).start();
    }

    private void sendFollowsEvent(Object respone, int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (respone != null) {
                    EventBus.getDefault().post(new FollowsThemeEventBus(respone, position));
                }
            }
        }).start();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        LogUtil.e("beforeTextChanged", charSequence + "");
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!TextUtils.isEmpty(charSequence)) {
            themeDetailsSend.setVisibility(View.VISIBLE);
            themeDetailsShare.setVisibility(View.GONE);
        } else {
            themeDetailsShare.setVisibility(View.VISIBLE);
            themeDetailsSend.setVisibility(View.GONE);
        }
        content = charSequence;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        LogUtil.e("afterTextChanged", editable + "");
    }

    @Override
    public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keywordHeight)) {
            LogUtil.i("onLayoutChange", "监听到软键盘弹起...");
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keywordHeight)) {
            LogUtil.i("onLayoutChange", "监听到软件盘关闭...");
            if (TextUtils.isEmpty(content)) {
                parentId = "";
                etContent.setHint("说好听点，歌手需要你的鼓励");
            }
            etContent.clearFocus();
        }
    }

//    http://192.168.2.8:8080/share/index.html#/themeDeatil

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        LogUtil.e(TAG, "hideSoftInputView");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 分享
     */
    private void share(String title, String description, String url, String img) {
//        //分享的图片
        UMImage thumb = new UMImage(this, img);
//        //分享链接
        LogUtil.e("url", url);
        LogUtil.e("img", img);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述
//        //分享的图片
//        UMImage thumb = new UMImage(this, "http://img.zcool.cn/community/01635d571ed29832f875a3994c7836.png@900w_1l_2o_100sh.jpg");
////分享链接 必须以http开头
//        UMWeb web = new UMWeb("http://192.168.2.8:8080/share/index.html#/themeDeatil");
//        web.setTitle(title);//标题
//        web.setThumb(thumb);  //缩略图
//        web.setDescription(description);//描述
        new ShareAction(this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        toast("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        toast("分享失败");
                        LogUtil.e(TAG, "onError");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        toast("分享失败");
                        LogUtil.e(TAG, "onCancel");
                    }
                }).open();
    }


    //不要忘记重写分享成功后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        LogUtil.e(TAG, "onActivityResult");
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e(TAG, "onResume");
        hideSoftInputView();
        getDeatails(userId(), SpecialId);
        getCommentList(userId(), SpecialId, pageNum, pageSize);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ph_title_right_img:
                break;
            case R.id.theme_details_share:
                //分享
                if (isLogin()) {
                    getShareInfo();
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.theme_details_send:
                //评论
                if (isLogin()) {
                    String trim = etContent.getText().toString().trim();
                    replycomment(trim, parentId, SpecialId, userId());

                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.theme_follow:
                //关注
                LogUtil.e("是否登录", isLogin() + "");
                if (isLogin()) {
                    if (isFollows) {
                        theme_follow.setText("关注");
                        offtFollowList(beUserId, userId());
                    } else {
                        theme_follow.setText("已关注");
                        addFollowList(beUserId, userId());
                    }
                    isFollows = !isFollows;
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra(CommonConst.ISFINISH, 1);
                    startActivity(intent);
                }
                break;
            case R.id.theme_details_userimg:
                intent = new Intent(mActivity, UserInfoActivity.class);
                intent.putExtra("userId", beUserId);
                startActivity(intent);
                break;
        }
    }

    //点赞
    private void dotpraise(final String beingUserId, int flag, String musicId, String musicName, String musicPicture, String specialId, String userId) {
        addSubscribe(apis.dotpraise(new DotPraiseRequest(beingUserId, flag, musicId, musicName, musicPicture, specialId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<DotPraiseResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<DotPraiseResponse>handleResult())
                .subscribeWith(new HttpSubscriber<DotPraiseResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(DotPraiseResponse DotPraiseResponse) {

                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    //评论点赞
    private void themedotpraise(final String beingUserId, int flag, String commentId, String specialId, String userId) {
        addSubscribe(apis.dotpraise(new DotPraiseRequest(beingUserId, commentId, flag, specialId, userId))
                .compose(RxHttpUtil.<FutureHttpResponse<DotPraiseResponse>>rxSchedulerHelper())
                .compose(RxHttpUtil.<DotPraiseResponse>handleResult())
                .subscribeWith(new HttpSubscriber<DotPraiseResponse>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {
                    @Override
                    public void onNext(DotPraiseResponse DotPraiseResponse) {

                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        ++pageNum;
        getCommentList(userId(), SpecialId, pageNum, pageSize);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        pageNum = 1;
        songInfos.clear();
        list.clear();
        refreshLayout.setNoMoreData(false);
        getCommentList(userId(), SpecialId, pageNum, pageSize);
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
}
