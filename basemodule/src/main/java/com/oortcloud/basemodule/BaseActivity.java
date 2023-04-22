package com.oortcloud.basemodule;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //changeAppLanguage();
        initWebView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.action");  //这个ACTION和后面activity的ACTION一样就行，要不然收不到的
        registerReceiver(myBroadcastReceive, intentFilter);


    }


    protected String TAG = getClass().getSimpleName();

    @Override
    protected void attachBaseContext(Context newBase) {
        Log.e(TAG, "attachBaseContext");
        super.attachBaseContext(newBase);
//        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
//        //app杀进程启动后会调用Activity attachBaseContext
//        MultiLanguageUtil.getInstance().setConfiguration(newBase);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }



    @Override
    public void onBackPressed() {
        finish();
    }


    private void initWebView() {
        //new WebView(this).destroy();
    }
    public void changeAppLanguage() {
//        String sta = Store.getLanguageLocal(this);
//
//        if(sta.equals("")){
//            sta = "zh_CN";
//        }
//        if(sta != null && !"".equals(sta)){
//            // 本地语言设置
//     /*       Locale myLocale = new Locale(sta);
//            Resources res = getResources();
//            DisplayMetrics dm = res.getDisplayMetrics();
//            Configuration conf = res.getConfiguration();
//            conf.locale = myLocale;
//            res.updateConfiguration(conf, dm);*/
//            // 本地语言设置
//            //  Locale locale = new Locale("ug", Locale.CHINA.getCountry());
//            Locale myLocale=null;
//            if(sta.contains("zh")){
//                myLocale = new Locale("zh_CN",Locale.CHINESE.getCountry());
//            }else  if(sta.contains("en")){
//                myLocale = new Locale( "en",Locale.ENGLISH.getCountry());
//            }
//            else  if(sta.contains("ja")){
//                myLocale = new Locale( "ja",Locale.JAPANESE.getCountry());
//            }
//            Resources res = getResources();
//            DisplayMetrics dm = res.getDisplayMetrics();
//            Configuration conf = res.getConfiguration();
//            conf.locale = myLocale;
//            res.updateConfiguration(conf, dm);
//        }

    }
    BroadcastReceiver myBroadcastReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("广播", "----接收到的是----" + intent.getStringExtra("msg"));
            if(intent.getStringExtra("msg").equals("EVENT_REFRESH_LANGUAGE")){
                changeAppLanguage();
                recreate();//刷新界面

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceive);
       // EventBus.getDefault().unregister(this);
    }


    protected void setStatusBarLight(boolean light) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // >=5.0 背景为全透明
            /* >=5.0，this method(getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS));
            in some phone is half-transparent like vivo、nexus6p..
            in some phone is full-transparent
            so ...*/

            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (light) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
                window.setStatusBarColor(Color.TRANSPARENT);
                //window.setStatusBarColor(getResources().getColor(R.color.mainColor));
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                if (light) {
                    window.setStatusBarColor(getResources().getColor(R.color.main_color));
                } else {
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4背景为渐变半透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void toast(String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, str,
                        Toast.LENGTH_LONG).show();
            }
        });

    }


    public static void closeBoard(Context mcontext){
        InputMethodManager imm =(InputMethodManager) mcontext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        if(imm.isActive())  //一直是true
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public boolean isTopActivity(AppCompatActivity activity)
    {
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName().contains(activity.getLocalClassName());
    }


//    public static void hideSystemKeyBoard(Context mcontext,View v) {
//        InputMethodManager imm =(InputMethodManager) ((AbstractMmtClientActivity) mcontext)
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
//    }




}
