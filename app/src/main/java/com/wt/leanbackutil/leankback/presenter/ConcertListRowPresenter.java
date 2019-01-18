package com.wt.leanbackutil.leankback.presenter;

import android.view.ViewGroup;

import com.open.leanback.widget.HorizontalGridView;
import com.open.leanback.widget.ListRowPresenter;
import com.open.leanback.widget.RowPresenter;

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
        horizontalGridView.setHorizontalMargin(20);
        horizontalGridView.setClipChildren(false);
        horizontalGridView.setClipToPadding(false);
//        horizontalGridView.setAnimateChildLayout(false);
//        horizontalGridView.setHorizontalMargin(90);
        horizontalGridView.setPadding(90, 0, 90, 0);
        return vh;
    }
}
