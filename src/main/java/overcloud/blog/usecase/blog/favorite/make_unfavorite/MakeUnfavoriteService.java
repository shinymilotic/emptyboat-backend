package overcloud.blog.usecase.blog.favorite.make_unfavorite;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.common.auth.service.SpringAuthenticationService;
import overcloud.blog.common.exceptionhandling.InvalidDataException;
import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.FavoriteId;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.IFavoriteRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
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
    public RestResponse<Void> makeUnfavorite(String id) {
        Optional<ArticleEntity> articleEntity = articleRepository.findById(UUID.fromString(id));
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(resFactory.fail(UserResMsg.USER_NOT_FOUND)))
                .getUser();

        if (articleEntity.isPresent()) {
            FavoriteId favoritePk = new FavoriteId();
            favoritePk.setUserId(currentUser.getUserId());
            favoritePk.setArticleId(articleEntity.get().getArticleId());
            favoriteRepository.deleteById(favoritePk);
        }
        
        return resFactory.success(ArticleResMsg.ARTICLE_UNFAVORITE_SUCCESS, null);
    }
}
