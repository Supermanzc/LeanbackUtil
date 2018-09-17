package com.wt.leanbackutil.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wt.leanbackutil.util.LogUtil;

/**
 * Created by DELL on 2018/9/17.
 * 横线
 */

public class MetroTitleDecoration extends RecyclerView.ItemDecoration{

    private MetroTitleListener mAdapter;

    public MetroTitleDecoration(MetroTitleListener adapter){
        this.mAdapter = adapter;
    }

    private void createAndMeasureTitleView(RecyclerView parent){
        if(mAdapter == null){
            return;
        }

//        mAdapter.getTitleView(parent);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        LogUtil.e("-------------rect=" + outRect);
        LogUtil.e("-------------view=" + view.getClass().getSimpleName());
        LogUtil.e("----------state=" + state);

    }

    public interface MetroTitleListener {
        View getTitleView(int index, RecyclerView parent);
    }
}
