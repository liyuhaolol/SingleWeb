package spa.lyh.cn.ft_webview.webview;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import spa.lyh.cn.ft_webview.R;
import spa.lyh.cn.ft_webview.webview.util.ShareUtil;
import spa.lyh.cn.lib_utils.PixelUtils;
import spa.lyh.cn.lib_utils.translucent.BarUtils;
import spa.lyh.cn.lib_utils.translucent.TranslucentUtils;
import spa.lyh.cn.lib_utils.translucent.navbar.NavBarFontColorControler;
import spa.lyh.cn.lib_utils.translucent.statusbar.StatusBarFontColorControler;


/**
 * 作者：李宇昊
 * 作为转跳界面使用
 */
public class WebViewActivity extends AppCompatActivity{
    private RelativeLayout copy,close,share;
    private String url;
    private String title;
    private WebView webView;
    private TextView title_tv;
    private ProgressBar progressBar;
    private Object shareDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        //初始化布局
        TranslucentUtils.setTranslucentBoth(this);
        StatusBarFontColorControler.setStatusBarMode(this,true);
        NavBarFontColorControler.setNavBarMode(this,true);
        BarUtils.autoFitBothBar(this,R.id.status_bar,R.id.nav_bar);
        //注册控件
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        copy = findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(url)){
                    //有链接
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData data;
                    if (!TextUtils.isEmpty(webView.getUrl())){
                        data = ClipData.newPlainText("url",webView.getUrl());
                    }else {
                        data = ClipData.newPlainText("url",url);
                    }
                    cmb.setPrimaryClip(data);
                    showToast("已复制到剪贴板");
                }else {
                    showToast("没有链接数据");
                }
            }
        });
        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(title)){
                    showShare();
                }
            }
        });
        title_tv = findViewById(R.id.title);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        //处理数据
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        initWebview();
        setWebviewClient();
        setWebChromeClient();
        //加载网页
        if (!TextUtils.isEmpty(url)){
            if (url.startsWith("http")){
                webView.loadUrl(url);
            }else {
                showToast("无效网页链接");
            }
        }else {
            showToast("网页链接为空");
        }
        checkShare();
        shareDialog = ShareUtil.initShareDialog(this);
        ShareUtil.registerResultListener(this,shareDialog);
    }

    private void checkShare(){
        if (TextUtils.isEmpty(title)){
            share.setVisibility(View.GONE);
            return;
        }
        if (!ShareUtil.isActivited()){
            share.setVisibility(View.GONE);
        }
    }

    /**
     * 显示分享
     */
    private void showShare() {
        String thisUrl;
        if (!TextUtils.isEmpty(webView.getUrl())){
            thisUrl = webView.getUrl();
        }else {
            thisUrl = url;
        }
        ShareUtil.initWebPageShare(shareDialog,title,thisUrl,null);
        ShareUtil.showDialog(shareDialog);
    }

    private void initWebview(){
        webView = findViewById(R.id.web);
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings webSettings = webView.getSettings();
        //屏蔽图片
        //webSettings.setBlockNetworkImage(true);
        // 不支持缩放
        webSettings.setSupportZoom(false);

        // 自适应屏幕大小
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //使用缓存
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);

        //DOM Storage
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        //启动对js的支持
        webSettings.setJavaScriptEnabled(true);
        //启动Autoplay
        //webSettings.setMediaPlaybackRequiresUserGesture(false);
        //对图片大小适配
        webSettings.setUseWideViewPort(true);
        //对文字大小适配
        webSettings.setLoadWithOverviewMode(true);
        // 判断系统版本是不是5.0或之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //让系统不屏蔽混合内容和第三方Cookie
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
            webSettings.setMixedContentMode(0);//永远允许
        }
    }

    private void setWebviewClient(){
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http")){
                    return super.shouldOverrideUrlLoading(view, url);
                }else {
                    //拦截
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                /*webView.getSettings().setBlockNetworkImage(false);
                if (!webView.getSettings().getLoadsImagesAutomatically()) {

                    webView.getSettings().setLoadsImagesAutomatically(true);
                }*/
            }
        });
    }

    private void setWebChromeClient(){
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(title)){
                    title_tv.setText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    progressBar.setProgress(newProgress);
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    private void showToast(String content){
        try{
            Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
