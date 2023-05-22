package overcloud.blog.application.article.core.exception;


import overcloud.blog.infrastructure.exceptionhandling.ApiError;

public class InvalidDataException extends RuntimeException {

    private ApiError apiError;

    public InvalidDataException(ApiError apiError) {
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }

    public void setApiError(ApiError apiError) {
        this.apiError = apiError;
    }
}
