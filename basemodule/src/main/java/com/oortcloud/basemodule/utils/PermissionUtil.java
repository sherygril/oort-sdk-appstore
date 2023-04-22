package com.oortcloud.basemodule.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.oortcloud.basemodule.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 权限请求工具类
 * Created by Simon on 2017/6/25.
 * <p>请求时调用
 * mPermissionUtil = new PermissionUtil(MainActivity.this);
 * mPermissionUtil.requertLocationPermission(0);
 * <p>回调结果处理
 * switch (requestCode) {
 * case 0:
 * boolean location = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
 * if (location) {
 * // 已获取权限-todo
 * } else {
 * mPermissionUtil.requertLocationPermission(0);
 * }
 * break;
 * }
 */
@SuppressWarnings("all")
public class PermissionUtil {

    private Context mContext;

    public PermissionUtil(Context context) {
        this.mContext = context;
    }

    /**
     * 请求获取拍照权限
     * permission:android.permission.CAMERA
     */
    public boolean requestCameraPermission(int requestCode) {
        boolean b = checkPermission(mContext, Manifest.permission.CAMERA, requestCode);
        if (b) {
            return true;
        } else {
            requestPermission(mContext, new String[]{Manifest.permission.CAMERA}, requestCode);
        }
        return false;
    }

    /**
     * 请求获取录音权限
     * permission:android.permission.RECORD_AUDIO
     */
    public boolean requestAudioPermission(int requestCode) {
        boolean b = checkPermission(mContext, Manifest.permission.RECORD_AUDIO, requestCode);
        if (b) {
            return true;
        } else {
            requestPermission(mContext, new String[]{Manifest.permission.RECORD_AUDIO}, requestCode);
        }
        return false;
    }

    /**
     * 请求获取写入数据权限
     * permission:android.permission.READ_EXTERNAL_STORAGE
     * permission:android.permission.WRITE_EXTERNAL_STORAGE
     */
    public boolean requestStoragePermission(int requestCode) {
        boolean b = checkPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode);
        if (b) {
            return true;
        } else {
            requestPermission(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }
        return false;
    }

    /**
     * 请求获取定位权限
     * permission:android.permission.ACCESS_FINE_LOCATION
     * permission:android.permission.ACCESS_COARSE_LOCATION
     */
    public boolean requestLocationPermission(int requestCode) {
        boolean b = checkPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION, requestCode);
        if (b) {
            return true;
        } else {
            requestPermission(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
        }
        return false;
    }

    /**
     * 请求获取联系人权限
     * permission:android.permission.WRITE_CONTACTS
     * permission:android.permission.GET_ACCOUNTS
     * permission:android.permission.READ_CONTACTS
     */
    public boolean requestContactPermission(int requestCode) {
        boolean b = checkPermission(mContext, Manifest.permission.WRITE_CONTACTS, requestCode);
        if (b) {
            return true;
        } else {
            requestPermission(mContext, new String[]{Manifest.permission.WRITE_CONTACTS}, requestCode);
        }
        return false;
    }

    /**
     * 请求获取短信权限
     * permission:android.permission.READ_SMS
     * permission:android.permission.RECEIVE_WAP_PUSH
     * permission:android.permission.RECEIVE_MMS
     * permission:android.permission.RECEIVE_SMS
     * permission:android.permission.SEND_SMS
     * permission:android.permission.READ_CELL_BROADCASTS
     */
    public boolean requestSmsPermission(int requestCode) {
        boolean b = checkPermission(mContext, Manifest.permission.SEND_SMS, requestCode);
        if (b) {
            return true;
        } else {
            requestPermission(mContext, new String[]{Manifest.permission.SEND_SMS}, requestCode);
        }
        return false;
    }

    /**
     * 请求获取手机状态权限
     * permission:android.permission.READ_CALL_LOG
     * permission:android.permission.READ_PHONE_STATE
     * permission:android.permission.CALL_PHONE
     * permission:android.permission.WRITE_CALL_LOG
     * permission:android.permission.USE_SIP
     * permission:android.permission.PROCESS_OUTGOING_CALLS
     * permission:com.android.voicemail.permission.ADD_VOICEMAIL
     */
    public boolean requestPhoneStatePermission(int requestCode) {
        boolean b = checkPermission(mContext, Manifest.permission.READ_PHONE_STATE, requestCode);
        if (b) {
            return true;
        } else {
            requestPermission(mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, requestCode);
        }
        return false;
    }

    /**
     * 请求获取日历权限
     * permission:android.permission.READ_CALENDAR
     * permission:android.permission.WRITE_CALENDAR
     */
    public boolean requestCalendarPermission(int requestCode) {
        boolean b = checkPermission(mContext, Manifest.permission.WRITE_CALENDAR, requestCode);
        if (b) {
            return true;
        } else {
            requestPermission(mContext, new String[]{Manifest.permission.WRITE_CALENDAR}, requestCode);
        }
        return false;
    }

    /**
     * 请求获取传感器权限
     * permission:android.permission.BODY_SENSORS
     */
    public boolean requestSensorsPermission(int requestCode) {
        boolean b = checkPermission(mContext, Manifest.permission.BODY_SENSORS, requestCode);
        if (b) {
            return true;
        } else {
            requestPermission(mContext, new String[]{Manifest.permission.BODY_SENSORS}, requestCode);
        }
        return false;
    }

    /**
     * 请求获取多个权限 - 一般用于首次进入提示
     * List<String> mPermissionsList = new ArrayList<>();
     * mPermissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
     * mPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
     * boolean b = requestPermissions(mPermissionsList);
     */
    public boolean requestPermissions(List<String> permissionsList) {
        boolean flag = true;
        List<String> mPermissionsList = new ArrayList<>();
        if (!mPermissionsList.isEmpty()) {
            mPermissionsList.clear();
        }
        for (int i = 0; i < permissionsList.size(); i++) {
            String permission = permissionsList.get(i);
            boolean b = checkPermission(mContext, permission, 10);
            if (!b) {
                flag = b;
                mPermissionsList.add(permission);
            }
        }
        if (!mPermissionsList.isEmpty()) {
            String[] permissions = (String[]) mPermissionsList.toArray(new String[mPermissionsList.size()]);
            requestPermission(mContext, permissions, 10);
        }
        return flag;
    }

    /**
     * 检测权限
     */
    public static boolean checkPermission(Context context, String permission, int code) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // judgePermission(context, permission, code);
        } else {
            return true;
        }
        return false;
    }

    /**
     * 请求权限
     */
    private void requestPermission(Context context, String[] permissions, int code) {
        ActivityCompat.requestPermissions((Activity) context, permissions, code);
    }

    /**
     * 判断是否已拒绝过权限
     *
     * @describe :如果应用之前请求过此权限但用户拒绝，此方法将返回 true;
     * -----------如果应用第一次请求权限或 用户在过去拒绝了权限请求，
     * -----------并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     */
    private void judgePermission(Context context, String permission, int code) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
            requestPermission(context, new String[]{permission}, code);
        } else {
            toPermissionSetting(context);
        }
    }

    /**
     * 跳转到权限设置界面
     */
    private void toPermissionSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }


    public static boolean checkSelfPermissions(@NonNull Activity activity, @NonNull String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取被拒绝的权限
     */
    public static List<String> getDeniedPermissions(@NonNull Activity activity, @NonNull String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }
        if (!deniedPermissions.isEmpty()) {
            return deniedPermissions;
        }
        return null;
    }


    /**
     * 是否拒绝了再次申请权限的请求（选择了不再询问 || 部分机型默认为不在询问）
     */
    public static boolean deniedRequestPermissionsAgain(@NonNull Activity activity, @NonNull String... permissions) {
        for (String permission : permissions) {
            /**
             * 注：调用该方法的前提是应用已申请过该权限，如应用未申请就调用此方法，返回false
             * 1.已请求过该权限且用户拒绝了请求,返回true
             * 2.用于拒绝了请求，且选择了不再询问,返回false
             * 3.设备规范禁止应用具有该权限，返回false
             */
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    public static void startApplicationDetailsSettings(@NonNull Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 申请权限<br/>
     *
     * @return 是否已经获取权限
     */
    public static boolean requestPermissions(Activity activity, int requestCode, String... permissions) {
        if (!checkSelfPermissions(activity, permissions)) {// 权限不全
            List<String> deniedPermissions = getDeniedPermissions(activity, permissions);
            if (deniedPermissions != null) {// 申请权限
                ActivityCompat.requestPermissions(activity, deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            }
            return false;
        }
        return true;
    }

    /**
     * 申请定位权限
     *
     * @param activity
     * @param requestCode
     * @return
     */
    public static boolean requestLocationPermissions(Activity activity, int requestCode) {
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (!checkSelfPermissions(activity, permissions)) {// 权限不全
            List<String> deniedPermissions = getDeniedPermissions(activity, permissions);
            if (deniedPermissions != null) {// 申请权限
                ActivityCompat.requestPermissions(activity, deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            }
            return false;
        }
        return true;
    }

    /**
     * 申请权限返回
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,
                                                  @NonNull OnRequestPermissionsResultCallbacks callBack) {
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }
        if (!granted.isEmpty()) {
            callBack.onPermissionsGranted(requestCode, granted, denied.isEmpty());
        }
        if (!denied.isEmpty()) {
            callBack.onPermissionsDenied(requestCode, denied, granted.isEmpty());
        }
    }


    /**
     * 申请权限返回
     */
    public interface OnRequestPermissionsResultCallbacks {
        /**
         * @param isAllGranted 是否全部同意
         */
        void onPermissionsGranted(int requestCode, List<String> perms, boolean isAllGranted);

        /**
         * @param isAllDenied 是否全部拒绝
         */
        void onPermissionsDenied(int requestCode, List<String> perms, boolean isAllDenied);
    }
}