package com.wt.leanbackutil.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

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

    public BaseAdapter(){

    }

    public BaseAdapter(List<T> data, B context) {
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

    /**
     * 初始化view
     *
     * @param context
     * @param layoutId
     * @param container
     * @return
     */
    public View getInflateView(Context context, int layoutId, @Nullable ViewGroup container) {
        return LayoutInflater.from(context).inflate(layoutId, container, false);
    }
}
