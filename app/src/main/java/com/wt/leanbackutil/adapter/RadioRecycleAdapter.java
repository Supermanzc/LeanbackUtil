package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.RadioItemHolder;
import com.wt.leanbackutil.fragment.LeanBackFragment;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.ViewUtils;

import java.util.List;


public class RadioRecycleAdapter extends BaseAdapter<RadioItem, LeanBackFragment> {


    public RadioRecycleAdapter(List<RadioItem> data, LeanBackFragment context) {
        super(data, context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_radio_view, null);
//        View view = LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.item_radio_view, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        return new RadioItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioItem radioItem = mData.get(position);
        final RadioItemHolder radioItemHolder = (RadioItemHolder) holder;
        radioItemHolder.titleView.setText(radioItem.getRadio_name());
        FrescoUtil.getInstance().loadImage(radioItemHolder.imageView, radioItem.getRadio_pic(), FrescoUtil.TYPE_ONE);
//        Glide.with(mFragment.getActivity()).load(radioItem.getRadio_pic()).into(radioItemHolder.imageView);
        radioItemHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                radioItemHolder.titleView.setTextColor(mContext.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                ViewUtils.scaleView(v, hasFocus);
            }
        });
    }
}
