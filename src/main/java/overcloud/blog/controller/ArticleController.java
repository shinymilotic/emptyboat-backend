package overcloud.blog.controller;

import java.util.UUID;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.create_article.ArticleRequest;
import overcloud.blog.usecase.blog.create_article.CreateArticleService;
import overcloud.blog.usecase.blog.delete_article.DeleteArticleService;
import overcloud.blog.usecase.blog.get_article.GetArticleResponse;
import overcloud.blog.usecase.blog.get_article.GetArticleService;
import overcloud.blog.usecase.blog.get_article_list.GetArticleListService;
import overcloud.blog.usecase.blog.get_article_list.GetArticlesResponse;
import overcloud.blog.usecase.blog.update_article.UpdateArticleRequest;
import overcloud.blog.usecase.blog.update_article.UpdateArticleService;
import overcloud.blog.usecase.common.response.RestResponse;

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
    public RestResponse<UUID> createArticle(@RequestBody ArticleRequest createArticleRequest) {
        return createArticleService.createArticle(createArticleRequest);
    }

    @Secured({"ADMIN", "USER"})
    @PutMapping(ApiConst.ARTICLE)
    public RestResponse<UUID> updateArticle(@RequestBody UpdateArticleRequest updateArticleRequest,
                                               @PathVariable("id") String id) {
        return updateArticleService.updateArticle(updateArticleRequest, id);
    }

    @Secured({"ADMIN", "USER"})
    @DeleteMapping(ApiConst.ARTICLE)
    public RestResponse<Void> deleteArticle(@PathVariable String id) {
        return deleteArticleService.deleteArticle(id);
    }

    @GetMapping(ApiConst.ARTICLE)
    public RestResponse<GetArticleResponse> getArticle(@PathVariable String id) {
        return getArticleService.getArticle(id);
    }

    @GetMapping(ApiConst.ARTICLES)
    public RestResponse<GetArticlesResponse> getArticles(@RequestParam(required = false) String tag,
                                           @RequestParam(required = false) String author,
                                           @RequestParam(required = false) String favorited,
                                           @RequestParam(value = "size", defaultValue = "20") int limit,
                                           @RequestParam(defaultValue = "") String lastArticleId) {
        return getArticleListService.getArticles(tag, author, favorited, limit, lastArticleId);
    }
}
