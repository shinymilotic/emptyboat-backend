package overcloud.blog.application.tag.core;

import overcloud.blog.infrastructure.validation.Error;

public enum TagError implements Error {
    TAG_EXISTS("tag.exists","Tag existed!");

    private String messageId;

    private String errorMessage;

    TagError(String messageId, String errorMessage) {
        this.messageId = messageId;
        this.errorMessage = errorMessage;
    }

    @Override
    public void setMessageId(String id) {
        this.messageId = id;
    }

    @Override
    public String getMessageId() {
        return messageId;
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
