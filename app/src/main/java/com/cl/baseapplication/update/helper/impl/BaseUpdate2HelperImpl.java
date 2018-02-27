package com.cl.baseapplication.update.helper.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;

import com.cl.baseapplication.update.data.UpdateInfo;
import com.cl.baseapplication.update.helper.BaseUpdate2Helper;


public class BaseUpdate2HelperImpl extends BaseUpdate2Helper {
    private Context context;

    public BaseUpdate2HelperImpl(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public AlertDialog buildCheckUpdateDialog(UpdateInfo updateInfo) {
        if ("1".equalsIgnoreCase(updateInfo.getForce())) {
            return (new AlertDialog.Builder(this.context)).
                    setTitle("应用更新" + updateInfo.getTitle())
                    .setCancelable(false)
                    .setMessage(Html.fromHtml(null != updateInfo.getContent() ? updateInfo.getContent() : ""))
                    .setPositiveButton("现在更新", this.genPositiveDialogClickListener(updateInfo))
                    .create();
        }
        return (new AlertDialog.Builder(this.context)).
                setTitle("应用更新" + updateInfo.getTitle())
                .setCancelable(false)
                .setMessage(Html.fromHtml(null != updateInfo.getContent() ? updateInfo.getContent() : ""))
                .setPositiveButton("现在更新", this.genPositiveDialogClickListener(updateInfo))
                .setNegativeButton(this.genNegativeButtonText(updateInfo),
                        this.genNegativeDialogClickListener(updateInfo))
                .create();
    }


    @Override
    public ProgressDialog buildUpdateDialog() {
        return new ProgressDialog(this.context);
    }

    @Override
    public AlertDialog buildTipDialog() {
        //修改点击事件逻辑
        return (new AlertDialog.Builder(this.context)
                .setTitle("存储空间不足")
                .setCancelable(false).setMessage("手机系统存储空间不足！\n请删除不必要的文件\n重新下载")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((Activity) BaseUpdate2HelperImpl.this.context).finish();
                    }
                }).create());
    }
}
