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
package android.support.v17.leanback.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.v17.leanback.R;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * A {@link android.view.ViewGroup} that shows items in a vertically scrolling list. The items
 * come from the {@link RecyclerView.Adapter} associated with this view.
 * <p>
 * {@link RecyclerView.Adapter} can optionally implement {@link FacetProviderAdapter} which
 * provides {@link FacetProvider} for a given view type;  {@link RecyclerView.ViewHolder}
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
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
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

    private Paint mTempPaint = new Paint();
    private boolean mFadingLowEdge;
    private int mLowFadeShaderLength;
    private int mLowFadeShaderOffset;
    private LinearGradient mLowFadeShader;
    private Bitmap mTempBitmapLow;
    private boolean mFadingHighEdge; // 底部
    private int mHighFadeShaderLength;
    private int mHighFadeShaderOffset;
    private LinearGradient mHighFadeShader;
    private Bitmap mTempBitmapHigh;

    /**
     * https://juejin.im/entry/58bb9c8c570c35006c5f6933
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        final boolean needsFadingLow = needsFadingLowEdge();
        final boolean needsFadingHigh = needsFadingHighEdge();
        if (!needsFadingLow) {
            mTempBitmapLow = null;
        }
        if (!needsFadingHigh) {
            mTempBitmapHigh = null;
        }
        //
        if (!needsFadingLow && !needsFadingHigh) {
            super.draw(canvas);
            return;
        }
        //
        int lowEdge = -mLowFadeShaderOffset;
        int highEdge = getHeight() + mLowFadeShaderOffset;
        if (getClipToPadding() || getClipChildren()) {
            lowEdge += getPaddingTop();
            highEdge -= getPaddingBottom();
        }
        // draw not-fade content
        int save = canvas.save();
//        canvas.clipRect(0, lowEdge + (mFadingLowEdge ? mLowFadeShaderLength : 0),
//                getWidth(), highEdge - (mFadingHighEdge ? mHighFadeShaderLength : 0));
//        canvas.clipRect(0, lowEdge,
//                getWidth(), highEdge);
        super.draw(canvas);
        canvas.restoreToCount(save);
        //
        mTempPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        // top 顶部阴影.
        if (needsFadingLow) {
//            mLowFadeShader = new LinearGradient(0, lowEdge, 0, lowEdge + mLowFadeShaderLength,
//                    Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
            mTempPaint.setShader(mLowFadeShader);
            canvas.drawRect(0,
                    lowEdge,
                    getWidth(),
                    lowEdge + mLowFadeShaderLength, mTempPaint);
        }
        // bottom 底部阴影.
        if (needsFadingHigh) {
            mHighFadeShader = new LinearGradient(0, highEdge - mHighFadeShaderLength, 0, highEdge,
                    Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP);
            mTempPaint.setShader(mHighFadeShader);
            canvas.drawRect(0,
                    highEdge - mHighFadeShaderLength,
                    getWidth(),
                    highEdge, mTempPaint);
        }
    }

    /**
     * Top顶部判断
     * @return true 显示顶部阴影 false 反之
     */
    private boolean needsFadingLowEdge() {
        if (!mFadingLowEdge) {
            return false;
        }
        final int c = getChildCount();
        for (int i = 0; i < c; i++) {
            View view = getChildAt(i);
            if (mLayoutManager.getOpticalTop(view) <
                    getPaddingTop() - mLowFadeShaderOffset) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the fade out Top edge to transparent.   Note turn on fading edge is very expensive
     * that you should turn off when VerticalGridView is scrolling.
     */
    public final void setFadingTopEdge(boolean fading) {
        if (mFadingLowEdge != fading) {
            mFadingLowEdge = fading;
            if (!mFadingLowEdge) {
                mTempBitmapLow = null;
            }
            invalidate();
            updateLayerType();
        }
    }

    /**
     * Returns true if Top edge fading is enabled.
     */
    public final boolean getFadingTopEdge() {
        return mFadingLowEdge;
    }

    /**
     * Sets the Top edge fading length in pixels.
     */
    public final void setFadingTopEdgeLength(int fadeLength) {
        if (mLowFadeShaderLength != fadeLength) {
            mLowFadeShaderLength = fadeLength;
            if (mLowFadeShaderLength != 0) {
                mLowFadeShader = new LinearGradient(0, 0, 0, mLowFadeShaderLength,
                        Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
            } else {
                mLowFadeShader = null;
            }
            invalidate();
        }
    }

    /**
     * Returns the Top edge fading length in pixels.
     */
    public final int getFadingTopEdgeLength() {
        return mLowFadeShaderLength;
    }

    /**
     * Sets the distance in pixels between fading start position and Top padding edge.
     * The fading start position is positive when start position is inside Top padding
     * area.  Default value is 0, means that the fading starts from Top padding edge.
     */
    public final void setFadingTopEdgeOffset(int fadeOffset) {
        if (mLowFadeShaderOffset != fadeOffset) {
            mLowFadeShaderOffset = fadeOffset;
            invalidate();
        }
    }

    /**
     * Returns the distance in pixels between fading start position and Top padding edge.
     * The fading start position is positive when start position is inside Top padding
     * area.  Default value is 0, means that the fading starts from Top padding edge.
     */
    public final int getFadingTopEdgeOffset() {
        return mLowFadeShaderOffset;
    }

    private Bitmap getTempBitmapLow() {
        if (mTempBitmapLow == null
                || mTempBitmapLow.getHeight() != mLowFadeShaderLength
                || mTempBitmapLow.getWidth() != getWidth()) {
            mTempBitmapLow = Bitmap.createBitmap(getWidth(), mLowFadeShaderLength,
                    Bitmap.Config.ARGB_8888);
        }
        return mTempBitmapLow;
    }

    /**
     * Bottom底部判断
     * @return true 显示底部阴影 false 反之
     */
    private boolean needsFadingHighEdge() {
        if (!mFadingHighEdge) {
            return false;
        }
        final int c = getChildCount();
        for (int i = c - 1; i >= 0; i--) {
            View view = getChildAt(i);
            if (mLayoutManager.getOpticalBottom(view) > getHeight()
                    - getPaddingBottom() + mHighFadeShaderOffset) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the fade out bottom edge to transparent.   Note turn on fading edge is very expensive
     * that you should turn off when VerticalGridView is scrolling.
     */
    public final void setFadingBottomEdge(boolean fading) {
        if (mFadingHighEdge != fading) {
            mFadingHighEdge = fading;
            if (!mFadingHighEdge) {
//                mTempBitmapHigh = null;
            }
            invalidate();
            updateLayerType();
        }
    }

    /**
     * Returns true if fading bottom edge is enabled.
     */
    public final boolean getFadingBottomEdge() {
        return mFadingHighEdge;
    }

    /**
     * Sets the bottom edge fading length in pixels.
     */
    public final void setFadingBottomEdgeLength(int fadeLength) {
        if (mHighFadeShaderLength != fadeLength) {
            mHighFadeShaderLength = fadeLength;
            if (mHighFadeShaderLength != 0) {
                mHighFadeShader = new LinearGradient(0, 0, 0, mHighFadeShaderLength,
                        Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP);
            } else {
                mHighFadeShader = null;
            }
            invalidate();
        }
    }

    /**
     * Returns the bottom edge fading length in pixels.
     */
    public final int getFadingBottomEdgeLength() {
        return mHighFadeShaderLength;
    }

    /**
     * Returns the distance in pixels between fading start position and bottom padding edge.
     * The fading start position is positive when start position is inside bottom padding
     * area.  Default value is 0, means that the fading starts from bottom padding edge.
     */
    public final void setFadingBottomEdgeOffset(int fadeOffset) {
        if (mHighFadeShaderOffset != fadeOffset) {
            mHighFadeShaderOffset = fadeOffset;
            invalidate();
        }
    }

    /**
     * Sets the distance in pixels between fading start position and bottom padding edge.
     * The fading start position is positive when start position is inside bottom padding
     * area.  Default value is 0, means that the fading starts from bottom padding edge.
     */
    public final int getFadingBottomEdgeOffset() {
        return mHighFadeShaderOffset;
    }

    private Bitmap getTempBitmapHigh() {
        if (mTempBitmapHigh == null
                || mTempBitmapHigh.getHeight() != mHighFadeShaderLength
                || mTempBitmapHigh.getWidth() != getWidth()) {
            // TODO: fix logic for sharing mTempBitmapLow
            if (false && mTempBitmapLow != null
                    && mTempBitmapLow.getHeight() == mHighFadeShaderLength
                    && mTempBitmapLow.getWidth() == getWidth()) {
                // share same bitmap for low edge fading and high edge fading.
                mTempBitmapHigh = mTempBitmapLow;
            } else {
                mTempBitmapHigh = Bitmap.createBitmap(getWidth(), mHighFadeShaderLength,
                        Bitmap.Config.ARGB_8888);
            }
        }
        return mTempBitmapHigh;
    }

    /**
     * Updates the layer type for this view.
     * If fading edges are needed, use a hardware layer.  This works around the problem
     * that when a child invalidates itself (for example has an animated background),
     * the parent view must also be invalidated to refresh the display list which
     * updates the the caching bitmaps used to draw the fading edges.
     */
    private void updateLayerType() {
        if (mFadingLowEdge || mFadingHighEdge) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            setWillNotDraw(false);
        } else {
            setLayerType(View.LAYER_TYPE_NONE, null);
            setWillNotDraw(true);
        }
    }

}
