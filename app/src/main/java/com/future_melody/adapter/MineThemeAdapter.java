package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.net.respone.MineReconmendThemeRespone;
import com.future_melody.utils.LogUtil;

import java.util.List;

/**
 * Author WZL
 * Date：2018/5/14 37
 * Notes: 发现，推荐主题
 */
public class MineThemeAdapter extends BaseAdapter {
    private Context mContext;
    private List<MineReconmendThemeRespone> listBeans;
    private ThemeClickListener shareClickListener;
    private ThemeClickListener followsClickListener;


    public MineThemeAdapter(Context mContext, List<MineReconmendThemeRespone> listBeans) {
        this.mContext = mContext;
        this.listBeans = listBeans;
    }

    @Override
    public int getCount() {
        if (listBeans != null) {
            return listBeans.size();
        } else {
            return 0;
        }
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
            view = View.inflate(mContext, R.layout.item_mine_recommend_theme, null);
            holder = new ViewHolder();
            holder.btn_to_theme_details = view.findViewById(R.id.btn_to_theme_details);
            holder.theme_bg = view.findViewById(R.id.theme_bg);
            holder.theme_btn_player = view.findViewById(R.id.theme_btn_player);
            holder.theme_song_name = view.findViewById(R.id.theme_song_name);
            holder.theme_song_num = view.findViewById(R.id.theme_song_num);
            holder.theme_song_info = view.findViewById(R.id.theme_song_info);
            holder.release_content = view.findViewById(R.id.release_content);
            holder.theme_time = view.findViewById(R.id.theme_time);
            holder.theme_btn_share = view.findViewById(R.id.theme_btn_share);
            holder.theme_zan_num = view.findViewById(R.id.theme_zan_num);
            holder.theme_commend_num = view.findViewById(R.id.theme_commend_num);
            holder.time = view.findViewById(R.id.time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MineReconmendThemeRespone bean = listBeans.get(i);
        LogUtil.e("listBeans", listBeans.size() + "");
        RequestOptions options = new RequestOptions();
        RequestOptions placeholder = options.placeholder(R.mipmap.moren);
        Glide.with(mContext)
                .load(bean.special_picture)
                .apply(placeholder)
                .into(holder.theme_bg);
//        Glide.with(mContext).load(bean.getUserHeadUrl()).into(holder.theme_user_img);
        holder.theme_song_name.setText(bean.special_title);
        holder.theme_song_num.setText("共" + bean.musicnumber + "首");
        holder.release_content.setText(bean.special_describe);
        holder.theme_time.setText(bean.recommend_time);
        holder.theme_zan_num.setText(bean.likesnumber + "");
        holder.theme_commend_num.setText(bean.commentsnumber + "");
        holder.time.setText(bean.create_times);
        if (shareClickListener != null) {
            holder.theme_btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareClickListener.onClick(i);
                }
            });
        }
        return view;
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

    public static class ViewHolder {
        private RelativeLayout btn_to_theme_details;
        private ImageView theme_bg;
        private ImageView theme_btn_player;
        private TextView theme_song_name;
        private TextView theme_song_num;
        private TextView theme_song_info;
        private TextView release_content;
        private TextView theme_time;
        private ImageView theme_btn_share;
        private TextView theme_zan_num;
        private TextView theme_commend_num;
        private TextView time;

    }
}
