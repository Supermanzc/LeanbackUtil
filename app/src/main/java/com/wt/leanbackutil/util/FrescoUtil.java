package com.wt.leanbackutil.util;

import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wt.leanbackutil.App;
import com.wt.leanbackutil.R;

/**
 * Created by DELL on 2018/8/21.
 */

public class FrescoUtil {

    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;
    private static FrescoUtil INSTANCE;

    private FrescoUtil() {
    }

    public static FrescoUtil getInstance() {
        synchronized (FrescoUtil.class) {
            if (INSTANCE == null) {
                INSTANCE = new FrescoUtil();
            }
            return INSTANCE;
        }
    }

    /**
     * 加载图片100*100
     *
     * @param simpleDraweeView
     * @param url
     */
    public void loadImage(SimpleDraweeView simpleDraweeView, String url, int type) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
        switch (type) {
            case TYPE_ONE:
                hierarchy.setFailureImage(R.drawable.program_loading_100);
                hierarchy.setPlaceholderImage(R.drawable.program_loading_100);
                break;
            case TYPE_TWO:
                hierarchy.setFailureImage(R.drawable.program_loading_640);
                hierarchy.setPlaceholderImage(R.drawable.program_loading_640);
                break;
            case TYPE_THREE:
                hierarchy.setFailureImage(R.drawable.program_loading_975);
                hierarchy.setPlaceholderImage(R.drawable.program_loading_975);
                break;
            default:
                hierarchy.setFailureImage(R.drawable.program_loading_100);
                hierarchy.setPlaceholderImage(R.drawable.program_loading_100);
                break;
        }

        //设置圆角
        RoundingParams roundingParams = RoundingParams.fromCornersRadii(
                App.getInstance().getResources().getDimensionPixelOffset(R.dimen.w_8),
                App.getInstance().getResources().getDimensionPixelOffset(R.dimen.h_8),
                App.getInstance().getResources().getDimensionPixelOffset(R.dimen.w_8),
                App.getInstance().getResources().getDimensionPixelOffset(R.dimen.h_8));
        hierarchy.setRoundingParams(roundingParams);
        simpleDraweeView.setHierarchy(hierarchy);

        //渐进式JPEG图仅仅支持网络图Uri.parse(url)
        ImageRequest request = getImageRequest(Uri.parse(url), simpleDraweeView);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .build();

        simpleDraweeView.setController(controller);
        simpleDraweeView.setImageURI(url);
    }

    private ImageRequest getImageRequest(Uri uri, SimpleDraweeView simpleDraweeView) {
        int width;
        int height;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            width = simpleDraweeView.getWidth();
            height = simpleDraweeView.getHeight();
        } else {
            width = simpleDraweeView.getMeasuredWidth();
            height = simpleDraweeView.getMeasuredHeight();
        }

        //根据请求路径生成ImageRequest的构造者
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        //调整解码图片的大小(结合setDownsampleEnabled配置使用)
        if (width > 0 && height > 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        return builder.build();
    }
}
