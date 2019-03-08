package com.wt.leanbackutil.view;

/**
 * Created by zhouwei on 17/5/26.
 */

public interface WheelHolderCreator<VH extends WheelViewHolder> {
    /**
     * 创建ViewHolder
     * @return
     */
    public VH createViewHolder();
}
