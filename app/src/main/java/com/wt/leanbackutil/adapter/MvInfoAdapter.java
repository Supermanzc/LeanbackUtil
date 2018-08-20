package com.wt.leanbackutil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.MvInfoOneHolder;
import com.wt.leanbackutil.adapter.holder.MvInfoTwoHolder;
import com.wt.leanbackutil.fragment.HomeMvFragment;
import com.wt.leanbackutil.model.RadioInfo;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.ViewUtils;

import java.util.List;

/**
 * Created by DELL on 2018/8/8.
 *
 * @author junyan
 *         电台行adapter
 */

public class MvInfoAdapter extends RecyclerView.Adapter {

    private HomeMvFragment mFragment;
    private List<RadioInfo> radioInfos;
    private int mNumLine = 5;

    private static final int VIEW_TYPE_ONE = 0;
    private static final int VIEW_TYPE_TWO = 1;

    private int createIndexHolder = 1;
    private int bindIndexHolder = 0;

    public MvInfoAdapter(HomeMvFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtil.d("onCreateViewHolder---------------viewType=" + viewType + "  调用" + createIndexHolder + "次");
        createIndexHolder++;
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_ONE) {
            view = View.inflate(parent.getContext(), R.layout.item_mv_info_one, null);
            holder = new MvInfoOneHolder(view);
        } else if (viewType == VIEW_TYPE_TWO) {
            view = View.inflate(parent.getContext(), R.layout.item_mv_info_two, null);
            holder = new MvInfoTwoHolder(view);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_mv_info_one, null);
            holder = new MvInfoOneHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogUtil.d("onBindViewHolder---------------viewType=" + holder.getItemViewType() + " 当前的坐标=" + position + "  调用" + bindIndexHolder + "次");
        bindIndexHolder++;

        RadioInfo radioInfo = radioInfos.get(position);
        MvItemAdapter mvItemAdapter;
        int marginLeft = mFragment.getResources().getDimensionPixelOffset(R.dimen.w_100);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ONE:
                MvInfoOneHolder mvInfoOneHolder = (MvInfoOneHolder) holder;
                mvInfoOneHolder.titleView.setText(radioInfo.getRadio_group_name());

                mvItemAdapter = new MvItemAdapter(mFragment);
                mvItemAdapter.setData(radioInfo.getRadios());
                mvInfoOneHolder.horizontalGridView.removeAllViews();
                for (int i = 0; i < radioInfo.getRadios().size(); i++) {
                    View view = getItemView(mvInfoOneHolder.horizontalGridView, radioInfo.getRadios().get(i));
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            mFragment.getResources().getDimensionPixelOffset(R.dimen.w_273),
                            mFragment.getResources().getDimensionPixelOffset(R.dimen.w_318));
                    layoutParams.leftMargin = marginLeft * i + layoutParams.width * i;
                    view.setLayoutParams(layoutParams);
                    mvInfoOneHolder.horizontalGridView.addView(view);
                }
                break;
            case VIEW_TYPE_TWO:
                MvInfoTwoHolder mvInfoTwoHolder = (MvInfoTwoHolder) holder;
                mvItemAdapter = new MvItemAdapter(mFragment);
                mvItemAdapter.setData(radioInfo.getRadios());
                mvInfoTwoHolder.horizontalGridView.removeAllViews();
                for (int i = 0; i < radioInfo.getRadios().size(); i++) {
                    View view = getItemView(mvInfoTwoHolder.horizontalGridView, radioInfo.getRadios().get(i));
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            mFragment.getResources().getDimensionPixelOffset(R.dimen.w_273),
                            mFragment.getResources().getDimensionPixelOffset(R.dimen.w_318));
                    layoutParams.leftMargin = marginLeft * i + layoutParams.width * i;
                    view.setLayoutParams(layoutParams);
                    mvInfoTwoHolder.horizontalGridView.addView(view);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return radioInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return radioInfos.get(position).getType() == 1 ? VIEW_TYPE_ONE : VIEW_TYPE_TWO;
    }

    public void setData(List<RadioInfo> radioInfos) {
        this.radioInfos = radioInfos;
    }

    private View getItemView(ViewGroup parent, RadioItem radioItem) {
        View view = View.inflate(parent.getContext(), R.layout.item_mv_view, null);
        ImageView imageView = view.findViewById(R.id.img_view);
        TextView titleView = view.findViewById(R.id.title_view);
        titleView.setText(radioItem.getRadio_name());
        Glide.with(mFragment.getActivity()).load(radioItem.getRadio_pic()).into(imageView);
        view.setFocusable(true);
        view.setClickable(true);
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView textView = v.findViewById(R.id.title_view);
                textView.setTextColor(mFragment.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                mFragment.setOnFocusChange(v, hasFocus);
                if (hasFocus) {
                    v.bringToFront();
                }
                ViewUtils.scaleView(v, hasFocus);
            }
        });
        return view;
    }
}
