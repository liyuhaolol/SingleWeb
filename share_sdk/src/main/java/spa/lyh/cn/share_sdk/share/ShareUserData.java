package spa.lyh.cn.share_sdk.share;


/**
 * 第三方登录得到的用户数据
 */
public class ShareUserData {
    private String userIcon;
    private String userId;
    private String userName;
    private String thirdPartTitle;
    private String type;

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThirdPartTitle() {
        return thirdPartTitle;
    }

    public void setThirdPartTitle(String thirdPartTitle) {
        this.thirdPartTitle = thirdPartTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
