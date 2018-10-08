package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.SongSheetItemHolder;
import com.wt.leanbackutil.fragment.HomeSongFragment;
import com.wt.leanbackutil.model.RadioCategory;
import com.wt.leanbackutil.model.RadioSubcategory;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author junyan
 * list列表价Decoration类型的处理
 */

public class SongListDecorationAdapter extends BaseAdapter<RadioSubcategory, HomeSongFragment> {

    private List<RadioCategory> categoryList;

    public SongListDecorationAdapter(List<RadioSubcategory> data, HomeSongFragment context) {
        super(data, context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = getInflateView(mContext.getContext(), R.layout.item_radio_view, parent);
        inflateView.setFocusable(true);
        return new SongSheetItemHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioSubcategory radioItem = mData.get(position);
        final SongSheetItemHolder radioItemHolder = (SongSheetItemHolder) holder;
        if (radioItem.isRealEntity()) {
            radioItemHolder.titleView.setText(radioItem.getRadio_name());
            FrescoUtil.getInstance().loadImage(radioItemHolder.imageView, radioItem.getRadio_pic(), FrescoUtil.TYPE_ONE);
            radioItemHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    radioItemHolder.titleView.setTextColor(mContext.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                    ViewUtils.scaleView(v, hasFocus);
                }
            });
            radioItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        radioItemHolder.itemView.setVisibility(radioItem.isRealEntity() ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置Title值
     *
     * @param categoryList
     */
    public void setCategoryTitles(List<RadioCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
