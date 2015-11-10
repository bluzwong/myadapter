package com.github.bluzwong.app;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.github.bluzwong.myadapter.MyAdapter;
import kale.adapter.AdapterItem;
import kale.adapter.recycler.CommonRcvAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //MainAdapter adapter = new MainAdapter(getLayoutInflater());

        for (int i = 0; i < 5; i++) {
            items.add("item" + i);
        }

        CommonRcvAdapter adapter = new CommonRcvAdapter<String>(items) {
            @NonNull
            @Override
            public AdapterItem<String> getItemView(Object o) {
                return new TitleItem();
            }

            @Override
            public Object getItemViewType(String s) {
                Log.i("bruce-rec", "getItemViewType:" + s);
                return super.getItemViewType(s);
            }
        };
        //adapter.setItems(items);
        final MyAdapter myAdapter = new MyAdapter(recyclerView, adapter);
        myAdapter.setOnLoadListener(4, new Runnable() {
            @Override
            public void run() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (items.size() > 20) {
                            myAdapter.statusNoMore();
                            return;
                        }
                        for (int i = 0; i < 4; i++) {
                            items.add("load " + i);
                        }
                        myAdapter.statusReset();
                        myAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myAdapter.statusEnable(false);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        for (int i = 0; i < 5; i++) {
                            items.add("refresh item" + i);
                        }
                        myAdapter.notifyDataSetChanged();
                        myAdapter.statusEnable(true);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }
}
