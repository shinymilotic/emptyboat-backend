package overcloud.blog.usecase.user.common;

public class UserResMsg {
    public static final String USER_NOT_FOUND = "user.get-current-user.not-found";
    public static final String USER_USERNAME_EXIST = "user.username.exists";
    public static final String USER_EMAIL_EXIST = "user.email.exists";
    public static final String USER_EMAIL_NO_EXIST = "user.email.no-exists";
    public static final String USER_NON_ENABLED = "user.enabled.non";
    public static final String USER_LOGIN_SUCCESS = "user.login.success";
    public static final String USER_LOGIN_FAILED = "user.login.failed";
    public static final String USER_LOGIN_EMAIL_SIZE = "user.login.email.size";
    public static final String USER_LOGIN_EMAIL_NOTNULL = "user.login.email.not-null";
    public static final String USER_LOGIN_PASSWORD_NOTNULL = "user.login.password.not-null";
    public static final String USER_REGISTER_SUCCESS = "user.register.success";
    public static final String USER_REGISTER_FAILED = "user.register.failed";
    public static final String USER_REGISTER_USERNAME_NOTBLANK = "user.register.username.not-blank";
    public static final String USER_REGISTER_USERNAME_SIZE = "user.register.username.size";
    public static final String USER_REGISTER_USERNAME_LETTERANDNUMBER = "user.register.username.letter-and-number";
    public static final String USER_REGISTER_EMAIL_NOTBLANK = "user.register.email.not-blank";
    public static final String USER_REGISTER_EMAIL_VALID = "user.register.email.valid";
    public static final String USER_REGISTER_PASSWORD_NOTBLANK = "user.register.password.not-blank";
    public static final String USER_REGISTER_PASSWORD_SIZE = "user.register.password.size";
    public static final String USER_UPDATE_FAILED = "user.update.failed";
    public static final String USER_UPDATE_SUCCESS = "user.update.success";
    public static final String USER_FOLLOW_SUCCESS = "user.follow.success";
    public static final String USER_UNFOLLOW_SUCCESS = "user.unfollow.success";
    public static final String AUTHORIZE_SUCCESS = "authorize.success";
    public static final String AUTHORIZE_FAILED = "authorize.failed";
    public static final String TOKEN_TIMEOUT = "authorize.token.timeout";
    public static final String REFRESH_TOKEN_FAILED = "refresh-token.failed";
    public static final String REFRESH_TOKEN_SUCCESS = "refresh-token.success";
    public static final String USER_LOGOUT_SUCCESS = "user.logout.success";
    // get curren user
    public static final String USER_GET_CURRENT_USER = "user.get-current-user";
    // get followers
    public static final String USER_GET_FOLLOWERS = "user.get-followers";
    // get profile
    public static final String USER_GET_PROFILE = "user.get-profile";
    // get user list
    public static final String USER_GET_USER_LIST = "user.get-user-list";
    // get role user
    public static final String GET_ROLE_USER = "user.get-roles-user";
}
