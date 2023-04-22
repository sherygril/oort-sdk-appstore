package com.oortcloud.basemodule;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import com.oortcloud.basemodule.constant.Constant;
import com.oortcloud.basemodule.utils.DeviceIdFactory;
import com.oortcloud.basemodule.utils.DeviceUtil;
import com.oortcloud.basemodule.utils.DnsUtils;
import com.oortcloud.basemodule.utils.LogUtils;
import com.oortcloud.basemodule.utils.fastsharedpreferences.FastSharedPreferences;

import java.io.File;
import java.lang.reflect.Method;

import static com.oortcloud.basemodule.constant.Constant.IS_USE_DOMAIN;

public class CommonApplication extends Application {
    /**
     * 系统上下文
     */
    private static Context mAppContext;
	private static double mlatitude;
    private static double mlongitude;
    private static String maddress;
    public static boolean canRefresh;
    public static String mSeralNum;
    public static String GUID;
    //log路径
    private static final String LOG_PATH= Environment.getExternalStorageDirectory().getPath() + File.separator + "Omm" +  File.separator;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this,LOG_PATH);

        mAppContext = getApplicationContext();
        //内存泄漏检测
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this);
//        }
        //初始化快速存取库
        FastSharedPreferences.init(this);

//        LogUtils.Builder lBuilder = new LogUtils.Builder(this);
        LogUtils.Builder lBuilder = new LogUtils.Builder(this)
                .setLogSwitch(true)// 设置log总开关，默认开
                .setGlobalTag("Oortcloud")// 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setBorderSwitch(false)// 输出日志是否带边框开关，默认开
                .setLogFilter(LogUtils.V);// log过滤器，和logcat过滤器同理，默认Verbose
        //域名解析劫持
        if (IS_USE_DOMAIN) {
            DnsUtils.hook();
        }
    }

    /**
     * 获取系统上下文：用于ToastUtil类
     */
    public static Context getAppContext() {
        if(mAppContext == null){
            mAppContext = getApplicationWithReflection().getApplicationContext();
        }
        return mAppContext;
    }



    private static Application getApplicationWithReflection() {
        try {
            Method method = Class.forName("android.app.ActivityThread").getMethod("currentActivityThread");
            method.setAccessible(true);
            Object activityThread = method.invoke(null);
            Object app = activityThread.getClass().getMethod("getApplication").invoke(activityThread);
            return (Application) app;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getLocalIp() {
        //value for name deviceIp == null 会报此错误
        String localIp = DeviceUtil.getLocalIpAddress(getAppContext());
        return TextUtils.isEmpty(localIp) ? "" : localIp;
    }

    public static String getGateway() {
        return Constant.BASE_URL;
    }

    public static double getMlatitude() {
        return mlatitude;
    }

    public static double getMlongitude() {
        return mlongitude;
    }

    public static String getMaddress() {
        return maddress;
    }

    public static void setMlatitude(double mlatitude) {
        CommonApplication.mlatitude = mlatitude;
    }

    public static void setMlongitude(double mlongitude) {
        CommonApplication.mlongitude = mlongitude;
    }

    public static void setMaddress(String maddress) {
        CommonApplication.maddress = maddress;
    }

    public static String getmSeralNum(){

        String seralnum = DeviceIdFactory.getSerialNumber();
        return seralnum;
    }
}

