package overcloud.blog.application.article.favorite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.favorite.repository.FavoriteRepository;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.favorite.FavoriteEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private SpringAuthenticationService authenticationService;

    @Autowired
    private ArticleRepository articleRepository;

    public void makeFavorite(String slug) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        UserEntity userEntity = authenticationService.getCurrentUser().get().getUser().get();
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        favoriteEntity.setArticle(articleEntity);
        favoriteEntity.setUser(userEntity);

        favoriteRepository.save(favoriteEntity);
    }

    public void makeUnfavorite(String slug) {
        favoriteRepository.deleteBySlug(slug);
    }
}
