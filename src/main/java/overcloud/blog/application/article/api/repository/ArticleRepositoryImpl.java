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
    public List<ArticleEntity> findByTagAndAuthorAndFavorite(String tag, String author, String favorited, int limit, int offset) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT articles.author_id,");
        sql.append("articles.id,");
        sql.append("articles.body,");
        sql.append("articles.created_at,");
        sql.append("articles.description,");
        sql.append("articles.slug,");
        sql.append("articles.title,");
        sql.append("articles.updated_at ");
        sql.append("from articles ");
        sql.append("left join article_tag on article_tag.article_id = articles.id ");
        sql.append("left join tag on article_tag.tag_id = tag.id ");
        sql.append("left join favorites on articles.id = favorites.article_id ");
        sql.append("AND  articles.author_id = favorites.user_id ");
        sql.append("inner join users on users.id = articles.author_id ");
        sql.append("WHERE users.username is not null ");

        if(StringUtils.hasText(tag)) {
            sql.append("AND tag.name = :tag ");
        }

        if(StringUtils.hasText(author)) {
            sql.append("AND users.username = :author ");
        }

        if(StringUtils.hasText(favorited)) {
            sql.append("AND favorites.user_id = users.id ");
            sql.append("AND users.username = :favorited ");
        }

        sql.append(" LIMIT :limit OFFSET :offset ");

        Query query = entityManager.createNativeQuery(sql.toString(), ArticleEntity.class);
        if(StringUtils.hasText(tag)) {
            query.setParameter("tag", tag);
        }
        if(StringUtils.hasText(author)) {
            query.setParameter("author", author);
        }
        if(StringUtils.hasText(favorited)) {
            query.setParameter("favorited", favorited);
        }
        query.setParameter("limit", limit);
        query.setParameter("offset", offset);

        return query.getResultList();
    }
}
