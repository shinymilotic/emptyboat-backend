package overcloud.blog.application.article.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.application.article.api.dto.delete.DeleteArticleResponse;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.comment.repository.CommentRepository;
import overcloud.blog.application.article.favorite.repository.FavoriteRepository;

import java.util.UUID;

@Service
public class DeleteArticleService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ArticleRepository articleRepository;

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
