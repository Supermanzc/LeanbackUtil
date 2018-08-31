package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by DELL on 2018/8/9.
 *
 * @author junyan
 *         顶层adapter
 */

public abstract class BaseAdapter<T, B> extends RecyclerView.Adapter {

    protected List<T> mData;
    protected B mContext;

    public BaseAdapter() {
    }

    public BaseAdapter(B context, List<T> data) {
        mContext = context;
        this.mData = data;
    }

    /**
     * 设置上下文
     *
     * @param context
     */
    public void setContext(B context) {
        mContext = context;
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void appendData(List<T> data) {
        if (null == data) {
            return;
        }
        int size = mData.size();
        mData.addAll(size, data);
        notifyItemRangeInserted(size, mData.size());
    }
}
