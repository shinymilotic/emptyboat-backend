package overcloud.blog.usecase.common.response;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.validation.ResMsg;

@Component
public class ExceptionFactory {

    private final MessageSource messageSource;

    public ExceptionFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public InvalidDataException invalidDataException(ResMsg resMsg, List<ApiValidationError> data) {
        String message = messageSource.getMessage(resMsg.getMessageId(), null, Locale.getDefault());
        RestResponse<ApiValidationError> response = RestResponse.<ApiValidationError>builder()
            .code(resMsg.getMessageId())
            .message(message)
            .data(data)
            .build();
        return new InvalidDataException(response);
    }
}
