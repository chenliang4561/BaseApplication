package com.cl.baseapplication.update.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.cl.baseapplication.update.data.UpdateInfo;
import com.cl.baseapplication.update.util.AppUtil;
import com.cl.baseapplication.update.util.ParseUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public abstract class BaseUpdate2Helper {

    private Context context;
    private AlertDialog checkUpdateDialog;
    private ProgressDialog updateDialog;
    private int vCode;

    private File file;
    private Response response;
    private Call downCall;

    private boolean downloadSuccess = false;
    private boolean isIoExp = false;
    private boolean isUrlRight = false;


    public BaseUpdate2Helper(Context context) {
        this.context = context;
        this.vCode = ParseUtil.getCode(AppUtil.getAppVersionName(context));
    }

    protected String genNegativeButtonText(UpdateInfo updateInfo) {
        return "1".equalsIgnoreCase(updateInfo.getForce()) ? "退出应用" : "取消更新";
    }

    protected DialogInterface.OnClickListener genPositiveDialogClickListener(final UpdateInfo updateInfo) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseUpdate2Helper.this.updateAPK(updateInfo);
            }
        };
    }

    protected DialogInterface.OnClickListener genNegativeDialogClickListener(final UpdateInfo updateInfo) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if ("1".equalsIgnoreCase(updateInfo.getForce())) {
                    BaseUpdate2Helper.this.checkUpdateDialog.dismiss();
                    System.exit(0);
                } else {
                    BaseUpdate2Helper.this.checkUpdateDialog.dismiss();
                    Toast.makeText(context, "取消更新", Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();
                }

            }
        };
    }

    public void onCheckUpdateSuccess(UpdateInfo updateInfo, boolean isShow) {
        this.checkUpdateDialog = this.buildCheckUpdateDialog(updateInfo);
        BaseUpdate2Helper.this.checkUpdateDialog.show();

//        if (ParseUtil.getCode(updateInfo.getVersion()) > this.vCode) {
//            this.checkUpdateDialog = this.buildCheckUpdateDialog(updateInfo);
//            BaseUpdate2Helper.this.checkUpdateDialog.show();
//        } else {
//            if (isShow)
//                Toast.makeText(this.context, "已经是最新版本", Toast.LENGTH_SHORT).show();
//            ((Activity) context).finish();
//        }
    }

    private void updateAPK(final UpdateInfo updateInfo) {
        this.updateDialog = this.buildUpdateDialog();
        this.updateDialog.setProgressStyle(1);
        this.updateDialog.setTitle("下载更新包(KB)");
        this.updateDialog.setCancelable(false);
        this.updateDialog.setButton(-2, "取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if ("1".equalsIgnoreCase(updateInfo.getForce())) {
                    BaseUpdate2Helper.this.checkUpdateDialog.dismiss();
                    System.exit(0);
                } else {
                    BaseUpdate2Helper.this.updateDialog.dismiss();
                }
                cancelDownload();
            }
        });
        BaseUpdate2Helper.this.updateDialog.show();
        (new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                try {
                    file = new File(Environment.getExternalStorageDirectory(),
                            context.getPackageName() + File.separator + "update" + File.separator + "update.apk");
                    if (file.exists()) {
                        file.delete();
                        file.createNewFile();
                    } else {
                        file.mkdirs();
                    }
                    OkHttpClient okHttpClient = new OkHttpClient();
                    downCall = okHttpClient.newCall(new Request.Builder().url(updateInfo.getUrl()).addHeader("Content-Type", "application/json").build());
                    response = downCall.execute();
                    ResponseBody body = response.body();
                    if (response.isSuccessful() && body != null) {
                        isUrlRight = false;
                        long updateAPKFileSize = body.contentLength();
                        if (checkExternalAvailable(updateAPKFileSize)) {

                            //修改检查可用空间 显示的对话框
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    BaseUpdate2Helper.this.buildTipDialog().show();
                                }
                            });

                            return;
                        }
                        InputStream is = body.byteStream();
                        BufferedInputStream input = new BufferedInputStream(is);

                        //---------------new logic begin-----------------
                        FileOutputStream output = new FileOutputStream(file);
                        BaseUpdate2Helper.this.updateDialog.setMax((Long.valueOf(updateAPKFileSize / 1024L)).intValue());
                        byte[] data = new byte[1024 * 4];
                        long getFileSize = 0L;
                        int count;
                        while ((count = input.read(data)) != -1) {
                            getFileSize += count;
                            output.write(data, 0, count);
                            BaseUpdate2Helper.this.updateDialog.setProgress((Long.valueOf(getFileSize / 1024L)).intValue());
                        }
                        output.flush();
                        //---------------new logic end-------------------

                        output.close();
                        input.close();
                        is.close();
                        BaseUpdate2Helper.this.downloadSuccess = isApkFile(context, file.getAbsolutePath());
                    } else {
                        isUrlRight = true;
                    }
                } catch (IOException var16) {
                    isIoExp = true;
                    BaseUpdate2Helper.this.downloadSuccess = false;
                } finally {
                    if (BaseUpdate2Helper.this.updateDialog.isShowing()) {
                        BaseUpdate2Helper.this.updateDialog.dismiss();
                    }
                }
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                if (BaseUpdate2Helper.this.downloadSuccess) {
                    if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
//                        //唤醒回调进行安装
//                        if (null != wakeUpInstall24Callback) {
//                            wakeUpInstall24Callback.onWakeUpInstall24(context, file);
//                        }
                        // TODO: 2017/9/13     //7.0的安装应用
//                        Uri apkUri = FileProvider7.getUriForFile(context, file);
                        Uri apkUri = Uri.fromFile(file);
                        intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    } else {
                        intent1 = new Intent("android.intent.action.VIEW");
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    }
                    BaseUpdate2Helper.this.context.startActivity(intent1);

//                    AppUtil.installApk(context, file, FileProviderUtil.getAuthority(context));
                }

                if (BaseUpdate2Helper.this.downloadSuccess) {
                    // TODO: 2017/9/13   //退出程序
                    System.exit(0);
                } else {
                    if (!isIoExp) {
                        if (isUrlRight) {
                            (new Handler(Looper.getMainLooper())).post((new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BaseUpdate2Helper.this.context, "下载失败，请重新下载", Toast.LENGTH_SHORT).show();
                                    if (!BaseUpdate2Helper.this.checkUpdateDialog.isShowing()
                                            && "1".equalsIgnoreCase(updateInfo.getForce())) {
                                        BaseUpdate2Helper.this.checkUpdateDialog.show();
                                    } else {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((Activity) context).finish();
                                            }
                                        });
                                    }
                                }
                            }));
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BaseUpdate2Helper.this.context, "更新失败，请检查您的网络连接", Toast.LENGTH_SHORT).show();
                                    if (!BaseUpdate2Helper.this.checkUpdateDialog.isShowing()
                                            && "1".equalsIgnoreCase(updateInfo.getForce())) {
                                        BaseUpdate2Helper.this.checkUpdateDialog.show();
                                    } else {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((Activity) context).finish();
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    } else {
                        isIoExp = false;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if ("1".equalsIgnoreCase(updateInfo.getForce())) {
                                    if (!BaseUpdate2Helper.this.checkUpdateDialog.isShowing()
                                            && "1".equalsIgnoreCase(updateInfo.getForce())) {
                                        BaseUpdate2Helper.this.checkUpdateDialog.show();
                                    } else {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((Activity) context).finish();
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(context, "更新失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((Activity) context).finish();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
                Looper.loop();
            }
        })).start();
    }

    private boolean checkExternalAvailable(long updateAPKFileSize) {
        long blockSizeLong;
        long availableBlocksLong;
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSizeLong = statFs.getBlockSizeLong();
            availableBlocksLong = statFs.getAvailableBlocksLong();
        } else {
            blockSizeLong = statFs.getBlockSize();
            availableBlocksLong = statFs.getAvailableBlocks();
        }
        long l = blockSizeLong * availableBlocksLong;
        return updateAPKFileSize > l;
    }

    private boolean isApkFile(Context context, String filePath) {
        boolean isNull = false;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (packageInfo != null)
                isNull = true;
        } catch (Exception e) {
            isNull = false;
            Log.w("BaseUpdate2Helper", e.getMessage());
        }
        return isNull;
    }

    private void cancelDownload() {
        if (file != null && file.exists()) {
            file.delete();
        }
        if (downCall != null && !downCall.isCanceled()) {
            downCall.cancel();
        }
    }

    public abstract AlertDialog buildCheckUpdateDialog(UpdateInfo var1);

    public abstract ProgressDialog buildUpdateDialog();

    public abstract AlertDialog buildTipDialog();

}