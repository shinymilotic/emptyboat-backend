package overcloud.blog.application.user.core;

import overcloud.blog.infrastructure.validation.Error;


public enum UserError implements Error {
    USER_NOT_FOUND("user.get-current-user.not-found","You have to sign in first!"),
    USER_USERNAME_EXIST("user.username.exists","Username already used!"),
    USER_EMAIL_EXIST("user.email.exists", "Email already used!");

    ;

    private String id;

    private String errorMessage;

    UserError(String id, String errorMessage) {
        this.id = id;
        this.errorMessage = errorMessage;
    }

    @Override
    public void setMessageId(String id) {
        this.id = id;
    }

    @Override
    public String getMessageId() {
        return id;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
