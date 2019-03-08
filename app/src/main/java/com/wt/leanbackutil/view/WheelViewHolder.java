package com.wt.leanbackutil.view;

import android.content.Context;
import android.view.View;

/**
 * Created by zhouwei on 17/5/26.
 */

public interface WheelViewHolder<T> {
    /**
     *  创建View
     * @param context
     * @param position 表示哪一页
     * @return
     */
    View createView(Context context, int position);

    /**
     * 绑定数据
     * @param context
     * @param data
     */
    void onBind(Context context, View view, T data);
}
