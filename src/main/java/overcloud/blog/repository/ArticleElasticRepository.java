package overcloud.blog.repository.a;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import overcloud.blog.usecase.article.search.ArticleElastic;

public interface ArticleElasticRepository extends ElasticsearchRepository<ArticleElastic, String> {
}
