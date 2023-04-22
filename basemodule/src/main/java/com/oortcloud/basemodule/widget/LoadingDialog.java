package com.oortcloud.basemodule.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.oortcloud.basemodule.R;

import java.lang.ref.WeakReference;

public class LoadingDialog {
    private TextView loadingText;
    LoadViewCircularRing mLoadingView;
    Dialog mLoadingDialog;
    WeakReference<Context> mContext;

    public LoadingDialog(Context context, String msg) {
        mContext = new WeakReference<>(context);
        init(mContext.get(), msg);
    }

    private void init(Context context, String msg) {
        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_view, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_view);
        // 页面中的LoadingView
        mLoadingView = (LoadViewCircularRing) view.findViewById(R.id.lv_circularring);
        // 页面中显示文本
        loadingText = (TextView) view.findViewById(R.id.loading_text);
        // 显示文本
        loadingText.setText(msg);
        // 创建自定义样式的Dialog
        if(mLoadingDialog == null){
            mLoadingDialog = new Dialog(context, R.style.loading_dialog);
        }
        // 设置返回键无效
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public LoadingDialog(String msg, Context context) {
        init(context, msg);
        mLoadingDialog.setCancelable(true);//返回键取消有效
        mLoadingDialog.setCanceledOnTouchOutside(false);//点击外部不消失
    }

    public void setLoadingText(String msg) {
        if (loadingText != null) {
            loadingText.setText(msg);
        }
    }

    public void show() {
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
            mLoadingView.startAnim();
        }
    }

    public void close() {
        if (mLoadingDialog != null) {
            mLoadingView.stopAnim();
            mLoadingDialog.dismiss();
        }
    }

    public void setMsg(String msg) {
        loadingText.setText(msg);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        if (listener != null) {
            mLoadingDialog.setOnDismissListener(listener);
        }
    }
}