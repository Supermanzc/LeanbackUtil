package com.wt.leanbackutil.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.PlayVlcActivity;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.activity.SearchFocusActivity;
import com.wt.leanbackutil.activity.UpdateActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author junyan
 *         分类button处理
 */

public class CategoryFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.category_search_focus, R.id.vlc_mv, R.id.update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_search_focus:
                startActivity(new Intent(getContext(), SearchFocusActivity.class));
                break;
            case R.id.vlc_mv:
                startActivity(new Intent(getContext(), PlayVlcActivity.class));
                break;
            case R.id.update:
                startActivity(new Intent(getContext(), UpdateActivity.class));
                break;
            default:
                break;
        }
    }
}
