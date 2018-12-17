package com.future_melody.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.activity.UserInfoActivity;
import com.future_melody.common.CommonConst;
import com.future_melody.mode.RecommendSpecialVoListBean;
import com.future_melody.utils.LogUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author WZL
 * Date：2018/5/14 37
 * Notes: 发现，推荐主题
 */
public class RecommendThemesAdapter extends BaseAdapter {
    private Context mContext;
    private List<RecommendSpecialVoListBean> listBeans;
    private ThemeClickListener shareClickListener;
    private ThemeClickListener followsClickListener;

    public RecommendThemesAdapter(Context mContext, List<RecommendSpecialVoListBean> listBeans) {
        this.mContext = mContext;
        this.listBeans = listBeans;
    }

    @Override
    public int getCount() {
        return listBeans == null ? 0 : listBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_recommend_theme, null);
            holder = new ViewHolder();
            holder.btn_to_theme_details = view.findViewById(R.id.btn_to_theme_details);
            holder.theme_bg = view.findViewById(R.id.theme_bg);
            holder.theme_user_img = view.findViewById(R.id.theme_user_img);
            holder.theme_btn_player = view.findViewById(R.id.theme_btn_player);
            holder.theme_song_name = view.findViewById(R.id.theme_song_name);
            holder.theme_song_num = view.findViewById(R.id.theme_song_num);
            holder.theme_song_info = view.findViewById(R.id.theme_song_info);
            holder.release_name = view.findViewById(R.id.release_name);
            holder.release_user_start = view.findViewById(R.id.release_user_start);
            holder.theme_follow = view.findViewById(R.id.theme_follow);
            holder.release_content = view.findViewById(R.id.release_content);
            holder.theme_time = view.findViewById(R.id.theme_time);
            holder.theme_btn_share = view.findViewById(R.id.theme_btn_share);
            holder.theme_zan_num = view.findViewById(R.id.theme_zan_num);
            holder.theme_commend_num = view.findViewById(R.id.theme_commend_num);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        RecommendSpecialVoListBean bean = listBeans.get(i);
        LogUtil.e("listBeans", listBeans.size() + "");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.moren);
        Glide.with(mContext).load(bean.getSpecialPictureUrl()).apply(requestOptions).into(holder.theme_bg);
        Glide.with(mContext).load(bean.getUserHeadUrl()).into(holder.theme_user_img);
        holder.theme_song_name.setText(bean.getSpecialTitle());
        holder.release_name.setText(bean.getNickname());
        holder.release_user_start.setText("来自" + bean.getPlanetName() + "的" + bean.getAsteroidName());
        holder.theme_song_num.setText("共" + bean.getMusicCount() + "首");
        holder.release_content.setText(bean.getSpecialDescription());
        holder.theme_zan_num.setText(bean.getLikeCount() + "");
        holder.theme_commend_num.setText(bean.getCommentCount() + "");
        holder.theme_time.setText(bean.getSpecialCreateTime());
        holder.theme_song_info.setText("The future melody");
        if (CommonConst.userId().equals(bean.getUserId())) {
            holder.theme_follow.setVisibility(View.GONE);
        } else {
            holder.theme_follow.setVisibility(View.VISIBLE);
        }
        if (bean.getIsAttention() == 0) {
            holder.theme_follow.setText("关注");
        } else {
            holder.theme_follow.setText("已关注");
        }
        holder.theme_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra("userId", bean.getUserId());
                mContext.startActivity(intent);
            }
        });
        if (shareClickListener != null) {
            holder.theme_btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareClickListener.onClick(i);
                }
            });
        }

        if (followsClickListener != null) {
            holder.theme_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    followsClickListener.onClick(i);
                }
            });
        }
        return view;
    }

    public static class ViewHolder {
        private RelativeLayout btn_to_theme_details;
        private ImageView theme_bg;
        private CircleImageView theme_user_img;
        private ImageView theme_btn_player;
        private TextView theme_song_name;
        private TextView theme_song_num;
        private TextView theme_song_info;
        private TextView release_name;
        private TextView release_user_start;
        private TextView theme_follow;
        private TextView release_content;
        private TextView theme_time;
        private ImageView theme_btn_share;
        private TextView theme_zan_num;
        private TextView theme_commend_num;

    }

    public interface ThemeClickListener {
        void onClick(int i);
    }

    public void setShareClickListener(ThemeClickListener themeClickListener) {
        this.shareClickListener = themeClickListener;
    }

    public void setFollowsClickListener(ThemeClickListener themeClickListener) {
        this.followsClickListener = themeClickListener;
    }
}
