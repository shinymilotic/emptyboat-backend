package overcloud.blog.application.article.core.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.infrastructure.sql.PlainQueryBuilder;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SearchArticlesRepositoryImpl implements SearchArticlesRepository {

    @PersistenceContext
    EntityManager entityManager;

    private final PlainQueryBuilder plainQueryBuilder;

    public SearchArticlesRepositoryImpl(PlainQueryBuilder plainQueryBuilder) {
        this.plainQueryBuilder = plainQueryBuilder;
    }

    @Override
    public List<ArticleEntity> findByCriteria(String tag, String author, String favorited, int limit, int page) {
        List<ArticleEntity> resultList = new ArrayList<>();

        resultList = fetchArticlesTagList(tag);
        resultList = fetchArticlesAuthor(resultList, author);
        resultList = fetchArticlesFavorite(resultList, favorited);

        resultList = entityManager
                .createQuery("SELECT articles FROM ArticleEntity articles" +
                                " WHERE articles IN :articles ",
                        ArticleEntity.class)
                .setParameter("articles", resultList)
                .setFirstResult(plainQueryBuilder.getOffset(page, limit))
                .setMaxResults(limit)
                .getResultList();

        return resultList;
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
                .createQuery("SELECT distinct tags FROM TagEntity tags" +
                                " LEFT JOIN fetch tags.articleTags articleTags " +
                                " WHERE (tags.name = :tag )",
                        TagEntity.class)
                .setParameter("tag", tag);

        return tagQuery;
    }
}
