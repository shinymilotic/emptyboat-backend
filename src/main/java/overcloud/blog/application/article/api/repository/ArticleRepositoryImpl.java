package overcloud.blog.application.article.api.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import overcloud.blog.domain.ArticleTag;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.article.tag.TagEntity;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<ArticleEntity> findByCriteria(String tag, String author, String favorited, int limit, int page) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT article FROM ArticleEntity article ");

        Query query = entityManager
                .createQuery(sql.toString())
                .setFirstResult(getOffset(page, limit))
                .setMaxResults(limit);

        List<ArticleEntity> articleEntities = query.getResultList();

        if(StringUtils.hasText(tag)) {
            articleEntities = fetchArticleTags(tag, getOffset(page, limit), limit);
        }

        if(StringUtils.hasText(author)) {
            articleEntities = fetchArticleAuthor(author, getOffset(page, limit), limit);
        }

        if(StringUtils.hasText(favorited)) {
            articleEntities = fetchArticleFavorites(favorited, getOffset(page, limit), limit);
        }

        return articleEntities;
    }

    private int getOffset(int page, int limit) {
        int offset = limit*(page-1);

        return offset;
    }
    private List<ArticleEntity> fetchArticleFavorites(String favorited, int offset, int limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT article FROM ArticleEntity article ");
        sql.append("LEFT JOIN fetch article.favorites as favorites ");
        sql.append(" WHERE (favorites.user.username = :favorited) ");

        Query query = entityManager
                .createQuery(sql.toString())
                .setFirstResult(offset)
                .setMaxResults(limit);

        query.setParameter("favorited", favorited);

        return query.getResultList();    }

    private List<ArticleEntity> fetchArticleAuthor(String author, int offset, int limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT article FROM ArticleEntity article ");
        sql.append(" WHERE (article.author.username = :author)  ");

        Query query = entityManager
                .createQuery(sql.toString())
                .setFirstResult(offset)
                .setMaxResults(limit);

        query.setParameter("author", author);

        return query.getResultList();
    }

    private List<ArticleEntity> fetchArticleTags(String tag, int offset, int limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT article FROM ArticleEntity article ");
        sql.append(" LEFT JOIN fetch article.articleTags as articleTags ");
        sql.append(" WHERE (articleTags.tag.name = :tag)  ");

        Query query = entityManager
                .createQuery(sql.toString())
                .setFirstResult(offset)
                .setMaxResults(limit);

        query.setParameter("tag", tag);

        return query.getResultList();
    }
}
