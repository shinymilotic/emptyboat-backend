package overcloud.blog.application.article.comment.core;

import overcloud.blog.infrastructure.validation.Error;

public enum CommentError implements Error {
    COMMENT_ARTICLE_NOT_EXIST("comment.article.not-exist", "The article doesn't exist");

    private String messageId;

    private String errorMessage;

    CommentError(String messageId, String errorMessage) {
        this.messageId = messageId;
        this.errorMessage = errorMessage;
    }

    @Override
    public void setMessageId(String messageId) {
        this.messageId = messageId;
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
