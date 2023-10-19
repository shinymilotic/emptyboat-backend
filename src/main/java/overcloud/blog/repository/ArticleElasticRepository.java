package overcloud.blog.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import overcloud.blog.application.article.search.ArticleElastic;

import java.util.UUID;


@Repository
public interface ArticleElasticRepository extends ElasticsearchRepository<ArticleElastic, String> {
}
