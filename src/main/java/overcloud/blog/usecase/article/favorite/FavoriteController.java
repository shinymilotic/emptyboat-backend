package overcloud.blog.usecase.article.favorite;

import org.springframework.web.bind.annotation.*;

import overcloud.blog.infrastructure.ApiConst;
import overcloud.blog.usecase.article.create_article.ArticleResponse;
import overcloud.blog.usecase.article.favorite.make_favorite.MakeUnfavoriteService;
import overcloud.blog.usecase.article.favorite.make_unfavorite.MakeFavoriteService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class FavoriteController {

    private final MakeFavoriteService makeFavoriteService;

    private final MakeUnfavoriteService makeUnfavoriteService;

    public FavoriteController(MakeFavoriteService makeFavoriteService,
                              MakeUnfavoriteService makeUnfavoriteService) {
        this.makeFavoriteService = makeFavoriteService;
        this.makeUnfavoriteService = makeUnfavoriteService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(ApiConst.ARTICLES_SLUG_FAVORITE)
    public ArticleResponse makeFavorite(@PathVariable String slug) {
        return makeFavoriteService.makeFavorite(slug);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(ApiConst.ARTICLES_SLUG_FAVORITE)
    public ArticleResponse makeUnfavorite(@PathVariable String slug) {
        return makeUnfavoriteService.makeUnfavorite(slug);
    }
}
