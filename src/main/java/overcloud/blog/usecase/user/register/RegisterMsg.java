package overcloud.blog.usecase.user.register;

public class RegisterMsg {
    public static final String USERNAME_NOTBLANK = "user.register.username.not-blank";
    public static final String USERNAME_SIZE = "user.register.username.size";
    public static final String USERNAME_LETTERANDNUMBER = "user.register.username.letter-and-number";
    public static final String EMAIL_NOTBLANK = "user.register.email.not-blank";
    public static final String EMAIL_VALID = "user.register.email.valid";
    public static final String PASSWORD_NOTBLANK = "user.register.password.not-blank";
    public static final String PASSWORD_SIZE = "user.register.password.size";
    public static final String USERNAME_EXISTS = "user.register.username-exists";
    public static final String EMAIL_EXISTS = "user.register.email-exists";
    public static final String REGISTER_FAILED = "user.register.failed";
}
