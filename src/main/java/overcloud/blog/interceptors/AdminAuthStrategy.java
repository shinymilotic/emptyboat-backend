package overcloud.blog.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminAuthStrategy implements AuthStrategy {

    private static final Map<String, Set<String>> EXCLUDED_RESOURCE = Map.of(
    );

    @Override
    public boolean auth(HttpServletRequest request) {
        return true;
    }
}
