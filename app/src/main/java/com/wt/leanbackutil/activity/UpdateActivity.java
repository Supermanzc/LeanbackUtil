package com.wt.leanbackutil.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.util.LogUtil;

import java.io.File;

/**
 * @author junyan
 *         更新验证，编译的低版本向高版本更新
 */

public class UpdateActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        File file = getApplication().getExternalCacheDir();
        LogUtil.e("file----------" + file.getAbsolutePath());
//        /storage/emulated/0/Android/data/com.wt.leanbackutil/cache
        installApk(file.getAbsolutePath() + "/a.apk");
    }

    private void installApk(String apkPath) {
        File apkFile = new File(apkPath);
        PackageInfo info = getPackageManager().getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            apkFile.setExecutable(true, false);
            apkFile.setReadable(true, false);
            apkFile.setWritable(true, false);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            startActivity(i);
        }
    }
}
