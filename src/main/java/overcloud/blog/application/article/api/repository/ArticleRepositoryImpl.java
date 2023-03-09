package overcloud.blog.application.article.api.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import overcloud.blog.domain.ArticleTag;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.tag.TagEntity;

import java.util.List;

@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<ArticleEntity> findByCriteria(String tag, String author, String favorited, int limit, int offset) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT article FROM ArticleEntity article ");
        sql.append("LEFT JOIN ArticleTag articleTag ON articleTag.article.id = article.id ");
        sql.append("LEFT JOIN FavoriteEntity favorite ON favorite.article.id = article.id ");
        sql.append("WHERE true  ");
        if(StringUtils.hasText(tag)) {
            sql.append(" AND (articleTag.tag.name = :tag)  ");
        }
        if(StringUtils.hasText(author)) {
            sql.append(" AND (article.author.username = :author)  ");
        }
        if(StringUtils.hasText(favorited)) {
            sql.append(" AND (favorite.user.username = :favorited) ");
        }

        Query query = entityManager
                .createQuery(sql.toString())
                .setFirstResult(offset)
                .setMaxResults(limit);

        if(StringUtils.hasText(tag)) {
            query.setParameter("tag", tag);
        }

        if(StringUtils.hasText(author)) {
            query.setParameter("author", author);
        }

        if(StringUtils.hasText(favorited)) {
            query.setParameter("favorited", favorited);
        }

        List<ArticleEntity> articleEntities = query.getResultList();


        return articleEntities;
    }
}
