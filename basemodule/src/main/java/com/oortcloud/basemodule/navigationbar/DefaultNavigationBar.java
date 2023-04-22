package com.oortcloud.basemodule.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.oortcloud.basemodule.R;

/**
 * @FileName: DefaultNavigationBar.java
 * @Author: ZZJun / @CreateDate: 2020/7/12 19:35
 * @Version: 1.0
 * @Function:
 */
public class DefaultNavigationBar<D extends
        DefaultNavigationBar.Builder.DefaultNavigationParams> extends
        AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {
    public DefaultNavigationBar(Builder.DefaultNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return  R.layout.title_bar;
    }

    @Override
    public void applyView() {
        // 绑定效果

        setText(R.id.title_tv, getParams().mTitle);

        if (getParams().mMoreClickListener != null){
            findViewById(R.id.home_rl).setVisibility(View.VISIBLE);
            setOnClickListener(R.id.home_tv , getParams().mLeftClickListener);
            setOnClickListener(R.id.more_tv , getParams().mMoreClickListener);
        }else {
            findViewById(R.id.back).setVisibility(View.VISIBLE);
            setOnClickListener(R.id.back , getParams().mLeftClickListener);
            setParams(R.id.right_text,getParams().mRightText , getParams().mRightRes , getParams().mRightClickListener);
            setParams(R.id.right_setting_text,getParams().mSettingText , getParams().mSettingRes , getParams().mSettingClickListener);
        }

    }

    private void setParams(int viewId , String text , int res , View.OnClickListener listener){
        setText(viewId, text);
        setRes(viewId, res);
        setOnClickListener(viewId, listener);
    }

    public static class Builder extends AbsNavigationBar.Builder{
        DefaultNavigationParams P ;
        public Builder(Context context, ViewGroup parent) {
            super(context , parent);
             P = new DefaultNavigationParams(context , parent);

        }
        public Builder(Context context) {
            this(context , null);
        }

        @Override
        public DefaultNavigationBar builder() {
            DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar(P);
            return defaultNavigationBar;
        }
        // 1. 设置所有效果
        public Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }
        public Builder setRightText(String rightText) {
            P.mRightText = rightText;
            return this;
        }

        public Builder setRightRes(int rightRes) {
            P.mRightRes = rightRes;
            return this;
        }
        public Builder setSettingRes(int settingRes) {
            P.mSettingRes = settingRes;
            return this;
        }
        /**
         * 设置更多点击事件
         */
        public Builder
        setMoreClickListener(View.OnClickListener moreListener) {
            P.mMoreClickListener = moreListener;
            return this;
        }
        /**
         * 设置black点击事件
         */
        public Builder
        setLeftClickListener(View.OnClickListener moreListener) {

            P.mMoreClickListener = moreListener;
            return this;
        }
         /**
         * 设置右边的点击事件
         */
        public Builder
        setRightClickListener(View.OnClickListener rightListener) {

            P.mRightClickListener = rightListener;
            return this;
        }
        /**
         * 设置Setting的点击事件
         */
        public Builder
        setSettingClickListener(View.OnClickListener settingListener) {

            P.mSettingClickListener = settingListener;
            return this;
        }


        protected static class DefaultNavigationParams
                extends AbsNavigationBar.Builder.AbsNavigationParams{
            protected String mTitle;

            protected String mLeftText;

            protected int mLeftRes;

            protected String mSearchText;

            protected int mSearchRes;

            protected String mDownloadText;

            protected int mDownloadRes;

            protected String mRightText;

            protected int mRightRes;

            protected String mSettingText;

            protected int mSettingRes;


            // 后面还有一些通用的
            protected View.OnClickListener mRightClickListener;

            protected View.OnClickListener mSearchClickListener;

            protected View.OnClickListener mDownloadClickListener;

            protected View.OnClickListener mMoreClickListener;

            protected View.OnClickListener mSettingClickListener;

            protected View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 关闭当前Activity
                    ((Activity) mContext).finish();
                }
            };
            protected DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }

        }

    }


}
