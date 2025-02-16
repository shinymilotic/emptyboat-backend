package overcloud.blog.response;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import overcloud.blog.exception.InvalidDataException;

@Component
public class ResFactory {
    private final MessageSource messageSource;

    public ResFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public InvalidDataException fail(String messageId) {
        String message = messageSource.getMessage(messageId, null, Locale.getDefault());

        ApiError error = ApiError.from(messageId, message);

        return new InvalidDataException(error);
    }

    public ApiValidationError failDetail(String messageId) {
        String message = messageSource.getMessage(messageId, null, Locale.getDefault());

        return new ApiValidationError(messageId, message);
    }
}
