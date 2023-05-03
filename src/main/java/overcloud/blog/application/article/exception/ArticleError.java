package overcloud.blog.application.article.exception;

public enum ArticleError {

    ARTICLE_NOT_FOUND("The article doesn't exist"), TITLE_EXIST("Title exist");

    private String message;

    ArticleError(String message) {
        this.message  = message;
    }

    public String getValue() {
        return message;
    }
}