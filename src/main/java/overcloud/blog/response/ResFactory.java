package overcloud.blog.response;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ResFactory {
    private final MessageSource messageSource;

    public ResFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public ApiError fail(String messageId) {
        String message = messageSource.getMessage(messageId, null, Locale.getDefault());

        return ApiError.from(messageId, message);
    }

    public ApiValidationError failDetail(String messageId) {
        String message = messageSource.getMessage(messageId, null, Locale.getDefault());

        return new ApiValidationError(messageId, message);
    }
}
