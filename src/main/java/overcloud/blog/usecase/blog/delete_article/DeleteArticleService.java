package overcloud.blog.usecase.blog.delete_article;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
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

    public DeleteArticleService(IArticleRepository articleRepository,
                                IArticleTagRepository articleTagRepository,
                                ICommentRepository commentRepository,
                                IFavoriteRepository favoriteRepository) {
        this.articleRepository = articleRepository;
        this.articleTagRepository = articleTagRepository;
        this.commentRepository = commentRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Void deleteArticle(String id) {
        UUID articleId = UUID.fromString(id);
        commentRepository.deleteByArticleId(articleId);
        favoriteRepository.deleteByArticleId(articleId);
        articleTagRepository.deleteByArticleId(articleId);
        articleRepository.deleteById(articleId);
        articleRepository.updateSearchVector();
        
        return null;
    }
}
