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
    private View viewDisable;

    public MyHolder(View itemView) {
        super(itemView);
        progressBarLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading_myadapter);
        tvClick = (TextView) itemView.findViewById(R.id.tv_click_to_load_more_myadapter);
        viewDisable = itemView.findViewById(R.id.view_diasble_myadapter);
    }

    public ProgressBar getProgressBarLoading() {
        return progressBarLoading;
    }

    public TextView getTvClick() {
        return tvClick;
    }

    public View getViewDisable() {
        return viewDisable;
    }
}
