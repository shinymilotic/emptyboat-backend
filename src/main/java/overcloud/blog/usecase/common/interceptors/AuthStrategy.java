package overcloud.blog.usecase.common.interceptors;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthStrategy {

    boolean auth(HttpServletRequest request);


}
