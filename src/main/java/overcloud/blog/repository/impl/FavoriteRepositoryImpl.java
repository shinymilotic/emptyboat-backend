package overcloud.blog.repository.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import overcloud.blog.entity.FavoriteEntity;
import overcloud.blog.repository.IFavoriteRepository;
import overcloud.blog.repository.jparepository.JpaFavoriteRepository;

@Repository
public class FavoriteRepositoryImpl implements IFavoriteRepository {

    private final JpaFavoriteRepository jpa;
    
    public FavoriteRepositoryImpl(JpaFavoriteRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<FavoriteEntity> findById(UUID userId, UUID articleId) {
        return this.jpa.findById(userId, articleId);
    }

    @Override
    public void deleteByArticleSlug(String slug) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteByArticleSlug'");
    }
}
