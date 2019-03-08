package com.wt.leanbackutil.leankback.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.open.leanback.widget.ArrayObjectAdapter;
import com.open.leanback.widget.ListRow;
import com.open.leanback.widget.RowHeaderPresenter;
import com.open.leanback.widget.RowPresenter;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.ViewUtils;
import com.wt.leanbackutil.view.WheelHolderCreator;
import com.wt.leanbackutil.view.WheelRelativeLayout;
import com.wt.leanbackutil.view.WheelViewHolder;
import com.wt.leanbackutil.view.WheelViewPager;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author junyan
 *         轮播数据填充
 */

public class ConcertViewPagerPresenter extends RowPresenter {

    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.concert_view_pager,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindRowViewHolder(ViewHolder vh, Object item) {
        super.onBindRowViewHolder(vh, item);
        WheelViewPager wheelViewPager = vh.view.findViewById(R.id.wheel_view_pager);
        ListRow listRow = (ListRow) item;
        ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
        int size = arrayObjectAdapter.size();
        List<SingItem> singItems = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            SingItem singItem = (SingItem) arrayObjectAdapter.get(i);
            singItems.add(singItem);
        }
        RowHeaderPresenter.ViewHolder headerViewHolder = vh.getHeaderViewHolder();
        LinearLayout linearLayout = headerViewHolder.view.findViewById(R.id.indicator_container);
        wheelViewPager.setPager(singItems, 8,
                linearLayout, new WheelHolderCreator() {
                    @Override
                    public WheelViewHolder createViewHolder() {
                        return new WheelViewHolder() {

                            private WheelRelativeLayout bringToFrontRelative;

                            @Override
                            public View createView(Context context, int position) {
                                bringToFrontRelative = (WheelRelativeLayout) LayoutInflater.from(context).inflate(R.layout.wheel_pager_singer, null);
                                return bringToFrontRelative;
                            }

                            @Override
                            public void onBind(Context context, View viewWheel, Object data) {
                                bringToFrontRelative = (WheelRelativeLayout) viewWheel;
                                int childCount = bringToFrontRelative.getChildCount();
                                List<SingItem> singItemList = (List<SingItem>) data;
                                for (int i = 0; i < childCount; i++) {
                                    View view = bringToFrontRelative.getChildAt(i);
                                    if (i > singItemList.size() - 1) {
                                        view.setVisibility(View.GONE);
                                    } else {
                                        SingItem singItem = singItemList.get(i);
                                        view.setVisibility(View.VISIBLE);
                                        TextView titleView = view.findViewById(R.id.title_view);
                                        TextView descriptionView = view.findViewById(R.id.description_view);
                                        SimpleDraweeView imageView = view.findViewById(R.id.img_view);
                                        ViewUtils.onFocus(imageView);
                                        view.setFocusable(true);
                                        titleView.setText(singItem.getSong_name());
                                        descriptionView.setText(singItem.getSinger_name());
                                        FrescoUtil.getInstance().loadImage(imageView, singItem.getAlbum_pic(), FrescoUtil.TYPE_ONE);
                                        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                            @Override
                                            public void onFocusChange(View v, boolean hasFocus) {
                                                if (hasFocus) {
                                                    v.bringToFront();
                                                }
                                                v.clearAnimation();
                                                ViewUtils.scaleView(v, hasFocus);
                                            }
                                        });
                                        if(view.hasFocus()){
                                            ViewUtils.scaleView(view, true);
                                        }
                                    }
                                }
                            }
                        };
                    }
                });
    }
}
