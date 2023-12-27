package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.infrastructure.sql.PlainQueryBuilder;
import overcloud.blog.repository.SearchArticlesRepository;
import overcloud.blog.usecase.article.get_article_list.ArticleSummary;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SearchArticlesRepositoryImpl implements SearchArticlesRepository {

    private final EntityManager entityManager;

    private final PlainQueryBuilder plainQueryBuilder;

    public SearchArticlesRepositoryImpl(EntityManager entityManager, PlainQueryBuilder plainQueryBuilder) {
        this.entityManager = entityManager;
        this.plainQueryBuilder = plainQueryBuilder;
    }

    @Override
    public List<ArticleSummary> findByCriteria(String tag, String author, String favorited, int limit, int page) {

        StringBuilder query = new StringBuilder("SELECT  ");
        query.append(" a.id, a.slug, a.title, a.description, a.body, t.name , a.createdAt, a.updatedAt, ");
        query.append(" author.username, author.bio, author.image , author.followersCount ");
//        query.append(" favorited, favoritesCount, author.username, author.bio, author.image, following, followersCount) ");
        query.append(" FROM ArticleEntity a ");
        query.append(" INNER JOIN  ");
        query.append(" (SELECT u.id as id, u.username as username, u.bio as bio, u.image as image, COUNT(follow.id.followerId) as followersCount ");
        query.append(" FROM UserEntity u ");
        query.append(" LEFT JOIN FollowEntity follow ON u.id = follow.id.followeeId ");
        query.append(" GROUP BY u.id , u.username , u.bio , u.image) as author ");

        query.append(" ON a.author.id = author.id  ");

        if(StringUtils.hasText(author)) {
            query.append(" AND author.username = :author ");
        }

        query.append(" LEFT JOIN ArticleTag at ON a.id = at.id.articleId ");
        query.append(" LEFT JOIN TagEntity t ON at.id.tagId = t.id ");

        if(StringUtils.hasText(tag)) {
            query.append(" AND t.name = :tag ");
        }

        query.append(" LEFT JOIN FavoriteEntity f ON f.id.articleId = a.id ");

        if(StringUtils.hasText(favorited)) {
            query.append(" LEFT JOIN UserEntity fu ON fu.id = f.id.userId ");
            query.append(" AND fu.usernname = :favorited ) ");
        }
//        query.append(" GROUP BY   a.id, a.slug, a.title, a.description, a.body, a.createdAt, a.updatedAt  ");

        Query resultList = entityManager
                .createQuery(query.toString())
                .setFirstResult(plainQueryBuilder.getOffset(page, limit))
                .setMaxResults(limit);

        if(StringUtils.hasText(tag)) {
            resultList.setParameter("author", author);
        }

        if(StringUtils.hasText(tag)) {
            resultList.setParameter("tag", tag);
        }

        if(StringUtils.hasText(favorited)) {
            resultList.setParameter("favorited", favorited);
        }

        List<Object> list = resultList.getResultList();
        return null;
    }

    private List<ArticleEntity> fetchArticlesFavorite(List<ArticleEntity> resultList, String favorited) {
        StringBuilder favoritedCondition = new StringBuilder();

        if(StringUtils.hasText(favorited)) {
            favoritedCondition.append(" AND EXISTS (SELECT f FROM FavoriteEntity f WHERE f.user.username = :favorited AND f.id.articleId = articles.id) ");
        }

        TypedQuery<ArticleEntity> articleQuery = entityManager
                .createQuery("SELECT articles FROM ArticleEntity articles" +
                                " WHERE articles IN :articles " +
                                favoritedCondition,
                        ArticleEntity.class)
                .setParameter("articles", resultList);

        if(StringUtils.hasText(favorited)) {
            articleQuery.setParameter("favorited", favorited);
        }

        return articleQuery.getResultList();
    }

    private List<ArticleEntity> fetchArticlesTagList(String tag) {
        List<TagEntity> tagList = new ArrayList<>();
        StringBuilder tagCondition = new StringBuilder("");

        if(StringUtils.hasText(tag)) {
            tagList = fetchTagList(tag).getResultList();
            tagCondition.append(" WHERE articleTags.tag IN :tagList ");
        }

        TypedQuery<ArticleEntity> results = entityManager
                .createQuery("SELECT distinct articles FROM ArticleEntity articles" +
                                " LEFT JOIN fetch articles.articleTags articleTags " +
                                tagCondition,
                        ArticleEntity.class);

        if(!tagList.isEmpty()) {
            results.setParameter("tagList", tagList);
        }

        return results.getResultList();
    }

    private List<ArticleEntity> fetchArticlesAuthor(List<ArticleEntity> articles, String author) {
        StringBuilder authorCondition = new StringBuilder();

        if(StringUtils.hasText(author)) {
            authorCondition.append(" AND (author.username = :author)");
        }

        TypedQuery<ArticleEntity> articleQuery = entityManager
               .createQuery("SELECT distinct articles FROM ArticleEntity articles" +
                                " LEFT JOIN fetch articles.author author" +
                               " WHERE articles IN :articles " +
                                authorCondition,
                        ArticleEntity.class)
                .setParameter("articles", articles);

        if(StringUtils.hasText(author)) {
            articleQuery.setParameter("author", author);
        }

        return articleQuery.getResultList();
    }

    private TypedQuery<TagEntity> fetchTagList(String tag) {
        TypedQuery<TagEntity> tagQuery = entityManager
                .createQuery("""
                                SELECT distinct tags FROM TagEntity tags\
                                 LEFT JOIN fetch tags.articleTags articleTags \
                                 WHERE (tags.name = :tag )\
                                """,
                        TagEntity.class)
                .setParameter("tag", tag);

        return tagQuery;
    }
}
