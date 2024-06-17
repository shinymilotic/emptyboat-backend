package overcloud.blog.usecase.common.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.core.validation.ResMsg;

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

    public static ApiErrorDetail from(ResMsg error) {
        ApiErrorDetail apiValidationError = new ApiErrorDetail();
        apiValidationError.setId(error.getMessageId());
        apiValidationError.setMessage(error.getMessage());

        return apiValidationError;
    }
}