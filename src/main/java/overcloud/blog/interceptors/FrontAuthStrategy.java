package overcloud.blog.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.ApiConst;
import overcloud.blog.infrastructure.AuthError;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FrontAuthStrategy implements AuthStrategy {

    private static final Map<String, Set<String>> EXCLUDED_RESOURCE = Map.ofEntries(
            Map.entry(ApiConst.ROLES, Set.of("POST", "PUT", "GET", "DELETE")),
            Map.entry(ApiConst.ROLES_USERNAME, Set.of("POST", "PUT", "GET", "DELETE")),
            Map.entry(ApiConst.USERS_USERNAME_ASSIGNMENT, Set.of("POST", "PUT", "GET", "DELETE")),
            Map.entry(ApiConst.USERS_LOGIN_ADMIN, Set.of("POST"))
    );
    @Override
    public void auth(HttpServletRequest request) {
        String urlTemplate = (String) request.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String method = request.getMethod();

        if (EXCLUDED_RESOURCE.containsKey(urlTemplate)) {
            Set<String> u = EXCLUDED_RESOURCE.get(urlTemplate);
            if(u != null && u.contains(method)) {
                throw new InvalidDataException(ApiError.from(AuthError.AUTHORIZE_FAILED));
            }
        }
    }
}
