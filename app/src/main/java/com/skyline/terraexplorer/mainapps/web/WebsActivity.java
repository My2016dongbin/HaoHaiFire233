package com.skyline.terraexplorer.mainapps.web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.activity.BaseActivity;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;

import java.io.File;

public class WebsActivity extends BaseActivity implements ReWebChomeClient.OpenFileChooserCallBack {
    private WebView activity_web;
    private ActionBar actionBar;
    //private TextView utCell;
    String webUrl = "";
    private String TAG = WebsActivity.class.getSimpleName();


    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mUploadMsg5Plus;
    boolean loadSuccess = false;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(!loadSuccess){
                activity_web.loadUrl(webUrl);
                mHandler.sendEmptyMessageDelayed(0,15000);
            }
        }
    };


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.detectFileUriExposure();
        }
        //沉浸式状态栏
        /*if (Build.VERSION.SDK_INT >= 23) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }*/
        //去除ActionBar
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_bottom_event);

        Intent intent = getIntent();
        webUrl = intent.getStringExtra("webUrl");
        Log.e(TAG, "onCreate: webUrl = " + webUrl);


        requestPermission();

        activity_web = findViewById(R.id.activity_web);
        actionBar = findViewById(R.id.action_bar);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("绿地监管");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        //utCell = findViewById(R.id.utCell);

        setData();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else {
                //activity_web.loadUrl("https://xxxxxxxxxxxxxxxxxxxxxxxx");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    public class AndroidToJS {
        @JavascriptInterface
        public void stopRefresh(){
            loadSuccess = true;
            Log.e(TAG, "stopRefresh: web回调" );
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void setData() {

        /*utCell.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                int what = event.getButtonState();
                if (what == MotionEvent.EDGE_RIGHT) {
                    Toast.makeText(WebsActivity.this, "ok", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });*/
        activity_web.addJavascriptInterface(new AndroidToJS(), "android");
        activity_web.setWebViewClient(new SafeWebViewClient());
        activity_web.setWebChromeClient(new ReWebChomeClient(WebsActivity.this));
        fixDirPath();

        WebSettings settings = activity_web.getSettings();
        if (settings == null) {
            return;
        }

        //支持js使用
        settings.setJavaScriptEnabled(true);
        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);
        //设置WebView缓存模式
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //支持启用缓存模式
        settings.setAppCacheEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //保存密码提醒
        settings.setSavePassword(true);
        //支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);//不显示缩放按钮

        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.supportMultipleWindows();
        settings.setAllowContentAccess(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSaveFormData(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);


        activity_web.loadUrl(webUrl);
    }


    /**
     * JS调用android的方法
     * <p>
     * •getClient html页面的JS可以通过这个方法回调原生APP，这个方法有个注解@JavascriptInterface，这个是必须的，
     * 这个方法有个字符串参数，这个方法跟我们在onCreate中调用addJavascriptInterface传入的name一起使用的。
     * 例如html中想要回调这个方法可以这样写:javascript:android.getClient(“传一个字符串给客户端”);
     *
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void getClient(String str) {
        Log.e("WebActivity", "html调用客户端:" + str);
    }

    public class SafeWebViewClient extends WebViewClient {
        /**
         * 当WebView得页面Scale值发生改变时回调
         */
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        /**
         * 是否在 WebView 内加载页面
         *
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            /*非http或https的自定义的协议时webView出现ERR_UNKNOWN_URL_SCHEME 异常的解决方案 bingo~*/
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }
            }
            view.loadUrl(url);
            return true;
        }

        /**
         * WebView 开始加载页面时回调，一次Frame加载对应一次回调
         *
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            showLoadings();
        }

        /**
         * WebView 完成加载页面时回调，一次Frame加载对应一次回调
         *
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "onPageFinished: url= " + url);
            if(url==null || url.length()==0 || url.equals("about:blank")){
            }

        }

        /**
         * WebView 加载页面资源时会回调，每一个资源产生的一次网络加载，除非本地有当前 url 对应有缓存，否则就会加载。
         *
         * @param view WebView
         * @param url  url
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        /**
         * WebView 访问 url 出错
         */
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            Log.e(TAG, "onReceivedError: onReceivedError WebView 访问 url 出错" );

        }

        /**
         * WebView ssl 访问证书出错，handler.cancel()取消加载，handler.proceed()对然错误也继续加载
         *
         * @param view
         * @param handler
         * @param error
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
//            hideLoadings();
        }


    }

    public class SafeWebChromeClient extends WebChromeClient {

        /*
         * 输出Web端日志
         * */
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }

        /**
         * 当前 WebView 加载网页进度
         *
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        /**
         * Js 中调用 alert() 函数，产生的对话框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(WebsActivity.this)
                            .setMessage(message)
                            .setPositiveButton("确定", null)
                            .setOnKeyListener(new DialogInterface.OnKeyListener() {// 屏蔽keycode等于84之类的按键
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    return true;
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });
            // 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            result.confirm();
            return true;
        }

        /**
         * 处理 Js 中的 Confirm 对话框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, final String message, final JsResult result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(WebsActivity.this)
                            .setMessage(message)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    result.confirm();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    result.cancel();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    result.cancel();
                                }
                            })
                            .setOnKeyListener(new DialogInterface.OnKeyListener() {// 屏蔽keycode等于84之类的按键
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    return true;
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });

            return true;
        }

        /**
         * 处理 JS 中的 Prompt对话框
         *
         * @param view
         * @param url
         * @param message
         * @param defaultValue
         * @param result
         * @return
         */
        @Override
        public boolean onJsPrompt(final WebView view, String url, final String message, final String defaultValue, final JsPromptResult result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final EditText et = new EditText(view.getContext());
                    et.setSingleLine();
                    et.setText(defaultValue);
                    new AlertDialog.Builder(WebsActivity.this)
                            .setMessage(message)
                            .setView(et)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    result.confirm(et.getText().toString());
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    result.cancel();
                                }
                            })
                            .setOnKeyListener(new DialogInterface.OnKeyListener() {// 屏蔽keycode等于84之类的按键
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    return true;
                                }
                            })
                            .setCancelable(false)
                            .show();

                }
            });

            return true;
        }

        /**
         * 接收web页面的icon
         *
         * @param view
         * @param icon
         */
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        /**
         * 接收web页面的 Title
         *
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (activity_web.canGoBack()) {
                Log.e(TAG, "onKey: canGoBack");
                activity_web.goBack();
            } else {
                Log.e(TAG, "onKey: !canGoBack");
                exit_();
            }
            return true;
        }

        return false;
    }

    private boolean extT = false;
    private void exit_(){
        if(extT){
            WebsActivity.this.finish();
        }else{
            Toast.makeText(this, "再次返回退出应用", Toast.LENGTH_SHORT).show();
            extT = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    extT = false;
                }
            },2000);
        }
    }


    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new ReOnCancelListener());
        alertDialog.setTitle("选择图片");
        alertDialog.setItems(R.array.options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mSourceIntent = ImageUtil.choosePicture();
                            startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                        } else {
                            mSourceIntent = ImageUtil.takeBigPicture();
                            startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
                        }
                    }
                }
        );
        alertDialog.show();
    }

    private class ReOnCancelListener implements DialogInterface.OnCancelListener {

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
            }
            if (mUploadMsg5Plus != null) {
                mUploadMsg5Plus.onReceiveValue(null);
                mUploadMsg5Plus = null;
            }
        }
    }


    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showOptions();
    }

    @Override
    public void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback) {
        mUploadMsg5Plus = filePathCallback;
        showOptions();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: resultCode " + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            if(mUploadMsg!=null){
                mUploadMsg.onReceiveValue(null);
            }
            if(mUploadMsg5Plus!=null) {
                mUploadMsg5Plus.onReceiveValue(null);
            }
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
                try {
                    //指定拍照路径后data返回null
                    if (mUploadMsg == null && mUploadMsg5Plus == null) {
                        return;
                    }
                    String newPhotoPath = mSourceIntent.getStringExtra("newPhotoPath");
                    if(newPhotoPath==null){
                        break;
                    }
                    Uri uri = Uri.fromFile(new File(newPhotoPath));
                    if (mUploadMsg != null) {
                        mUploadMsg.onReceiveValue(uri);
                        mUploadMsg = null;
                    } else {
                        mUploadMsg5Plus.onReceiveValue(new Uri[]{uri});
                        mUploadMsg5Plus = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onActivityResult: e " + e.toString() );
                }
                break;
            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    if (data == null) {
                        return;
                    }
                    if (mUploadMsg == null && mUploadMsg5Plus == null) {
                        return;
                    }
                    String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                    if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                        Log.w(TAG, "sourcePath empty or not exists.");
                        break;
                    }
                    Uri uri = Uri.fromFile(new File(sourcePath));
                    if (mUploadMsg != null) {
                        mUploadMsg.onReceiveValue(uri);
                        mUploadMsg = null;
                    } else {
                        mUploadMsg5Plus.onReceiveValue(new Uri[]{uri});
                        mUploadMsg5Plus = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
