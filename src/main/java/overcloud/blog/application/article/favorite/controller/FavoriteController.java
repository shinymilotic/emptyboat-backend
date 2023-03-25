package overcloud.blog.application.article.favorite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.application.article.favorite.dto.SingleArticleResponse;
import overcloud.blog.application.article.favorite.service.FavoriteService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @CrossOrigin(origins = "*")
    @PostMapping("articles/{slug}/favorite")
    public SingleArticleResponse makeFavorite(@PathVariable("slug") String slug) {
        return favoriteService.makeFavorite(slug);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("articles/{slug}/favorite")
    public SingleArticleResponse makeUnfavorite(@PathVariable("slug") String slug) {
        return favoriteService.makeUnfavorite(slug);
    }
}
