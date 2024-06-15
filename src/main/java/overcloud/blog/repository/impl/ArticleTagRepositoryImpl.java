package overcloud.blog.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.ArticleTag;
import overcloud.blog.repository.IArticleTagRepository;
import overcloud.blog.repository.jparepository.JpaArticleTagRepository;

@Repository
public class ArticleTagRepositoryImpl implements IArticleTagRepository {

    private final JpaArticleTagRepository jpa;
    
    public ArticleTagRepositoryImpl(JpaArticleTagRepository jpa) {
        // Empty constructor
        this.jpa = jpa;
    }

    @Override
    public void saveAll(List<ArticleTag> articleTags) {
        // TODO Auto-generated method stub
        this.jpa.saveAll(articleTags);
    }
}
