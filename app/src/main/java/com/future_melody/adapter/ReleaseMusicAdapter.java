package com.future_melody.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.future_melody.R;

import java.util.List;

/**
 * Author WZL
 * Date：2018/5/28 30
 * Notes:
 */
public class ReleaseMusicAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> strings;

    public ReleaseMusicAdapter(Context mContext, List<String> strings) {
        this.mContext = mContext;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
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
        ViewHolder holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_release_music, null);
            holder = new ViewHolder();
            holder.text_music_name = view.findViewById(R.id.text_music_name);
            holder.text_music_delete = view.findViewById(R.id.text_music_delete);
            holder.text_music_name.setText(strings.get(i));
            holder.text_music_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "就不给你删除，气不气", Toast.LENGTH_SHORT).show();
                }
            });
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }
    public class ViewHolder{
        private TextView text_music_delete;
        private TextView text_music_name;
    }
}
