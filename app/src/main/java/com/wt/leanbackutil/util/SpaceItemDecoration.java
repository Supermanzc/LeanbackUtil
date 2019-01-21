package com.wt.leanbackutil.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mRightSpace;
    private int mLeftSpace;
    private int mTopSpace;
    private int mBottomSpace;

    public SpaceItemDecoration(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        this.mRightSpace = rightSpace;
        this.mLeftSpace = leftSpace;
        this.mTopSpace = topSpace;
        this.mBottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = mLeftSpace;
        outRect.left = mRightSpace;
        outRect.top = mTopSpace;
        outRect.bottom = mBottomSpace;
    }

}