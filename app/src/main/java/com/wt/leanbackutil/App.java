package com.wt.leanbackutil;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.LolipopBitmapMemoryCacheSupplier;

import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

/**
 * Created by DELL on 2018/8/21.
 */

public class App extends Application {

    private static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        //日志开启
        FLog.setMinimumLoggingLevel(FLog.ERROR);
        OkHttpClient okHttpClient = new OkHttpClient();
        //添加fresco日志
        Set<RequestListener> listeners = new HashSet<>();
        listeners.add(new RequestLoggingListener());

        //当前内存紧张时采取的措施
        MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                LogUtil.e("Fresco  trim------------" + String.format("onCreate suggestedTrimRatio : %d", suggestedTrimRatio));
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio) {
                    //清除内存缓存
                    Fresco.getImagePipeline().clearMemoryCaches();
//                Fresco.getImagePipeline().clearCaches();
                }
            }
        });
        //小图片的磁盘配置,用来储存用户头像之类的小图
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(this)
                //缓存图片基路径
                .setBaseDirectoryPath(this.getCacheDir())
                //文件夹名
                .setBaseDirectoryName(getString(R.string.app_name))
                //默认缓存的最大大小。
                .setMaxCacheSize(40 * ByteConstants.MB)
                //缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnLowDiskSpace(20 * ByteConstants.MB)
                //缓存的最大大小,当设备极低磁盘空间
                .setMaxCacheSizeOnVeryLowDiskSpace(10 * ByteConstants.MB)
                .build();

        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(this, okHttpClient)
                //须和ImageRequest的ResizeOptions一起使用，作用就是在图片解码时根据ResizeOptions所设的宽高的像素进行解码，这样解码出来可以得到一个更小的Bitmap。
                .setDownsampleEnabled(true)
                .setRequestListeners(listeners)
                //对网络图片进行resize处理,减少内存消耗
                .setResizeAndRotateEnabledForNetwork(true)
                //处理android 5.0以上的机子不能肆意使用匿名内存区域
//                .setBitmapMemoryCacheParamsSupplier(new LolipopBitmapMemoryCacheSupplier((ActivityManager) getSystemService(ACTIVITY_SERVICE)))
                //内存紧张的时候处理模式
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                //设置小图片的内存空间
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();

        Fresco.initialize(this, config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public static Context getInstance() {
        return INSTANCE;
    }
}
