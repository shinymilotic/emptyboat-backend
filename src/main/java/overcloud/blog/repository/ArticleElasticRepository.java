package overcloud.blog.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import overcloud.blog.usecase.article.search.ArticleElastic;

public interface ArticleElasticRepository extends ElasticsearchRepository<ArticleElastic, String> {
}
