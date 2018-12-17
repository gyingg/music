package com.future_melody.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.future_melody.R;
import com.future_melody.adapter.GuidePageAdapter;
import com.future_melody.base.BaseActivity;
import com.future_melody.utils.SPUtils;

import java.util.LinkedList;
import java.util.List;

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private int[] ids = {R.mipmap.page1, R.mipmap.page2, R.mipmap.page3};
    private List<View> imageViews;
    private Button into;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.view_pager_guide);
        into = findViewById(R.id.into);
        imageViews = new LinkedList<>();
        for (int id : ids) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setBackgroundResource(id);
            imageViews.add(imageView);
        }
        GuidePageAdapter adapter = new GuidePageAdapter(imageViews);
        adapter.setItemClickListener(new GuidePageAdapter.ItemClickListener() {
            @Override
            public void click(int position) {
                if (position == ids.length - 1) {
                    startActivity(new Intent(mActivity, MainNewActivity.class));
                    finish();
                }
            }
        });
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        viewPager.addOnPageChangeListener(this);
        into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, MainNewActivity.class));
                SPUtils.getInstance().put("isFirst", false);
                finish();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == ids.length - 1) {
            into.setVisibility(View.VISIBLE);
        } else {
            into.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
