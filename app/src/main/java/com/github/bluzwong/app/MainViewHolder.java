package com.github.bluzwong.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wangzhijie on 2015/11/10.
 */
public class MainViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle;
    public MainViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_list_item_line_title);
    }
}
