package com.future_melody.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;
import com.future_melody.activity.UserInfoActivity;
import com.future_melody.mode.StarDetailsUserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author WZL
 * Date：2018/5/15 24
 * Notes:
 */
public class StarDetailsUserAdapter extends RecyclerView.Adapter<StarDetailsUserAdapter.StarDetailsUserHodler> {
    private Context mContext;
    private List<StarDetailsUserModel> list;

    public StarDetailsUserAdapter(Context mContext, List<StarDetailsUserModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public StarDetailsUserAdapter.StarDetailsUserHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_star_new_details_user, null);
        return new StarDetailsUserAdapter.StarDetailsUserHodler(view);
    }

    @Override
    public void onBindViewHolder(StarDetailsUserAdapter.StarDetailsUserHodler holder, int position) {
        final StarDetailsUserModel model = list.get(position);
        RequestOptions RequestOptions = new RequestOptions();
        RequestOptions.placeholder(R.mipmap.moren);
        Glide.with(mContext).load(model.userHeadUrl).apply(RequestOptions).into(holder.star_details_user_img);
        holder.star_details_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra("userId", model.userId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StarDetailsUserHodler extends RecyclerView.ViewHolder {
        private ImageView star_details_user_img;

        public StarDetailsUserHodler(View itemView) {
            super(itemView);
            star_details_user_img = itemView.findViewById(R.id.star_details_user_img);
        }
    }
}
