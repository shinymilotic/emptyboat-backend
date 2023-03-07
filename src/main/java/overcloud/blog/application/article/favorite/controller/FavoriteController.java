package overcloud.blog.application.article.favorite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import overcloud.blog.application.article.favorite.service.FavoriteService;

@RestController
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("articles/{slug}/favorite")
    public void makeFavorite(@PathVariable("slug") String slug) {
        favoriteService.makeFavorite(slug);
    }

    @DeleteMapping("articles/{slug}/favorite")
    public void makeUnfavorite(@PathVariable("slug") String slug) {
        favoriteService.makeUnfavorite(slug);
    }
}
