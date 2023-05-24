package overcloud.blog.application.article;


import overcloud.blog.application.article.create_article.CreateArticleService;
import overcloud.blog.application.article.delete_article.DeleteArticleService;
import overcloud.blog.application.article.create_article.ArticleRequest;
import overcloud.blog.application.article.create_article.ArticleResponse;
import overcloud.blog.application.article.delete_article.DeleteArticleResponse;
import overcloud.blog.application.article.get_article.GetArticleResponse;
import overcloud.blog.application.article.get_article_list.GetArticlesResponse;
import overcloud.blog.application.article.update_article.UpdateArticleResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.article.get_article.GetArticleService;
import overcloud.blog.application.article.get_article_list.GetArticleListService;
import overcloud.blog.application.article.update_article.UpdateArticleService;

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

    @PostMapping("/articles")
    public ArticleResponse createArticle(@RequestBody ArticleRequest createArticleRequest) {
        return createArticleService.createArticle(createArticleRequest);
    }

    @PutMapping("/articles/{slug}")
    public UpdateArticleResponse updateArticle(@RequestBody ArticleRequest updateArticleRequest,
                                               @PathVariable("slug") String currentSlug) {
        return updateArticleService.updateArticle(updateArticleRequest, currentSlug);
    }

    @DeleteMapping("/articles/{id}")
    public DeleteArticleResponse deleteArticle(@PathVariable("id") String id ) {
        return deleteArticleService.deleteArticle(id);
    }

    @GetMapping("/articles/{slug}")
    public GetArticleResponse getArticle(@PathVariable("slug") String slug ) {
        return getArticleService.getArticle(slug);
    }

    @GetMapping("/articles")
    public GetArticlesResponse getArticles(@RequestParam(value = "tag", required = false) String tag,
                                           @RequestParam(value = "author", required = false) String author,
                                           @RequestParam(value = "favorited", required = false) String favorited,
                                           @RequestParam(value = "size", defaultValue = "20") int limit,
                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "searchParam", defaultValue = "") String searchParam) {
        return getArticleListService.getArticles(tag, author, favorited, limit, page, searchParam);
    }

    @GetMapping("/articles/feed")
    public GetArticlesResponse getArticlesFeed(@RequestParam(value = "size", defaultValue = "20") int size,
                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "searchParam", defaultValue = "") String searchParam) {
        return getArticleListService.getArticlesFeed(size, page, searchParam);
    }

}
