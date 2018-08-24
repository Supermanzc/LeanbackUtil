package com.wt.leanbackutil.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.disk.NoOpDiskTrimmableRegistry;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

import static android.content.Context.ACTIVITY_SERVICE;

public class ImagePipelineConfigUtils {

    /**
     * 分配的可用内存
     */
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    /**
     * 使用的缓存数量
     */
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;

    /**
     * 小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
     */
    private static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 20 * ByteConstants.MB;

    /**
     * 小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
     */
    private static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 60 * ByteConstants.MB;

    /**
     * 默认图极低磁盘空间缓存的最大值
     */
    private static final int MAX_DISK_CACHE_VERYLOW_SIZE = 20 * ByteConstants.MB;

    /**
     * 默认图低磁盘空间缓存的最大值
     */
    private static final int MAX_DISK_CACHE_LOW_SIZE = 60 * ByteConstants.MB;

    /**
     * 默认图磁盘缓存的最大值
     */
    private static final int MAX_DISK_CACHE_SIZE = 100 * ByteConstants.MB;

    /**
     * 小图所放路径的文件夹名
     */
    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "ImagePipelineCacheSmall";

    /**
     * 默认图所放路径的文件夹名
     */
    private static final String IMAGE_PIPELINE_CACHE_DIR = "ImagePipelineCacheDefault";

    private static ArrayList<MemoryTrimmable> mMemoryTimmable;

    public static ImagePipelineConfig getDefaultImagePipelineConfig(Context context) {

        //内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                // 内存缓存中总图片的最大大小,以字节为单位。
                MAX_MEMORY_CACHE_SIZE,
                // 内存缓存中图片的最大数量。
                Integer.MAX_VALUE,
                // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                MAX_MEMORY_CACHE_SIZE,
                // 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE,
                // 内存缓存中单个图片的最大大小。
                Integer.MAX_VALUE);

        //修改内存图片缓存数量，空间策略（这个方式有点恶心）
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };

        //小图片的磁盘配置 //缓存图片基路径
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                //文件夹名
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                //默认缓存的最大大小。
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                //缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)
                //缓存的最大大小,当设备极低磁盘空间
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

        //默认图片的磁盘配置 //缓存图片基路径
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(new File(getDiskCacheDir(context)))
                //文件夹名
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                //默认缓存的最大大小。
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                //缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)
                //缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

//        FLog.setMinimumLoggingLevel(FLog.ERROR);
        //添加fresco日志
        Set<RequestListener> listeners = new HashSet<>();
        listeners.add(new RequestLoggingListener());

        mMemoryTimmable = new ArrayList();

        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = OkHttpImagePipelineConfigFactory.newBuilder(context, new OkHttpClient())
//        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
                //须和ImageRequest的ResizeOptions一起使用，作用就是在图片解码时根据ResizeOptions所设的宽高的像素进行解码，这样解码出来可以得到一个更小的Bitmap。
                .setDownsampleEnabled(true)
                .setRequestListeners(listeners)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)
                .setMainDiskCacheConfig(diskCacheConfig)
//                .setMemoryTrimmableRegistry(NoOpMemoryTrimmableRegistry.getInstance())
                //处理android 5.0以上的机子不能肆意使用匿名内存区域
                .setBitmapMemoryCacheParamsSupplier(new LolipopBitmapMemoryCacheSupplier((ActivityManager) context.getApplicationContext().getSystemService(ACTIVITY_SERVICE)))
                .setMemoryTrimmableRegistry(new MemoryTrimmableRegistry() {
                    @Override
                    public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
                        mMemoryTimmable.add(trimmable);
                        LogUtil.e("Fresco  registerMemoryTrimmable------------size=" + mMemoryTimmable.size());
                    }

                    @Override
                    public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {

                    }
                })
                .setResizeAndRotateEnabledForNetwork(true);

        // 就是这段代码，用于清理缓存
        NoOpMemoryTrimmableRegistry.getInstance().registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                LogUtil.e("Fresco  trim------------" + String.format("onCreate suggestedTrimRatio : %d", suggestedTrimRatio));
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
                }
            }
        });

        return configBuilder.build();
    }

    /**
     * 获取系统缓存
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static void trimMemory(int level) {
        if (mMemoryTimmable != null) {
            for (MemoryTrimmable trimmable : mMemoryTimmable) {
                trimmable.trim(MemoryTrimType.OnSystemLowMemoryWhileAppInBackground);
            }
            mMemoryTimmable.clear();
        }
    }
}