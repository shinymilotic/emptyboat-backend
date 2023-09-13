package overcloud.blog.application.user.core.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.repository.PagingUserRepository;
import overcloud.blog.infrastructure.sql.PlainQueryBuilder;

import java.util.List;

public class PagingUserRepositoryImpl implements PagingUserRepository {

    @PersistenceContext
    EntityManager entityManager;

    private final PlainQueryBuilder queryBuilder;

    public PagingUserRepositoryImpl(PlainQueryBuilder plainQueryBuilder) {
        this.queryBuilder = plainQueryBuilder;
    }

    @Override
    public List<UserEntity> findAll(int page, int size) {
        return entityManager
                .createQuery("SELECT users FROM UserEntity users", UserEntity.class)
                .setFirstResult(queryBuilder.getOffset(page, size))
                .setMaxResults(size)
                .getResultList();

    }
}
