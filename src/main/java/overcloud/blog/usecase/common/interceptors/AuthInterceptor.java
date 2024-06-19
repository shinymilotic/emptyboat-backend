package overcloud.blog.usecase.common.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import overcloud.blog.entity.RoleEntity;
import overcloud.blog.usecase.common.auth.AuthResMsg;
import overcloud.blog.usecase.common.exceptionhandling.ApiError;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;

import java.util.List;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final Map<String, AuthStrategy> authStrategy;

    public AuthInterceptor(Map<String, AuthStrategy> authStrategy) {
        this.authStrategy = authStrategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuth = false;
        if (authentication != null && !"anonymousUser".equals(authentication.getPrincipal())) {
            List<RoleEntity> roleEntities = (List<RoleEntity>) authentication.getAuthorities();

            for (RoleEntity role : roleEntities) {
                String authority = role.getAuthority();

                switch (authority) {
                    case "ADMIN" -> {
                        if (authStrategy.get("adminAuthStrategy").auth(request)) {
                            isAuth = true;
                        }
                    }
                    case "USER" -> {
                        if (authStrategy.get("frontAuthStrategy").auth(request)) {
                            isAuth = true;
                        }
                    }
                }
            }
        } else {
            authStrategy.get("guestAuthStrategy").auth(request);
            isAuth = true;
        }

        if (!isAuth) {
            throw new InvalidDataException(ApiError.from(AuthResMsg.AUTHORIZE_FAILED));
        }

        return isAuth;
    }

}
