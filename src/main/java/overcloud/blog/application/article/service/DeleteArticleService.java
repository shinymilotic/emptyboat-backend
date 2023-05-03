package overcloud.blog.application.article.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.application.article.dto.delete.DeleteArticleResponse;
import overcloud.blog.application.article.repository.ArticleRepository;
import overcloud.blog.application.article.comment.repository.CommentRepository;
import overcloud.blog.application.article.favorite.repository.FavoriteRepository;

import java.util.UUID;

@Service
public class DeleteArticleService {

    private final CommentRepository commentRepository;

    private final FavoriteRepository favoriteRepository;

    private final ArticleRepository articleRepository;

    public DeleteArticleService(CommentRepository commentRepository,
                                FavoriteRepository favoriteRepository,
                                ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.favoriteRepository = favoriteRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public DeleteArticleResponse deleteArticle(String id) {
        UUID uuid = UUID.fromString(id);
        DeleteArticleResponse deleteArticleResponse = new DeleteArticleResponse();
        commentRepository.deleteByArticle(uuid);
        favoriteRepository.deleteByArticle(uuid);
        articleRepository.deleteById(uuid);
        deleteArticleResponse.setId(uuid.toString());
        return deleteArticleResponse;
    }
}
