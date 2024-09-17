package overcloud.blog.common.interceptors;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthStrategy {

    boolean auth(HttpServletRequest request);


}
