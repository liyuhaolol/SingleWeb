package spa.lyh.cn.ft_webview.webview

import android.content.ClipData
import android.content.ClipboardManager
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import spa.lyh.cn.ft_webview.BuildConfig
import spa.lyh.cn.ft_webview.R
import spa.lyh.cn.ft_webview.databinding.ActivityWebviewBinding
import spa.lyh.cn.ft_webview.webview.base.BaseActivity
import spa.lyh.cn.ft_webview.webview.util.ShareUtil
import spa.lyh.cn.lib_utils.translucent.BarUtils
import spa.lyh.cn.lib_utils.translucent.TranslucentUtils
import spa.lyh.cn.lib_utils.translucent.navbar.NavBarFontColorControler
import spa.lyh.cn.lib_utils.translucent.statusbar.StatusBarFontColorControler

class WebViewActivity:BaseActivity() {
    private lateinit var b:ActivityWebviewBinding
    private var ua:String = ""
    private var url:String = ""
    private var title: String = ""
    private var shareDialog:Any? = null
    private var isFirstCheck = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(b.root)
        ua = intent.getStringExtra("ua")?:""
        //初始化布局
        TranslucentUtils.setTranslucentBoth(window)
        StatusBarFontColorControler.setStatusBarMode(window, true)
        NavBarFontColorControler.setNavBarMode(window, true)
        BarUtils.autoFitStatusBar(this, b.statusBar)
        //注册控件
        b.close.setOnClickListener(View.OnClickListener {
            finish()
        })

        b.copy.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(url)){
                //有链接
                val cmb: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val data: ClipData = if (!TextUtils.isEmpty(b.web.url)){
                    ClipData.newPlainText("url",b.web.url)
                }else{
                    ClipData.newPlainText("url",url)
                }
                cmb.setPrimaryClip(data)
                showToast(getString(R.string.copy))
            }else{
                showToast(getString(R.string.no_web_url))
            }
        })
        b.share.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(title)) {
                showShare()
            }
        })
        b.progressBar.progress = 0
        //处理数据
        url = intent.getStringExtra("url")?:""
        title = intent.getStringExtra("title")?:""
        initWebview()
        setWebviewClient()
        setWebChromeClient()
        //加载网页
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http")) {
                b.web.loadUrl(url)
            } else {
                showToast(getString(R.string.wrong_url))
            }
        } else {
            showToast(getString(R.string.no_web_url))
        }
        checkShare()
        shareDialog = ShareUtil.initShareDialog(this)
        ShareUtil.registerResultListener(this, shareDialog)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (b.web.canGoBack()) {
                    b.web.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        BarUtils.NavbarHeightCallback(
            this
        ) { height, navbarType ->
            val navbarView = findViewById<View>(R.id.nav_bar)
            val oldHeight = navbarView.measuredHeight
            if (oldHeight != height) {
                //高度发生改变
                if (navbarType == BarUtils.NORMAL_NAVIGATION) {
                    val layoutParams = navbarView.layoutParams
                    layoutParams.height = height
                    navbarView.layoutParams = layoutParams
                } else {
                    val layoutParams = navbarView.layoutParams
                    layoutParams.height = 0
                    navbarView.layoutParams = layoutParams
                }
            }
        }
        b.web.onResume()
        b.web.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        b.web.onPause()
        b.web.pauseTimers()
    }

    override fun onDestroy() {
        super.onDestroy()
        b.web.destroy()
    }

    private fun initWebview() {
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        val settings: WebSettings = b.web.settings
        if (!TextUtils.isEmpty(ua)) {
            //设置app的UA
            settings.setUserAgentString(ua)
        }
        //支持大开js脚本
        settings.javaScriptEnabled = true
        settings.databaseEnabled = true
        settings.domStorageEnabled = true
        //允许缩放
        settings.setSupportZoom(true)
        //原网页基础上缩放
        settings.builtInZoomControls = true
        //支持js打开新窗口
        settings.javaScriptCanOpenWindowsAutomatically = true
        //任意比例缩放
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        //解除数据阻止
        settings.blockNetworkImage = false
        //设置缓存
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        //隐藏放大按钮
        settings.displayZoomControls = false
        //settings.setTextZoom(200);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        settings.setSupportMultipleWindows(false)
    }

    private fun setWebviewClient(){
        b.web.webViewClient = object :WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return if (view?.url!!.startsWith("http")){
                    super.shouldOverrideUrlLoading(view, request)
                }else {
                    //拦截
                    true
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                /*webView.getSettings().setBlockNetworkImage(false);
                if (!webView.getSettings().getLoadsImagesAutomatically()) {

                    webView.getSettings().setLoadsImagesAutomatically(true);
                }*/
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError
            ) {
                super.onReceivedSslError(view, handler, error)
                //Log.e("qwer", "报错:$error")
            }
    }
    }

    private fun setWebChromeClient() {
        b.web.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, newTitle: String) {
                if (!TextUtils.isEmpty(newTitle)) {
                    if (!TextUtils.isEmpty(title)) {
                        if (isFirstCheck) {
                            //第一次设置标题
                            if (!TextUtils.isEmpty(title)) {
                                b.title.text = title
                            } else {
                                b.title.text = newTitle
                            }
                            isFirstCheck = false
                        } else {
                            b.title.text = newTitle
                        }
                    } else {
                        b.title.text = newTitle
                    }
                } else {
                    if (!TextUtils.isEmpty(title)) {
                        b.title.text = title
                    }
                }
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    b.progressBar.progress = newProgress
                    b.progressBar.visibility = View.GONE //加载完网页进度条消失
                } else {
                    b.progressBar.visibility = View.VISIBLE //开始加载网页时显示进度条
                    b.progressBar.progress = newProgress //设置进度值
                }
            }
        }
    }

    private fun checkShare() {
        if (TextUtils.isEmpty(title)) {
            b.share.visibility = View.GONE
            return
        }
        if (!ShareUtil.isActivited()) {
            b.share.visibility = View.GONE
        }
    }

    private fun showShare() {
        val thisUrl: String = if (!TextUtils.isEmpty(b.web.url)) {
            b.web.url!!
        } else {
            url
        }
        ShareUtil.initWebPageShare(shareDialog, b.title.text.toString(), thisUrl, null)
        ShareUtil.showDialog(shareDialog)
    }


    private fun showToast(content: String) {
        try {
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}