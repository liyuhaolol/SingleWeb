package spa.lyh.cn.share_sdk.share;

/**
 * Created by liyuhao on 2017/12/27.
 * 封装平台和数据
 */

public class ShareData {
    public ShareParams params;

    public ShareManager.PlatformType type;//平台

    public ShareData(ShareManager.PlatformType type,ShareParams params){
        this.params = params;
        this.type = type;
    }
}
