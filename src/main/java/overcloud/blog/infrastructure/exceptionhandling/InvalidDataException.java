package overcloud.blog.infrastructure.exceptionhandling;


import overcloud.blog.infrastructure.validation.Error;

import java.util.List;

public class InvalidDataException extends RuntimeException {

    private ApiError apiError;

    public InvalidDataException(ApiError apiError) {
        this.apiError = apiError;
    }

    public InvalidDataException(Error error) {
        this.apiError = ApiError.from(error);
    }

    public InvalidDataException(List<Error> errors) {
        this.apiError = new ApiError();
        for (Error error : errors) {
            this.apiError.addApiErrorDetail(ApiErrorDetail.from(error));
        }
    }

    public ApiError getApiError() {
        return apiError;
    }

    public void setApiError(ApiError apiError) {
        this.apiError = apiError;
    }
}
