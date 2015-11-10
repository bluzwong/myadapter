package com.github.bluzwong.app;

import android.view.View;
import android.widget.TextView;
import kale.adapter.AdapterItem;

/**
 * Created by wangzhijie on 2015/11/10.
 */
public class TitleItem implements AdapterItem<String> {
    @Override
    public int getLayoutResId() {
        return R.layout.listitem_main_activity;
    }

    public TextView tvTitle;
    @Override
    public void onBindViews(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tv_list_item_line_title);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(String s, int i) {
        tvTitle.setText(s);
    }
}
