package com.github.bluzwong.myadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by wangzhijie on 2015/11/10.
 */
public class MyHolder extends RecyclerView.ViewHolder {

    private ProgressBar progressBarLoading;
    private TextView tvClick;

    public MyHolder(View itemView) {
        super(itemView);
        progressBarLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
        tvClick = (TextView) itemView.findViewById(R.id.tv_click_to_load_more);
    }

    public ProgressBar getProgressBarLoading() {
        return progressBarLoading;
    }

    public TextView getTvClick() {
        return tvClick;
    }
}
