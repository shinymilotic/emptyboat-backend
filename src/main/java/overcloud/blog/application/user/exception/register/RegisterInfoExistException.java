package overcloud.blog.application.user.exception.register;

import overcloud.blog.infrastructure.exceptionhandling.dto.ApiError;

import java.util.List;

public class RegisterInfoExistException extends RuntimeException {

    private ApiError apiError;

    public RegisterInfoExistException(ApiError apiError) {
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }

    public void setApiError(ApiError apiError) {
        this.apiError = apiError;
    }
}