package com.wt.leanbackutil.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.open.leanback.widget.ArrayObjectAdapter;
import com.open.leanback.widget.HorizontalGridView;
import com.open.leanback.widget.ItemBridgeAdapter;
import com.open.leanback.widget.ListRow;
import com.open.leanback.widget.ListRowPresenter;
import com.open.leanback.widget.PresenterSelector;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.leankback.presenter.RadioCardPresenter;
import com.wt.leanbackutil.leankback.seletor.CardPresenterSelector;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.util.FileJsonUtils;
import com.wt.leanbackutil.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DELL on 2018/8/17.
 *
 * @author junyan
 *         使用Leanback的模式使用api
 */

public class LeanBackFragment extends BaseFragment {

    @BindView(R.id.horizontal_grid_view)
    HorizontalGridView horizontalGridView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leanback, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        LogUtil.d("onCreateView-----------------" + LeanBackFragment.class.getSimpleName());
        return view;
    }

    private void initView() {
        String json = FileJsonUtils.inputStreamToString(getResources().openRawResource(R.raw.launcher_cards));
        RadioItem[] rows = new Gson().fromJson(json, RadioItem[].class);

        RadioCardPresenter radioCardPresenter = new RadioCardPresenter(getContext());
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(radioCardPresenter);
        for (RadioItem radioItem : rows) {
            listRowAdapter.add(radioItem);
        }

        //桥接
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(listRowAdapter);
        horizontalGridView.setHorizontalMargin(getResources().getDimensionPixelOffset(R.dimen.w_20));
        horizontalGridView.setAdapter(itemBridgeAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
//        Fresco.getImagePipeline().clearMemoryCaches();
//        Fresco.getImagePipeline().clearDiskCaches();
//        Fresco.getImagePipeline().clearCaches();
    }
}
