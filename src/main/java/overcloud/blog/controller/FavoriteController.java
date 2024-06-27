package overcloud.blog.controller;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.favorite.make_favorite.MakeFavoriteService;
import overcloud.blog.usecase.blog.favorite.make_unfavorite.MakeUnfavoriteService;
import overcloud.blog.usecase.common.response.RestResponse;

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
    public RestResponse<Void> makeFavorite(@PathVariable String slug) {
        return makeFavoriteService.makeFavorite(slug);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(ApiConst.ARTICLES_SLUG_FAVORITE)
    public RestResponse<Void> makeUnfavorite(@PathVariable String slug) {
        return makeUnfavoriteService.makeUnfavorite(slug);
    }
}
