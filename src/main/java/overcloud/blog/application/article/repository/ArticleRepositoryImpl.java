package overcloud.blog.application.article.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import overcloud.blog.application.article.ArticleEntity;
import overcloud.blog.application.tag.TagEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<ArticleEntity> findByCriteria(String tag, String author, String favorited, int limit, int page, String searchParam) {
        List<ArticleEntity> resultList = new ArrayList<>();

        resultList = fetchArticlesTagList(tag);
        resultList = fetchArticlesAuthor(resultList, author);
        resultList = fetchArticlesFavorite(resultList, favorited);

        if(StringUtils.hasText(searchParam)) {
            resultList = fetchBySearchParam(resultList, searchParam).getResultList();
        }

        resultList = entityManager
                .createQuery("SELECT articles FROM ArticleEntity articles" +
                                " WHERE articles IN :articles ",
                        ArticleEntity.class)
                .setParameter("articles", resultList)
                .setFirstResult(getOffset(page, limit))
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }

    private Query fetchBySearchParam(List<ArticleEntity> resultList, String searchParam) {
        StringBuilder articleQueryStmt = new StringBuilder();

        if(!resultList.isEmpty()) {
            StringBuilder parameterStr = new StringBuilder("(:?0");
            IntStream.range(1, resultList.size()).forEach(i -> parameterStr.append(",:?" + i));
            parameterStr.append(")");
            articleQueryStmt.append(" AND articles.id IN ");
            articleQueryStmt.append(parameterStr);
        }

        Query articleQuery = entityManager
                .createNativeQuery("SELECT articles.* FROM articles" +
                                " WHERE concat(articles.body, ' ', articles.title, ' ', articles.description) @@ to_tsquery(:searchParam) " +
                                articleQueryStmt,
                        ArticleEntity.class);

        IntStream.range(0, resultList.size()).forEach(index -> {
            String parameterName = "?" + index;
            articleQuery.setParameter(parameterName, resultList.get(index).getId());
        });

        if (StringUtils.hasText(searchParam)) {
            articleQuery.setParameter("searchParam", searchParam);
        }

        return articleQuery;
    }

    private List<ArticleEntity> fetchArticlesFavorite(List<ArticleEntity> resultList, String favorited) {
        StringBuilder favoritedCondition = new StringBuilder();

        if(StringUtils.hasText(favorited)) {
            favoritedCondition.append(" AND favorites.user.username = :favorited ");
        }

        TypedQuery<ArticleEntity> articleQuery = entityManager
                .createQuery("SELECT articles FROM ArticleEntity articles" +
                                " LEFT JOIN fetch articles.favorites favorites " +
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

    private int getOffset(int page, int limit) {
        int offset = limit*(page-1);

        return offset;
    }

}
