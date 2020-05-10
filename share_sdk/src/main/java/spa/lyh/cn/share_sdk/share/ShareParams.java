package spa.lyh.cn.share_sdk.share;

import android.graphics.Bitmap;

public class ShareParams {
    private int shareType;
    private String title;
    private String text;
    private String imageUrl;
    private Bitmap imageData;
    private String url;
    private String titleUrl;
    private String hashtag;

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImageData() {
        return imageData;
    }

    public void setImageData(Bitmap imageData) {
        this.imageData = imageData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}
