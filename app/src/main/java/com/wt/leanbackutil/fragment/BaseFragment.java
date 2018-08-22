package com.wt.leanbackutil.fragment;

import android.support.v4.app.Fragment;

import com.wt.leanbackutil.util.LogUtil;

/**
 * Created by DELL on 2018/8/6.
 *
 * @author junyan
 *         父类fragment
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.e("onHiddenChanged------------------" + hidden);
    }

    /**
     * 刷新Fragment界面
     */
    public void refreshRecyclerUi(){

    }
}
