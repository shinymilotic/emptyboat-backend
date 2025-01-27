package overcloud.blog.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiError {
    private List<ApiValidationError> errors;

    public ApiError(List<ApiValidationError> data) {
        this.errors = data;
    }

    public static ApiError from(List<ApiValidationError> data) {
        return new ApiError(data);
    }

    public static ApiError from(String id, String message) {
        return new ApiError(List.of(new ApiValidationError(id, message)));
    }
}
