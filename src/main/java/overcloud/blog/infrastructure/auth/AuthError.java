package overcloud.blog.infrastructure.auth;

import overcloud.blog.infrastructure.validation.Error;

public enum AuthError implements Error {
    AUTHORIZE_FAILED("authorize.failed", "Authorize failed!"),

    TOKEN_TIMEOUT("authorize.token.timeout", "Token timeout!");


    private String messageId;

    private String errorMessage;

    AuthError(String messageId, String errorMessage) {
        this.messageId = messageId;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessageId() {
        return this.messageId;
    }

    @Override
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
