package spa.lyh.cn.share_sdk.interfaces;


public interface SharePlatformListener {

    void onComplete();

    void onError(String reason);

    void onCancel();
}
