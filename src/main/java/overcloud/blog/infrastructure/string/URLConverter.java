package overcloud.blog.infrastructure.string;

public class URLConverter {
    public static String toSlug(String title) {
        String slug = title.toLowerCase();
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");
        slug = slug.replaceAll("\\s+", "-");
        return slug;
    }
}
