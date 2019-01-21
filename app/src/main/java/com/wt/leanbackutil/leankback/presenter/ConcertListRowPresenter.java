package com.wt.leanbackutil.leankback.presenter;

import android.view.ViewGroup;

import com.open.leanback.widget.BaseGridView;
import com.open.leanback.widget.HorizontalGridView;
import com.open.leanback.widget.ListRowPresenter;
import com.open.leanback.widget.RowPresenter;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.util.SpaceItemDecoration;

/**
 * @author junyan
 *         每行数据填充
 */

public class ConcertListRowPresenter extends ListRowPresenter {

    @Override
    protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        ViewHolder vh = (ViewHolder) super.createRowViewHolder(parent);
        HorizontalGridView horizontalGridView = vh.getGridView();
        horizontalGridView.setFocusDrawingOrderEnabled(true);
        horizontalGridView.setHorizontalMargin(parent.getResources().getDimensionPixelOffset(R.dimen.w_20));
        horizontalGridView.setClipChildren(false);
        horizontalGridView.setClipToPadding(false);
//        horizontalGridView.setAnimateChildLayout(false);
//        horizontalGridView.setHorizontalMargin(90);
        horizontalGridView.setPadding(parent.getResources().getDimensionPixelOffset(R.dimen.w_80),
                parent.getResources().getDimensionPixelOffset(R.dimen.h_20),
                parent.getResources().getDimensionPixelOffset(R.dimen.w_80), 0);
        horizontalGridView.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
//        horizontalGridView.setBackgroundResource(R.color.color_green);


        //设置上下间距
//        horizontalGridView.addItemDecoration(new SpaceItemDecoration(0,
//                parent.getResources().getDimensionPixelOffset(R.dimen.h_40), 0, 0));
        return vh;
    }
}
