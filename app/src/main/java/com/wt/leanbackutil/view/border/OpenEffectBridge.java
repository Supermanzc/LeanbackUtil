package com.wt.leanbackutil.view.border;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.wt.leanbackutil.App;
import com.wt.leanbackutil.R;
import com.zhouwei.mzbanner.CustomViewPager;

import java.lang.ref.WeakReference;

import hk.reco.baselib.util.Logger;

public class OpenEffectBridge extends BaseEffectBridgeWrapper {

    public static final int DEFAULT_TRAN_DUR_ANIM = 200;
    private int mTranDurAnimTime = DEFAULT_TRAN_DUR_ANIM;
    private AnimatorSet mCurrentAnimatorSet;
    private boolean isInDraw = false;
    private boolean mIsHide = false;
    private boolean mAnimEnabled = true;
    private boolean isDrawUpRect = true;
    private View mFocusView;
    private NewAnimatorListener mNewAnimatorListener;

    @Override
    public void onInitBridge(MainUpView view) {
        super.onInitBridge(view);
        /**
         * 防止边框第一次出现,<br>
         * 从另一个地方飘过来的问题.<br>
         */
        view.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置是否移动边框在最下层(绘制的层次). <br>
     * true : 移动边框在最上层. 反之否. <br>
     */
    public void setDrawUpRectEnabled(boolean isDrawUpRect) {
        this.isDrawUpRect = isDrawUpRect;
        getMainUpView().invalidate();
    }

    /**
     * 控件动画时间.
     */
    public void setTranDurAnimTime(int time) {
        mTranDurAnimTime = time;
    }

    public int getTranDurAnimTime() {
        return this.mTranDurAnimTime;
    }

    /**
     * 让动画失效.
     */
    public void setAnimEnabled(boolean animEnabled) {
        this.mAnimEnabled = animEnabled;
    }

    public void clearAnimator() {
        mCurrentAnimatorSet.end();
    }

    public boolean isAnimEnabled() {
        return this.mAnimEnabled;
    }

    /**
     * 隐藏移动的边框.
     */
    public void setVisibleWidget(boolean isHide) {
        this.mIsHide = isHide;
        getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
    }

    public boolean isVisibleWidget() {
        return this.mIsHide;
    }

    public interface NewAnimatorListener {
        public void onAnimationStart(OpenEffectBridge bridge, View view, Animator animation);

        public void onAnimationEnd(OpenEffectBridge bridge, View view, Animator animation);
    }

    /**
     * 监听动画的回调.
     */
    public void setOnAnimatorListener(NewAnimatorListener newAnimatorListener) {
        mNewAnimatorListener = newAnimatorListener;
    }

    public NewAnimatorListener getNewAnimatorListener() {
        return mNewAnimatorListener;
    }

    @Override
    public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
        if (!mAnimEnabled)
            return;
        if (oldFocusView != null) {
            oldFocusView.setScaleY(scaleY);
            oldFocusView.setScaleX(scaleX);
//            ((ViewGroup) oldFocusView.getParent()).invalidate();
//			oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start();
        }
    }

    @Override
    public void onFocusView(View focusView, float scaleX, float scaleY) {
        mFocusView = focusView;
        if (!mAnimEnabled) {
            return;
        }
        if (focusView != null) {
            focusView.setScaleY(scaleY);
            focusView.setScaleX(scaleX);
//			focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start(); // 放大焦点VIEW的动画.
            runTranslateAnimation(focusView, scaleX, scaleY);
        }
    }

    @Override
    public void onFocusScaleView(View focusView, float scaleX, float scaleY) {
        if (!mAnimEnabled)
            return;
        if (focusView != null) {
            focusView.setScaleY(scaleY);
            focusView.setScaleX(scaleX);
        }
    }

    @Override
    public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
        mFocusView = toView;
        try {
            flyWhiteBorder(toView, getMainUpView(), scaleX, scaleY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float mScaleX = 0;
    private float mScaleY = 0;

    /**
     * 移动边框的动画处理函数.
     */
    @Override
    public void flyWhiteBorder(final View focusView, View moveView, float scaleX, float scaleY) {
        // 用于修复5.0边框错位问题.
        this.mScaleX = mScaleX;
        this.mScaleY = mScaleY;

        int newWidth = 0;
        int newHeight = 0;
        int oldWidth = 0;
        int oldHeight = 0;

        float newX = 0;
        float newY = 0;

        if (focusView != null) {
            newWidth = (int) (Math.rint(focusView.getMeasuredWidth() * scaleX));
            newHeight = (int) (Math.rint(focusView.getMeasuredHeight() * scaleY));
            oldWidth = moveView.getMeasuredWidth();
            oldHeight = moveView.getMeasuredHeight();
            // 获取moveView在屏幕上的位置.
            Rect fromRect = findLocationWithView(moveView);
            // 获取focusView在屏幕上的位置.
            Rect toRect = findLocationWithView(focusView);
            int x = toRect.left - fromRect.left;
            int y = toRect.top - fromRect.top;
            newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
            newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
            getMainUpView().setLayoutParams(new RelativeLayout.LayoutParams(newWidth, newHeight));
            getMainUpView().setTranslationX(newX);
            getMainUpView().setTranslationY(newY);
            getMainUpView().setVisibility(View.VISIBLE);
//            getMainUpView().scrollTo(toRect.left, toRect.top);
        }


        // 取消之前的动画.
//        if (mCurrentAnimatorSet != null) {
//            mCurrentAnimatorSet.cancel();
//        }

//        ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(moveView, "translationX", newX);
//        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(moveView, "translationY", newY);
//        // BUG，因为缩放会造成图片失真(拉伸).
//        // hailong.qiu 2016.02.26 修复 :)
//        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "width", oldWidth,
//                (int) newWidth);
//        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "height", oldHeight,
//                (int) newHeight);
//        //
//        AnimatorSet mAnimatorSet = new AnimatorSet();
//        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
//        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
//        mAnimatorSet.setDuration(mTranDurAnimTime);
//        mAnimatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                if (!isDrawUpRect)
//                    isInDraw = false;
//                if (mIsHide) {
//                    getMainUpView().setVisibility(View.INVISIBLE);
//                }
//                if (mNewAnimatorListener != null)
//                    mNewAnimatorListener.onAnimationStart(OpenEffectBridge.this, focusView, animation);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//                if (!isDrawUpRect)
//                    isInDraw = false;
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if (!isDrawUpRect)
//                    isInDraw = true;
//                getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
//                if (mNewAnimatorListener != null)
//                    mNewAnimatorListener.onAnimationEnd(OpenEffectBridge.this, focusView, animation);
//
//                // XF add（先锋TV开发(404780246)修复)
//                // BUG:5.0系统边框错位.
////				if (Utils.getSDKVersion() >= 21) {
////					int newWidth = (int) (focusView.getMeasuredWidth() *
////							mScaleX);
////					int newHeight = (int) (focusView.getMeasuredHeight() *
////							mScaleY);
////					getMainUpView().getLayoutParams().width = newWidth;
////					getMainUpView().getLayoutParams().height = newHeight;
////					getMainUpView().requestLayout();
////				}
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                if (!isDrawUpRect)
//                    isInDraw = false;
//            }
//        });
//        mAnimatorSet.start();
//        mCurrentAnimatorSet = mAnimatorSet;
    }

    private RecyclerViewScrollListener mRecyclerViewScrollListener;
    private WeakReference<RecyclerView> mWeakRecyclerView;
    private boolean mReAnim = false;

    protected Rect findOffsetDescendantRectToMyCoords(View descendant) {
        final ViewGroup root = (ViewGroup) getMainUpView().getParent();
        final Rect rect = new Rect();
        mReAnim = false;
        if (descendant == root) {
            return rect;
        }

        final View srcDescendant = descendant;

        ViewParent theParent = descendant.getParent();
        Object tag;
        Point point;

        // search and offset up to the parent
        while ((theParent != null)
                && (theParent instanceof View)
                && (theParent != root)) {
            Log.d("tt", "theParent---------------------------theParent=" + theParent.getClass().getSimpleName());
//            if (descendant instanceof ViewPager || theParent instanceof ViewPager) {
//                rect.offset(0, descendant.getTop() - descendant.getScrollY());
//            } else {
//                rect.offset(descendant.getLeft() - descendant.getScrollX(),
//                        descendant.getTop() - descendant.getScrollY());
//            }

            if (descendant instanceof ViewPager || theParent instanceof ViewPager || theParent instanceof CustomViewPager || descendant instanceof CustomViewPager) {
                rect.offset(0, descendant.getTop() - descendant.getScrollY());
                if (theParent instanceof ViewPager) {
                    rect.offset(App.getInstance().getResources().getDimensionPixelSize(R.dimen.w_443), descendant.getTop() - descendant.getScrollY());
                }
            } else {
                rect.offset(descendant.getLeft() - descendant.getScrollX(),
                        descendant.getTop() - descendant.getScrollY());
            }

            //兼容TvRecyclerView
            if (theParent instanceof RecyclerView) {
                final RecyclerView rv = (RecyclerView) theParent;
                registerScrollListener(rv);
                tag = rv.getTag();
                if (null != tag && tag instanceof Point) {
                    point = (Point) tag;
                    rect.offset(-point.x, -point.y);
                }
                if (null == tag && rv.getScrollState() != RecyclerView.SCROLL_STATE_IDLE
                        && (mRecyclerViewScrollListener.mScrolledX != 0 || mRecyclerViewScrollListener.mScrolledY != 0)) {
                    mReAnim = true;
                }
            }
            descendant = (View) theParent;
            theParent = descendant.getParent();
        }

        // now that we are up to this view, need to offset one more time
        // to get into our coordinate space
        if (theParent == root) {
            rect.offset(descendant.getLeft() - descendant.getScrollX(),
                    descendant.getTop() - descendant.getScrollY());
        }

        rect.right = rect.left + srcDescendant.getMeasuredWidth();
        rect.bottom = rect.top + srcDescendant.getMeasuredHeight();

        return rect;
    }

    private void registerScrollListener(RecyclerView recyclerView) {
        if (null != mWeakRecyclerView && mWeakRecyclerView.get() == recyclerView) {
            return;
        }

        if (null == mRecyclerViewScrollListener) {
            mRecyclerViewScrollListener = new RecyclerViewScrollListener(this);
        }

        if (null != mWeakRecyclerView && null != mWeakRecyclerView.get()) {
            mWeakRecyclerView.get().removeOnScrollListener(mRecyclerViewScrollListener);
            mWeakRecyclerView.clear();
        }

        recyclerView.removeOnScrollListener(mRecyclerViewScrollListener);
        recyclerView.addOnScrollListener(mRecyclerViewScrollListener);
        mWeakRecyclerView = new WeakReference<>(recyclerView);
    }

    private static class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        private WeakReference<OpenEffectBridge> mFocusBorder;
        private int mScrolledX = 0, mScrolledY = 0;

        public RecyclerViewScrollListener(OpenEffectBridge border) {
            mFocusBorder = new WeakReference<>(border);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mScrolledX = Math.abs(dx) == 1 ? 0 : dx;
            mScrolledY = Math.abs(dy) == 1 ? 0 : dy;
//            Logger.i(TAG, "onScrolled...dx=" + dx + " dy=" + dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                final OpenEffectBridge border = mFocusBorder.get();
                final View focused = recyclerView.getFocusedChild();
                if (null != border && null != focused) {
                    if (border.mReAnim || mScrolledX != 0 || mScrolledY != 0) {
                        Log.i("onScrollStateChanged", "onScrollStateChanged...scaleX = " + border.mScaleX + " scaleY = " + border.mScaleY);
                        border.flyWhiteBorder(focused, border.getMainUpView(), border.mScaleX, border.mScaleY);
                    }
                }
                mScrolledX = mScrolledY = 0;
            }
        }
    }

    /**
     * 重寫了繪製的函數.
     */
    @Override
    public boolean onDrawMainUpView(Canvas canvas) {
        canvas.save();
        if (!isDrawUpRect) {
            // 绘制阴影.
            onDrawShadow(canvas);
            // 绘制最上层的边框.
            onDrawUpRect(canvas);
        }
        // 绘制焦点子控件.
        if (mFocusView != null && (!isDrawUpRect && isInDraw)) {
            onDrawFocusView(canvas);
        }
        //
        if (isDrawUpRect) {
            // 绘制阴影.
            onDrawShadow(canvas);
            // 绘制最上层的边框.
            onDrawUpRect(canvas);
        }
        canvas.restore();
        return true;
    }

    public void onDrawFocusView(Canvas canvas) {
        View view = mFocusView;
        canvas.save();
        float scaleX = (float) (getMainUpView().getWidth()) / (float) view.getWidth();
        float scaleY = (float) (getMainUpView().getHeight()) / (float) view.getHeight();
        canvas.scale(scaleX, scaleY);
        view.draw(canvas);
        canvas.restore();
    }

}