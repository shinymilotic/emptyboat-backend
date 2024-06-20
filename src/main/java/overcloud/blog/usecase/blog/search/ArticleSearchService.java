package overcloud.blog.usecase.blog.search;

import overcloud.blog.usecase.blog.get_article_list.GetArticlesResponse;
import overcloud.blog.usecase.common.response.RestResponse;

public interface ArticleSearchService {
    RestResponse<GetArticlesResponse> searchArticles(String searchParam, int size, String lastArticleId);
}
