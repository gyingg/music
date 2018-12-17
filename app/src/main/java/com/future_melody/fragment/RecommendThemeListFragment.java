package com.future_melody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.future_melody.R;
import com.future_melody.activity.LoginActivity;
import com.future_melody.adapter.RecommendThemeListAdapter;
import com.future_melody.base.BaseFragment;
import com.future_melody.bean.ShareItemBean;
import com.future_melody.common.CommonConst;
import com.future_melody.mode.RemendMusicNewModle;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.RemmendNewThemeRequest;
import com.future_melody.net.request.ShareSuccsecRequest;
import com.future_melody.net.request.ThemePickRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.RemmendNewThemeRespone;
import com.future_melody.net.respone.ShareSuccsecRespone;
import com.future_melody.net.respone.ThemePickRespone;
import com.future_melody.utils.AdtailPopWindow;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.ImgUtils;
import com.future_melody.utils.LogUtil;
import com.future_melody.utils.TipLinearUtil;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.aidl.model.TempInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/9/29 32
 * Notes:
 */
public class RecommendThemeListFragment extends BaseFragment implements OnViewPagerListener, AdtailPopWindow.OnItemClickListener {
    private String TAG = "RecommendThemeListFragment";
    private RecyclerView recommend_rv;
    private RecommendThemeListAdapter adapter;
    private ThemeFragmentConment fragmentConment;
    private String SpecialId;

    private AdtailPopWindow adtailPopWindow;
    private List<ShareItemBean> list;
    private String shareurl;
    private String shareSpId;
    private int sharePosition;
    private UMImage image;
    private int isLike;
    private int likeCount;
    private int shareCount;
    private int commentCount;
    private ViewPagerLayoutManager mLayoutManager;
    private List<RemmendNewThemeRespone> respones;
    private ArrayList<SongInfo> songInfos = new ArrayList<>();
    private int mPosition = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend_theme_list;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        recommend_rv = view.findViewById(R.id.recommend_rv);
    }

    @Override
    protected void initData() {
        respones = new LinkedList<>();
        mLayoutManager = new ViewPagerLayoutManager(mActivity, OrientationHelper.VERTICAL);
        mLayoutManager.setOnViewPagerListener(this);
        getInfo();
        list = new ArrayList<>();
        list.add(new ShareItemBean(R.mipmap.locel_img, "保存图片"));
        list.add(new ShareItemBean(R.mipmap.weixin, "微信"));
        list.add(new ShareItemBean(R.mipmap.friends, "朋友圈"));
        list.add(new ShareItemBean(R.mipmap.weibo, "微博"));
        list.add(new ShareItemBean(R.mipmap.qq, "QQ"));
        adtailPopWindow = new AdtailPopWindow(mActivity);//创建popwindow
        adtailPopWindow.setTitle("分享到");
        adtailPopWindow.setList(list);
        adtailPopWindow.setOnItemClickListener(this);
    }

    private void getInfo() {
        addSubscribe(apis.remend_new_theme(new RemmendNewThemeRequest(userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<List<RemmendNewThemeRespone>>>rxSchedulerHelper())
                .compose(RxHttpUtil.<List<RemmendNewThemeRespone>>handleResult())
                .subscribeWith(new HttpSubscriber<List<RemmendNewThemeRespone>>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                    }
                }) {
                    @Override
                    public void onNext(List<RemmendNewThemeRespone> remmendNewThemeRespone) {
                        respones.clear();
                        if (remmendNewThemeRespone.size() > 0) {
                            respones.addAll(remmendNewThemeRespone);
                            adapter = new RecommendThemeListAdapter(mActivity, respones);
                            recommend_rv.setLayoutManager(mLayoutManager);
                            recommend_rv.setAdapter(adapter);
                            adapter.ShareClickListener(new RecommendThemeListAdapter.ThemeClickListener() {
                                @Override
                                public void set(int i) {
                                    if (isLogin()) {
                                        RemmendNewThemeRespone respone = remmendNewThemeRespone.get(i);
                                        shareurl = respone.special.shareSpecialPicture;
                                        shareSpId = respone.special.specialid;
                                        sharePosition = i;
                                        adtailPopWindow.setImg(shareurl);  //设置图片
                                        initShare(shareurl);
                                        adtailPopWindow.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.TOP, 0, 0);
                                    } else {
                                        Intent intent = new Intent(mActivity, LoginActivity.class);
                                        intent.putExtra(CommonConst.ISFINISH, 1);
                                        startActivity(intent);
                                    }
                                }
                            });
                            adapter.CommendClickListener(new RecommendThemeListAdapter.ThemeClickListener() {
                                @Override
                                public void set(int i) {
                                    RemmendNewThemeRespone respone = remmendNewThemeRespone.get(i);
                                    SpecialId = respone.special.specialid;
                                    showPopWindow(SpecialId);
                                }
                            });
                            adapter.PickClickListener(new RecommendThemeListAdapter.ThemeClickListener() {
                                @Override
                                public void set(int i) {
                                    if (isLogin()) {
                                        RemmendNewThemeRespone respone = remmendNewThemeRespone.get(i);
                                        themePick(respone.special.specialid, i);
                                    } else {
                                        Intent intent = new Intent(mActivity, LoginActivity.class);
                                        intent.putExtra(CommonConst.ISFINISH, 1);
                                        startActivity(intent);
                                    }
                                }
                            });
//                            isLike = respone.isLike;
//                            likeCount = respone.likeCount;
//                            shareCount = respone.shareCount;
//                            commentCount = respone.commentCount;
//                            SpecialId = respone.special.specialid;
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onInitComplete() {
        LogUtil.e(TAG, "onInitComplete");
    }

    @Override
    public void onPageRelease(boolean isNext, int position) {
        LogUtil.e(TAG, "onPageRelease:" + "释放位置:" + position + " 下一页:" + isNext);
    }

    @Override
    public void onPageSelected(int position, boolean isBottom) {
        LogUtil.e(TAG, "onPageSelected:" + "选中位置:" + position + "  是否是滑动到底部:" + isBottom);
        if (mPosition == position) {
            LogUtil.e(TAG, "没有完全滑动" + mPosition);
        } else {
            mPosition = position;
            LogUtil.e(TAG, "完全滑动" + mPosition);
            songInfos.clear();
            RemmendNewThemeRespone respone = respones.get(mPosition);
            RemendMusicNewModle musicModel = respone.musicList.get(0);
            SongInfo model = new SongInfo();
            TempInfo tempInfo = new TempInfo();
            LogUtil.e("adapter==========", mPosition + "");
            if (musicModel.lyrics != null && !TextUtils.isEmpty(musicModel.lyrics)) {
                tempInfo.setTemp_1(musicModel.lyrics);
            }
            LogUtil.e("url", musicModel.musicName);
            tempInfo.setTemp_2(musicModel.isLike + "");
            tempInfo.setTemp_3(musicModel.userId);
            tempInfo.setTemp_7("themeMusic");
            model.setTempInfo(tempInfo);
            model.setSongUrl(musicModel.musicPath);
            model.setSongCover(musicModel.coverUrl);
            model.setSongId(musicModel.musicId);
            model.setArtist(musicModel.singerName);
            if (TextUtils.isEmpty(musicModel.musicName)) {
                model.setSongName("<未知>");
            } else {
                model.setSongName(musicModel.musicName);
            }
            songInfos.add(model);
            MusicManager.get().playMusic(songInfos, 0);
        }

    }

    //主题点赞
    public void themePick(String specialId, int i) {
        addSubscribe(apis.theme_pick(new ThemePickRequest(specialId, userId()))
                .compose(RxHttpUtil.<FutureHttpResponse<ThemePickRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ThemePickRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ThemePickRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ThemePickRespone themePickRespone) {
                    }

                    @Override
                    public void onComplete() {
                        RemmendNewThemeRespone respone = respones.get(i);
                        if (respone.isLike == 0) {
                            respone.isLike = 1;
                            respone.likeCount = respone.likeCount + 1;
                        } else {
                            respone.isLike = 0;
                            respone.likeCount = respone.likeCount - 1;
                        }
                        adapter.notifyDataSetChanged();
                        dismissLoadingDialog();
                    }
                }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
    }

    private void initShare(String url) {
/*        //分享的图片
        UMImage image = new UMImage(this, pictureUrl);//网络图片
        //分享链接 必须以http开头
        web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(summary);//描述*/
        //网络图片
        image = new UMImage(mActivity, url);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        image.compressStyle = UMImage.CompressStyle.QUALITY;

    }

    @Override
    public void setOnItemClickListener(int position, View v) {
        switch (position) {
            case 0:
                saveImg();
                break;
            case 1:
                shareOfPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case 2:
                shareOfPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case 3:
                shareOfPlatform(SHARE_MEDIA.SINA);
                break;
            case 4:
                shareOfPlatform(SHARE_MEDIA.QQ);
                break;
        }
        adtailPopWindow.dismiss();
    }

    private void shareOfPlatform(SHARE_MEDIA media) {
        new ShareAction(mActivity)
                .setPlatform(media)
                .withMedia(image) //web
                .setCallback(umShareListener)
                .share();
    }

    private void saveImg() {
        ImgUtils imgUtils = new ImgUtils(mActivity);//图片保存相册工具类
        new Thread(new Runnable() {
            @Override
            public void run() {
                //在这里要判断是否拥有内存卡读写权限
                LogUtil.e("保存图片", shareurl + "1234567890-");
                String imgUrl = shareurl;
                imgUtils.saveBitmap(imgUtils.getHttpBitmap(imgUrl), imgUrl.substring(imgUrl.lastIndexOf("/")));
            }
        }).start();
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            toast("分享成功");
            share_succes(shareSpId);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            toast("分享失败" + throwable.getMessage());

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            toast("分享取消");
        }
    };

    //分享成功回调
    public void share_succes(String specialId) {
        addSubscribe(apis.shareRespone(new ShareSuccsecRequest(userId(), specialId))
                .compose(RxHttpUtil.<FutureHttpResponse<ShareSuccsecRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<ShareSuccsecRespone>handleResult())
                .subscribeWith(new HttpSubscriber<ShareSuccsecRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        TipLinearUtil.create(mActivity).showTipMessage(exception.getMessage());
                    }
                }) {

                    @Override
                    public void onNext(ShareSuccsecRespone themePickRespone) {
                    }

                    @Override
                    public void onComplete() {
                        RemmendNewThemeRespone respone = respones.get(sharePosition);
                        respone.shareCount = respone.shareCount + 1;
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        dismissLoadingDialog();
                    }
                }));
    }
    //显示评论
    private void showPopWindow(String shareSpId) {
        if (fragmentConment == null) {
            fragmentConment = new ThemeFragmentConment();
            Bundle bundle = new Bundle();
            bundle.putString("SpecialId", shareSpId);
            fragmentConment.setArguments(bundle);
        }
        LogUtil.e("高度：", 2 / CommonUtils.getWindowHeight(mActivity) + "hhh" + CommonUtils.getWindowHeight(mActivity));
        fragmentConment.setTopOffset((CommonUtils.getWindowHeight(mActivity) / 5) * 2);
        fragmentConment.show(getFragmentManager(), "FRAGMENT");
    }
}
