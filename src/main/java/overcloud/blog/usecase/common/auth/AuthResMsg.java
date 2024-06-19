package overcloud.blog.usecase.common.auth;

import overcloud.blog.usecase.common.validation.ResMsg;

public enum AuthResMsg implements ResMsg{
    AUTHORIZE_FAILED("authorize.failed"),
    TOKEN_TIMEOUT("authorize.token.timeout");

    private String messageId;

    AuthResMsg(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
