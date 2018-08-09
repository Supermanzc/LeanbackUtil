package com.wt.leanbackutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import com.open.leanback.widget.HorizontalGridView;
import com.wt.leanbackutil.adapter.TitleGuideAdapter;
import com.wt.leanbackutil.adapter.listener.AsyncFocusListener;
import com.wt.leanbackutil.fragment.BaseFragment;
import com.wt.leanbackutil.fragment.HomeLoadMoreFragment;
import com.wt.leanbackutil.fragment.HomeRadioFragment;
import com.wt.leanbackutil.fragment.HomeRecommendFragment;
import com.wt.leanbackutil.fragment.adapter.GuideFragmentPageAdapter;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.view.TvViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/6.
 *
 * @author junyan
 *         viewPager+fragment 结构
 */

public class HomeActivity extends FragmentActivity {

    @BindView(R.id.title_guide)
    HorizontalGridView titleGuide;
    @BindView(R.id.tv_view_pager)
    TvViewPager tvViewPager;

    private int[] mainTabs = new int[]{
            R.string.main_tab_home,
            R.string.main_tab_latest_music,
            R.string.main_tab_rank,
            R.string.main_tab_singer,
            R.string.main_tab_song_sheet,
            R.string.main_tab_mv,
            R.string.main_tab_classical_music,
            R.string.main_tab_music_hall,
            R.string.main_tab_radio,
    };

    private int mPosition = -1;
    private List<BaseFragment> mBaseFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mBaseFragments = new ArrayList<>();
        //初始化Fragment
        GuideFragmentPageAdapter pageAdapter = new GuideFragmentPageAdapter(getSupportFragmentManager());
        for (int i = 0; i < mainTabs.length; i++) {
            BaseFragment fragment;
            if (i == mainTabs.length - 1) {
                fragment = new HomeRadioFragment();
            } else if (i == 1) {
                fragment = new HomeLoadMoreFragment();
            } else {
                fragment = new HomeRecommendFragment();
            }
//            Bundle bundle = new Bundle();
//            bundle.putString("title", getResources().getString(mainTabs[i]));
//            homeRecommendFragment.setArguments(bundle);
            mBaseFragments.add(fragment);
            pageAdapter.add(fragment);
        }
        tvViewPager.setOffscreenPageLimit(0);
        tvViewPager.setAdapter(pageAdapter);

        //初始化title
        TitleGuideAdapter titleGuideAdapter = new TitleGuideAdapter(mainTabs, this);
        titleGuideAdapter.setAsycFocusListener(new AsyncFocusListener<Integer>() {
            @Override
            public void focusPosition(Integer position) {
                //处理当前焦点的位置
                LogUtil.e("position-------------" + position);
                if (position == mPosition) {
                    //回到首页
                    BaseFragment baseFragment = mBaseFragments.get(position);
                    baseFragment.refreshRecyclerUi();
                } else {
                    tvViewPager.setCurrentItem(position);
                    mPosition = position;
                }
            }
        });
        titleGuide.setHorizontalMargin(getResources().getDimensionPixelOffset(R.dimen.w_40));
        titleGuide.setAdapter(titleGuideAdapter);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                View focusedChild = titleGuide.getFocusedChild();
                if (focusedChild == null) {
                    titleGuide.setSelectedPosition(mPosition);
                    titleGuide.requestFocusFromTouch();
                } else if (focusedChild != null && mPosition != 0) {
                    titleGuide.setSelectedPosition(0);
                    titleGuide.requestFocusFromTouch();
                } else {
                    finish();
                }
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
