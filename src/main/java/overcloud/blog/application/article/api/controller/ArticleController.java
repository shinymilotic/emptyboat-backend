package overcloud.blog.application.article.api.controller;


import org.hibernate.sql.Update;
import overcloud.blog.application.article.api.dto.create.CreateArticleRequest;
import overcloud.blog.application.article.api.dto.create.CreateArticleResponse;
import overcloud.blog.application.article.api.dto.delete.DeleteArticleResponse;
import overcloud.blog.application.article.api.dto.get.GetArticlesResponse;
import overcloud.blog.application.article.api.dto.update.UpdateArticleRequest;
import overcloud.blog.application.article.api.dto.update.UpdateArticleResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.article.api.service.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ArticleController {

    @Autowired
    private CreateArticleService createArticleService;

    @Autowired
    private UpdateArticleService updateArticleService;

    @Autowired
    private GetArticleService getArticleService;

    @Autowired
    private GetArticleListService getArticleListService;

    @Autowired
    private DeleteArticleService deleteArticleService;


    @PostMapping("/articles")
    public CreateArticleResponse createArticle(@Valid @RequestBody CreateArticleRequest createArticleRequest) {
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
    public GetArticlesResponse getArticle(@PathVariable("slug") String slug ) {
        return getArticleService.getArticle(slug);
    }

    @GetMapping("/articles")
    public GetArticlesResponse getArticles(@RequestParam(value = "tag", required = false) String tag,
                                           @RequestParam(value = "author", required = false) String author,
                                           @RequestParam(value = "favorited", required = false) String favorited,
                                           @RequestParam(value = "size", defaultValue = "20") int limit,
                                           @RequestParam(value = "page", defaultValue = "0") int page) {
        return getArticleListService.getArticles(tag, author, favorited, limit, page);
    }

    @GetMapping("/articles/feed")
    public GetArticlesResponse getArticlesFeed(@RequestParam(value = "size", defaultValue = "20") int size,
                                           @RequestParam(value = "page", defaultValue = "0") int page) {
        return getArticleListService.getArticlesFeed(size, page);
    }
}
