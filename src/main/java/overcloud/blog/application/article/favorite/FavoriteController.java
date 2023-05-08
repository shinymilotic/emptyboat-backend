package overcloud.blog.application.article.favorite;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.article.favorite.core.dto.SingleArticleResponse;
import overcloud.blog.application.article.favorite.make_favorite.MakeUnfavoriteService;
import overcloud.blog.application.article.favorite.make_unfavorite.MakeFavoriteService;

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
    @PostMapping("articles/{slug}/favorite")
    public SingleArticleResponse makeFavorite(@PathVariable("slug") String slug) {
        return makeFavoriteService.makeFavorite(slug);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("articles/{slug}/favorite")
    public SingleArticleResponse makeUnfavorite(@PathVariable("slug") String slug) {
        return makeUnfavoriteService.makeUnfavorite(slug);
    }
}
