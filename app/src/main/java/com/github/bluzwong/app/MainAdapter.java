package com.github.bluzwong.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhijie on 2015/11/10.
 */
public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private List<String> items = new ArrayList<>();
    private LayoutInflater inflater;
    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public MainAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(inflater.inflate(R.layout.listitem_main_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.tvTitle.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
