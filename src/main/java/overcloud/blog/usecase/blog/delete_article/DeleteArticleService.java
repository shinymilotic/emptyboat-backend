package overcloud.blog.usecase.blog.delete_article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.repository.IArticleRepository;
import overcloud.blog.repository.ICommentRepository;
import overcloud.blog.repository.IFavoriteRepository;
import overcloud.blog.usecase.blog.common.ArticleResMsg;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;

@Service
public class DeleteArticleService {
    private final IArticleRepository articleRepository;
    private final ICommentRepository commentRepository;
    private final IFavoriteRepository favoriteRepository;
    private final ResFactory resFactory;

    public DeleteArticleService(IArticleRepository articleRepository,
                                ICommentRepository commentRepository,
                                IFavoriteRepository favoriteRepository,
                                ResFactory resFactory) {
        this.articleRepository = articleRepository;
        this.resFactory = resFactory;
        this.commentRepository = commentRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public RestResponse<DeleteArticleResponse> deleteArticle(String slug) {
        DeleteArticleResponse deleteArticleResponse = new DeleteArticleResponse();
        commentRepository.deleteByArticleSlug(slug);
        favoriteRepository.deleteByArticleSlug(slug);
        articleRepository.deleteBySlug(slug);
        deleteArticleResponse.setSlug(slug);
        articleRepository.updateSearchVector();
        
        return resFactory.success(ArticleResMsg.ARTICLE_DELETE_SUCCESS, deleteArticleResponse);
    }
}
