package overcloud.blog.usecase.blog.common;

import overcloud.blog.usecase.common.validation.ResMsg;

public enum TagResMsg implements ResMsg {

    // TAG_EXISTS("tag.exists", "Tag existed!"),
    // TAG_NO_EXISTS("tag.no-exists", "These tags doesn't exist: %s!");
    TAG_EXISTS("tag.exists"),
    TAG_NO_EXISTS("tag.no-exists");

    private String messageId;

    TagResMsg(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public void setMessageId(String id) {
        this.messageId = id;
    }
}
