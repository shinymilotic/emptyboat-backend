package overcloud.blog.usecase.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiValidationError {
    private String messageId;
    private String message;

    public static ApiValidationError from(String messageId, String message) {
        ApiValidationError apiValidationError = new ApiValidationError();
        apiValidationError.setMessageId(messageId);
        apiValidationError.setMessage(message);
        return apiValidationError;
    }
}