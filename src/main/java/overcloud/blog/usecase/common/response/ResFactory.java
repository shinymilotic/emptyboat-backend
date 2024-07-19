package overcloud.blog.usecase.common.response;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ResFactory {

    private final MessageSource messageSource;

    public ResFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public <T>RestResponse<T> success(String messageId, T data) {
        return RestResponse.<T>builder()
            .code(messageId)
            .message(messageSource.getMessage(messageId, null, Locale.getDefault()))
            .data(data).build();
    }

    public <T> ApiError fail(String messageId) {
        String message = messageSource.getMessage(messageId, null, Locale.getDefault());

        return ApiError.from(messageId, message);
    }

    public <T> ApiValidationError failDetail(String messageId) {
        String message = messageSource.getMessage(messageId, null, Locale.getDefault());

        return new ApiValidationError(messageId, message);
    }
}
