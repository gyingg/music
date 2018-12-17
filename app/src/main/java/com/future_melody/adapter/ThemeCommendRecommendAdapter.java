package com.future_melody.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.activity.UserInfoActivity;
import com.future_melody.mode.CommentModel;
import com.future_melody.utils.LogUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Y on 2018/5/16.
 */

public class ThemeCommendRecommendAdapter extends RecyclerView.Adapter<ThemeCommendRecommendAdapter.ViewHolder> {

    private Context context;
    private List<CommentModel> list;
    private MusicClickListener musicClickListener;
    private ThemeDetailsAdapter.ThemeClickListener themeClicklistener;

    public ThemeCommendRecommendAdapter(Context context, List<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recomde_theme_details, parent, false);
        ThemeCommendRecommendAdapter.ViewHolder viewHolder = new ThemeCommendRecommendAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentModel respone = list.get(position);
        Glide.with(context).load(respone.headUrl).into(holder.theme_details_userimg);
        holder.details_username.setText(respone.nickname);
        holder.details_context.setText(respone.commentContent);
        if (respone.parentCommentContent != null && !TextUtils.isEmpty(respone.parentCommentContent)) {
            holder.layout_huifu.setVisibility(View.VISIBLE);
            holder.text_huifu_user.setText("@" + respone.parentNickname);
            holder.details_context_huifu.setText(respone.parentCommentContent);
        } else {
            holder.layout_huifu.setVisibility(View.GONE);
        }
        holder.details_num.setText(respone.likeCount + "");
        if (respone.isLike == 0) {
            holder.img_zan.setImageResource(R.mipmap.icon_theme_details_unzan);
        } else {
            holder.img_zan.setImageResource(R.mipmap.icon_theme_details_zan);
        }

        if (musicClickListener != null) {
            holder.btn_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicClickListener.onClick(position, view);
                }
            });
        }
        if (themeClicklistener != null) {
            holder.theme_layout_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    themeClicklistener.GetInfo(position);
                }
            });
        }
        holder.theme_details_userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , UserInfoActivity.class);
                intent.putExtra("userId", respone.userId);
                context.startActivity(intent);
            }
        });


        holder.layout_huifu.post(new Runnable() {

            @Override
            public void run() {
//                holder.layout_huifu.getWidth(); // 获取宽度
                LogUtil.e("chu", holder.layout_huifu.getHeight() + "");
                holder.layout_huifu.getHeight(); // 获取高度
                ViewGroup.LayoutParams layoutParams = holder.bg_huifu.getLayoutParams();
                layoutParams.height = holder.layout_huifu.getHeight();
                layoutParams.width = 1;
                holder.bg_huifu.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView theme_details_userimg;
        private TextView details_username;
        private TextView details_context;
        private TextView text_huifu_user;
        private TextView details_context_huifu;
        private RelativeLayout layout_huifu;
        private RelativeLayout btn_item;
        private ImageView img_zan;
        private TextView details_num;
        private LinearLayout theme_layout_zan;
        private View bg_huifu;

        public ViewHolder(View itemView) {
            super(itemView);
            theme_details_userimg = itemView.findViewById(R.id.theme_details_userimg);
            details_username = itemView.findViewById(R.id.details_username);
            details_context = itemView.findViewById(R.id.details_context);
            text_huifu_user = itemView.findViewById(R.id.text_huifu_user);
            details_context_huifu = itemView.findViewById(R.id.details_context_huifu);
            layout_huifu = itemView.findViewById(R.id.layout_huifu);
            btn_item = itemView.findViewById(R.id.btn_item);
            img_zan = itemView.findViewById(R.id.img_zan);
            details_num = itemView.findViewById(R.id.details_num);
            theme_layout_zan = itemView.findViewById(R.id.theme_layout_zan);
            bg_huifu = itemView.findViewById(R.id.bg_huifu);
        }
    }

    public interface MusicClickListener {
        void onClick(int i, View view);
    }

    public void setMusicClickListener(MusicClickListener musicClicklistener) {
        this.musicClickListener = musicClicklistener;
    }

    public interface ThemeClickListener {
        void GetInfo(int i);
    }

    public void setThemeClickListener(ThemeDetailsAdapter.ThemeClickListener themeClickListener) {
        this.themeClicklistener = themeClickListener;
    }
}
