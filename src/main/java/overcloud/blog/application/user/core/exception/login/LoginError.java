package overcloud.blog.application.user.core.exception.login;


public enum LoginError {

    LOGIN_INFO_INVALID("Login information is invalid");

    private String message;

    LoginError(String message) {
        this.message  = message;
    }

    public String getValue() {
        return message;
    }
}
