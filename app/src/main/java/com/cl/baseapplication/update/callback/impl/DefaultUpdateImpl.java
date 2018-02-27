package com.cl.baseapplication.update.callback.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cl.baseapplication.base.BaseApplication;
import com.cl.baseapplication.update.callback.UpdateCallback;
import com.cl.baseapplication.update.data.UpdateInfo;

import java.util.HashMap;
import java.util.Map;


/**
 * @author admin
 */
public class DefaultUpdateImpl implements UpdateCallback {
    public static String IMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    @Override
    public void onCheckUpdate(final Context context, boolean isShow, final UpdateResultCallback resultCallback) {

        //网络请求
        final String type = "android";
        final String uuid = IMEI(context);
        final String version = getVersionName(context);
        final String murl = "http://temp03.longruinet.com/LR_APP/API/";
        final String url = murl + "update.ashx";
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = JSONObject.parseObject(s);
                JSONObject resData = jsonObject.getJSONObject("resData");
                if (!resData.isEmpty()) {
                    if ("1".equals(resData.getString("result"))) {
                        UpdateInfo body = new UpdateInfo();
                        String version_des = resData.getString("version_des");//版本描述
                        String url = resData.getString("url");//下载连接
                        String update_type = resData.getString("update_type");//是否强制升级   0不强制   1强制
                        String title = resData.getString("title");//最新版本标题
                        String type = resData.getString("type");//升级类型1客户主动升级    2系统静默升级
                        String wgtUrl = resData.getString("wgtUrl");//静默升级下载地址

                        body.setForce(update_type);
                        body.setUrl(url);
                        body.setContent(version_des);
                        body.setTitle(title);

                        resultCallback.onUpdateSuccessResultCallback(body);
                    } else if ("0".equals(resData.getString("result"))) {

//                        resultCallback.onUpdateFailureResultCallback(resData.getString("errorMsg"));
                    } else {

//                        resultCallback.onUpdateFailureResultCallback(resData.getString("errorMsg"));
                    }
                }else {

                }
                Log.i("aa", "post请求成功" + s);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("aa", "post请求失败" + volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("terminalCode", uuid);
                map.put("version", version);
                map.put("type", type);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BaseApplication.getHttpQueue().add(request);

        //成功 执行  resultCallback.onUpdateSuccessResultCallback(body);

        //失败 执行  resultCallback.onUpdateFailureResultCallback();

//        if (null != updateInterface) {
//            Call<JsonResult<UpdateInfo>> call = updateInterface.checkUpdate(param);
//
//            call.enqueue(new BCallback<UpdateInfo>() {
//                @Override
//                public void onSuccess(int code, UpdateInfo body, String msg, int status) {
//                    resultCallback.onUpdateSuccessResultCallback(body);
//                }
//
//                @Override
//                public void onFailure(@ErrorKind String kind, @ErrorKind String msg) {
//                    resultCallback.onUpdateFailureResultCallback();
//                }
//            });
//        }

    }

    private String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }

    }

}
