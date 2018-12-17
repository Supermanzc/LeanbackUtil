/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.open.leanback.widget;

import android.app.Instrumentation;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.open.leanback.R;
import com.open.leanback.util.ThreadPoolManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;


/**
 * A {@link android.view.ViewGroup} that shows items in a vertically scrolling list. The items
 * come from the {@link Adapter} associated with this view.
 * <p>
 * {@link Adapter} can optionally implement {@link FacetProviderAdapter} which
 * provides {@link FacetProvider} for a given view type;  {@link ViewHolder}
 * can also implement {@link FacetProvider}.  Facet from ViewHolder
 * has a higher priority than the one from FacetProiderAdapter associated with viewType.
 * Supported optional facets are:
 * <ol>
 * <li> {@link ItemAlignmentFacet}
 * When this facet is provided by ViewHolder or FacetProviderAdapter,  it will
 * override the item alignment settings set on VerticalGridView.  This facet also allows multiple
 * alignment positions within one ViewHolder.
 * </li>
 * </ol>
 */
public class VerticalGridView extends BaseGridView {

    public VerticalGridView(Context context) {
        this(context, null);
    }

    public VerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mLayoutManager.setOrientation(VERTICAL);
        initAttributes(context, attrs);
    }

    protected void initAttributes(Context context, AttributeSet attrs) {
        initBaseGridViewAttributes(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbVerticalGridView);
        setColumnWidth(a);
        setNumColumns(a.getInt(R.styleable.lbVerticalGridView_numberOfColumns, 1));
        a.recycle();
    }

    void setColumnWidth(TypedArray array) {
        TypedValue typedValue = array.peekValue(R.styleable.lbVerticalGridView_columnWidth);
        if (typedValue != null) {
            int size = array.getLayoutDimension(R.styleable.lbVerticalGridView_columnWidth, 0);
            setColumnWidth(size);
        }
    }

    /**
     * Sets the number of columns.  Defaults to one.
     */
    public void setNumColumns(int numColumns) {
        mLayoutManager.setNumRows(numColumns);
        requestLayout();
    }

    /**
     * Sets the column width.
     *
     * @param width May be {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}, or a size
     *              in pixels. If zero, column width will be fixed based on number of columns
     *              and view width.
     */
    public void setColumnWidth(int width) {
        mLayoutManager.setRowHeight(width);
        requestLayout();
    }

//    long mOldTime = 0;
//    long mTimeStamp = 280;
//
//    /**
//     *  设置按键滚动的时间间隔.
//     *  在小于time的间隔内消除掉.
//     */
//    public void setKeyScrollTime(long time) {
//        this.mTimeStamp = time;
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        /**
//         *  用于优化按键快速滚动卡顿的问题.
//         */
//        if (event.getRepeatCount() >= 2 && event.getAction()==KeyEvent.ACTION_DOWN){
//            long currentTime = System.currentTimeMillis();
//            if (currentTime - mOldTime <= mTimeStamp) {
//                return true;
//            }
//            mOldTime = currentTime;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    private static final int MORE_STATE_END = 0; // 加载结束
    private static final int MORE_SATTE_LOADING = 1; // 加载中
    private static final int MORE_STATE_NO_DATA = -1; // 加载更多已没有数据.

    private int mMoreState = MORE_STATE_END;
    private OnLoadMoreListener mOnLoadMoreListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener cb) {
        this.mOnLoadMoreListener = cb;
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) {
            // 加载更多回调
            if (getLastVisiblePosition() >= getAdapter().getItemCount() - 1) {
                if (null != mOnLoadMoreListener) {
                    if (mMoreState == MORE_STATE_END) {
                        mMoreState = MORE_SATTE_LOADING;
                        mOnLoadMoreListener.onLoadMore();
                        Log.d("hailongqiu", "onScrollStateChanged onLoadMore 加载更多");
                    }
                }
            }
        }
        super.onScrollStateChanged(state);
    }

    public void resetMoreRefresh() {

    }

    /**
     * 加载更多结束调用.
     */
    public void endMoreRefreshComplete() {
        mMoreState = MORE_STATE_END;
    }

    /**
     * 没有更多加载.
     */
    public void endRefreshingWithNoMoreData() {
        mMoreState = MORE_STATE_NO_DATA;
    }

    public int getLastVisiblePosition() {
        final int childCount = getChildCount();
        if (childCount == 0) {
            return 0;
        } else {
            return getChildAdapterPosition(getChildAt(childCount - 1));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 禁止滑动翻页
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 禁止滑动翻页
        return false;
    }
}
