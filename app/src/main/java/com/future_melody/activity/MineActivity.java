package com.future_melody.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.future_melody.R;
import com.future_melody.adapter.MineAdapter;
import com.future_melody.adapter.VpAdapter;
import com.future_melody.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的界面 类似轮播图 第二张加载出一般半
 */
public class MineActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private List<View> list;
    private LinearLayout linearLayout;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mine;
    }

    @Override
    protected void initView() {
        viewPager =  findViewById(R.id.vp);
        linearLayout =  findViewById(R.id.ll);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        //添加条目布局
        View item1 = View.inflate(this, R.layout.item1, null);
        View item2 = View.inflate(this, R.layout.item2, null);
        list.add(item1);
        list.add(item2);

        //添加指示器
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(90, 50);
            params.setMargins(0,0,20,0);
            imageView.setLayoutParams(params);
            if (i==0)   imageView.setImageResource(R.drawable.shense);
            else        imageView.setImageResource(R.drawable.qianse);
            linearLayout.addView(imageView);
            viewPager.setAdapter(new MineAdapter(list,this));
            //设置界面
            viewPager.setPageMargin(30);
            viewPager.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //切换指示器
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            ImageView childAt = (ImageView) linearLayout.getChildAt(i);
            if (i==position) childAt.setImageResource(R.drawable.shense);
            else  childAt.setImageResource(R.drawable.qianse);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
