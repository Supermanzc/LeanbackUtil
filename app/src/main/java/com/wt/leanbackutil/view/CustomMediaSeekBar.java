
package com.wt.leanbackutil.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

/**
 * 播放进度条
 */
public class CustomMediaSeekBar extends AppCompatSeekBar {
    private Drawable thumb = null;

    /**
     * @param context
     */
    public CustomMediaSeekBar(Context context) {
        super(context);
    }

    public CustomMediaSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomMediaSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置进度条滑块
     *
     * @param thumb 滑块图片
     */
    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        this.thumb = thumb;
    }

    public int getCurrentThumbX() {
        int max = getMax();
        float scale = max > 0 ? (float) getProgress() / (float) max : 0;
        int available = getWidth() - getPaddingLeft() - getPaddingRight();
        if (thumb == null) {
            throw new NullPointerException("SeekBar图标为空，请使用setThumb接口设置图标");
        }
        int thumbPos = (int) (scale * available);
        return thumbPos;
    }

}
