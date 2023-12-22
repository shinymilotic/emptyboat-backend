package overcloud.blog.repository.jparepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import overcloud.blog.usecase.article.search.ArticleElastic;

@Repository
public interface JpaArticleElasticRepository extends ElasticsearchRepository<ArticleElastic, String> {
}
