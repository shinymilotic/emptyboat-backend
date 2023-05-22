package overcloud.blog.application.user.core;

import overcloud.blog.infrastructure.validation.Error;


public enum UserError implements Error {
    USER_NOT_FOUND("user.get-current-user.not-found","You have to sign in first!");
    ;

    private String id;

    private String errorMessage;

    UserError(String id, String errorMessage) {
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
