package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by DELL on 2018/8/9.
 *
 * @author junyan
 *         顶层adapter
 */

public abstract class BaseAdapter<T, B> extends RecyclerView.Adapter {

    protected T mData;
    protected B mContext;

    public void setContext(B context){
        mContext = context;
    }

    public void setData(T data) {
        this.mData = data;
    }
}
