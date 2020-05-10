package spa.lyh.cn.share_sdk.pop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.List;

import spa.lyh.cn.share_sdk.R;
import spa.lyh.cn.share_sdk.interfaces.SharePlatformListener;
import spa.lyh.cn.share_sdk.share.Platform;
import spa.lyh.cn.share_sdk.share.ShareData;
import spa.lyh.cn.share_sdk.share.ShareManager;
import spa.lyh.cn.lib_utils.AppUtils;
import spa.lyh.cn.share_sdk.share.ShareParams;

public class ShareImageDialog extends Dialog{

    private ViewGroup contentView;
    private Context context;
    private LinearLayout cancel,share_wx,share_pyq,share_qq,share_wb,share_tw,share_fb;
    private String title;
    private String picurl = "";
    private Bitmap picBit;
    private SharePlatformListener listener;
    private Handler handler;




    public ShareImageDialog(@NonNull Context context, String title, List<String> picurl) {
        this(context, R.style.CommonDialog,title,picurl);
    }

    public ShareImageDialog(@NonNull Context context, int themeResId, String title, List<String> picurl) {
        super(context, themeResId);
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
        String name = "【"+context.getString(R.string.app_name)+"】";
        //if (!title.contains(name)){
        this.title = title + name;
        //}else {
        //this.title = title;
        //}
        picBit = AppUtils.getImageFromAssetsFile(context,"ic_launcher.png");
        if (picurl.size() > 0){
            String surl = picurl.get(0);
            surl = surl.substring(surl.lastIndexOf(".")+1,surl.length());
            if (!surl.equals("gif")){//不分享gif
                this.picurl = picurl.get(0);
                //Log.e(" liyuhao",picurl.get(0));
            }
        }
        initDialogStyle();
    }

    private void initDialogStyle(){
        setContentView(createDialogView(R.layout.dialog_share));
    }


    private ViewGroup createDialogView(int layoutId){
        contentView = (ViewGroup) LayoutInflater.from(getContext()).inflate(layoutId, null);
        cancel = contentView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        share_wx = contentView.findViewById(R.id.share_wx);
        share_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareData data;
                ShareParams params;
                params = new ShareParams();
                params.setShareType(Platform.SHARE_IMAGE);
                params.setTitle(title);
                params.setText(context.getString(R.string.come_from)+context.getString(R.string.app_name));
                if (!TextUtils.isEmpty(picurl)){
                    params.setImageUrl(picurl);
                }else {
                    params.setImageData(picBit);
                }
                data = new ShareData(ShareManager.PlatformType.Wechat,params);
                if (listener != null){
                    ShareManager.getInstance().shareData(data,handler,listener);
                }
                dismiss();
            }
        });
        share_pyq = contentView.findViewById(R.id.share_pyq);
        share_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareData data;
                ShareParams params;
                params = new ShareParams();
                /*params.setText(title);
                if (!TextUtils.isEmpty(picurl)){
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImageUrl(picurl);
                }else {
                    params.setShareType(Platform.SHARE_TEXT);
                }*/
                params.setShareType(Platform.SHARE_IMAGE);
                params.setTitle(title);
                if (!TextUtils.isEmpty(picurl)){
                    params.setImageUrl(picurl);
                }else {
                    params.setImageData(picBit);
                }
                data = new ShareData(ShareManager.PlatformType.WechatMoment,params);
                if (listener != null){
                    ShareManager.getInstance().shareData(data,handler,listener);
                }
                dismiss();
            }
        });
        share_qq = contentView.findViewById(R.id.share_qq);
        share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareData data;
                ShareParams params;
                params = new ShareParams();
                if (!TextUtils.isEmpty(picurl)){
                    params.setImageUrl(picurl);
                }else {
                    params.setImageData(picBit);
                }
                data = new ShareData(ShareManager.PlatformType.QQ,params);
                if (listener != null){
                    ShareManager.getInstance().shareData(data,handler,listener);
                }
                dismiss();
            }
        });
        share_wb = contentView.findViewById(R.id.share_wb);
        share_wb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareData data;
                ShareParams params;
                params = new ShareParams();
                params.setText(title);
                if (!TextUtils.isEmpty(picurl)){
                    params.setImageUrl(picurl);
                }else {
                    params.setImageData(picBit);
                }
                data = new ShareData(ShareManager.PlatformType.Weibo,params);
                if (listener != null){
                    ShareManager.getInstance().shareData(data,handler,listener);
                }
                dismiss();
            }
        });
        share_tw = contentView.findViewById(R.id.share_tw);
        share_tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareData data;
                ShareParams params;
                params = new ShareParams();
                params.setText(title);
                if (!TextUtils.isEmpty(picurl)){
                    params.setImageUrl(picurl);
                }else {
                    params.setImageUrl("http://app.jrlamei.com/images/logo.png");
                }
                data = new ShareData(ShareManager.PlatformType.Twitter,params);
                if (listener != null){
                    ShareManager.getInstance().shareData(data,handler,listener);
                }
                dismiss();
            }
        });
        share_fb = contentView.findViewById(R.id.share_fb);
        share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareData data;
                ShareParams params;
                params = new ShareParams();
                params.setHashtag(title);
                params.setImageUrl(picurl);
                data = new ShareData(ShareManager.PlatformType.Facebook,params);
                if (listener != null){
                    ShareManager.getInstance().shareData(data,handler,listener);
                }
                dismiss();
            }
        });
        return contentView;
    }

    @Override
    public void show() {
        super.show();
        //设置靠底部
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        layoutParams.width = dm.widthPixels;
        //layoutParams.height = dm.heightPixels;
        contentView.setLayoutParams(layoutParams);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
    }

    public void setShareResultListener(SharePlatformListener listener){
        this.listener = listener;
    }
}
