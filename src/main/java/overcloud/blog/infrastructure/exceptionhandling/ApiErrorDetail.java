package overcloud.blog.infrastructure.exceptionhandling;

import lombok.*;
import overcloud.blog.infrastructure.validation.Error;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorDetail {
    private String id;
    private String message;

    public ApiErrorDetail() {
    }

    public static ApiErrorDetail from(String id, String message) {
        ApiErrorDetail apiValidationError = new ApiErrorDetail();
        apiValidationError.setId(id);
        apiValidationError.setMessage(message);

        return apiValidationError;
    }

    public static ApiErrorDetail from(Error error) {
        ApiErrorDetail apiValidationError = new ApiErrorDetail();
        apiValidationError.setId(error.getMessageId());
        apiValidationError.setMessage(error.getErrorMessage());

        return apiValidationError;
    }
}