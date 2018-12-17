package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.net.respone.AttentionThemeRespone;
import com.future_melody.utils.LogUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author WZL
 * Date：2018/5/14 37
 * Notes: 发现，推荐主题
 */
public class FollowThemeAdapter extends BaseAdapter {
    private Context mContext;
    private List<AttentionThemeRespone> listBeans;

    public FollowThemeAdapter(Context mContext, List<AttentionThemeRespone> listBeans) {
        this.mContext = mContext;
        this.listBeans = listBeans;
    }

    @Override
    public int getCount() {
        return listBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
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
        AttentionThemeRespone bean = listBeans.get(i);
        LogUtil.e("listBeans", listBeans.size() + "");
        Glide.with(mContext)
                .load(bean.specialPictureUrl)
                .into(holder.theme_bg);
        Glide.with(mContext).load(bean.userHeadUrl).into(holder.theme_user_img);
        holder.theme_song_name.setText("主题标题");
        holder.release_name.setText(bean.nickname);
        holder.release_user_start.setText("来自"+bean.planetName+"的"+bean.asteroidName);
        holder.theme_song_num.setText("共" + bean.musicCount + "首");
        holder.release_content.setText(bean.specialDescription);
        holder.theme_zan_num.setText(bean.likeCount + "");
        holder.theme_commend_num.setText(bean.commentCount + "");
        holder.theme_time.setText(bean.specialCreateTime);
        holder.theme_song_info.setText("future ");
        if (bean.isAttention > 0) {
            holder.theme_follow.setText("关注");
        } else {
            holder.theme_follow.setText("已关注");
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
}
