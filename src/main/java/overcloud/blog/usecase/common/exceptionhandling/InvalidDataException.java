package overcloud.blog.usecase.common.exceptionhandling;


import java.util.List;

import overcloud.blog.core.validation.ResMsg;

public class InvalidDataException extends RuntimeException {

    private ApiError apiError;

    public InvalidDataException(ApiError apiError) {
        this.apiError = apiError;
    }

    public InvalidDataException(ResMsg error) {
        this.apiError = ApiError.from(error);
    }

    public InvalidDataException(List<ResMsg> errors) {
        this.apiError = new ApiError();
        for (ResMsg error : errors) {
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
