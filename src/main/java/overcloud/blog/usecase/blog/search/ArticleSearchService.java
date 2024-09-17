package overcloud.blog.usecase.blog.search;

import overcloud.blog.common.response.RestResponse;
import overcloud.blog.usecase.blog.get_article_list.GetArticlesResponse;

public interface ArticleSearchService {
    RestResponse<GetArticlesResponse> searchArticles(String searchParam, int size, String lastArticleId);
}
