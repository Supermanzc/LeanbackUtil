package com.wt.leanbackutil.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.wt.leanbackutil.R;

/**
 * @author junyan
 *         权限帮助类
 */
public class PermissionHelper {


    /**
     * 文件读写权限返回code码
     */
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 101;


    public static final int REQUEST_OPEN_APPLICATION_SETTINGS_CODE = 12345;


    private Activity baseActivity;
    /**
     * NotNull
     */
    private final PermissionModel[] permissionModels;

    private OnApplyPermissionListener onApplyPermissionListener;

    public PermissionHelper(Activity baseActivity) {
        this.baseActivity = baseActivity;
        permissionModels = new PermissionModel[]{
                new PermissionModel("存储空间", Manifest.permission.WRITE_EXTERNAL_STORAGE, "我们需要读写存储卡权限以方便我们临时保存一些数据", WRITE_EXTERNAL_STORAGE_CODE)
        };
    }

    public PermissionHelper(Activity baseActivity, PermissionModel[] permissionModels) {
        this.baseActivity = baseActivity;
        if (permissionModels == null) {
            permissionModels = new PermissionModel[]{
                    new PermissionModel("存储空间", Manifest.permission.WRITE_EXTERNAL_STORAGE, "我们需要读写存储卡权限以方便我们临时保存一些数据", WRITE_EXTERNAL_STORAGE_CODE)
            };
        }
        this.permissionModels = permissionModels;
    }


    private boolean haveRequestCode(int requestCode) {
        for (int i = 0; i < permissionModels.length; i++) {
            PermissionModel permissionModel = permissionModels[i];
            if (permissionModel != null && requestCode == permissionModel.requestCode) {
                return true;
            }
        }
        return false;
    }


    /**
     * 打开应用设置界面
     */
    private boolean openApplicationSettings(int requestCode) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + baseActivity.getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            baseActivity.startActivityForResult(intent, requestCode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查找申请权限的解释短语
     */
    private String findPermissionExplain(String permission) {
        if (permission != null) {
            for (int i = 0; i < permissionModels.length; i++) {
                PermissionModel permissionModel = permissionModels[i];
                if (permissionModel != null && permissionModel.permission != null && permission.equals(permissionModel.permission)) {
                    return permissionModel.explain;
                }
            }
        }
        return null;
    }

    /**
     * 查找申请权限的名称
     */
    private String findPermissionName(String permission) {
        if (permission != null) {
            for (int i = 0; i < permissionModels.length; i++) {
                PermissionModel permissionModel = permissionModels[i];
                if (permissionModel != null && permissionModel.permission != null && permission.equals(permissionModel.permission)) {
                    return permissionModel.name;
                }
            }
        }
        return null;
    }

    private void applyAllPermissionSuccess() {
        if (onApplyPermissionListener != null) {
            onApplyPermissionListener.onAfterApplyAllPermission();
            onApplyPermissionListener = null;
        }
    }

    private void applyError(String error) {
        if (onApplyPermissionListener != null) {
            onApplyPermissionListener.onApplyError(error);
            onApplyPermissionListener = null;
        }
    }

    public void setOnApplyPermissionListener(OnApplyPermissionListener onApplyPermissionListener) {
        this.onApplyPermissionListener = onApplyPermissionListener;
    }


    /**
     * 对应Activity的onRequestPermissionsResult方法
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (haveRequestCode(requestCode)) {
            if (permissions != null && grantResults != null && permissions.length > 0 && permissions[0] != null && grantResults.length > 0) {
                String permission = permissions[0];
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(baseActivity, permission)) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(baseActivity, R.style.DialogAlertStyle).setTitle("权限申请").setMessage(findPermissionExplain(permission))
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                applyPermissions();
                                            }
                                        });
                        builder.setCancelable(false);
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity, R.style.DialogAlertStyle).setTitle("权限申请")
                                .setMessage("请在打开的窗口的权限中开启" + findPermissionName(permission) + "权限,以正常使用本应用")
                                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        openApplicationSettings(REQUEST_OPEN_APPLICATION_SETTINGS_CODE);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        baseActivity.finish();
                                    }
                                });
                        builder.setCancelable(false);
                        builder.show();
                    }
                } else {// 到这里就表示用户允许了本次请求,我们继续检查是否还有待申请的权限没有申请
                    if (isAllRequestedPermissionGranted()) {
                        applyAllPermissionSuccess();
                    } else {
                        applyPermissions();
                    }
                }
            } else {
                applyError("权限申请返回异常");
            }
        } else {
            applyError("权限申请未知异常");
        }
    }


    /**
     * 对应Activity的onActivityResult方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_OPEN_APPLICATION_SETTINGS_CODE:
                if (isAllRequestedPermissionGranted()) {
                    applyAllPermissionSuccess();
                } else {
                    baseActivity.finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断是否所有的权限都被授权了
     */
    public boolean isAllRequestedPermissionGranted() {
        for (int i = 0; i < permissionModels.length; i++) {
            PermissionModel permissionModel = permissionModels[i];
            if (permissionModel != null && permissionModel.permission != null
                    && ContextCompat.checkSelfPermission(baseActivity, permissionModel.permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void applyPermissions() {
        for (int i = 0; i < permissionModels.length; i++) {
            PermissionModel permissionModel = permissionModels[i];
            if (permissionModel != null && permissionModel.permission != null
                    && ContextCompat.checkSelfPermission(baseActivity, permissionModel.permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(baseActivity, new String[]{permissionModel.permission}, permissionModel.requestCode);
                return;
            }
        }
        applyAllPermissionSuccess();
    }


    public static class PermissionModel {
        /**
         * 权限名称
         */
        public String name;
        /**
         * 请求的权限
         */
        public String permission;
        /**
         * 解析为什么请求这个权限
         */
        public String explain;

        /**
         * 请求代码
         */
        public int requestCode;

        public PermissionModel(String name, String permission, String explain, int requestCode) {
            this.name = name;
            this.permission = permission;
            this.explain = explain;
            this.requestCode = requestCode;
        }
    }

    /**
     * 权限申请事件监听
     */
    public interface OnApplyPermissionListener {
        /**
         * 申请所有权限之后的逻辑
         */
        void onAfterApplyAllPermission();

        void onApplyError(String error);

    }

}
