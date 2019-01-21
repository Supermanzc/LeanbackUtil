package com.wt.leanbackutil.leankback.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.leanback.widget.RowPresenter;
import com.wt.leanbackutil.R;

/**
 * @author junyan
 * 主题派系
 */

public class ConcertThemePresenter extends RowPresenter {

    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.concert_view_theme,
                parent, false);
        return new ConcertThemeHolder(view);
    }

    class ConcertThemeHolder extends ViewHolder{


        public ConcertThemeHolder(View view) {
            super(view);
        }
    }
}
