package com.oortcloud.basemodule.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @FileName: AbsNavigationBar.java
 * @Author: ZZJun / @CreateDate: 2020/7/10 22:07
 * @Version: 1.0
 * @Function: 头部Builder基类
 */
public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams>implements INavigationBar {
    private P mParams;

    private View mNavigationView;

    public AbsNavigationBar( P params){
        this.mParams = params;
        createAndBindView();
    }

    /**
     * 绑定和创建View
     */
    private void createAndBindView() {
//        //创建View
//        mNavigationView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId() , mParams.mParent ,false);
//        //添加
//       mParams.mParent.addView(mNavigationView , 0 );

        // 1. 创建View
        if(mParams.mParent == null){
            // 获取activity的根布局，View源码
            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext))
                    .getWindow().getDecorView();
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }

        // 处理Activity的源码，后面再去看


        if(mParams.mParent == null){
            return;
        }

        mNavigationView = LayoutInflater.from(mParams.mContext).
                inflate(bindLayoutId(), mParams.mParent, false);// 插件换肤

        // 2.添加
        mParams.mParent.addView(mNavigationView, 0);
       applyView();
    }

    /**
     * 设置文本
     * @param viewId
     * @param text
     */
    protected void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if(!TextUtils.isEmpty(text)){
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }
    /**
     * 设置图片资源
     * @param viewId
     * @param res
     */
    protected void setRes(int viewId, int res) {
        TextView tv = findViewById(viewId);
        if(res != 0){
            tv.setVisibility(View.VISIBLE);
            tv.setBackgroundResource(res);
        }
    }

    /**
     * 设置点击
     * @param viewId
     * @param listener
     */
    protected void setOnClickListener(int viewId,View.OnClickListener listener){
        findViewById(viewId).setOnClickListener(listener);
    }

    public <T extends View> T findViewById(int viewId){
        return (T)mNavigationView.findViewById(viewId);
    }


    public P getParams() {
        return mParams;
    }
    public abstract static class Builder{

        public Builder(Context context , ViewGroup params){
        }
        public abstract AbsNavigationBar builder();


        public static class AbsNavigationParams{
            public Context mContext;
            public ViewGroup mParent;

            protected AbsNavigationParams(Context context , ViewGroup parent){
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }

}
