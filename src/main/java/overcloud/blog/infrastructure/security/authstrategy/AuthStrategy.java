package overcloud.blog.infrastructure.security.authstrategy;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthStrategy {

    void auth(HttpServletRequest request);
}
