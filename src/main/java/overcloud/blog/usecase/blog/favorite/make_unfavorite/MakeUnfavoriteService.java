package overcloud.blog.usecase.blog.favorite.make_unfavorite;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ResFactory;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.FavoriteId;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.IFavoriteRepository;
import overcloud.blog.usecase.user.common.UserResMsg;

@Service
public class MakeUnfavoriteService {
    private final IFavoriteRepository favoriteRepository;
    private final SpringAuthenticationService authenticationService;
    private final IArticleRepository articleRepository;
    private final ResFactory resFactory;

    public MakeUnfavoriteService(IFavoriteRepository favoriteRepository,
                                 SpringAuthenticationService authenticationService,
                                 IArticleRepository articleRepository,
                                 ResFactory resFactory) {
        this.favoriteRepository = favoriteRepository;
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
        this.resFactory = resFactory;
    }

    @Transactional
    public Void makeUnfavorite(String id) {
        Optional<ArticleEntity> articleEntity = articleRepository.findById(UUID.fromString(id));
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> resFactory.fail(UserResMsg.USER_NOT_FOUND))
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
