package com.future_melody.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.future_melody.R;
import com.future_melody.adapter.DialogThemeAdapter;
import com.future_melody.base.FutureApplication;
import com.future_melody.mode.RemendMusicNewModle;
import com.future_melody.mode.ThemeRecommendIMusic;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.LogUtil;
import com.lzx.musiclibrary.aidl.model.SongInfo;

import java.util.List;

/**
 * Author WZL
 * Date：2018/8/27 38
 * Notes:
 */
public class RemomendThemwMusicPp {
    private ListView listView;
    private PopupWindow window;
    //窗口在x轴偏移量
    private int xOff = 0;
    //窗口在y轴的偏移量
    private int yOff = 0;

    public RemomendThemwMusicPp(Context context, List<RemendMusicNewModle> musicVoList, List<SongInfo> songInfos) {

        window = new PopupWindow(context);
        //ViewGroup.LayoutParams.WRAP_CONTENT，自动包裹所有的内容
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        //点击 back 键的时候，窗口会自动消失
        window.setBackgroundDrawable(new BitmapDrawable());
        //加载自定义视图
        View localView = LayoutInflater.from(context).inflate(R.layout.dialog_recommend_music, null);
        listView = localView.findViewById(R.id.listview_dialog);
        listView.setAdapter(new DialogThemeAdapter(context, musicVoList, songInfos));
        listView.setTag(window);
        //设置显示的视图
        window.setContentView(localView);
    }

//    public void setItemClickListener(DialogThemeAdapter.OnItemClickListener listener) {
//        listView.setOnItemClickListener(listener);
//    }

    public void dismiss() {
        window.dismiss();
    }


    public void show(View paramView) {
        window.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int i = window.getContentView().getMeasuredWidth();
        window.showAsDropDown(paramView, -CommonUtils.dip2px(FutureApplication.getInstance(), 225), -CommonUtils.dip2px(FutureApplication.getInstance(), 348));
        LogUtil.e("X轴", (-paramView.getWidth() - i) + "===i" + i + "====paramView" + paramView.getWidth()+"----"+CommonUtils.dip2px(FutureApplication.getInstance() ,40));
//        window.showAsDropDown(paramView, -(paramView.getWidth() - CommonUtils.dip2px(FutureApplication.getContext(), -100)), -100);
        window.update();
    }
}
