package com.future_melody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.mode.LikeBean;
import com.future_melody.utils.LogUtil;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/5/14 37
 * Notes: 喜欢页面
 */
public class LikeMusicAdapter extends RecyclerView.Adapter<LikeMusicAdapter.LikeMusicViewHodler> {
    private Context mContext;
    private List<LikeBean> list;
    private ArrayList<SongInfo> songInfos;
    private ItemClickListener itemClickListener;

    public LikeMusicAdapter(Context mContext, List<LikeBean> list, ArrayList<SongInfo> songInfos) {
        this.mContext = mContext;
        this.list = list;
        this.songInfos = songInfos;
    }

    @Override
    public LikeMusicViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_like_music, null);
        return new LikeMusicAdapter.LikeMusicViewHodler(view);
    }

    @Override
    public void onBindViewHolder(LikeMusicViewHodler holder, int position) {
        LikeBean likeBean = list.get(position);
        String string = (position + 1) + "";
        if (string.length() > 1) {
            holder.like_music_num.setText(string + "");
        } else {
            holder.like_music_num.setText("0" + string);
        }
        holder.tv_name.setText(likeBean.music_name);
        holder.tv_count.setText("来自" + likeBean.nickname + "的推荐");
        if (holder.singer_name != null) {
            holder.singer_name.setText("—  " + likeBean.singer_name);
        }
        SongInfo songInfo = songInfos.get(position);
        if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
            holder.gif_player.setVisibility(View.VISIBLE);
            holder.like_music_num.setVisibility(View.GONE);
            if (MusicManager.isPlaying()) {
                Glide.with(mContext).load(R.mipmap.gif_player).into(holder.gif_player);
            } else {
                holder.gif_player.setImageResource(R.mipmap.icon_player_gig_stop);
            }
        } else {
            holder.gif_player.setVisibility(View.GONE);
            holder.like_music_num.setVisibility(View.VISIBLE);
        }
        RequestOptions RequestOption = new RequestOptions();
        RequestOption.placeholder(R.mipmap.moren);
        Glide.with(mContext)
                .load(likeBean.music_picture)
                .apply(RequestOption)
                .into(holder.like_music_img);

        if (itemClickListener != null) {
            holder.btn_toplayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.set(position);
                }
            });
        }


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getLayoutPosition());
                    LogUtil.e("holder.getAdapterPosition()", position + "");
                }
            }
        });

        /*SongInfo songInfo = songInfos.get(position);
        if (MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
            holder.gif_player.setVisibility(View.VISIBLE);
            holder.like_music_num.setVisibility(View.GONE);
            Glide.with(mContext).load(R.mipmap.gif_player).into(holder.gif_player);
        } else {
            holder.gif_player.setVisibility(View.GONE);
            holder.like_music_num.setVisibility(View.VISIBLE);
        }*/
    }


    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int position);
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class LikeMusicViewHodler extends RecyclerView.ViewHolder {

        private final TextView like_music_num;
        private final TextView tv_name;
        private final TextView singer_name;
        private final RoundedImageView like_music_img;
        private final TextView tv_count;
        private ImageView gif_player;
        private final RelativeLayout btn_toplayer;
        private final Button btnDelete;

        public LikeMusicViewHodler(View itemView) {
            super(itemView);
            like_music_num = itemView.findViewById(R.id.like_music_num);
            singer_name = itemView.findViewById(R.id.singer_name);
            tv_name = itemView.findViewById(R.id.tv_name);
            like_music_img = itemView.findViewById(R.id.like_music_img);
            tv_count = itemView.findViewById(R.id.tv_count);
            btn_toplayer = itemView.findViewById(R.id.btn_toplayer);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            gif_player = itemView.findViewById(R.id.gif_player);

        }
    }

    public interface ItemClickListener {
        void set(int i);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
