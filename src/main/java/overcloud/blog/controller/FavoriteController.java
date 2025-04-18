package overcloud.blog.controller;

import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.favorite.make_favorite.MakeFavoriteService;
import overcloud.blog.usecase.blog.favorite.make_unfavorite.MakeUnfavoriteService;

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
    @PostMapping("/articles/{id}/favorite")
    public Void makeFavorite(@PathVariable String id) {
        return makeFavoriteService.makeFavorite(id);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/articles/{id}/unfavorite")
    public Void makeUnfavorite(@PathVariable String id) {
        return makeUnfavoriteService.makeUnfavorite(id);
    }
}
