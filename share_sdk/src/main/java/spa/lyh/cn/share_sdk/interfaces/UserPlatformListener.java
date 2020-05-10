package spa.lyh.cn.share_sdk.interfaces;

import spa.lyh.cn.share_sdk.share.ShareUserData;

public interface UserPlatformListener {
    void onComplete(ShareUserData data);

    void onError(String reason);

    void onCancel();
}
