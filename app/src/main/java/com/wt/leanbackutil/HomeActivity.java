package com.wt.leanbackutil;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.HorizontalGridView;

import com.wt.leanbackutil.adapter.TitleGuideAdapter;
import com.wt.leanbackutil.adapter.listener.AsyncFocusListener;
import com.wt.leanbackutil.view.TvViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/6.
 *
 * @author junyan
 *         viewPager+fragment 结构
 */

public class HomeActivity extends Activity {

    @BindView(R.id.title_guide)
    HorizontalGridView titleGuide;
    @BindView(R.id.tv_view_pager)
    TvViewPager tvViewPager;

    private int[] mainTabs = new int[]{
            R.string.main_tab_home,
            R.string.main_tab_latest_music,
            R.string.main_tab_rank,
            R.string.main_tab_singer,
            R.string.main_tab_song_sheet,
            R.string.main_tab_mv,
            R.string.main_tab_classical_music,
            R.string.main_tab_music_hall,
            R.string.main_tab_radio,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        TitleGuideAdapter titleGuideAdapter = new TitleGuideAdapter(mainTabs, this);
        titleGuideAdapter.setAsycFocusListener(new AsyncFocusListener<Integer>() {
            @Override
            public void focusPosition(Integer position) {
                //处理当前焦点的位置

            }
        });
        titleGuide.setHorizontalSpacing(getResources().getDimensionPixelOffset(R.dimen.w_40));
        titleGuide.setAdapter(titleGuideAdapter);
    }
}
