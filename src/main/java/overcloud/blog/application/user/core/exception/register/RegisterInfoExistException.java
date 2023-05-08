package overcloud.blog.application.user.core.exception.register;

import overcloud.blog.infrastructure.exceptionhandling.ApiError;

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