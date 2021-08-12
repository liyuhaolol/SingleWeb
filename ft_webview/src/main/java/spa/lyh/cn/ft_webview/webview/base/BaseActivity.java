package spa.lyh.cn.ft_webview.webview.base;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import spa.lyh.cn.ft_webview.webview.util.LanguageUtils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        // 国际化适配（绑定语种）
        if (LanguageUtils.isActivited()){
            super.attachBaseContext(LanguageUtils.getLanguageContext(newBase));
        }else {
            super.attachBaseContext(newBase);
        }
    }
}
