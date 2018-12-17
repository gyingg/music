package com.future_melody.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.future_melody.R;
import com.future_melody.mode.ThemeDetailsMusicBean;
import com.future_melody.utils.GlideUtil;

import java.util.List;

/**
 * Author WZL
 * Date：2018/5/14 37
 */
public class ThemeNewDetailsMusicAdapter extends RecyclerView.Adapter<ThemeNewDetailsMusicAdapter.MusicViewHolder> {

    private Context mContext;
    private ItemClickListener itemClickListener;
    private List<ThemeDetailsMusicBean> musicBeanList;

    public ThemeNewDetailsMusicAdapter(Context mContext, List<ThemeDetailsMusicBean> musicBeanList) {
        this.mContext = mContext;
        this.musicBeanList = musicBeanList;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_theme_new_details, null);
        return new ThemeNewDetailsMusicAdapter.MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        ThemeDetailsMusicBean bean = musicBeanList.get(position);
        GlideUtil.setThePicture(mContext, bean.coverUrl, holder.img_music_cover);
        holder.item_text_sing_name.setText(bean.singerName.trim());
        holder.item_text_song_name.setText(bean.musicName.trim());
        if (itemClickListener != null) {
            holder.btn_layout_to_player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.set(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return musicBeanList.size();
    }


    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_music_cover;
        private TextView item_text_song_name;
        private TextView item_text_sing_name;
        private LinearLayout btn_layout_to_player;

        public MusicViewHolder(View view) {
            super(view);
            img_music_cover = view.findViewById(R.id.img_music_cover);
            item_text_song_name = view.findViewById(R.id.item_text_song_name);
            item_text_sing_name = view.findViewById(R.id.item_text_sing_name);
            btn_layout_to_player = view.findViewById(R.id.btn_layout_to_player);
        }
    }

    public interface ItemClickListener {
        void set(int i);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
