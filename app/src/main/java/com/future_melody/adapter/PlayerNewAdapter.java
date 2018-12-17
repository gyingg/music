package com.future_melody.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.utils.GlideUtil;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * Author WZL
 * Date：2018/5/14 37
 * Notes: 发现，推荐主题
 */
public class PlayerNewAdapter extends RecyclerView.Adapter<PlayerNewAdapter.PlayerNewHodler> {

    private Context mContext;
    private ItemClickListener itemClickListener;

    public PlayerNewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PlayerNewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_player_music_img, null);
        return new PlayerNewAdapter.PlayerNewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerNewHodler holder, int position) {
        SongInfo songInfo = MusicManager.get().getPlayList().get(position);
        Glide.with(mContext).load(songInfo.getSongCover()).into(holder.music_img);
        if (itemClickListener != null) {
            holder.music_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.set(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return MusicManager.get().getPlayList().size();
    }


    public static class PlayerNewHodler extends RecyclerView.ViewHolder {
        private ImageView music_img;

        public PlayerNewHodler(View view) {
            super(view);
            music_img = view.findViewById(R.id.music_img);
        }
    }

    public interface ItemClickListener {
        void set(int i);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
