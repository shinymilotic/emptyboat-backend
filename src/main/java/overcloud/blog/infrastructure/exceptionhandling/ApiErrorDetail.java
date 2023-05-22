package overcloud.blog.infrastructure.exceptionhandling;

import lombok.*;

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
}