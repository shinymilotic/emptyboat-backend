package overcloud.blog.application.article.core.exception;

import overcloud.blog.infrastructure.exceptionhandling.ApiError;

public class WriteArticleException extends RuntimeException {
    private ApiError apiError;

    public WriteArticleException(ApiError apiError) {
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }

    public void setApiError(ApiError apiError) {
        this.apiError = apiError;
    }
}
