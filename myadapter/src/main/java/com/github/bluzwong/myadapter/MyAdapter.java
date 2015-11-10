package com.github.bluzwong.myadapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wangzhijie on 2015/11/10.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> targetAdapter;
    private static final int myTypeRes = R.layout.item_loading;
    private MyHolder myHolder;

    private static final int STATUS_NORMAL = 0, STATUS_IS_LOADING = 1,
            STATUS_SHOW_CLICK = 2, STATUS_NO_MORE = 3,
            STATUS_NORMAL_LOADING = 4, STATUS_DISABLE = -1;

    private int currentStatus = STATUS_NORMAL;

    public MyAdapter(RecyclerView recyclerView, RecyclerView.Adapter targetAdapter) {
        if (recyclerView == null || targetAdapter == null) {
            throw new IllegalArgumentException("can not be null");
        }
        this.recyclerView = recyclerView;
        this.targetAdapter = (RecyclerView.Adapter<RecyclerView.ViewHolder>) targetAdapter;
        recyclerView.setAdapter(this);
    }

    public void setCurrentStatus(int currentStatus) {
        if (currentStatus < STATUS_DISABLE || currentStatus > STATUS_NORMAL_LOADING) {
            currentStatus = -1;
        }
        this.currentStatus = currentStatus;
        syncStatus();
    }

    public void statusReset() {
        setCurrentStatus(STATUS_NORMAL);
        syncStatus();
    }

    public void statusNoMore() {
        setCurrentStatus(STATUS_NO_MORE);
    }

    public void statusEnable(boolean enable) {
        setCurrentStatus(enable ? STATUS_NORMAL : STATUS_DISABLE);
    }

    private void syncStatus() {
        if (myHolder == null) {
            return;
        }

        switch (currentStatus) {
            case STATUS_IS_LOADING:
            case STATUS_NORMAL_LOADING: {
                myHolder.getProgressBarLoading().setVisibility(View.VISIBLE);
                myHolder.getTvClick().setVisibility(View.GONE);
                break;
            }
            case STATUS_SHOW_CLICK: {
                myHolder.getProgressBarLoading().setVisibility(View.GONE);
                myHolder.getTvClick().setVisibility(View.VISIBLE);
                break;
            }
            case STATUS_NO_MORE:
            case STATUS_NORMAL:
            case STATUS_DISABLE: {
                myHolder.getProgressBarLoading().setVisibility(View.GONE);
                myHolder.getTvClick().setVisibility(View.GONE);
                break;
            }
            default:
                break;
        }
    }

    private int findFirstInScreen() {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
        }
        if (manager instanceof StaggeredGridLayoutManager) {
            int[] positions = ((StaggeredGridLayoutManager) manager).findFirstCompletelyVisibleItemPositions(null);
            if (positions.length >= 0) {
                return positions[positions.length - 1];
            }
        }
        return -1;
    }

    private int findLastInScreen() {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
        }
        if (manager instanceof StaggeredGridLayoutManager) {
            int[] positions = ((StaggeredGridLayoutManager) manager).findLastCompletelyVisibleItemPositions(null);
            if (positions.length >= 0) {
                return positions[positions.length - 1];
            }
        }
        return -1;
    }

    private boolean shouldShowClickInsteadOfLoading() {
        // all items completely shown in screen
        int lastInScreen = findLastInScreen();
        int firstInScreen = findFirstInScreen();
        int itemCount = targetAdapter.getItemCount();
        if (lastInScreen == -1 && firstInScreen == -1) {
            return true;
        }
        return lastInScreen <= itemCount && firstInScreen == 0;
    }

    private void checkLoadingOrClick() {
        if (currentStatus == STATUS_NORMAL || currentStatus == STATUS_SHOW_CLICK) {
            if (shouldShowClickInsteadOfLoading()) {
                setCurrentStatus(STATUS_SHOW_CLICK);
            } else {
                setCurrentStatus(STATUS_NORMAL_LOADING);
            }
        }
    }

    private boolean isMyView(int position) {
        return position >= targetAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isMyView(position)) {
            return myTypeRes;
        }
        return targetAdapter.getItemViewType(position);
    }

    private boolean shouldShowLoading(int remainingCount) {
        return findLastInScreen() >= getItemCount() - 1 - remainingCount;
    }

    public void setOnLoadListener(final int remainingCount, Runnable loadListener) {
        if (loadListener == null) {
            return;
        }
        onLoad = loadListener;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!(currentStatus == STATUS_NORMAL || currentStatus == STATUS_NORMAL_LOADING)) {
                    return;
                }
                if (dy > 0 && shouldShowLoading(remainingCount)) {
                    setCurrentStatus(STATUS_IS_LOADING);
                    onLoad.run();
                }
            }
        });
    }

    private Runnable onLoad;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == myTypeRes) {
            View view = LayoutInflater.from(recyclerView.getContext()).inflate(viewType, parent, false);
            myHolder = new MyHolder(view);
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentStatus == STATUS_SHOW_CLICK && onLoad != null) {
                        setCurrentStatus(STATUS_IS_LOADING);
                        onLoad.run();
                    }
                }
            });
            return myHolder;
        }
        return targetAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            checkLoadingOrClick();
        } else {
            targetAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return targetAdapter.getItemCount() + 1;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder == null) {
            return;
        }

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params != null && params instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == getItemCount() - 1) {
            ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
            return;
        }
        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isMyView(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }
}
