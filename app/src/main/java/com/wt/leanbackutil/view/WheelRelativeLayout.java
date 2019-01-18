package com.wt.leanbackutil.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.open.leanback.bring.BringToFrontRelative;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.ViewUtils;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author junyan
 *         数据填充左右分页的
 */

public class WheelRelativeLayout<T> extends BringToFrontRelative {

    private View focusedView;
    /**
     * 行
     */
    private int row = 2;
    /**
     * 列
     */
    private int column = 4;

    private List<View> views = new ArrayList<>();

    private List<SingItem> singItems;

    public WheelRelativeLayout(Context context) {
        super(context);
    }

    public WheelRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WheelRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (focusedView != null) {
            boolean result = focusedView.requestFocus(direction, previouslyFocusedRect);
            return result;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
//        if (hasFocus() &&  (direction == FOCUS_RIGHT || direction == FOCUS_LEFT)) {
        if (hasFocus()) {
            focusedView = getFocusedChild();
        } else {
            if (isFocusable()) {
                views.add(this);
                return;
            }
        }
        super.addFocusables(views, direction, focusableMode);
    }

    /**
     * 设置列和行
     *
     * @param column
     * @param row
     */
    public void setColumnAndRow(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public void initView(List<SingItem> singItems, int width, int height, int columnWidth) {
        views.clear();
        removeAllViews();
        this.singItems = singItems;
        int pageSize = row * column;
        for (int j = 0; j < pageSize; j++) {
            View view = getView(j, width, height);
            views.add(view);
            addView(view);
        }

        //设置item的位置
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (i / column > 0) {
                layoutParams.setMargins((i % column) * width + (i % column) * columnWidth, height + 100, 0, 0);
                view.setLayoutParams(layoutParams);
            } else {
                layoutParams.setMargins((i % column) * width + (i % column) * columnWidth, 0, 0, 0);
                view.setLayoutParams(layoutParams);
            }
        }

    }

    private View getView(int position, int width, int height) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.wheel_item_coustom, this, false);
        FrameLayout frameLayout = view.findViewById(R.id.frame_img);
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        TextView titleView = view.findViewById(R.id.title_view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titleView.getLayoutParams();
        layoutParams.width = width;
        titleView.setLayoutParams(layoutParams);
        TextView descriptionView = view.findViewById(R.id.description_view);
        LinearLayout.LayoutParams descriptionViewLayoutParams = (LinearLayout.LayoutParams) descriptionView.getLayoutParams();
        descriptionViewLayoutParams.width = width;
        descriptionView.setLayoutParams(descriptionViewLayoutParams);
        SingItem singItem = getSingItem(position);
        SimpleDraweeView imageView = view.findViewById(R.id.img_view);

        ViewUtils.onFocus(imageView);
        view.setFocusable(true);
        view.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                v.bringToFront();
                ViewUtils.scaleView(v, hasFocus);
            }
        });

        if(singItem == null){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
            titleView.setText(singItem.getDiss_name());
            descriptionView.setText(singItem.getMv_desc());
            descriptionView.setVisibility(View.GONE);
            FrescoUtil.getInstance().loadImage(imageView, singItem.getPic_url(), FrescoUtil.TYPE_ONE);
        }

        return view;
    }

    public SingItem getSingItem(int positon){
        if(positon > singItems.size() - 1){
            return null;
        }
        return singItems.get(positon);
    }


    /**
     *
     * @param focused
     * @param direction
     * @return
     */
//    @Override
//    public View focusSearch(View focused, int direction) {
//        View focusView = super.focusSearch(focused, direction);
//        if ((focusView == null) && (direction == View.FOCUS_RIGHT)) {
//            // 到达最右边，焦点下移.(注意:建议放到Executors的Runnable里面执行哈，这里简化代码)
//            ThreadPoolManager.getInstance().execute(new Runnable() {
//                @Override
//                public void run() {
//                    new Instrumentation().sendKeyDownUpSync(KEYCODE_DPAD_DOWN);
//                }
//            });
//        } else if ((focusView == null) && (direction == View.FOCUS_LEFT)) {
//            ThreadPoolManager.getInstance().execute(new Runnable() {
//                @Override
//                public void run() {
//                    new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
//                }
//            });
//            // 到达最左边，焦点下移.
//        }
//        return focusView;
//    }
}
