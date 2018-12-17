package com.future_melody.utils;
import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.future_melody.R;


/**
 * @author ning
 * @describe setImageUtil
 * @Glide https://mrfu.me/2016/02/27/Glide_Getting_Started/
 * GlideUtil.setThePicture(activity, imgurl, buyal_img);
 * 上下文  图片url  imageview
 */
public class GlideUtil {
    //第一个就是设置普通展位图的
    public static void setThePicture(Context context, String picUrl, ImageView iv) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.moren)
                .error(R.mipmap.moren)
                .dontAnimate();

        if (picUrl == null) picUrl = "";
        if (picUrl.endsWith(".gif") || picUrl.endsWith(".GIF")) {
            Glide.with(context).asGif().load(picUrl)
                    .apply(requestOptions)
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(iv);
        } else {
            Glide.with(context).load(picUrl)
                    .apply(requestOptions)
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transition(new DrawableTransitionOptions().crossFade()).into(iv);
        }
    }


    //第二个是设置头像占位图的
    public static void setHeadPortrait(Context context, String picUrl, ImageView iv) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.moren)
                .error(R.mipmap.moren)
                .dontAnimate();
        if (picUrl == null) picUrl = "";
        if (picUrl.endsWith(".gif") || picUrl.endsWith(".GIF")) {
            Glide.with(context).load(picUrl)
                    .apply(requestOptions)
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transition(new DrawableTransitionOptions().crossFade()).into(iv);
        } else {
            Glide.with(context).load(picUrl)
                    .apply(requestOptions)
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transition(new DrawableTransitionOptions().crossFade()).into(iv);
        }
    }


    //第三个就是不设置占位图普通加载的
    public static void setNoResponseImages(Context context, String picUrl, ImageView iv) {
        if (picUrl == null) picUrl = "";
        Glide.with(context).load(picUrl).apply(new RequestOptions().dontAnimate()).transition(new DrawableTransitionOptions().crossFade()).into(iv);
    }
}
