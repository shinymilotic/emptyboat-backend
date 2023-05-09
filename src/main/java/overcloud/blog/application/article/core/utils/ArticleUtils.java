package overcloud.blog.application.article.core.utils;

import org.springframework.util.StringUtils;

public class ArticleUtils {
    public static String toSlug(String title) {
        String slug = "";

        if(StringUtils.hasText(title)) {
            slug = title.toLowerCase();
            slug = slug.replaceAll("[^a-z0-9\\s-]", "");
            slug = slug.replaceAll("\\s+", "-");
        }

        return slug;
    }
}
