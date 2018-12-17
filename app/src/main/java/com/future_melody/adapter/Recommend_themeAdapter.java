package com.future_melody.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.mode.LikeBean;
import com.future_melody.mode.Recommend_Theme_Bean;

import java.util.List;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Y on 2018/5/24.
 */

public class Recommend_themeAdapter extends RecyclerView.Adapter<Recommend_themeAdapter.ViewHolder> {

    private Context context;
    private List<Recommend_Theme_Bean> list;

    public Recommend_themeAdapter(Context context, List<Recommend_Theme_Bean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_themefragment, null);
        Recommend_themeAdapter.ViewHolder viewHolder = new Recommend_themeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recommend_Theme_Bean recommend_theme_bean = list.get(position);
        holder.song_num_count.setText("共" + recommend_theme_bean.musicCount + "首" + "");
        holder.music_name.setText(recommend_theme_bean.specialTitle);
        holder.theme_commend_cound.setText(recommend_theme_bean.likeCount + "");
        holder.theme_zan_num.setText(recommend_theme_bean.commentCount + "");
        //瀑布流
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int leftMargin = 20;
        int rightMargin = 20;
        layoutParams.setMargins(leftMargin, 20, rightMargin, 20);
        //条目宽度
        int itemWidth = ((getScreenWidth(holder.itemView.getContext())) / 2 - leftMargin - rightMargin);
        //缩放比
        double widthScale = recommend_theme_bean.width * 1.0 / itemWidth;
        //设置宽度
        layoutParams.weight = itemWidth;
        //设置高度
        if (recommend_theme_bean.width > itemWidth) {
            //大缩
            layoutParams.height = (int) (recommend_theme_bean.height / widthScale);
        } else {
            //小大
            layoutParams.height = (int) (recommend_theme_bean.height / widthScale);
        }
        holder.rel.setLayoutParams(layoutParams);
        //加载
        RequestOptions RequestOption = new RequestOptions();
        RequestOption.placeholder(R.mipmap.moren);
        RequestOption.override(layoutParams.width, layoutParams.height);
        Glide.with(holder.itemView.getContext())
                .load(recommend_theme_bean.specialPictureUrl)
                .apply(RequestOption)
                .into(holder.img_background);
          /*  if (position % 2 == 0) {
             layoutParams.height = 400;
            layoutParams.width=500;}*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recitem.attention(position);
            }
        });

        holder.theme_btn_fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapitem.attention(position);
            }
        });

    }

    //Recitem接口
    private Recitem recitem;
    public interface Recitem{
        void attention(int i);
    }
    public void setRecitem(Recitem recitem) {
        this.recitem = recitem;
    }


    private Shapitem shapitem;
    public interface Shapitem{
        void attention(int i);
    }

    public void setShapitem(Shapitem shapitem) {
        this.shapitem = shapitem;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView song_num_count;
        private final TextView music_name;
        private final ImageView img_background;
        private final TextView theme_commend_cound;
        private final TextView theme_zan_num;
        private final RelativeLayout rel;
        private final ImageView theme_btn_fabulous;

        public ViewHolder(View itemView) {
            super(itemView);
            rel = itemView.findViewById(R.id.rel);
            song_num_count = itemView.findViewById(R.id.song_num_count);
            music_name = itemView.findViewById(R.id.music_name);
            img_background = itemView.findViewById(R.id.img_background);
            theme_commend_cound = itemView.findViewById(R.id.theme_commend_cound);
            theme_zan_num = itemView.findViewById(R.id.theme_zan_num);
            theme_btn_fabulous = itemView.findViewById(R.id.theme_btn_fabulous);
        }
    }
}
