package com.wt.leanbackutil.view.borderView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;

import com.wt.leanbackutil.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个抽出来的Effect.<br>
 * 你可以继承它来改写一些函数，<br>
 * Effect 是从BorderView抽出来的类.
 * 主要用于动画的封装.
 * 比如 <br>
 * <p>
 * {@link BorderEffect#initAnimatorList(TargeValues)}<br>
 * {@link BorderEffect#getMoveAnimatorList(TargeValues)}<br>
 * {@link BorderEffect#getScaleAnimatorList}<br>
 *
 * @Author: hailong.qiu
 * @Maintainer: hailong.qiu
 * @Date: 17-07-01 下午7:52
 * @Copyright: 2016 www.androidtvdev.com Inc. All rights reserved.
 */
public class BorderEffect {
    // 默认动画完成时间
    private static final int DEFAULT_DURATION = 300;
    // 默认缩放比例.
    private static final float DEFAULT_SCALE = 1.0f;

    private Interpolator mInterpolator;
    private int mDuration = DEFAULT_DURATION;
    private float mScaleX = DEFAULT_SCALE;
    private float mScaleY = DEFAULT_SCALE;

    private RectF mPaddingRectF = new RectF();
    private BorderView mBorderView;
    private View mOldView;
    private Animator.AnimatorListener mListener;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private boolean mEnabled = true;
    private boolean mVisible = true;

    public void abtainConfig(BorderView borderView) {
        this.mBorderView = borderView;
         /* 调用用户的配置 */
        if (mOnConfigListener != null) {
            mOnConfigListener.onEffectConfig(this.mBorderView, this);
        }
    }

    /**
     * 初始化 attr 属性.
     *
     * @param context
     * @param attrs   TypedArray
     */
    public void obtainAttributes(BorderView borderView, Context context, AttributeSet attrs) {
        this.mBorderView = borderView;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderView);
        float scaleX = ta.getFloat(R.styleable.BorderView_bv_scale_x, DEFAULT_SCALE);
        float scaleY = ta.getFloat(R.styleable.BorderView_bv_scale_y, DEFAULT_SCALE);
        int duration = ta.getInteger(R.styleable.BorderView_bv_duration, DEFAULT_DURATION);
        boolean enabled = ta.getBoolean(R.styleable.BorderView_bv_enabled, true);
        boolean visible = ta.getBoolean(R.styleable.BorderView_bv_visible, true);
        int res = ta.getResourceId(R.styleable.BorderView_bv_res, 0);
        float padding = ta.getDimension(R.styleable.BorderView_bv_padding, 0);
        // 设置属性.
        if (scaleX != DEFAULT_SCALE) {
            setScaleX(scaleX);
        }
        if (scaleY != DEFAULT_SCALE) {
            setScaleY(scaleY);
        }
        if (duration != DEFAULT_DURATION) {
            setDuration(duration);
        }
        setEnabled(enabled);
        setVisible(visible);
        if (res != 0) {
            setBorderRes(res);
        }
        if (padding != 0) {
            setPadding(padding);
        }
        ta.recycle();
    }

    /**
     * 设置资源图片.
     *
     * @param res int drawable目录的资源.
     * @return BorderEffect
     */
    public BorderEffect setBorderRes(int res) {
        mBorderView.setBackgroundResource(res);
        return this;
    }

    /**
     * 设置图片资源
     *
     * @param drawable Drawable
     * @return BorderEffect
     */
    public BorderEffect setBorderDrawable(Drawable drawable) {
        mBorderView.setBackgroundDrawable(drawable);
        return this;
    }

    /**
     * 设置.9图片或图片资源和边框的间距.
     *
     * @param value float
     * @return BorderEffect
     */
    public BorderEffect setPadding(float value) {
        return setPadding(new RectF(value, value, value, value));
    }

    /**
     * 设置边框间距.
     *
     * @param rectF RectF
     * @return BorderEffect
     */
    public BorderEffect setPadding(RectF rectF) {
        mPaddingRectF.set(rectF);
        return this;
    }

    /**
     * 是否可用，<br>
     * 如果为 true，整个BorderView 是可用的.<br>
     * 反之为 false, 是不可用的，动画什么都失效了.
     *
     * @param enabled true 可用 false 不可用
     * @return BorderEffect
     */
    public BorderEffect setEnabled(boolean enabled) {
        this.mEnabled = enabled;
        return this;
    }

    /**
     * 获取是否可用
     *
     * @return boolean类型.
     */
    public boolean isEnabled() {
        return this.mEnabled;
    }

    /**
     * 设置控件是否可见<br>
     * 如果 true ，BorderView 是可见的. <br>
     * 反之为 false, 是见不到的，但是动画还是可以执行.
     *
     * @param visible true 可见 false 隐藏.
     * @return BorderEffect
     */
    public BorderEffect setVisible(boolean visible) {
        this.mVisible = visible;
        getBorderView().setVisibility(this.mVisible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    /**
     * 获取是否可见
     *
     * @return boolean类型.
     */
    public boolean isVisible() {
        return this.mVisible;
    }

    /**
     * 缩放比例X.
     *
     * @param scaleX float类型.
     * @return BorderEffect
     */
    public BorderEffect setScaleX(float scaleX) {
        this.mScaleX = scaleX;
        return this;
    }

    /**
     * 获取缩放比例X.
     *
     * @return float类型.
     */
    public float getScaleX() {
        return this.mScaleX;
    }

    /**
     * 缩放比例Y.
     *
     * @param scaleY float类型.
     * @return BorderEffect
     */
    public BorderEffect setScaleY(float scaleY) {
        this.mScaleY = scaleY;
        return this;
    }

    /**
     * 获取缩放比例Y.
     *
     * @return float类型.
     */
    public float getScaleY() {
        return this.mScaleY;
    }

    /**
     * 设置动画运行完成时间(开始到结束的总共时间).
     *
     * @param duration int类型.
     * @return BorderEffect
     */
    public BorderEffect setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    /**
     * 获取动画的完成时间.
     *
     * @return int类型
     */
    public int getDuration() {
        return this.mDuration;
    }

    /**
     * 获取动画速率(插值)mInterpolator
     *
     * @return Interpolator
     */
    public Interpolator getInterpolator() {
        return mInterpolator != null ? mInterpolator : new DecelerateInterpolator(1);
    }

    /**
     * 设置动画速率，插值.
     *
     * @param interpolator 动画速率，插值.
     * @return
     */
    public BorderEffect setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    /**
     * 缩小/放大的动画列表List. <br>
     * 可以重写次方法，造自己的缩放动画列表.
     *
     * @param view    newFocus 或 oldFocus.
     * @param isScale true 放大 false 缩小.
     * @return 动画列表(List).
     */
    public List<Animator> getScaleAnimatorList(View view, boolean isScale) {
        List<Animator> animatorList = new ArrayList<Animator>();
        if (view == null)
            return animatorList;
        try {
            // 放大.
            float scaleXBefore = DEFAULT_SCALE;
            float scaleYBefore = DEFAULT_SCALE;
            float scaleXAfter = mScaleX;
            float scaleYAfter = mScaleY;
            // 缩小.
            if (!isScale) {
                scaleXBefore = mScaleX;
                scaleYBefore = mScaleY;
                scaleXAfter = DEFAULT_SCALE;
                scaleYAfter = DEFAULT_SCALE;
            }
            ObjectAnimator scaleX = new ObjectAnimator().ofFloat(view, "scaleX", scaleXBefore, scaleXAfter);
            ObjectAnimator scaleY = new ObjectAnimator().ofFloat(view, "scaleY", scaleYBefore, scaleYAfter);
            animatorList.add(scaleX);
            animatorList.add(scaleY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return animatorList;
    }

    /**
     * 移动的动画集合.<br>
     * 可以重写此方法，造自己的移动动画列表.
     *
     * @param targeValues 参考 {@link TargeValues}
     * @return List Animator
     */
    public List<Animator> getMoveAnimatorList(final TargeValues targeValues) {
        List<Animator> animatorList = new ArrayList<>();
        PropertyValuesHolder valuesWithdHolder = PropertyValuesHolder.ofInt("width", targeValues.oldWidth, targeValues.newWidth);
        PropertyValuesHolder valuesHeightHolder = PropertyValuesHolder.ofInt("height", targeValues.oldHeight, targeValues.newHeight);
//        PropertyValuesHolder valuesXHolder = PropertyValuesHolder.ofFloat("translationX", targeValues.oldX, targeValues.newX);
//        PropertyValuesHolder valuesYHolder = PropertyValuesHolder.ofFloat("translationY", targeValues.oldY, targeValues.newY);
        PropertyValuesHolder valuesXHolder = PropertyValuesHolder.ofFloat("translationX", targeValues.newX);
        PropertyValuesHolder valuesYHolder = PropertyValuesHolder.ofFloat("translationY", targeValues.newY);
        final ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(getBorderView(), valuesWithdHolder,
                valuesHeightHolder, valuesYHolder, valuesXHolder);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue("width");
                int height = (int) animation.getAnimatedValue("height");
                float translationX = (float) animation.getAnimatedValue("translationX");
                float translationY = (float) animation.getAnimatedValue("translationY");
                View view = (View) getBorderView();
                if (view != null) {
                    view.getLayoutParams().width = width;
                    view.getLayoutParams().height = height;
                    if (width > 0 || height > 0) {
                        view.requestLayout();
                        view.postInvalidate();
                    }
                }
            }
        });
        animatorList.add(scaleAnimator);
        return animatorList;
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
        setScaleX(scaleX);
        setScaleY(scaleY);
        setTargeView(newFocus);
    }

    /**
     * 设置焦点控件,BorderView移动到焦点控件位置. <br>
     * 焦点控件按照 scaleX, scaleY来缩放.
     *
     * @param newFocus 焦点控件(BorderView移动的目的地)
     */
    public void setTargeView(View newFocus) {
        if (!this.mEnabled) {
            return;
        }
        View targeView = getBorderView();
        // 取消之前的动画.
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
            mAnimatorSet.end();
        }
        //
        int newXY[];
        int oldXY[];
        newXY = getLocation(newFocus);
        oldXY = getLocation(targeView);
        int newWidth;
        int newHeight;
        int oldWidth = (int) (targeView == null ? 0 : targeView.getMeasuredWidth());
        int oldHeight = (int) (targeView == null ? 0 : targeView.getMeasuredHeight());
        newWidth = (int) (newFocus.getMeasuredWidth() * getScaleX());
        newHeight = (int) (newFocus.getMeasuredHeight() * getScaleY());
        int newX = newXY[0] - (int) (Math.rint(mPaddingRectF.left) + (newWidth - newFocus.getMeasuredWidth()) / 2.0f);
        int newY = newXY[1] - (int) (Math.rint(mPaddingRectF.top) + (newHeight - newFocus.getMeasuredHeight()) / 2.0f);
        int oldX = oldXY[0];
        int oldY = oldXY[1];
        newWidth += ((int) Math.rint(mPaddingRectF.right) + (int) Math.rint(mPaddingRectF.left));
        newHeight += ((int) Math.rint(mPaddingRectF.bottom) + (int) Math.rint(mPaddingRectF.top));
        // 设置值.
        TargeValues targeValues = new TargeValues();
        targeValues.oldX = oldX;
        targeValues.oldY = oldY;
        targeValues.newX = newX;
        targeValues.newY = newY;
        targeValues.oldWidth = oldWidth;
        targeValues.oldHeight = oldHeight;
        targeValues.newWidth = newWidth;
        targeValues.newHeight = newHeight;
        targeValues.newFocus = newFocus;
        targeValues.oldFocus = mOldView;
        //
        mAnimatorSet = new AnimatorSet();
        // 设置动画集合.
        initAnimatorList(targeValues);
        // 设置一些基本的属性.
        mAnimatorSet.setInterpolator(getInterpolator());// 设置速率..
        mAnimatorSet.setDuration(getDuration()); // 设置边框移动时间.
        mAnimatorSet.addListener(mAnimatorListener);
        mAnimatorSet.start();
        mOldView = newFocus;
    }

    /**
     * 添加动画监听事件。
     *
     * @param listener Animator.AnimatorListener
     * @return BorderEffect
     */
    public BorderEffect addListener(Animator.AnimatorListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * 重写这里，创建自己的动画集合.<br>
     * 也可以创建自己的动画顺序.
     */
    public BorderEffect initAnimatorList(final TargeValues targeValues) {
        List<Animator> animlist = new ArrayList<>();
        // 缩小
        List<Animator> scaleFalseAnimatorList = getScaleAnimatorList(targeValues.oldFocus, false);
        animlist.addAll(scaleFalseAnimatorList);
        // 移动
        List<Animator> moveAnimatorList = getMoveAnimatorList(targeValues);
        animlist.addAll(moveAnimatorList);
        // 放大.
        if (getScaleX() > DEFAULT_SCALE || getScaleY() > DEFAULT_SCALE) {
            List<Animator> scaleAnimatorList = getScaleAnimatorList(targeValues.newFocus, true);
            animlist.addAll(scaleAnimatorList);
        }
//        mAnimatorSet.playSequentially(animlist); // 串行
        mAnimatorSet.playTogether(animlist); // 并行.
        return this;
    }

    /**
     * 返回动画的集合(mAnimatorSet).
     *
     * @return AnimatorSet
     */
    public AnimatorSet getAnimatorSet() {
        return this.mAnimatorSet;
    }

    /**
     * 初始化 BorderView.
     *
     * @param borderView BorderView
     * @return BorderEffect
     */
    public BorderEffect initBorderView(BorderView borderView) {
        this.mBorderView = borderView;
        return this;
    }

    /**
     * 获取 {@link BorderView}
     *
     * @return BorderView
     */
    public BorderView getBorderView() {
        return this.mBorderView;
    }

    /**
     * 用于保存一些属性.
     */
    public class TargeValues {
        public View newFocus;
        public View oldFocus;
        public int newX;
        public int newY;
        public int oldX;
        public int oldY;
        public int oldWidth;
        public int oldHeight;
        public int newWidth;
        public int newHeight;

        @Override
        public String toString() {
            return "TargeValues{" +
                    "newFocus=" + newFocus +
                    ", oldFocus=" + oldFocus +
                    ", newX=" + newX +
                    ", newY=" + newY +
                    ", oldX=" + oldX +
                    ", oldY=" + oldY +
                    ", oldWidth=" + oldWidth +
                    ", oldHeight=" + oldHeight +
                    ", newWidth=" + newWidth +
                    ", newHeight=" + newHeight +
                    '}';
        }
    }

    Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            onAnimStart(animation);
            if (mListener != null) {
                mListener.onAnimationStart(animation);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onAnimEnd(animation);
            if (mListener != null) {
                mListener.onAnimationEnd(animation);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            onAnimCancel(animation);
            if (mListener != null) {
                mListener.onAnimationCancel(animation);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            onAnimRepeat(animation);
            if (mListener != null) {
                mListener.onAnimationRepeat(animation);
            }
        }
    };

    /**
     * 动画开始的时候做的一些事情.<br>
     * 可以重写此函数.
     *
     * @param animation Animator
     */
    public void onAnimStart(Animator animation) {

    }

    /**
     * 动画结束的时候做的一些事情.<br>
     * 可以重写此函数.
     *
     * @param animation
     */
    public void onAnimEnd(Animator animation) {
        // 第一次边框从其它地方跑出来的问题.
        if (isEnabled() && isVisible()) {
            mBorderView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 动画取消时做的一些时间.<br>
     * 可以重写此函数.
     *
     * @param animation Animator
     */
    public void onAnimCancel(Animator animation) {

    }

    /**
     * 当动画重复时做的一些事情.<br>
     * 可以重写此函数.
     *
     * @param animation Animator
     */
    public void onAnimRepeat(Animator animation) {

    }

    /**
     * 获取控件在父控件的位置.
     *
     * @param v 焦点控件.
     * @return int[]
     */
    private int[] getLocation(View v) {
        int[] location = new int[2];
        if (v != null) {
            Rect rect = findLocationWithView(v);
            location[0] = rect.left;
            location[1] = rect.top;
            if (v instanceof EditText) {
                int[] tempLocation = new int[2];
                v.getLocationOnScreen(tempLocation);
                location[0] = tempLocation[0];
            }
        }
        return location;
    }

    /**
     * 获取控件在父控件的位置.
     *
     * @param view 焦点控件.
     * @return Rect
     */
    private Rect findLocationWithView(View view) {
        ViewGroup root = (ViewGroup) getBorderView().getParent();
        Rect rect = new Rect();
        root.offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }

    /**
     * 初始化移动边框的Effect属性.
     */
    public void initValues() {
    }

    private OnConfigListener mOnConfigListener;

    public void setOnConfigListener(OnConfigListener mOnConfigListener) {
        this.mOnConfigListener = mOnConfigListener;
    }

    public interface OnConfigListener {
        public void onEffectConfig(BorderView borderView, BorderEffect borderEffect);
    }

}
