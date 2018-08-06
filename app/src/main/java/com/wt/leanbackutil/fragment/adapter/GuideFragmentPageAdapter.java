package com.wt.leanbackutil.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wt.leanbackutil.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/8/6.
 * @author junyan
 * 导航的对应的Fragment
 */

public class GuideFragmentPageAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragments;

    public GuideFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }

    /**
     * 添加fragment
     * @param fragment
     */
    public void add(BaseFragment fragment){
        mFragments.add(fragment);
    }

    /**
     * 添加所有的Fragment
     * @param fragments
     */
    public void addAll(List<BaseFragment> fragments){
        mFragments.clear();
        mFragments.addAll(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
