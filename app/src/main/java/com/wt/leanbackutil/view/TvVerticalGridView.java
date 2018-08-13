package com.wt.leanbackutil.view;

import android.content.Context;
import android.util.AttributeSet;

import com.open.leanback.widget.VerticalGridView;

/**
 * Created by DELL on 2018/8/13.
 *
 * @author junyan
 *         主要是解决外交焦点框问题
 */

public class TvVerticalGridView extends VerticalGridView {

    public TvVerticalGridView(Context context) {
        super(context);
    }

    public TvVerticalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
