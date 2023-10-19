package overcloud.blog.application.article.delete_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.ArticleRepository;
import overcloud.blog.repository.CommentRepository;
import overcloud.blog.repository.FavoriteRepository;

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
    public DeleteArticleResponse deleteArticle(String slug) {
        DeleteArticleResponse deleteArticleResponse = new DeleteArticleResponse();
/*
        commentRepository.deleteByArticleSlug(slug);
        favoriteRepository.deleteByArticleSlug(slug);
*/
        articleRepository.deleteBySlug(slug);
        deleteArticleResponse.setSlug(slug);
        return deleteArticleResponse;
    }
}
