package overcloud.blog.application.article.core;

import overcloud.blog.infrastructure.validation.Error;

public enum ArticleError implements Error {
    TAG_EMPTY, TAG_NO_EXISTS, TITLE_NOT_BLANK, TITLE_LENGTH, DESCRIPTION_NOT_BLANK, DESCRIPTION_LENGTH, ARTICLE_NO_EXISTS;


    private String id;

    private String message;
    @Override
    public void setMessageId(String id) {
        this.id = id;
    }

    @Override
    public String getMessageId() {
        return id;
    }

    @Override
    public void setErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
