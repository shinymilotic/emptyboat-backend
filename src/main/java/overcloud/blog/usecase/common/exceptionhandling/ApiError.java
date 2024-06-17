package overcloud.blog.usecase.common.exceptionhandling;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.core.validation.ResMsg;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiError {

    @JsonProperty("errors")
    private List<ApiErrorDetail> apiErrorDetails;

    public ApiError() {
        super();
        this.apiErrorDetails = new ArrayList<>();
    }

    public static ApiError from(ResMsg error) {
        ApiErrorDetail apiErrorDetail = new ApiErrorDetail();
        apiErrorDetail.setId(error.getMessageId());
        apiErrorDetail.setMessage(error.getMessage());
        ApiError apiError = new ApiError();
        apiError.getApiErrorDetails().add(apiErrorDetail);
        return apiError;
    }

    public static ApiError from(List<ApiErrorDetail> errorDetail) {
        if (errorDetail == null) {
            errorDetail = new ArrayList<>();
        }
        ApiError apiError = new ApiError();
        apiError.setApiErrorDetails(errorDetail);
        return apiError;
    }

    public void addApiErrorDetail(ApiErrorDetail subError) {
        if (apiErrorDetails == null) {
            apiErrorDetails = new ArrayList<>();
        }
        apiErrorDetails.add(subError);
    }
}
