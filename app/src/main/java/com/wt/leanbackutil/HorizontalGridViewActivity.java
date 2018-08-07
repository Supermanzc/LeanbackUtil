package com.wt.leanbackutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.open.leanback.widget.HorizontalGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2018/8/3.
 *
 * @author junyan
 *         HorizontalGridView 的使用
 */

public class HorizontalGridViewActivity extends FragmentActivity {

    private static final int VIEW_TYPE_ONE = 0;
    private static final int VIEW_TYPE_TWO = 1;
    @BindView(R.id.horizontal_grid)
    HorizontalGridView horizontalGrid;

    private List<String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {

    }


    private void initData() {
        mData = new ArrayList<>();
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        mData.add("推荐");
        mData.add("排行榜");
        mData.add("歌单");
        mData.add("歌手");
        mData.add("MV");
        mData.add("电台");
        horizontalGrid.setAdapter(new GridAdapter());
    }

    class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder myViewHolder = null;
            View view = null;
            switch (viewType) {
                case VIEW_TYPE_ONE:
                    view = View.inflate(parent.getContext(), R.layout.item_grid_title_view, null);
                    myViewHolder = new TitleViewHolder(view);
                    break;
                case VIEW_TYPE_TWO:
                    view = View.inflate(parent.getContext(), R.layout.item_grid_image_view, null);
                    myViewHolder = new ImageViewHolder(view);
                    break;
                default:
                    break;
            }
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_ONE:
                    TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                    String title = mData.get(position);
                    titleViewHolder.titleView.setText(title);
                    titleViewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            TextView tv = view.findViewById(R.id.tv_title);
                            View lineView = view.findViewById(R.id.title_line_view);
                            lineView.setBackgroundColor(getResources().getColor(hasFocus ? R.color.title_select_color : R.color.clear_color));
                            tv.setTextColor(getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                        }
                    });
                    break;
                case VIEW_TYPE_TWO:
                    ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                    imageViewHolder.imageView.setImageResource(R.drawable.ic_launcher_background);
                    imageViewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            View lineView = view.findViewById(R.id.title_line_view);
                            lineView.setBackgroundColor(getResources().getColor(hasFocus ? R.color.title_select_color : R.color.clear_color));
                        }
                    });
                    break;
                default:
                    break;
            }

        }

        @Override
        public int getItemViewType(int position) {
            if (position % 2 == 0) {
                return VIEW_TYPE_ONE;
            } else {
                return VIEW_TYPE_TWO;
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        /**
         * 定义文字tab
         */
        class TitleViewHolder extends RecyclerView.ViewHolder {

            TextView titleView;

            public TitleViewHolder(View itemView) {
                super(itemView);
                titleView = itemView.findViewById(R.id.tv_title);
            }
        }

        /**
         * 定义图片tab
         */
        class ImageViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;

            public ImageViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.image_view);
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                horizontalGrid.setAnimation(null);
                horizontalGrid.scrollToPosition(0);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
