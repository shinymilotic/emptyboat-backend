package overcloud.blog.application.article.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ArticleElasticRepository extends ElasticsearchRepository<ArticleElastic, String> {
}
