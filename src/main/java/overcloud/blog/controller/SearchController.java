package overcloud.blog.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import overcloud.blog.infrastructure.ApiConst;
import overcloud.blog.usecase.article.get_article_list.GetArticlesResponse;
import overcloud.blog.usecase.article.search.ArticleSearchService;
//import overcloud.blog.usecase.article.search.ArticleSearchServiceES;
import overcloud.blog.usecase.article.search.ArticleSearchServicePG;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class SearchController {

    private final ArticleSearchService articleSearchService;

    public SearchController(ArticleSearchServicePG articleSearchService) {
        this.articleSearchService = articleSearchService;
    }

    @GetMapping(ApiConst.ARTICLES_SEARCH)
    public GetArticlesResponse searchArticles(@RequestParam(value = "q", defaultValue = "") String searchParam,
                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                              @RequestParam(value = "lastArticleId", defaultValue = "") String lastArticleId) {
        return articleSearchService.searchArticles(searchParam, size, lastArticleId);
    }
}
