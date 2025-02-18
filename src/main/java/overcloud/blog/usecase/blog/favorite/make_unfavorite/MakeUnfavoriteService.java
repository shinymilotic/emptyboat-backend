package overcloud.blog.usecase.blog.favorite.make_unfavorite;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.FavoriteId;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.IFavoriteRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

@Service
public class MakeUnfavoriteService {
    private final IFavoriteRepository favoriteRepository;
    private final SpringAuthenticationService authenticationService;
    private final IArticleRepository articleRepository;
    private final ObjectsValidator validator;

    public MakeUnfavoriteService(IFavoriteRepository favoriteRepository,
                                 SpringAuthenticationService authenticationService,
                                 IArticleRepository articleRepository,
                                 ObjectsValidator validator) {
        this.favoriteRepository = favoriteRepository;
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
        this.validator = validator;
    }

    @Transactional
    public Void makeUnfavorite(String id) {
        Optional<ArticleEntity> articleEntity = articleRepository.findById(UUID.fromString(id));
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> validator.fail(UserResMsg.USER_NOT_FOUND))
                .getUser();

        if (articleEntity.isPresent()) {
            FavoriteId favoritePk = new FavoriteId();
            favoritePk.setUserId(currentUser.getUserId());
            favoritePk.setArticleId(articleEntity.get().getArticleId());
            favoriteRepository.deleteById(favoritePk);
        }
        
        return null;
    }
}
