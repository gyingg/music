package com.future_melody.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.future_melody.R;

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.OnlineViewHodler> {
    private Context context;

    @Override
    public OnlineViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_on_line, parent, false);
        return new OnlineViewHodler(view);
    }

    @Override
    public void onBindViewHolder(OnlineViewHodler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class OnlineViewHodler extends RecyclerView.ViewHolder {
        public OnlineViewHodler(View itemView) {
            super(itemView);
        }
    }
}
