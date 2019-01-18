package com.wt.leanbackutil.leankback.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.leanback.widget.Presenter;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.adapter.holder.WheelConcertHolder;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wheel_item_singer,
                parent, false);
        context = parent.getContext();
        return new WheelConcertHolder(view, parent.getContext(), type);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        LogUtil.e("------------------" + item.toString());
        final WheelConcertHolder recommendSingHolder = (WheelConcertHolder) viewHolder;
        SingItem singItem = (SingItem) item;
        switch (recommendSingHolder.type) {
            case RecommendInfo.TYPE_ONE:
                FrescoUtil.getInstance().loadImage(recommendSingHolder.imageView, singItem.getPic(), FrescoUtil.TYPE_ONE);
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
