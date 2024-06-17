package overcloud.blog.usecase.common.auth;

import overcloud.blog.core.validation.ResMsg;

public enum AuthError implements ResMsg {
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
    public String getMessage() {
        return this.errorMessage;
    }

    @Override
    public void setMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
