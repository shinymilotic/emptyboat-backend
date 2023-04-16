package overcloud.blog.application.article.favorite.utils;

import org.springframework.stereotype.Component;
import overcloud.blog.domain.article.favorite.FavoriteEntity;
import overcloud.blog.domain.user.UserEntity;

import java.util.List;

@Component
public class FavoriteUtils {

    public boolean isFavorited(UserEntity user, List<FavoriteEntity> favorites) {

        for (FavoriteEntity favorite: favorites) {
            UserEntity u = favorite.getUser();
            if(user.equals(u)) {
                return true;
            }
        }

        return false;
    }

}
