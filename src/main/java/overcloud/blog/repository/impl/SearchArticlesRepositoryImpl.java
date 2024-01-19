package overcloud.blog.repository.impl;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.infrastructure.datetime.DateTimeFormat;
import overcloud.blog.infrastructure.sql.PlainQueryBuilder;
import overcloud.blog.repository.SearchArticlesRepository;
import overcloud.blog.usecase.article.get_article_list.ArticleSummary;
import overcloud.blog.usecase.article.get_article_list.GetArticlesResponse;
import overcloud.blog.usecase.article.get_article_list.GetArticlesSingleResponse;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class SearchArticlesRepositoryImpl implements SearchArticlesRepository {

    private final EntityManager entityManager;

    private final PlainQueryBuilder plainQueryBuilder;

    public SearchArticlesRepositoryImpl(EntityManager entityManager, PlainQueryBuilder plainQueryBuilder) {
        this.entityManager = entityManager;
        this.plainQueryBuilder = plainQueryBuilder;
    }

    @Override
    public List<ArticleSummary> findByCriteria(UUID currentUserId, String tag, String author, String favorited, int limit, String lastArticleId) {
        StringBuilder query = new StringBuilder();
        query.append("select a.id, a.slug, a.title, a.description, a.body, t.name as tag, a.created_at as createdAt, fa.favorited, ");
        query.append(" fa.favoritesCount, author.username, author.bio, author.image, f1.following, f1.followersCount ");
        query.append("from ");
        query.append("(select articles.id, slug, body, title, description, created_at, author_id ");
        query.append("from articles ");
        query.append(ifTag("left join article_tag on articles.id = article_tag.article_id left join tags on tags.id = article_tag.tag_id", tag));
        StringBuilder articleWhereStatement = new StringBuilder();
        if (StringUtils.hasText(lastArticleId)) {
            articleWhereStatement.append("  articles.id < uuid(:lastArticleId) ");
        }
        articleWhereStatement.append(ifTag(operator(articleWhereStatement, " AND "), tag));
        articleWhereStatement.append(ifTag(" tags.name = :tag ", tag));
        query.append(operator(articleWhereStatement," WHERE "));
        query.append(articleWhereStatement);

        query.append(" ORDER BY articles.id DESC limit :limit) a ");
        query.append("left join users author on ");
        query.append("author.id = a.author_id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("f.followee_id, ");
        query.append("bool_or(f.follower_id = :currentUserId) as following, ");
        query.append("COUNT(f.follower_id) followersCount ");
        query.append("from ");
        query.append("follows f ");
        query.append("group by ");
        query.append("f.followee_id) f1 on ");
        query.append("f1.followee_id = author.id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("article_id , ");
        query.append("bool_or(user_id = :currentUserId) as favorited, ");
        query.append(ifFavorited("bool_or(fu.username = :favorited) as favoritedUser, ", favorited));
        query.append("COUNT(user_id) favoritesCount ");
        query.append("from ");
        query.append("favorites ");
        query.append(ifFavorited("left join (select id, username from users where username = :favorited) fu on fu.id = favorites.user_id ", favorited));
        query.append("group by ");
        query.append("article_id) fa on ");
        query.append("fa.article_id = a.id ");
        query.append("left join article_tag at2 on ");
        query.append("a.id = at2.article_id ");
        query.append("left join tags t on ");
        query.append("t.id = at2.tag_id ");

        StringBuilder whereStatement = new StringBuilder();
        whereStatement.append(ifAuthor(" author.username = :author ", author));
        whereStatement.append(ifFavorited(operator(whereStatement, " AND "), favorited));
        whereStatement.append(ifFavorited(" fa.favoritedUser = true", favorited));
        whereStatement.append(ifTag(operator(whereStatement, " AND "), tag));
        whereStatement.append(ifTag(" t.name = :tag ", tag));
        query.append(operator(whereStatement," WHERE "));
        query.append(whereStatement);
        query.append(" ORDER BY a.id DESC  ");

        Query resultList = entityManager.createNativeQuery(query.toString(), Tuple.class);
        resultList.setParameter("currentUserId", currentUserId);
        if (StringUtils.hasText(lastArticleId)) {
            resultList.setParameter("lastArticleId", lastArticleId);
        }
        resultList.setParameter("limit", limit);
        if(StringUtils.hasText(author)) {
            resultList.setParameter("author", author);
        }
        if(StringUtils.hasText(tag)) {
            resultList.setParameter("tag", tag);
        }
        if(StringUtils.hasText(favorited)) {
            resultList.setParameter("favorited", favorited);
        }

        List<Tuple> articlesData = resultList.getResultList();

        return toArticleSummaryList(articlesData);
    }

    @Override
    public List<ArticleSummary> findByIds(List<UUID> articles, UUID currentUserId, String lastArticleId) {
        StringBuilder articleIDs = new StringBuilder();
        for(UUID article : articles) {
            articleIDs.append(article.toString());
        }

        StringBuilder query = new StringBuilder();
        query.append("select a.id, a.slug, a.title, a.description, a.body, t.name as tag, a.created_at as createdAt, fa.favorited, ");
        query.append(" fa.favoritesCount, author.username, author.bio, author.image, f1.following, f1.followersCount ");
        query.append("from ");
        query.append("(select articles.id, slug, body, title, description, created_at, author_id ");
        query.append("from articles ");
        query.append(" WHERE id IN (:articleIDs) ");
        query.append(" ORDER BY id DESC ) a ");
        query.append("left join users author on ");
        query.append("author.id = a.author_id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("f.followee_id, ");
        query.append("bool_or(f.follower_id = :currentUserId) as following, ");
        query.append("COUNT(f.follower_id) followersCount ");
        query.append("from ");
        query.append("follows f ");
        query.append("group by ");
        query.append("f.followee_id) f1 on ");
        query.append("f1.followee_id = author.id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("article_id , ");
        query.append("bool_or(user_id = :currentUserId) as favorited, ");
        query.append("COUNT(user_id) favoritesCount ");
        query.append("from ");
        query.append("favorites ");
        query.append("group by ");
        query.append("article_id) fa on ");
        query.append("fa.article_id = a.id ");
        query.append("left join article_tag at2 on ");
        query.append("a.id = at2.article_id ");
        query.append("left join tags t on ");
        query.append("t.id = at2.tag_id ");
        query.append(" ORDER BY a.id DESC  ");

        Query resultList = entityManager.createNativeQuery(query.toString(), Tuple.class);
        resultList.setParameter("articleIDs", articles.get(0));
        resultList.setParameter("currentUserId", currentUserId);

        List<Tuple> articlesData = resultList.getResultList();

        return toArticleSummaryList(articlesData);
    }

    @Override
    public List<ArticleSummary> search(String keyword, UUID currentUserId, int limit, String lastArticleId) {
        StringBuilder query = new StringBuilder();
        query.append("select a.id, a.slug, a.title, a.description, a.body, t.name as tag, a.created_at as createdAt, fa.favorited, ");
        query.append(" fa.favoritesCount, author.username, author.bio, author.image, f1.following, f1.followersCount ");
        query.append("from ");
        query.append("(select articles.id, slug, body, title, description, created_at, author_id ");
        query.append("from articles ");
        query.append(" WHERE search_vector @@ to_tsquery('english', :keyword) ");
        if(StringUtils.hasText(lastArticleId)) {
            query.append(" AND id < uuid(:lastArticleId) ");
        }
        query.append(" ORDER BY id DESC ) a ");
        query.append("left join users author on ");
        query.append("author.id = a.author_id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("f.followee_id, ");
        query.append("bool_or(f.follower_id = :currentUserId) as following, ");
        query.append("COUNT(f.follower_id) followersCount ");
        query.append("from ");
        query.append("follows f ");
        query.append("group by ");
        query.append("f.followee_id) f1 on ");
        query.append("f1.followee_id = author.id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("article_id , ");
        query.append("bool_or(user_id = :currentUserId) as favorited, ");
        query.append("COUNT(user_id) favoritesCount ");
        query.append("from ");
        query.append("favorites ");
        query.append("group by ");
        query.append("article_id) fa on ");
        query.append("fa.article_id = a.id ");
        query.append("left join article_tag at2 on ");
        query.append("a.id = at2.article_id ");
        query.append("left join tags t on ");
        query.append("t.id = at2.tag_id ");
        query.append(" ORDER BY a.id DESC  ");

        Query resultList = entityManager.createNativeQuery(query.toString(), Tuple.class);
        if(StringUtils.hasText(lastArticleId)) {
            resultList.setParameter("lastArticleId", lastArticleId);
        }
        resultList.setParameter("keyword", keyword);
        resultList.setParameter("currentUserId", currentUserId);

        List<Tuple> articlesData = resultList.getResultList();

        return toArticleSummaryList(articlesData);
    }

    private List<ArticleSummary> toArticleSummaryList(List<Tuple> articlesData) {
        List<ArticleSummary> articleSummaries = new ArrayList<>();
        UUID previousArticleId = null;
        Map<UUID, ArticleSummary> articleSummaryMap = new HashMap<>();
        for (Tuple data : articlesData) {
            UUID articleId = (UUID) data.get("id");
            String tagName = (String) data.get("tag");
            if (articleId.equals(previousArticleId)) {
                articleSummaryMap.get(articleId).getTag().add((String) data.get("tag"));
            }

            ArticleSummary summary = new ArticleSummary();
            summary.setId((UUID) data.get("id"));
            summary.setSlug((String) data.get("slug"));
            summary.setTitle((String) data.get("title"));
            summary.setDescription((String) data.get("description"));
            summary.setBody((String) data.get("body"));
            List<String> tagList = new ArrayList<>();
            tagList.add(tagName);
            summary.setTag(tagList);
            summary.setCreatedAt((Timestamp) data.get("createdAt"));
            summary.setFavorited((Boolean) data.get("favorited"));
            summary.setFavoritesCount((Long) data.get("favoritesCount"));
            summary.setUsername((String) data.get("username"));
            summary.setBio((String) data.get("bio"));
            summary.setImage((String) data.get("image"));
            summary.setFollowing((Boolean)data.get("following"));
            summary.setFollowersCount((Long)data.get("followersCount"));
            articleSummaries.add(summary);
            articleSummaryMap.put(articleId, summary);
            previousArticleId = articleId;
        }
        return articleSummaries;
    }


    private String ifFavorited(String query, String favorited) {
        if (StringUtils.hasText(favorited)) {
            return query;
        }
        return "";
    }

    private String ifAuthor(String query, String author) {
        if (StringUtils.hasText(author)) {
            return query;
        }
        return "";
    }

    private String ifTag(String query, String tag) {
        if (StringUtils.hasText(tag)) {
            return query;
        }
        return "";
    }

    private String operator(StringBuilder st, String operator) {
        if (!st.isEmpty()) {
            return operator;
        }
        return "";
    }
}
