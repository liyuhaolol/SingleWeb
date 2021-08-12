package spa.lyh.cn.ft_webview.webview.util;

import android.content.Context;

import java.lang.reflect.Method;

public class LanguageUtils {
    private final static String className = "spa.lyh.cn.languagepack.LanguagesPack";

    /**
     * 检查分享模块是否启动
     * @return
     */
    public static boolean isActivited(){
        try{
            Class.forName(className);
            return true;
        }catch (Exception ignored){
            return false;
        }
    }

    public static Context getLanguageContext(Context context){
        Object obj;
        try{
            Class clazz = Class.forName(className);
            Method method = clazz.getMethod("attach",Context.class);
            obj = method.invoke(clazz,context);
            return (Context) obj;
        }catch (Exception ignored){
            return null;
        }
    }
}
