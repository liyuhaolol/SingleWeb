package spa.lyh.cn.ft_webview.webview.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import spa.lyh.cn.ft_webview.webview.util.LanguageUtils

open class BaseActivity:AppCompatActivity(){

    override fun attachBaseContext(newBase: Context?) {
        if(LanguageUtils.isActivited()){
            super.attachBaseContext(LanguageUtils.attach(newBase))
        }else{
            super.attachBaseContext(newBase)
        }
    }
}