package overcloud.blog.usecase.blog.common;

import overcloud.blog.core.validation.ResMsg;

public enum ArticleError implements ResMsg {
    ARTICLE_UPDATE_NO_AUTHORIZATION("article.update.no-authorization", "You don't have authorization to update this article!"),
    ARTICLE_NO_EXISTS("article.no-exists", "Article doesn't exist"),
    ARTICLE_TITLE_EXISTS("article.title-exist", "Tiêu đề đã tồn tại!");

    private String id;

    private String message;

    ArticleError(String id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String getMessageId() {
        return id;
    }

    @Override
    public void setMessageId(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
