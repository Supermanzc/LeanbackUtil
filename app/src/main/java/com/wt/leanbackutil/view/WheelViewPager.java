package com.wt.leanbackutil.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.RecommendInfo;
import com.wt.leanbackutil.util.PagerUtil;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author junyuan
 *         轮播ViewPager
 */

public class WheelViewPager<T> extends ViewPager {

    private LinearLayout mIndicatorContainer;
    private ArrayList<ImageView> mIndicators = new ArrayList<>();
    private int[] mIndicatorRes = new int[]{R.drawable.indicator_normal, R.drawable.indicator_selected};
    private PagerUtil pagerUtil;
    private int mCurrentItem = 0;
    private ViewPagerScroller mViewPagerScroller;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public WheelViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    public WheelViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOffscreenPageLimit(0);
        initViewPagerScroll();
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mViewPagerScroller = new ViewPagerScroller(this.getContext(), new AccelerateDecelerateInterpolator());
            mScroller.set(this, mViewPagerScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置数据
     *
     * @param data
     * @param pageSize
     */
    public void setData(List<T> data, int pageSize) {
        pagerUtil = PagerUtil.create(data, pageSize);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
                // 切换indicator
                int realSelectPosition = mCurrentItem % mIndicators.size();
                for (int i = 0; i < pagerUtil.getTotalPage(); i++) {
                    if (i == realSelectPosition) {
                        mIndicators.get(i).setImageResource(mIndicatorRes[1]);
                    } else {
                        mIndicators.get(i).setImageResource(mIndicatorRes[0]);
                    }
                }

                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        mCurrentItem = getCurrentItem();
        setOffscreenPageLimit(pagerUtil.getTotalPage());
    }

    /**
     * 初始化指示器
     *
     * @param linearLayout
     */
    public void initIndicator(LinearLayout linearLayout) {
        mIndicatorContainer = linearLayout;
        mIndicatorContainer.removeAllViews();
        mIndicators.clear();
        for (int i = 0; i < pagerUtil.getTotalPage(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setPadding(6, 0, 6, 0);

            if (i == (mCurrentItem % pagerUtil.getTotalPage())) {
                imageView.setImageResource(mIndicatorRes[1]);
            } else {
                imageView.setImageResource(mIndicatorRes[0]);
            }

            mIndicators.add(imageView);
            mIndicatorContainer.addView(imageView);
        }
    }

    public void addPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public void setPager(List<T> datas, int pageSize, LinearLayout linearLayout, MZHolderCreator mzHolderCreator) {
        if (datas == null || mzHolderCreator == null) {
            return;
        }
        setData(datas, pageSize);
        initIndicator(linearLayout);
        WheelPagerAdapter adapter = new WheelPagerAdapter(mzHolderCreator);
        setAdapter(adapter);
    }

    private class WheelPagerAdapter<T> extends PagerAdapter {

        private MZHolderCreator mzHolderCreator;

        public WheelPagerAdapter(MZHolderCreator mzHolderCreator) {
            this.mzHolderCreator = mzHolderCreator;
        }

        @Override
        public int getCount() {
            return pagerUtil.getTotalPage();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getView(position, container);
            container.addView(view);
            return view;
        }

        private View getView(int position, ViewGroup container) {
            List<T> pagedList = pagerUtil.getPagedList(position + 1);
            MZViewHolder viewHolder = mzHolderCreator.createViewHolder();
            if (viewHolder == null) {
                throw new RuntimeException("can not return a null holder");
            }
            View view = viewHolder.createView(container.getContext());
            if (pagedList != null && pagedList.size() > 0) {
                viewHolder.onBind(container.getContext(), position, pagedList);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


    /**
     * ＊由于ViewPager 默认的切换速度有点快，因此用一个Scroller 来控制切换的速度
     * <p>而实际上ViewPager 切换本来就是用的Scroller来做的，因此我们可以通过反射来</p>
     * <p>获取取到ViewPager 的 mScroller 属性，然后替换成我们自己的Scroller</p>
     */
    public static class ViewPagerScroller extends Scroller {

        private int mDuration = 1000;

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }
}
