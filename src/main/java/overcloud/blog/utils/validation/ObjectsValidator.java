package overcloud.blog.utils.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.context.MessageSource;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ApiError;
import overcloud.blog.response.ApiValidationError;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ObjectsValidator<T> {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final MessageSource messageSource;

    public ObjectsValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Optional<ApiError> validate(T objectToValidate) {
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);
        if (!violations.isEmpty()) {
            List<ApiValidationError> errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .map(this::failDetail)
                    .collect(Collectors.toList());

            return Optional.of(new ApiError(errors));
        }
        return Optional.empty();
    }

    public Optional<ApiError> addError(Optional<ApiError> error, String messageId) {
        if (error.isPresent()) {
            List<ApiValidationError> detail = error.get().getErrors();
            error.get().getErrors().add(this.failDetail(messageId));
            return Optional.of(ApiError.from(detail));
        } else {
            List<ApiValidationError> detail = new ArrayList<>();
            detail.add(this.failDetail(messageId));
            return Optional.of(new ApiError(detail));
        }
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
