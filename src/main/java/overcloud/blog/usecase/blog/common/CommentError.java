package overcloud.blog.usecase.blog.common;

import overcloud.blog.core.validation.ResMsg;

public enum CommentError implements ResMsg {
    COMMENT_ARTICLE_NOT_EXIST("comment.article.not-exist", "The article doesn't exist");

    private String messageId;

    private String errorMessage;

    CommentError(String messageId, String errorMessage) {
        this.messageId = messageId;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public void setMessageId(String messageId) {
        this.messageId = messageId;
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
