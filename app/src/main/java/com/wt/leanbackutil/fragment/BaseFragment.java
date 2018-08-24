package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e("onDestroyView---------------------" + this.getClass().getSimpleName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.d("setUserVisibleHint-----------------" + isVisibleToUser + "   fragment=" + this.getClass().getSimpleName());
//        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 刷新Fragment界面
     */
    public void refreshRecyclerUi(){

    }
}
