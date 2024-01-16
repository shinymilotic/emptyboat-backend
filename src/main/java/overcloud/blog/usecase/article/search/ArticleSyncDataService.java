package overcloud.blog.usecase.article.search;


import org.springframework.stereotype.Service;

//import overcloud.blog.repository.jparepository.JpaArticleElasticRepository;
import overcloud.blog.usecase.article.search.json.ArticleSyncData;

//@Service
//public class ArticleSyncDataService {
//
//    private final JpaArticleElasticRepository articleElasticRepository;
//
//    public ArticleSyncDataService(JpaArticleElasticRepository articleElasticRepository) {
//        this.articleElasticRepository = articleElasticRepository;
//    }
//
//    public void createArticle(ArticleSyncData article) {
//        ArticleElastic articleElastic = toArticleElastic(article);
//        articleElasticRepository.save(articleElastic);
//    }
//
//    public void updateArticle(ArticleSyncData article) {
//        ArticleElastic articleElastic = toArticleElastic(article);
//        articleElasticRepository.save(articleElastic);
//    }
//
//    public void deleteArticle(String id) {
//        articleElasticRepository.deleteById(id);
//    }
//
//    public ArticleElastic toArticleElastic(ArticleSyncData articleSyncData) {
//        ArticleElastic articleElastic = new ArticleElastic();
//        articleElastic.setId(articleSyncData.getId());
//        articleElastic.setBody(articleSyncData.getBody());
//        articleElastic.setDescription(articleSyncData.getDescription());
//        articleElastic.setTitle(articleSyncData.getTitle());
//        articleElastic.setSlug(articleSyncData.getSlug());
//
//        return articleElastic;
//    }
//}
