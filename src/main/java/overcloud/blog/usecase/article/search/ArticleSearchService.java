package overcloud.blog.usecase.article.search;

import overcloud.blog.usecase.article.get_article_list.GetArticlesResponse;

public interface ArticleSearchService {
    GetArticlesResponse searchArticles(String searchParam, int size, String lastArticleId);
}
