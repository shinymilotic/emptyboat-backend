package overcloud.blog.usecase.article.favorite.core.utils;

import org.springframework.stereotype.Component;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.FavoriteEntity;
import overcloud.blog.entity.UserEntity;

import java.util.List;

@Component
public class FavoriteUtils {

    public boolean isFavorited(UserEntity user, ArticleEntity article) {
        List<FavoriteEntity> favorites = article.getFavorites();

        for (FavoriteEntity favorite: favorites) {
            UserEntity u = favorite.getUser();
            if(user != null && user.equals(u)) {
                return true;
            }
        }

        return false;
    }

}
