package com.wt.leanbackutil.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.wt.leanbackutil.util.LogUtil;

/**
 * @author junyan
 * 包含做控制作用
 */

public class SurfaceRelativeLayout extends RelativeLayout {

    public SurfaceRelativeLayout(Context context) {
        super(context);
    }

    public SurfaceRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SurfaceRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SurfaceRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE){
            LogUtil.e("onWindowVisibilityChanged----------------------" + isCover());
            //开始某些任务
        }
        else if(visibility == INVISIBLE || visibility == GONE){

            //停止某些任务
        }
    }

    /**
     * 检测是否被遮住显示不全
     * @return
     */
    protected boolean isCover() {
        boolean cover = false;
        Rect rect = new Rect();
        cover = getGlobalVisibleRect(rect);
        if (cover) {
            if (rect.width() >= getMeasuredWidth() && rect.height() >= getMeasuredHeight()) {
                return !cover;
            }
        }
        return true;
    }
}
