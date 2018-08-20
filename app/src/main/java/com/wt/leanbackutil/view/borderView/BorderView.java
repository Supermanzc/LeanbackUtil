package com.wt.leanbackutil.view.borderView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * BorderView 移动边框 移动，放大，缩小动画.
 * <div class="special reference">
 * <h3>【开发人员指南】</h3>
 * <p>有两种使用方式</p>
 * <ul>
 * <li>在XML布局可以设置attr中的属性</li>
 * <li>使用 getBorderEffect 返回 {@link BorderEffect}，然后使用函数设置属性 <br>
 * eg:getBorderEffect().setPadding(10).setScaleX(1.2f).setScaleY(1.2f)... ...</li>
 * </ul>
 * </div>
 * <h3>【XML attr 属性】</h3>
 * <p>[注意] 在XML布局要加入此段 : xmlns:tv="http://schemas.android.com/apk/res-auto"</p>
 * <p>com.open.tv.R.styleable#BorderView_bv_duration 边框动画完成时间 (tv:bv_duration="300")</p>
 * <p>com.open.tv.R.styleable#BorderView_bv_res 边框图片 (tv:bv_res="@drawable/white_light_10")</p>
 * <p>com.open.tv.R.styleable#BorderView_bv_scale_x 缩放比例x (tv:bv_scale_x="1.0")</p>
 * <p>com.open.tv.R.styleable#BorderView_bv_scale_y 缩放比例y (tv:bv_scale_y="1.0")</p>
 * <p>com.open.tv.R.styleable#BorderView_bv_padding 边框间距 (tv:bv_padding="10dp")</p>
 * <p>com.open.tv.R.styleable#BorderView_bv_visible 是否可见(boolean)，参考 {@link BorderEffect#setVisible(boolean)} </p>
 * <p>com.open.tv.R.styleable#BorderView_bv_enabled 是否可用(boolean) ，参考 {@link BorderEffect#setEnabled(boolean)} </p>
 *
 * @Author: hailong.qiu
 * @Maintainer: hailong.qiu
 * @Date: 17-07-01 下午7:53
 * @Copyright: 2016 www.andriodtvdev.com Inc. All rights reserved.
 */
public class BorderView extends FrameLayout {

    private BorderEffect mBorderEffect;

    public BorderView(Context context) {
        this(context, null);
        attchToWindow(context);
    }

    public BorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 读取的优先级:
     * 1.配置中心. (最低)
     * 2.xml
     * 3.initValues.
     * 4.API函数. (最高)
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public BorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 设置默认的 BorderEffect.
        BorderEffect borderEffect = new BorderEffect();
         /* 配置中心读取配置 */
        borderEffect.abtainConfig(this);
        borderEffect.obtainAttributes(this, context, attrs);
        setBorderEffect(borderEffect);
        ((ViewGroup)getParent()).setClipChildren(false);
        ((ViewGroup)getParent()).setClipToPadding(false);
        // BUG, 因为第一次的时候，边框就显示出来了.
        this.setVisibility(View.INVISIBLE);
    }

    /**
     * 因为 BorderView(Context context)，<br>
     * 所以自动自己添加到父控件.
     *
     * @param context
     */
    private void attchToWindow(Context context) {
    }

    /**
     * 设置 BorderEffect.
     *
     * @param borderEffect
     */
    public void setBorderEffect(BorderEffect borderEffect) {
        this.mBorderEffect = borderEffect;
        if (this.mBorderEffect == null) {
            this.mBorderEffect = new BorderEffect();
        }
        this.mBorderEffect.initBorderView(this);
        this.mBorderEffect.initValues();
    }

    /**
     * 获取 {@link BorderEffect}
     *
     * @return {@link BorderEffect}
     */
    public BorderEffect getBorderEffect() {
        return this.mBorderEffect;
    }

    /**
     * 设置焦点控件,BorderView移动到焦点控件位置.<br>
     * 默认缩放比例为 x :1.0, y: 1.0
     *
     * @param newFocus 焦点控件(BorderView移动的目的地)
     */
    public void setTargeView(View newFocus) {
        this.mBorderEffect.setTargeView(newFocus);
    }

    /**
     * 设置焦点控件,BorderView移动到焦点控件位置. <br>
     * 焦点控件按照 scaleX, scaleY来缩放.
     *
     * @param newFocus 焦点控件(BorderView移动的目的地)
     * @param scaleX   缩放比例x.
     * @param scaleY   缩放比例y.
     */
    public void setTargeView(View newFocus, float scaleX, float scaleY) {
        this.mBorderEffect.setTargeView(newFocus, scaleX, scaleY);
    }

}
