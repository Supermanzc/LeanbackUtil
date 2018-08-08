package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/8.
 *
 * @author junyan
 *         电台
 */

public class HomeRadioFragment extends BaseFragment {

    @BindView(R.id.vertical_grid_view)
    VerticalGridView verticalGridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_radio, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void refreshRecyclerUi() {
        if(verticalGridView != null){
            verticalGridView.scrollToPosition(0);
        }
    }
}
