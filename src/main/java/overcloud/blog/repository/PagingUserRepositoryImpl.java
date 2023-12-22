package overcloud.blog.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.sql.PlainQueryBuilder;
import overcloud.blog.repository.PagingUserRepository;

import java.util.List;

@Repository
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
