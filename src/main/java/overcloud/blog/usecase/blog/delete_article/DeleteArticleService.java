package overcloud.blog.usecase.blog.delete_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.usecase.common.response.RestResponse;

@Service
public class DeleteArticleService {

    private final IArticleRepository articleRepository;

    public DeleteArticleService(IArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public RestResponse<DeleteArticleResponse> deleteArticle(String slug) {
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
