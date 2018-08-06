package com.wt.leanbackutil.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/6.
 *
 * @author junyan
 *         推荐
 */

public class HomeRecommendFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_recommend, container, false);
        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        String titleStr = arguments.getString("title", "");
        title.setText(titleStr);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
