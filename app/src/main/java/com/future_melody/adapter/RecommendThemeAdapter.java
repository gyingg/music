package com.future_melody.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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
public class RecommendThemeAdapter extends RecyclerView.Adapter<RecommendThemeAdapter.StarDetailsMusicHodler> {

    private Context mContext;
    private List<RecommendSpecialVoListBean> listBeans;
    private RecommendThemesAdapter.ThemeClickListener shareClickListener;
    private RecommendThemesAdapter.ThemeClickListener followsClickListener;
    private RecommendThemesAdapter.ThemeClickListener setTopClickListener;

    public RecommendThemeAdapter(Context mContext, List<RecommendSpecialVoListBean> listBeans) {
        this.mContext = mContext;
        this.listBeans = listBeans;
    }

    @NonNull
    @Override
    public StarDetailsMusicHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //item_recommend_theme
        View view = View.inflate(parent.getContext(), R.layout.item_recommend_theme2, null);
        return new RecommendThemeAdapter.StarDetailsMusicHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StarDetailsMusicHodler holder, int position) {
     /*   RecommendSpecialVoListBean bean = listBeans.get(position);
        LogUtil.e("listBeans", listBeans.size() + "");
        Glide.with(mContext)
                .load(bean.getSpecialPictureUrl())
                .into(holder.theme_bg);
        Glide.with(mContext).load(bean.getUserHeadUrl()).into(holder.theme_user_img);
        holder.theme_song_name.setText(bean.getSpecialTitle());
        holder.release_name.setText(bean.getNickname());
        holder.release_user_start.setText(bean.getAsteroidName());
        holder.theme_song_num.setText("共" + bean.getMusicCount() + "首");
        holder.release_content.setText(bean.getSpecialDescription());
        holder.theme_zan_num.setText(bean.getLikeCount() + "");
        holder.theme_commend_num.setText(bean.getCommentCount() + "");
        holder.theme_time.setText(bean.getSpecialCreateTime());
        holder.theme_song_info.setText("THE FUTURE MELODY");
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
        if (bean.rulerUserId != null) {
            if (bean.rulerUserId.equals(CommonConst.userId())) {
                holder.theme_user_img_super.setVisibility(View.VISIBLE);
            } else {
                holder.theme_user_img_super.setVisibility(View.GONE);
            }
        }
        if (bean.rulerUserId != null) {
            if (bean.rulerUserId.equals(CommonConst.userId())) {
                holder.btn_theme_top.setVisibility(View.VISIBLE);
            } else {
                holder.btn_theme_top.setVisibility(View.GONE);
            }
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
                    shareClickListener.onClick(position);
                }
            });
        }
        if (followsClickListener != null) {
            holder.theme_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    followsClickListener.onClick(position);
                }
            });
        }
        if (setTopClickListener != null) {
            holder.btn_theme_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTopClickListener.onClick(position);
                }
            });
        }*/
        RecommendSpecialVoListBean bean = listBeans.get(position);
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.placeholder(R.mipmap.moren);
        Glide.with(mContext).load(bean.getSpecialPictureUrl()).apply(requestOptions).into(holder.theme_bg);
        holder.tv_title.setText(bean.getSpecialTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemRec.itemRec(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }

    //rec条目点击
    private itemRec itemRec;
    public interface itemRec {
        void itemRec(int i);
    }

    public void setItemRec(itemRec itemRec) {
        this.itemRec = itemRec;
    }


    public static class StarDetailsMusicHodler extends RecyclerView.ViewHolder {

       /* private RelativeLayout btn_to_theme_details;
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
        private ImageView btn_theme_top;
        private ImageView theme_user_img_super;*/
       private ImageView theme_bg;
       private TextView tv_title;
        public StarDetailsMusicHodler(View view) {
            super(view);
            theme_bg = view.findViewById(R.id.theme_bg);
            tv_title = view.findViewById(R.id.tv_title);
     /*       btn_to_theme_details = view.findViewById(R.id.btn_to_theme_details);
            theme_bg = view.findViewById(R.id.theme_bg);
            theme_user_img = view.findViewById(R.id.theme_user_img);
            theme_btn_player = view.findViewById(R.id.theme_btn_player);
            theme_song_name = view.findViewById(R.id.theme_song_name);
            theme_song_num = view.findViewById(R.id.theme_song_num);
            theme_song_info = view.findViewById(R.id.theme_song_info);
            release_name = view.findViewById(R.id.release_name);
            release_user_start = view.findViewById(R.id.release_user_start);
            theme_follow = view.findViewById(R.id.theme_follow);
            release_content = view.findViewById(R.id.release_content);
            theme_time = view.findViewById(R.id.theme_time);
            theme_btn_share = view.findViewById(R.id.theme_btn_share);
            theme_zan_num = view.findViewById(R.id.theme_zan_num);
            theme_commend_num = view.findViewById(R.id.theme_commend_num);
            btn_theme_top = view.findViewById(R.id.btn_theme_top);
            theme_user_img_super = view.findViewById(R.id.theme_user_img_super);*/
        }
    }

    public interface ThemeClickListener {
        void onClick(int i);
    }

    public void setShareClickListener(RecommendThemesAdapter.ThemeClickListener themeClickListener) {
        this.shareClickListener = themeClickListener;
    }

    public void setFollowsClickListener(RecommendThemesAdapter.ThemeClickListener themeClickListener) {
        this.followsClickListener = themeClickListener;
    }

    public void setTopClickListener(RecommendThemesAdapter.ThemeClickListener themeClickListener) {
        this.setTopClickListener = themeClickListener;
    }
}
