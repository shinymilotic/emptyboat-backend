package overcloud.blog.application.article.core;

import overcloud.blog.infrastructure.validation.Error;

public enum ArticleError implements Error {
    ARTICLE_UPDATE_NO_AUTHORIZATION("article.update.no-authorization", "You don't have authorization to update this article!"),
    ARTICLE_NO_EXISTS("article.no-exists", "Article doesn't exist");

    private String id;

    private String message;

    ArticleError(String id, String message) {
        this.id = id;
        this.message = message;
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
    public void setErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
