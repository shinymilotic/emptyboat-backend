package overcloud.blog.application.article.favorite.core.utils;

import org.springframework.stereotype.Component;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.article.favorite.core.FavoriteEntity;
import overcloud.blog.application.user.core.UserEntity;

import java.util.List;
import java.util.Optional;

@Component
public class FavoriteUtils {

    public boolean isFavorited(Optional<UserEntity> user, ArticleEntity article) {
        List<FavoriteEntity> favorites = article.getFavorites();

        for (FavoriteEntity favorite: favorites) {
            UserEntity u = favorite.getUser();
            if(user.isPresent() && user.get().equals(u)) {
                return true;
            }
        }

        return false;
    }

}
