package com.future_melody.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.future_melody.R;
import com.future_melody.adapter.LocalMusicAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.mode.LocalMusicModel;
import com.future_melody.utils.LocalMusicUtils;
import com.future_melody.utils.TipLinearUtil;
import com.lzx.musiclibrary.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author WZL
 * Date：2018/5/16 28
 * Notes: 本地音乐列表
 */
public class LocalMusicActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.no_data)
    LinearLayout noData;
    private String TAG = "LocalMusicActivity";
    @BindView(R.id.ph_title_right_img)
    ImageView phTitleRightImg;
    @BindView(R.id.local_music_rv)
    ListView localMusicRv;
    private LocalMusicAdapter adapter;
    private List<LocalMusicModel> videoInfos;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_local_music;
    }

    @Override
    protected void initView() {
        setBlackBackble();
        setTitle("选择本地音乐");
        setTitleColor(R.color.color_333333);
        phTitleRightImg.setVisibility(View.GONE);
        setBarColor(R.color.white, true);
        setTitleLayoutColor(mActivity, R.color.white);
    }

    @Override
    protected void initData() {
        localMusicRv.setOnItemClickListener(this);
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            TipLinearUtil.create(mActivity).showTipMessage("读取SD卡失败");
            return;
        }
        videoInfos = LocalMusicUtils.getMusicData(mActivity);
        if ( videoInfos.size() <= 0) {
            noData.setVisibility(View.VISIBLE);
            localMusicRv.setVisibility(View.GONE);
        } else {
            noData.setVisibility(View.GONE);
            localMusicRv.setVisibility(View.VISIBLE);
            adapter = new LocalMusicAdapter(mActivity, videoInfos);
            localMusicRv.setAdapter(adapter);
        }
        LogUtil.e("13", videoInfos.size() + "");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LogUtil.e("onItemClick", videoInfos.get(i).getUrl());
        Intent intent = new Intent(mActivity, EditMusicInfoActivity.class);
        intent.putExtra("url", videoInfos.get(i).getUrl());
        intent.putExtra("song", videoInfos.get(i).getName());
        intent.putExtra("singer", videoInfos.get(i).getSinger());
        intent.putExtra("Duration", videoInfos.get(i).getDuration());
        startActivityForResult(intent, CommonConst.MUSIC_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(CommonConst.MUSIC_CODE, data);
        finish();
    }
}
