package overcloud.blog.usecase.blog.delete_article;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.repository.ArticleRepository;
import overcloud.blog.repository.ArticleTagRepository;
import overcloud.blog.repository.CommentRepository;
import overcloud.blog.repository.FavoriteRepository;

@Service
public class DeleteArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleTagRepository articleTagRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;

    public DeleteArticleService(ArticleRepository articleRepository,
                                ArticleTagRepository articleTagRepository,
                                CommentRepository commentRepository,
                                FavoriteRepository favoriteRepository) {
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
