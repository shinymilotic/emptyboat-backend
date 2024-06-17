package overcloud.blog.usecase.blog.common;

import overcloud.blog.core.validation.ResMsg;

public enum TagError implements ResMsg {

    TAG_EXISTS("tag.exists", "Tag existed!"),
    TAG_NO_EXISTS("tag.no-exists", "These tags doesn't exist: %s!");

    private String messageId;

    private String errorMessage;

    TagError(String messageId, String errorMessage) {
        this.messageId = messageId;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public void setMessageId(String id) {
        this.messageId = id;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public void setMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
