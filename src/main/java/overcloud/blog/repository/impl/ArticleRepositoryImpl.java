package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.repository.ArticleRepository;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.usecase.blog.common.ArticleSummary;
import overcloud.blog.usecase.blog.common.TagResponseSimple;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class ArticleRepositoryImpl implements ArticleRepository {
    private final JpaArticleRepository jpa;
    private final EntityManager entityManager;

    public ArticleRepositoryImpl(JpaArticleRepository jpa, EntityManager entityManager) {
        this.jpa = jpa;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<ArticleEntity> findById(UUID id) {
        return this.jpa.findById(id);
    }

    @Override
    public void updateSearchVector() {
        this.jpa.updateSearchVector();
    }

    @Override
    public void deleteById(UUID id) {
        this.jpa.deleteById(id);
    }

    @Override
    public Optional<Boolean> isTitleExist(String title) {
        return this.jpa.isTitleExist(title);
    }

    @Override
    public List<ArticleSummary> findBy(UUID currentUserId, UUID tagId, String author, String favorited, int limit, String lastArticleId) {
        StringBuilder query = new StringBuilder();
        query.append("select a.article_id, a.title, a.description, a.body, t.tag_id as tagId , t.name as tag, a.created_at as createdAt, fa.favorited, ");
        query.append(" fa.favoritesCount, author.username, author.bio, author.image, f1.following, f1.followersCount ");
        query.append("from ");
        query.append("(select articles.article_id, body, title, description, created_at, author_id ");
        query.append("from articles ");
        query.append("left join article_tags ON articles.article_id = article_tags.article_id ");
        query.append("inner join tag_follows tf ON tf.tag_id = article_tags.tag_id ");
        query.append("inner join tags on tags.tag_id = article_tags.tag_id ");
        query.append("where tf.follower_id = uuid(:currentUserId) ");

        StringBuilder articleWhereStatement = new StringBuilder();
        if (StringUtils.hasText(lastArticleId)) {
            articleWhereStatement.append("  articles.article_id < uuid(:lastArticleId) ");
        }
        articleWhereStatement.append(ifTag(operator(articleWhereStatement, " AND "), tagId));
        articleWhereStatement.append(ifTag(" tags.tag_id = :tag ", tagId));
        query.append(operator(articleWhereStatement, " AND "));
        query.append(articleWhereStatement);
        query.append(" GROUP BY articles.article_id ");

        query.append(" ORDER BY articles.article_id DESC ");
        query.append(" limit :limit) a ");
        query.append("left join users author on ");
        query.append("author.user_id = a.author_id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("f.followee_id, ");
        query.append("bool_or(f.follower_id = :currentUserId) as following, ");
        query.append("COUNT(f.follower_id) followersCount ");
        query.append("from ");
        query.append("follows f ");
        query.append("group by ");
        query.append("f.followee_id) f1 on ");
        query.append("f1.followee_id = author.user_id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("article_id , ");
        query.append("bool_or(favorites.user_id = :currentUserId) as favorited, ");
        query.append(ifFavorited("bool_or(fu.username = :favorited) as favoritedUser, ", favorited));
        query.append("COUNT(favorites.user_id) favoritesCount ");
        query.append("from ");
        query.append("favorites ");
        query.append(ifFavorited("left join (select user_id, username from users where username = :favorited) fu on fu.user_id = favorites.user_id ", favorited));
        query.append("group by ");
        query.append("article_id) fa on ");
        query.append("fa.article_id = a.article_id ");
        query.append("left join article_tags at2 on ");
        query.append("a.article_id = at2.article_id ");
        query.append("inner join tags t on ");
        query.append("t.tag_id = at2.tag_id ");

        StringBuilder whereStatement = new StringBuilder();
        whereStatement.append(ifAuthor(" author.username = :author ", author));
        whereStatement.append(ifFavorited(operator(whereStatement, " AND "), favorited));
        whereStatement.append(ifFavorited(" fa.favoritedUser = true", favorited));
        query.append(operator(whereStatement, " WHERE "));
        query.append(whereStatement);
        query.append(" ORDER BY a.article_id DESC  ");

        Query resultList = entityManager.createNativeQuery(query.toString(), Tuple.class);
        resultList.setParameter("currentUserId", currentUserId);
        if (StringUtils.hasText(lastArticleId)) {
            resultList.setParameter("lastArticleId", lastArticleId);
        }
        resultList.setParameter("limit", limit);
        if (StringUtils.hasText(author)) {
            resultList.setParameter("author", author);
        }
        if (tagId != null) {
            resultList.setParameter("tag", tagId);
        }
        if (StringUtils.hasText(favorited)) {
            resultList.setParameter("favorited", favorited);
        }

        List<Tuple> articlesData = resultList.getResultList();

        return toArticleSummaryList(articlesData);
    }

    @Override
    public ArticleSummary findArticleById(UUID articleId, UUID currentUserId) {
        String query = "select a.article_id, a.title, a.description, a.body, t.tag_id as tagId , t.name as tag, a.created_at as createdAt, fa.favorited, " +
                " fa.favoritesCount, author.username, author.bio, author.image, f1.following, f1.followersCount " +
                "from " +
                "(select articles.article_id, body, title, description, created_at, author_id " +
                "from articles " +
                " WHERE article_id = :articleId ) a " +
                "left join users author on " +
                "author.user_id = a.author_id " +
                "left join ( " +
                "select " +
                "f.followee_id, " +
                "bool_or(f.follower_id = :currentUserId) as following, " +
                "COUNT(f.follower_id) followersCount " +
                "from " +
                "follows f " +
                "group by " +
                "f.followee_id) f1 on " +
                "f1.followee_id = author.user_id " +
                "left join ( " +
                "select " +
                "article_id , " +
                "bool_or(user_id = :currentUserId) as favorited, " +
                "COUNT(favorites.user_id) favoritesCount " +
                "from " +
                "favorites " +
                "group by " +
                "article_id) fa on " +
                "fa.article_id = a.article_id " +
                "left join article_tags at2 on " +
                "a.article_id = at2.article_id " +
                "left join tags t on " +
                "t.tag_id = at2.tag_id " +
                " ORDER BY a.article_id DESC  ";

        Query resultList = entityManager.createNativeQuery(query, Tuple.class);
        resultList.setParameter("articleId", articleId);
        resultList.setParameter("currentUserId", currentUserId);

        List<Tuple> articlesData = resultList.getResultList();

        return toArticleSummary(articlesData);
    }

    @Override
    public List<ArticleSummary> search(String keyword, UUID currentUserId, int limit, String lastArticleId) {
        StringBuilder query = new StringBuilder();
        query.append("select a.article_id, a.title, a.description, a.body, t.tag_id as tagId , t.name as tag, a.created_at as createdAt, fa.favorited, ");
        query.append(" fa.favoritesCount, author.username, author.bio, author.image, f1.following, f1.followersCount ");
        query.append("from ");
        query.append("(select articles.article_id, body, title, description, created_at, author_id ");
        query.append("from articles ");
        query.append(" WHERE search_vector @@ to_tsquery('english', :keyword) ");
        if (StringUtils.hasText(lastArticleId)) {
            query.append(" AND article_id < uuid(:lastArticleId) ");
        }
        query.append(" ORDER BY article_id DESC  ");
        query.append(" limit :limit ) a ");
        query.append("left join users author on ");
        query.append("author.user_id = a.author_id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("f.followee_id, ");
        query.append("bool_or(f.follower_id = :currentUserId) as following, ");
        query.append("COUNT(f.follower_id) followersCount ");
        query.append("from ");
        query.append("follows f ");
        query.append("group by ");
        query.append("f.followee_id) f1 on ");
        query.append("f1.followee_id = author.user_id ");
        query.append("left join ( ");
        query.append("select ");
        query.append("article_id , ");
        query.append("bool_or(favorites.user_id = :currentUserId) as favorited, ");
        query.append("COUNT(favorites.user_id) favoritesCount ");
        query.append("from ");
        query.append("favorites ");
        query.append("group by ");
        query.append("article_id) fa on ");
        query.append("fa.article_id = a.article_id ");
        query.append("left join article_tags at2 on ");
        query.append("a.article_id = at2.article_id ");
        query.append("left join tags t on ");
        query.append("t.tag_id = at2.tag_id ");
        query.append(" ORDER BY a.article_id DESC  ");

        Query resultList = entityManager.createNativeQuery(query.toString(), Tuple.class);
        if (StringUtils.hasText(lastArticleId)) {
            resultList.setParameter("lastArticleId", lastArticleId);
        }
        resultList.setParameter("limit", limit);
        resultList.setParameter("keyword", keyword);
        resultList.setParameter("currentUserId", currentUserId);

        List<Tuple> articlesData = resultList.getResultList();

        return toArticleSummaryList(articlesData);
    }


    private ArticleSummary toArticleSummary(List<Tuple> articlesData) {
        ArticleSummary summary = new ArticleSummary();
        UUID previousArticleId = null;
        Map<UUID, ArticleSummary> articleSummaryMap = new HashMap<>();
        for (Tuple data : articlesData) {
            UUID articleId = (UUID) data.get("article_id");
            UUID tagId = (UUID) data.get("tagId");
            String tagName = (String) data.get("tag");
            TagResponseSimple tag = new TagResponseSimple();
            tag.setId(tagId.toString());
            tag.setName(tagName);
            if (articleId.equals(previousArticleId)) {
                articleSummaryMap.get(articleId).getTag().add(tag);
                continue;
            }

            summary.setId(articleId);
            summary.setTitle((String) data.get("title"));
            summary.setDescription((String) data.get("description"));
            summary.setBody((String) data.get("body"));
            List<TagResponseSimple> tagList = new ArrayList<>();
            tagList.add(tag);
            summary.setTag(tagList);
            summary.setCreatedAt((Timestamp) data.get("createdAt"));
            summary.setFavorited((Boolean) data.get("favorited"));
            summary.setFavoritesCount((Long) data.get("favoritesCount"));
            summary.setUsername((String) data.get("username"));
            summary.setBio((String) data.get("bio"));
            summary.setImage((String) data.get("image"));
            summary.setFollowing((Boolean) data.get("following"));
            summary.setFollowersCount((Long) data.get("followersCount"));
            articleSummaryMap.put(articleId, summary);
            previousArticleId = articleId;
        }
        return summary;
    }

    private List<ArticleSummary> toArticleSummaryList(List<Tuple> articlesData) {
        List<ArticleSummary> articleSummaries = new ArrayList<>();
        UUID previousArticleId = null;
        Map<UUID, List<TagResponseSimple>> articleSummaryMap = new HashMap<>();
        for (Tuple data : articlesData) {
            UUID articleId = (UUID) data.get("article_id");
            UUID tagId = (UUID) data.get("tagId");
            String tagName = (String) data.get("tag");
            TagResponseSimple tag = new TagResponseSimple();
            tag.setId(tagId.toString());
            tag.setName(tagName);

            if (articleId.equals(previousArticleId)) {
                articleSummaryMap.get(articleId).add(tag);
                continue;
            } else {
                List<TagResponseSimple> tagList = new ArrayList<>();
                tagList.add(tag);
                articleSummaryMap.put(articleId, tagList);
            }

            ArticleSummary summary = new ArticleSummary();
            summary.setId(articleId);
            summary.setTitle((String) data.get("title"));
            summary.setDescription((String) data.get("description"));
            summary.setBody((String) data.get("body"));
            summary.setTag(articleSummaryMap.get(articleId));
            summary.setCreatedAt((Timestamp) data.get("createdAt"));
            summary.setFavorited((Boolean) data.get("favorited"));
            summary.setFavoritesCount((Long) data.get("favoritesCount"));
            summary.setUsername((String) data.get("username"));
            summary.setBio((String) data.get("bio"));
            summary.setImage((String) data.get("image"));
            summary.setFollowing((Boolean) data.get("following"));
            summary.setFollowersCount((Long) data.get("followersCount"));
            articleSummaries.add(summary);
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

    private String ifTag(String query, UUID tagId) {
        if (tagId != null) {
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

    @Override
    public void save(ArticleEntity articleEntity) {
        this.jpa.save(articleEntity);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpa.deleteByUserId(userId);
    }
}
