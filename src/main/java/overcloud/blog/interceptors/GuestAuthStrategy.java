package overcloud.blog.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import overcloud.blog.controller.ApiConst;

import java.util.Map;
import java.util.Set;

@Component
public class GuestAuthStrategy implements AuthStrategy {
    private static final Map<String, Set<String>> EXCLUDED_RESOURCE = Map.ofEntries(
            Map.entry(ApiConst.ARTICLES, Set.of("POST")),
            Map.entry(ApiConst.ARTICLE, Set.of("PUT", "DELETE")),
            Map.entry(ApiConst.ARTICLE_COMMENTS, Set.of("POST")),
            Map.entry(ApiConst.ARTICLE_ID_FAVORITE, Set.of("DELETE", "POST")),
            Map.entry(ApiConst.ROLES_USERNAME, Set.of("GET")),
            Map.entry(ApiConst.ROLES, Set.of("GET", "PUT")),
            Map.entry(ApiConst.TAGS, Set.of("POST")),
            Map.entry(ApiConst.CURRENT_USER, Set.of("PUT", "GET")),
            Map.entry(ApiConst.USER_LIST, Set.of("GET")),
            Map.entry(ApiConst.USERS_LOGOUT, Set.of("POST")),
            Map.entry(ApiConst.USERS_USERNAME_ASSIGNMENT, Set.of("PUT")),
            Map.entry(ApiConst.PROFILES_USERNAME_FOLLOW, Set.of("POST", "DELETE")),
            Map.entry(ApiConst.PROFILES_USERNAME, Set.of("GET"))
    );

    @Override
    public boolean auth(HttpServletRequest request) {
        String urlTemplate = (String) request.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String method = request.getMethod();

        if (EXCLUDED_RESOURCE.containsKey(urlTemplate)) {
            Set<String> excludedUrl = EXCLUDED_RESOURCE.get(urlTemplate);
            return excludedUrl == null || !excludedUrl.contains(method);
        }

        return true;
    }
}
