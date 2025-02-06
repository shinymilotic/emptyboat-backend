package overcloud.blog.usecase.user.common;

public class UserResMsg {
    public static final String USER_NOT_FOUND = "user.get-current-user.not-found";
    public static final String USER_USERNAME_EXIST = "user.username.exists";
    public static final String USER_EMAIL_EXIST = "user.email.exists";
    public static final String USER_NON_ENABLED = "user.enabled.non";
    public static final String USER_LOGIN_FAILED = "user.login.failed";
    public static final String USER_LOGIN_EMAIL_SIZE = "user.login.email.size";
    public static final String USER_LOGIN_PASSWORD_NOTNULL = "user.login.password.not-null";
    public static final String USER_REGISTER_USERNAME_NOTBLANK = "user.register.username.not-blank";
    public static final String USER_REGISTER_USERNAME_SIZE = "user.register.username.size";
    public static final String USER_REGISTER_USERNAME_LETTERANDNUMBER = "user.register.username.letter-and-number";
    public static final String USER_REGISTER_EMAIL_NOTBLANK = "user.register.email.not-blank";
    public static final String USER_REGISTER_EMAIL_VALID = "user.register.email.valid";
    public static final String USER_REGISTER_PASSWORD_NOTBLANK = "user.register.password.not-blank";
    public static final String USER_REGISTER_PASSWORD_SIZE = "user.register.password.size";
    public static final String AUTHORIZE_FAILED = "authorize.failed";
    public static final String TOKEN_TIMEOUT = "authorize.token.timeout";
    public static final String REFRESH_TOKEN_FAILED = "refresh-token.failed";
}
