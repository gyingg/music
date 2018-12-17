package com.future_melody.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.adapter.ShareItemAdapter;
import com.future_melody.bean.ShareItemBean;

import java.util.List;

/**
 * 设置分享弹框出图片
 */
public class AdtailPopWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private ImageView imageView;
    private TextView cancel_adtail;
    private TextView btn_share_title;
    private RecyclerView rlv_adtail;
    private ShareItemAdapter shareItemAdapter;

    public void setList(List<ShareItemBean> list) {
        shareItemAdapter.setList(list);
        shareItemAdapter.notifyDataSetChanged();
    }

    public AdtailPopWindow(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void setImg(String imgurl) {
        if (imageView.getDrawable() == null)
            Glide.with(context).load(imgurl).into(imageView);
    }

    public void setImg(int imgRes) {
        if (imageView.getDrawable() == null)
            Glide.with(context).load(imgRes).into(imageView);
    }

    public void setTitle(String s) {
        if (btn_share_title != null) {
            btn_share_title.setText(s);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public RecyclerView getRlv_adtail() {
        return rlv_adtail;
    }

    public TextView getCancel_adtail() {
        return cancel_adtail;
    }


    public interface OnItemClickListener {
        void setOnItemClickListener(int position, View v);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void init() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.adtaillayout, null, false);
        imageView = inflate.findViewById(R.id.iv_adtail);
        rlv_adtail = inflate.findViewById(R.id.rlv_adtail);
        cancel_adtail = inflate.findViewById(R.id.cancel_adtail);
        btn_share_title = inflate.findViewById(R.id.btn_share_title);
        cancel_adtail.setOnClickListener(this);
        rlv_adtail.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        shareItemAdapter = new ShareItemAdapter();
        shareItemAdapter.setOnItemClickListener(new ShareItemAdapter.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(int position, View v) {
                onItemClickListener.setOnItemClickListener(position, v);
            }
        });
        rlv_adtail.setAdapter(shareItemAdapter);
        setContentView(inflate);
        //设置背景透明
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);//设置背景可点击;
        //设置宽与高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_adtail:
                dismiss();
                if (onCancelListener != null)
                    onCancelListener.onCancelListener();
                break;
        }
    }

    public interface OnCancelListener {
        void onCancelListener();
    }

    private OnCancelListener onCancelListener;

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }
}
