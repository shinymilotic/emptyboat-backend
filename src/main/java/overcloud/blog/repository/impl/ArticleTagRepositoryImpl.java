package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.ArticleTag;
import overcloud.blog.repository.IArticleTagRepository;
import overcloud.blog.repository.jparepository.JpaArticleTagRepository;

@Repository
public class ArticleTagRepositoryImpl implements IArticleTagRepository {
    private final JpaArticleTagRepository jpa;
    
    public ArticleTagRepositoryImpl(JpaArticleTagRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<ArticleTag> articleTags) {
        this.jpa.saveAll(articleTags);
    }

    @Override
    public void deleteByArticleId(UUID articleId) {
        this.jpa.deleteByArticleId(articleId);
    }
}
