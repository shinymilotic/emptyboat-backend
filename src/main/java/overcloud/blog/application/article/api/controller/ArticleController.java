package overcloud.blog.application.article.api.controller;


import overcloud.blog.application.article.api.dto.create.CreateArticleRequest;
import overcloud.blog.application.article.api.dto.create.CreateArticleResponse;
import overcloud.blog.application.article.api.dto.delete.DeleteArticleResponse;
import overcloud.blog.application.article.api.dto.get.single.GetArticleResponse;
import overcloud.blog.application.article.api.dto.get.multiple.GetArticlesResponse;
import overcloud.blog.application.article.api.dto.update.UpdateArticleRequest;
import overcloud.blog.application.article.api.dto.update.UpdateArticleResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.article.api.service.*;

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
    public CreateArticleResponse createArticle(@RequestBody CreateArticleRequest createArticleRequest) {
        return createArticleService.createArticle(createArticleRequest);
    }

    @PutMapping("/articles/{slug}")
    public UpdateArticleResponse updateArticle(@Valid @RequestBody UpdateArticleRequest updateArticleRequest,
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
