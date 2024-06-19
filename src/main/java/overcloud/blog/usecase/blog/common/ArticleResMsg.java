package overcloud.blog.usecase.blog.common;

import overcloud.blog.usecase.common.validation.ResMsg;

public enum ArticleResMsg implements ResMsg {
    // ARTICLE_UPDATE_NO_AUTHORIZATION("article.update.no-authorization", "You don't have authorization to update this article!"),
    // ARTICLE_NO_EXISTS("article.no-exists", "Article doesn't exist"),
    // ARTICLE_TITLE_EXISTS("article.title-exist", "Tiêu đề đã tồn tại!");
    ARTICLE_UPDATE_NO_AUTHORIZATION("article.update.no-authorization"),
    ARTICLE_NO_EXISTS("article.no-exists"),
    ARTICLE_TITLE_EXISTS("article.title-exist");

    private String messageId;

    ArticleResMsg(String messageId) {
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
