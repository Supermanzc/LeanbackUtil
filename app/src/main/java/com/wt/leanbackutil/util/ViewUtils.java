package com.wt.leanbackutil.util;

import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

/**
 * @author junyan
 *         放大和缩小工具类
 */
public class ViewUtils {

    /**
     * 动画 放大/缩小
     *
     * @param view
     * @param hasFocus
     */
    public static void scaleView(View view, boolean hasFocus) {
        float scale = hasFocus ? 1.2f : 1.0f;
        view.animate().scaleX(scale).scaleY(scale).setInterpolator(new AccelerateInterpolator()).setDuration(200);
    }

    /**
     * 改变图片的颜色
     * 参考：https://www.jianshu.com/p/9cae2250d0ed
     *
     * @param view  ImageView
     * @param color 颜色格式：0xA6FFFFFF
     */
    public static void setViewColorFilter(ImageView view, int color) {
        view.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

}