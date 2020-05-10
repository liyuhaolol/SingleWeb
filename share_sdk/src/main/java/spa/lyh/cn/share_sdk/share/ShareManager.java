package spa.lyh.cn.share_sdk.share;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.mob.MobSDK;

import java.util.HashMap;


import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qq.QQClientNotExistException;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import spa.lyh.cn.share_sdk.interfaces.SharePlatformListener;
import spa.lyh.cn.share_sdk.interfaces.UserPlatformListener;

/**
 * Created by liyuhao on 2017/12/27.
 * 封装ShareSDK
 */

public class ShareManager {
    private static ShareManager mShareManager = null;

    private Platform mCurrentPlatform;

    public static ShareManager getInstance(){
        if (mShareManager == null){
            synchronized (ShareManager.class){
                if (mShareManager == null){
                    mShareManager = new ShareManager();
                }
            }
        }
        return mShareManager;
    }

    public void shareData(ShareData data, final Handler handler, final SharePlatformListener listener){
        switch (data.type){
            case Wechat:
                mCurrentPlatform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case WechatMoment:
                mCurrentPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
                break;
            case QQ:
                mCurrentPlatform = ShareSDK.getPlatform(QQ.NAME);
                break;
            case Weibo:
                mCurrentPlatform = ShareSDK.getPlatform(SinaWeibo.NAME);
                break;
            case Twitter:
                mCurrentPlatform = ShareSDK.getPlatform(Twitter.NAME);
                break;
            case Facebook:
                mCurrentPlatform = ShareSDK.getPlatform(Facebook.NAME);
                break;
        }
        mCurrentPlatform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if(!platform.getName().equals("Facebook")){
                    //由于facebook就有BUG，没办法，facebook不能提示成功
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onComplete();
                        }
                    });
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                if (throwable instanceof WechatClientNotExistException){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError("请先安装微信客户端");
                        }
                    });
                }else if (throwable instanceof QQClientNotExistException){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError("请先安装QQ客户端");
                        }
                    });
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError("分享失败");
                        }
                    });
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onCancel();
                    }
                });
            }
        });
        //拼装数据
        Platform.ShareParams params = setData(data.params);
        mCurrentPlatform.share(params);
    }

    private Platform.ShareParams setData(ShareParams params){
        Platform.ShareParams param = new Platform.ShareParams();
        if (params.getShareType() > 0){
            param.setShareType(params.getShareType());
        }

        if (!TextUtils.isEmpty(params.getHashtag())){
            param.setHashtag(params.getHashtag());
        }

        if (!TextUtils.isEmpty(params.getText())){
            param.setText(params.getText());
        }

        if (!TextUtils.isEmpty(params.getTitle())){
            param.setTitle(params.getTitle());
        }

        if (!TextUtils.isEmpty(params.getTitleUrl())){
            param.setTitleUrl(params.getTitleUrl());
        }

        if (!TextUtils.isEmpty(params.getImageUrl())){
            param.setImageUrl(params.getImageUrl());
        }

        if (!TextUtils.isEmpty(params.getUrl())){
            param.setUrl(params.getUrl());
        }

        if (params.getImageData() != null){
            param.setImageData(params.getImageData());
        }
        return param;
    }

    public void getUserInfo(PlatformType type, final Handler handler, final UserPlatformListener listener){
        switch (type){
            case Wechat:
                mCurrentPlatform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case WechatMoment:
                mCurrentPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
                break;
            case QQ:
                mCurrentPlatform = ShareSDK.getPlatform(QQ.NAME);
                break;
            case Weibo:
                mCurrentPlatform = ShareSDK.getPlatform(SinaWeibo.NAME);
                break;
            case Twitter:
                mCurrentPlatform = ShareSDK.getPlatform(Twitter.NAME);
                break;
            case Facebook:
                mCurrentPlatform = ShareSDK.getPlatform(Facebook.NAME);
                break;
        }
        mCurrentPlatform.SSOSetting(false);

        if (mCurrentPlatform.isAuthValid()){
            mCurrentPlatform.removeAccount(true);
        }
        mCurrentPlatform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(final Platform platform, int action, HashMap<String, Object> hashMap) {
                if (action == Platform.ACTION_USER_INFOR) {
                    final ShareUserData userData = new ShareUserData();

                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    //通过DB获取各种数据
                    if ("SinaWeibo".equals(platform.getName())) {
                        userData.setUserIcon(platDB.getUserIcon());
                        userData.setUserId(platDB.getUserId());
                        userData.setUserName(platDB.getUserName());
                        userData.setThirdPartTitle("微博登录");
                        userData.setType("1");
                    }

                    if ("QQ".equals(platform.getName())) {
                        userData.setUserIcon(platDB.getUserIcon());
                        userData.setUserId(platDB.getUserId());
                        userData.setUserName(platDB.getUserName());
                        userData.setThirdPartTitle("QQ登录");
                        userData.setType("2");
                    }

                    if ("Wechat".equals(platform.getName())) {
                        userData.setUserIcon(platDB.getUserIcon());
                        userData.setUserId(platDB.getUserId());
                        userData.setUserName(platDB.getUserName());
                        userData.setThirdPartTitle("微信登录");
                        userData.setType("3");
                    }

                    if ("Facebook".equals(platform.getName())){
                        userData.setUserIcon(platDB.getUserIcon());
                        userData.setUserId(platDB.getUserId());
                        userData.setUserName(platDB.getUserName());
                        userData.setThirdPartTitle("Facebook登录");
                        userData.setType("4");
                    }

                    if ("Twitter".equals(platform.getName())) {//Twitter用户的昵称是在map里存的
                        userData.setUserIcon(platDB.getUserIcon());
                        userData.setUserId(platDB.getUserId());
                        userData.setUserName(platDB.getUserName());
                        userData.setThirdPartTitle("Twitter登录");
                        userData.setType("5");
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //showToast("第三方信息得到");
                            listener.onComplete(userData);
                        }
                    });

                }
            }

            @Override
            public void onError(final Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                if (throwable instanceof WechatClientNotExistException){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError("请先安装微信客户端");
                        }
                    });
                }else if (throwable instanceof QQClientNotExistException){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError("请先安装QQ客户端");
                        }
                    });
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError("授权登录失败");
                        }
                    });
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onCancel();
                    }
                });
            }
        });

        mCurrentPlatform.showUser(null);
    }

    public enum PlatformType{
        Wechat,WechatMoment,QQ,Weibo,Twitter,Facebook
    }
}
