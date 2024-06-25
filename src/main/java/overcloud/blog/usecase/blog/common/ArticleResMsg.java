package overcloud.blog.usecase.blog.common;


public class ArticleResMsg {
    public static final String ARTICLE_CREATE_SUCCESS = "article.create.success";
    public static final String ARTICLE_CREATE_FAILED = "article.create.failed";
    public static final String ARTICLE_UPDATE_SUCCESS = "article.update.success";
    public static final String ARTICLE_UPDATE_NO_AUTHORIZATION = "article.update.no-authorization";
    public static final String ARTICLE_NO_EXISTS = "article.no-exists";
    public static final String ARTICLE_TITLE_EXISTS = "article.title-exist";
    public static final String ARTICLE_TITLE_NOTBLANK = "Title must not be blank.";
    public static final String ARTICLE_TITLE_SIZE = "article.title.size";
    public static final String ARTICLE_DESCRIPTION_NOTBLANK = "article.description.not-blank";
    public static final String ARTICLE_DESCRIPTION_SIZE = "article.description.size";
    public static final String ARTICLE_BODY_NOTBLANK = "article.body.not-blank";
    public static final String ARTICLE_TAGS_NOTEMPTY = "article.tags.not-empty";
}
