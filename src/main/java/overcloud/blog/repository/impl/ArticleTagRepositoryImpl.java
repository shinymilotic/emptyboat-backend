package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.ArticleTag;
import overcloud.blog.repository.ArticleTagRepository;
import overcloud.blog.repository.jparepository.JpaArticleTagRepository;

@Repository
public class ArticleTagRepositoryImpl implements ArticleTagRepository {
    private final JpaArticleTagRepository jpa;
    
    public ArticleTagRepositoryImpl(JpaArticleTagRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<ArticleTag> articleTags) {
        jpa.saveAll(articleTags);
    }

    @Override
    public void deleteByArticleId(UUID articleId) {
        jpa.deleteByArticleId(articleId);
    }

    @Override
    public void deleteByTagId(UUID tagId) {
        jpa.deleteByTagId(tagId);
    }
}
