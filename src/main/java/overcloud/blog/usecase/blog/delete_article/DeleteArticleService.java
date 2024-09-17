package overcloud.blog.usecase.blog.delete_article;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.common.response.ResFactory;
import overcloud.blog.common.response.RestResponse;
import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.IArticleTagRepository;
import overcloud.blog.repository.ICommentRepository;
import overcloud.blog.repository.IFavoriteRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;

@Service
public class DeleteArticleService {
    private final IArticleRepository articleRepository;
    private final IArticleTagRepository articleTagRepository;
    private final ICommentRepository commentRepository;
    private final IFavoriteRepository favoriteRepository;
    private final ResFactory resFactory;

    public DeleteArticleService(IArticleRepository articleRepository,
                                IArticleTagRepository articleTagRepository,
                                ICommentRepository commentRepository,
                                IFavoriteRepository favoriteRepository,
                                ResFactory resFactory) {
        this.articleRepository = articleRepository;
        this.articleTagRepository = articleTagRepository;
        this.resFactory = resFactory;
        this.commentRepository = commentRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public RestResponse<Void> deleteArticle(String id) {
        UUID articleId = UUID.fromString(id);
        commentRepository.deleteByArticleId(articleId);
        favoriteRepository.deleteByArticleId(articleId);
        articleTagRepository.deleteByArticleId(articleId);
        articleRepository.deleteById(articleId);
        articleRepository.updateSearchVector();
        
        return resFactory.success(ArticleResMsg.ARTICLE_DELETE_SUCCESS, null);
    }
}
