package com.cl.baseapplication.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cl.baseapplication.R;
import com.cl.baseapplication.base.BaseActivity;
import com.cl.baseapplication.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.webkit.WebSettings.PluginState.ON;

/**
 * WebView -- 可打开Https,下载,打开系统相机相册
 *
 * @author cliang
 * @date 2017-12-11
 */

public class WebViewActivity extends BaseActivity {

    private static final String TAG = "WebViewActivity";
    private final static int FCR = 1;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String mCM;
    private String mUrl = "";
    private String mFilePath = "";
    private String mCompressPath = "";
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = findViewById(R.id.web_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mUrl = "https://www.baidu.com/";
        initView();
    }

    private void initView() {
        // 启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        //如果访问的页面中有Javascript，则WebView必须设置支持Javascript
        //是否使用缓存
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);

        //设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        //是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //是否显示缩放按钮，默认false
        settings.setBuiltInZoomControls(true);
        settings.setSupportMultipleWindows(false);

        //设置此属性，可任意比例缩放。大视图模式
        settings.setUseWideViewPort(false);
        //是否可以缩放，默认true
        settings.setSupportZoom(false);
        settings.setPluginState(ON);

        //可以加载html5网页
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);

        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        // 加载index.html
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                    view.loadUrl(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                handler.proceed();// 接受所有网站的证书
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                selectImage();
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                selectImage();
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                selectImage();
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), WebViewActivity.FCR);
            }

            //For Android 5.0+
            @Override
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {
                selectImage();
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        mFilePath = photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }

            }
        });

        //监听WebView中下载
        mWebView.setDownloadListener(new WebViewDownLoadListener());
    }

    /**
     * 打开图库,同时处理图片
     */
    private void selectImage() {
        mCompressPath = Environment.getExternalStorageDirectory().getPath() + "/QWB/temp";
        File file = new File(mCompressPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        mCompressPath = mCompressPath + File.separator + "compress.png";
        File image = new File(mCompressPath);
        if (image.exists()) {
            image.delete();
        }
    }

    /**
     * 创建图片
     */
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String timeStamp = formatter.format(curDate);
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{afterChosePic(mFilePath, mCompressPath)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            if (results == null) {
                return;
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
                if (null == mUM) {
                    return;
                }
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    /**
     * 选择照片后结束
     */
    private Uri afterChosePic(String oldPath, String newPath) {
        File newFile;
        try {
            newFile = FileUtil.compressFile(oldPath, newPath);
        } catch (Exception e) {
            e.printStackTrace();
            newFile = null;
        }
        return Uri.fromFile(newFile);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 自定义DownloadListener类，实现WebView中下载操作
     */
    private class WebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            System.out.println("download url .............." + url);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
