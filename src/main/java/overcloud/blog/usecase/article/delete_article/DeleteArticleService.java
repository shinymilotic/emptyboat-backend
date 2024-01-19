package overcloud.blog.usecase.article.delete_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.repository.jparepository.JpaArticleRepository;

@Service
public class DeleteArticleService {


    private final JpaArticleRepository articleRepository;

    public DeleteArticleService(
                                JpaArticleRepository articleRepository) {
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
        articleRepository.updateSearchVector();
        return deleteArticleResponse;
    }
}
