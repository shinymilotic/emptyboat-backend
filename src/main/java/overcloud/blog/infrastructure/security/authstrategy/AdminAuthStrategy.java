package overcloud.blog.infrastructure.security.authstrategy;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminAuthStrategy implements AuthStrategy {

    private static final Map<String, Set<String>> EXCLUDED_RESOURCE = Map.of(
    );

    @Override
    public void auth(HttpServletRequest request) {
    }
}
