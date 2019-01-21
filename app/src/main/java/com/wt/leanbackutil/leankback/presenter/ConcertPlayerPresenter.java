package com.wt.leanbackutil.leankback.presenter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.open.leanback.widget.ArrayObjectAdapter;
import com.open.leanback.widget.ListRow;
import com.open.leanback.widget.RowPresenter;
import com.open.leanback.widget.VerticalGridView;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * @author junyan
 *         视频播放框的页面
 */

public class ConcertPlayerPresenter extends RowPresenter implements SurfaceHolder.Callback {

    @BindViews({R.id.item_1, R.id.item_2, R.id.item_3, R.id.item_4})
    LinearLayout[] views;
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;

    private ViewHolder holder;
    private VerticalGridView verticalGridView;

//    private final ListItemsVisibilityCalculator mListItemVisibilityCalculator =
//            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);

    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.concert_view_player,
                parent, false);
        ButterKnife.bind(this, view);
        holder = new ViewHolder(view);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);
        verticalGridView = (VerticalGridView) parent;
        verticalGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                LogUtil.e("onScrollStateChanged-------------" + surfaceView.isShown() + "      newState=" + newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
//                    mListItemVisibilityCalculator.onScrollStateIdle(
//                            mItemsPositionGetter,
//                            mLayoutManager.findFirstVisibleItemPosition(),
//                            mLayoutManager.findLastVisibleItemPosition());
                    LogUtil.e("onScrollStateChanged-------------" + surfaceView.isShown()
                            + "   verticalGridView.getSelectedPosition()" + verticalGridView.getSelectedPosition());
                    if(surfaceView.isShown() && verticalGridView.getSelectedPosition() == 3){
                        //开始处理播放器数据

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return this.holder;
    }

    @Override
    protected void onBindRowViewHolder(ViewHolder vh, Object item) {
        super.onBindRowViewHolder(vh, item);
        ListRow listRow = (ListRow) item;
        ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
        int size = arrayObjectAdapter.size();
        List<SingItem> singItems = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            SingItem singItem = (SingItem) arrayObjectAdapter.get(i);
            singItems.add(singItem);
        }
        initData(singItems);
    }

    @Override
    protected void onUnbindRowViewHolder(ViewHolder vh) {
        super.onUnbindRowViewHolder(vh);
        LogUtil.e("onUnbindRowViewHolder--------------vh=" + vh);
    }

    private void initData(List<SingItem> singItems) {
        for (int i = 0; i < views.length; i++) {
            if (i > singItems.size() - 2) {
                views[i].setVisibility(View.GONE);
            } else {
                SingItem singItem = singItems.get(i + 1);
                views[i].setVisibility(View.VISIBLE);
                TextView titleView = views[i].findViewById(R.id.title_view);
                TextView descriptionView = views[i].findViewById(R.id.description_view);
                SimpleDraweeView imageView = views[i].findViewById(R.id.img_view);
                ViewUtils.onFocus(imageView);
                views[i].setFocusable(true);
                titleView.setText(singItem.getSong_name());
                descriptionView.setText(singItem.getSinger_name());
                FrescoUtil.getInstance().loadImage(imageView, singItem.getAlbum_pic(), FrescoUtil.TYPE_ONE);
                views[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            v.bringToFront();
                        }
                        ViewUtils.scaleView(v, hasFocus);
                    }
                });
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.e("surfaceCreated----------------holder" + holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.e("surfaceDestroyed----------------holder" + holder);
    }
}
