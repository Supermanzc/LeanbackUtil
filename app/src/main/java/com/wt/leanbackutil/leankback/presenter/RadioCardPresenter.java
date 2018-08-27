package com.wt.leanbackutil.leankback.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.open.leanback.widget.Presenter;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.RadioItem;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.ViewUtils;

/**
 * Created by DELL on 2018/8/17.
 *
 * @author junyan
 *         Presenter 有点像Adapter类型
 */

public class RadioCardPresenter extends Presenter {

    private Context mContext;

    public RadioCardPresenter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.item_radio_view, null);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        RadioItem radioItem = (RadioItem) item;
        final SimpleDraweeView imageView = viewHolder.view.findViewById(R.id.img_view);
        TextView textView = viewHolder.view.findViewById(R.id.title_view);
        textView.setText(radioItem.getRadio_name());
//        Glide.with(mContext).load(radioItem.getRadio_pic()).into(imageView);
        FrescoUtil.getInstance().loadImage(imageView, radioItem.getRadio_pic(), FrescoUtil.TYPE_ONE);
        viewHolder.view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                imageView.setBackgroundResource(hasFocus ? R.drawable.button_focus : R.drawable.button_normal);
                ViewUtils.scaleView(v, hasFocus);
            }
        });
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
