package com.future_melody.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.future_melody.R;
import com.future_melody.mode.XingMusicSetTomModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by Y on 2018/5/22.
 */

public class StatCommendAdapter extends BaseAdapter {

    private Context context;
    private List<XingMusicSetTomModel> lists;
    private checkboxClickListener listener;

    public StatCommendAdapter(Context context, List<XingMusicSetTomModel> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StatCommendHolder statCommendHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_stat_recommend, null);
            statCommendHolder = new StatCommendHolder();
            statCommendHolder.stat_icon = convertView.findViewById(R.id.stat_icon);
            statCommendHolder.stat_name = convertView.findViewById(R.id.stat_name);
            statCommendHolder.stat_lighten = convertView.findViewById(R.id.stat_lighten);
            statCommendHolder.checkbox = convertView.findViewById(R.id.checkbox);
            statCommendHolder.btn_layout = convertView.findViewById(R.id.btn_layout);
            convertView.setTag(statCommendHolder);
        } else {
            statCommendHolder = (StatCommendHolder) convertView.getTag();
        }
        XingMusicSetTomModel model = lists.get(position);
        Glide.with(context).load(model.music_picture).into(statCommendHolder.stat_icon);
        statCommendHolder.stat_name.setText(model.music_name);
        statCommendHolder.stat_lighten.setText("来自" + model.nickname + "推荐");
        if (model.isCheck) {
            statCommendHolder.checkbox.setChecked(true);
        } else {
            statCommendHolder.checkbox.setChecked(false);
        }
        if (listener != null) {
            StatCommendHolder finalStatCommendHolder = statCommendHolder;
            statCommendHolder.btn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.checked(position);
                }
            });
        }
        return convertView;
    }

    class StatCommendHolder {
        RoundedImageView stat_icon;
        TextView stat_name;
        TextView stat_lighten;
        CheckBox checkbox;
        private RelativeLayout btn_layout;
    }

    public interface checkboxClickListener {
        void checked(int i);
    }

    public void setcheckboxClickListener(checkboxClickListener clickListener) {
        this.listener = clickListener;
    }
}
