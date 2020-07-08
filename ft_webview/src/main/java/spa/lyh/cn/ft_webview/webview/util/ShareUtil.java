package spa.lyh.cn.ft_webview.webview.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class ShareUtil {

    private static String managerName = "spa.lyh.cn.share_sdk.ShareManager";
    private static String dialogName = "spa.lyh.cn.share_sdk.pop.ShareDialog";
    private final static String listener = "spa.lyh.cn.share_sdk.interfaces.SharePlatformListener";
    private final static String resultListener = "spa.lyh.cn.share_sdk.interfaces.PolicyGrantResult";

    /**
     * 检查分享模块是否启动
     * @return
     */
    public static boolean isActivited(){
        try{
            Class.forName(managerName);
            return true;
        }catch (Exception ignored){
            return false;
        }
    }

    public static Object initShareDialog(Activity activity){
        Object obj;
        try{
            Class clazz = Class.forName(dialogName);
            Constructor constructor = clazz.getConstructor(
                    Activity.class);
            obj = constructor.newInstance(activity);
            return obj;
        }catch (Exception ignored){
            return null;
        }
    }

    public static void initWebPageShare(Object dialog,String title,String url,List<String> picUrl){
        try{
            Method method = dialog.getClass().getMethod("initWebPageShare",String.class,String.class,List.class);
            method.invoke(dialog,title,url,picUrl);
        }catch (Exception ignored){
        }
    }

    public static void initImageShare(Object dialog,String title,List<String> picUrl){
        try{
            Method method = dialog.getClass().getMethod("initImageShare",String.class,List.class);
            method.invoke(dialog,title,picUrl);
        }catch (Exception ignored){
        }
    }

    public static void showDialog(Object dialog){
        try{
            Method method = dialog.getClass().getMethod("show");
            method.invoke(dialog);
        }catch (Exception ignored){
        }
    }

    public static void registerResultListener(final Activity activity, Object dialog){
        try{
            Class clazz = Class.forName(listener);
            ClassLoader loader = clazz.getClassLoader();
            Object listener = Proxy.newProxyInstance(loader, new Class[]{clazz}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getName().equals("onComplete")){
                    }else if (method.getName().equals("onError")){
                        Toast.makeText(activity,args[0].toString(),Toast.LENGTH_SHORT).show();
                    }else if (method.getName().equals("onCancel")){
                    }
                    return null;
                }
            });
            Method method = dialog.getClass().getMethod("setShareResultListener",clazz);
            method.invoke(dialog,listener);
        }catch (Exception ignored){
        }
    }

    public static void submitPolicy(){
        try{
            Class clazz = Class.forName(managerName);
            Class clazz2 = Class.forName(resultListener);
            ClassLoader loader = clazz2.getClassLoader();
            Object listener = Proxy.newProxyInstance(loader, new Class[]{clazz2}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getName().equals("onComplete")){
                        Log.d("ShareSDK", "隐私协议授权结果提交：成功");
                    }else if (method.getName().equals("onFailure")){
                        Log.d("ShareSDK", "隐私协议授权结果提交：失败");
                    }
                    return null;
                }
            });
            Method method = clazz.getMethod("getInstance");
            Object obj = method.invoke(null);

            Method method2 = clazz.getMethod("submitPolicyGrantResult",clazz2);
            method2.invoke(obj,listener);
        }catch (Exception ignored){
            ignored.printStackTrace();
        }
    }
}
