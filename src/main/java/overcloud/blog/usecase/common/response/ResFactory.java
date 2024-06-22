package overcloud.blog.usecase.common.response;

import java.util.List;
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

    public <T> RestResponse<ApiError> fail(String messageId, ApiError data) {
        return RestResponse.<ApiError>builder()
            .code(messageId)
            .message(messageSource.getMessage(messageId, null, Locale.getDefault()))
            .data(data).build();
    }

    public <T> RestResponse<ApiError> fail(String messageId) {
        return RestResponse.<ApiError>builder()
            .code(messageId)
            .message(messageSource.getMessage(messageId, null, Locale.getDefault()))
            .build();
    }
}
