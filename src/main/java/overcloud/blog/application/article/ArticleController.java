package overcloud.blog.application.article;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.access.annotation.Secured;
import overcloud.blog.application.article.create_article.CreateArticleService;
import overcloud.blog.application.article.delete_article.DeleteArticleService;
import overcloud.blog.application.article.create_article.ArticleRequest;
import overcloud.blog.application.article.create_article.ArticleResponse;
import overcloud.blog.application.article.delete_article.DeleteArticleResponse;
import overcloud.blog.application.article.get_article.GetArticleResponse;
import overcloud.blog.application.article.get_article_list.GetArticlesResponse;
import overcloud.blog.application.article.update_article.UpdateArticleRequest;
import overcloud.blog.application.article.update_article.UpdateArticleResponse;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.article.get_article.GetArticleService;
import overcloud.blog.application.article.get_article_list.GetArticleListService;
import overcloud.blog.application.article.update_article.UpdateArticleService;
import overcloud.blog.infrastructure.ApiConst;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ArticleController {
    private final CreateArticleService createArticleService;

    private final UpdateArticleService updateArticleService;

    private final GetArticleService getArticleService;

    private final GetArticleListService getArticleListService;

    private final DeleteArticleService deleteArticleService;


    public ArticleController(CreateArticleService createArticleService,
                             UpdateArticleService updateArticleService,
                             GetArticleService getArticleService,
                             GetArticleListService getArticleListService,
                             DeleteArticleService deleteArticleService) {
        this.createArticleService = createArticleService;
        this.updateArticleService = updateArticleService;
        this.getArticleService = getArticleService;
        this.getArticleListService = getArticleListService;
        this.deleteArticleService = deleteArticleService;
    }

    @Secured({"ADMIN", "USER"})
    @PostMapping(ApiConst.ARTICLES)
    public ArticleResponse createArticle(@RequestBody ArticleRequest createArticleRequest) throws JsonProcessingException {
        return createArticleService.createArticle(createArticleRequest);
    }

    @Secured({"ADMIN", "USER"})
    @PutMapping(ApiConst.ARTICLES_SLUG)
    public UpdateArticleResponse updateArticle(@RequestBody UpdateArticleRequest updateArticleRequest,
                                               @PathVariable("slug") String currentSlug) {
        return updateArticleService.updateArticle(updateArticleRequest, currentSlug);
    }
    @Secured({"ADMIN", "USER"})
    @DeleteMapping(ApiConst.ARTICLES_SLUG)
    public DeleteArticleResponse deleteArticle(@PathVariable("slug") String slug) {
        return deleteArticleService.deleteArticle(slug);
    }

    @GetMapping(ApiConst.ARTICLES_SLUG)
    public GetArticleResponse getArticle(@PathVariable("slug") String slug) {
        return getArticleService.getArticle(slug);
    }
    
    @GetMapping(ApiConst.ARTICLES)
    public GetArticlesResponse getArticles(@RequestParam(value = "tag", required = false) String tag,
                                           @RequestParam(value = "author", required = false) String author,
                                           @RequestParam(value = "favorited", required = false) String favorited,
                                           @RequestParam(value = "size", defaultValue = "20") int limit,
                                           @RequestParam(value = "page", defaultValue = "1") int page) {
        return getArticleListService.getArticles(tag, author, favorited, limit, page);
    }
}
