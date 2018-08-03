package com.wt.leanbackutil;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements View.OnFocusChangeListener {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_title_search)
    ImageView ivTitleSearch;
    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;
    @BindView(R.id.main_title_focus)
    ImageView mainTitleFocus;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_head)
    ImageView ivHead;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        for (int i = 0; i < mainTabs.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.main_title_item, layoutTitle, false);
            view.setOnFocusChangeListener(this);
            TextView textView = (TextView) view;
            textView.setText(getString(mainTabs[i]));
            layoutTitle.addView(view);

            if(i == 0){
                view.requestFocus();
                view.requestLayout();
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        TextView tv = (TextView) v;
        if(hasFocus){
            tv.setTextColor(getResources().getColor(R.color.main_title_focus));
        }else{
            tv.setTextColor(getResources().getColor(R.color.main_title_normal));
        }
    }
}
