package overcloud.blog.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import overcloud.blog.usecase.article.search.ArticleElastic;

@Repository
public interface ArticleElasticRepository extends ElasticsearchRepository<ArticleElastic, String> {
}
