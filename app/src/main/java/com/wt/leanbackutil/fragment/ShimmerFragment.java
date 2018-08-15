package com.wt.leanbackutil.fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.view.ShimmerLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DELL on 2018/8/15.
 *
 * @author junyan
 *         测试闪光问题
 */

public class ShimmerFragment extends BaseFragment {

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.shimmer)
    ShimmerLayout shimmerFrameLayout;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shimmer, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        Glide.with(getActivity()).load("http://y.gtimg.cn/music/common/upload/MUSIC_FOCUS/310313.jpeg").into(imageView);
        shimmerFrameLayout.setShimmerAngle(45);
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shimmerFrameLayout.stopShimmerAnimation();
        unbinder.unbind();
    }
}
