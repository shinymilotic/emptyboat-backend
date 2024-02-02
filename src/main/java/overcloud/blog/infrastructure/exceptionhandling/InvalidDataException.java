package overcloud.blog.infrastructure.exceptionhandling;


import overcloud.blog.infrastructure.validation.Error;

public class InvalidDataException extends RuntimeException {

    private ApiError apiError;

    public InvalidDataException(ApiError apiError) {
        this.apiError = apiError;
    }

    public InvalidDataException(Error error) {
        this.apiError = ApiError.from(error);
    }

    public ApiError getApiError() {
        return apiError;
    }

    public void setApiError(ApiError apiError) {
        this.apiError = apiError;
    }
}
