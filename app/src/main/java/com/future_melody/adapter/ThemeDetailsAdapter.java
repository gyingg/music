package com.future_melody.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.activity.UserInfoActivity;
import com.future_melody.net.respone.CommentListRespone;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author WZL
 * Date：2018/5/10 57
 * Notes: 动态详情 评论列表
 */
public class ThemeDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<CommentListRespone> modelList;
    private ThemeClickListener themeClicklistener;
    private ViewHolder holder;

    public ThemeDetailsAdapter(Context context, List<CommentListRespone> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
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
        holder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.item_theme_details, null);
            holder = new ViewHolder();
            holder.btn_item = view.findViewById(R.id.btn_item);
            holder.layout_huifu = view.findViewById(R.id.layout_huifu);
            holder.theme_layout_zan = view.findViewById(R.id.theme_layout_zan);
            holder.theme_details_userimg = view.findViewById(R.id.theme_details_userimg);
            holder.details_num = view.findViewById(R.id.details_num);
            holder.details_context = view.findViewById(R.id.details_context);
            holder.details_other_context = view.findViewById(R.id.details_other_context);
            holder.details_userfrom = view.findViewById(R.id.details_userfrom);
            holder.details_username = view.findViewById(R.id.details_username);
            holder.img_zan = view.findViewById(R.id.img_zan);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final CommentListRespone model = modelList.get(i);
        Glide.with(context).load(model.headUrl).into(holder.theme_details_userimg);
        holder.details_username.setText(model.nickname);
        if (TextUtils.isEmpty(model.planetName)) {
            holder.details_userfrom.setText(model.createTime + "");
        } else {
            holder.details_userfrom.setText(model.createTime + "  " + "来自" + model.planetName + "的" + model.asteroidName);
        }
        if (TextUtils.isEmpty(model.asteroidName)) {
            holder.details_userfrom.setVisibility(View.GONE);
        } else {
            holder.details_userfrom.setVisibility(View.VISIBLE);
        }
        holder.details_context.setText(model.commentContent);
        holder.details_num.setText(model.likeCount + "");
        if (model.parentCommentContent != null && !TextUtils.isEmpty(model.parentCommentContent)) {
            holder.layout_huifu.setVisibility(View.VISIBLE);
            String s = "回复" + model.parentNickname + ": ";
            String string = s + model.parentCommentContent;
            holder.details_other_context.setText(CommonUtils.setTextColor(context.getResources().getColor(R.color.appColor), string, 0, s.length()));
        } else {
            holder.layout_huifu.setVisibility(View.GONE);
        }
        LogUtil.e("model.isLike", model.isLike + "");
        if (model.isLike == 0) {
            holder.img_zan.setImageResource(R.mipmap.icon_theme_details_unzan);
        } else {
            holder.img_zan.setImageResource(R.mipmap.icon_theme_details_zan);
        }
        holder.theme_details_userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("userId", model.userId);
                context.startActivity(intent);
            }
        });
        if (themeClicklistener != null) {
            holder.theme_layout_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    themeClicklistener.GetInfo(i);
                }
            });
        }
        return view;
    }

    public static class ViewHolder {
        private RelativeLayout btn_item;
        private RelativeLayout layout_huifu;
        private LinearLayout theme_layout_zan;
        private CircleImageView theme_details_userimg;
        private ImageView img_zan;
        private TextView details_num;
        private TextView details_context;
        private TextView details_other_context;
        private TextView details_userfrom;
        private TextView details_username;
    }

    public interface ThemeClickListener {
        void GetInfo(int i);
    }

    public void setThemeClickListener(ThemeClickListener themeClickListener) {
        this.themeClicklistener = themeClickListener;
    }
}

