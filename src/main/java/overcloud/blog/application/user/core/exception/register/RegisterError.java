package overcloud.blog.application.user.core.exception.register;


public enum RegisterError {
    USERNAME_EXIST("Username already used"),

    EMAIL_EXIST("Email already used");

    private String message;

    RegisterError(String message) {
        this.message = message;
    }

    public String getValue() {
        return this.message;
    }
}
