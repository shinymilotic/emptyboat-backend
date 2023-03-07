package overcloud.blog.application.user.exception.login;

import lombok.val;
import org.springframework.http.HttpStatus;
import overcloud.blog.infrastructure.exceptionhandling.Error;

public enum LoginError {

    LOGIN_INFO_INVALID("Login information is invalid");

    private String message;

    LoginError(String message) {
        this.message  = message;
    }

    public String getValue() {
        return message;
    }
}
