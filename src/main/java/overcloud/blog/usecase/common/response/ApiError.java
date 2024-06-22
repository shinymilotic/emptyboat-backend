package overcloud.blog.usecase.common.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
    private List<ApiValidationError> errors;

    public ApiError(List<ApiValidationError> data) {
        this.errors = data;
    }

    public ApiError() {
        //TODO Auto-generated constructor stub
    }

    public static ApiError from(List<ApiValidationError> data) {
        return new ApiError(data);
    }
}
