package com.wt.leanbackutil.leankback.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.leanback.widget.Presenter;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.leankback.holder.WheelConcertChildrenHolder;
import com.wt.leanbackutil.leankback.holder.WheelConcertHolder;
import com.wt.leanbackutil.model.RecommendInfo;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.ViewUtils;

/**
 * @author junyan
 *         歌曲类型
 */

public class ConcertPresenter extends Presenter {

    /**
     * 表示歌曲类型
     */
    private int type;
    private Context context;

    public ConcertPresenter(int type) {
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
//        if (type == 1) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wheel_item_singer_children,
//                    parent, false);
//            viewHolder = new WheelConcertChildrenHolder(view);
//        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wheel_item_singer,
                    parent, false);
            viewHolder = new WheelConcertHolder(view, parent.getContext(), type);
//        }
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        LogUtil.e("------------------" + item.toString());
        SingItem singItem = (SingItem) item;
        final WheelConcertHolder recommendSingHolder = (WheelConcertHolder) viewHolder;
        switch (recommendSingHolder.type) {
            case RecommendInfo.TYPE_ONE:
                if (singItem.getType() == 1) {

                } else {
                    FrescoUtil.getInstance().loadImage(recommendSingHolder.imageView, singItem.getPic(), FrescoUtil.TYPE_ONE);
                }
                break;
            case RecommendInfo.TYPE_TWO:
                FrescoUtil.getInstance().loadImage(recommendSingHolder.imageView, singItem.getPic_url(), FrescoUtil.TYPE_ONE);
                recommendSingHolder.descriptionView.setText(singItem.getDiss_name());
                break;
            case RecommendInfo.TYPE_THREE:
                FrescoUtil.getInstance().loadImage(recommendSingHolder.imageView, singItem.getAlbum_pic(), FrescoUtil.TYPE_ONE);
                recommendSingHolder.titleView.setText(singItem.getSong_name());
                recommendSingHolder.descriptionView.setText(singItem.getSinger_name());
                break;
            case RecommendInfo.TYPE_FOUR:
                FrescoUtil.getInstance().loadImage(recommendSingHolder.imageView, singItem.getMv_pic(), FrescoUtil.TYPE_ONE);
                recommendSingHolder.titleView.setText(singItem.getMv_title());
                recommendSingHolder.descriptionView.setText(singItem.getSinger_name());
                break;
            default:
                break;
        }
        recommendSingHolder.view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                recommendSingHolder.titleView.setTextColor(context.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                recommendSingHolder.descriptionView.setTextColor(context.getResources().getColor(hasFocus ? R.color.title_select_color : R.color.title_none_color));
                ViewUtils.scaleView(v, hasFocus);
            }
        });
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
