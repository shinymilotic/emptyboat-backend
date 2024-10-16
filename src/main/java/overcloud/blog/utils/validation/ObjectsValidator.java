package overcloud.blog.utils.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import overcloud.blog.response.ApiError;
import overcloud.blog.response.ApiValidationError;
import overcloud.blog.response.ResFactory;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectsValidator<T> {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final ResFactory resFactory;

    public ObjectsValidator(ResFactory resFactory) {
        this.resFactory = resFactory;
    }

    public Optional<ApiError> validate(T objectToValidate) {
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);
        if (!violations.isEmpty()) {

            List<ApiValidationError> errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .map(resFactory::failDetail)
                    .collect(Collectors.toList());

            return Optional.of(new ApiError(errors));
        }

        return Optional.empty();
    }

    public Optional<ApiError> addError(Optional<ApiError> error, String messageId) {
        if (error.isPresent()) {
            List<ApiValidationError> detail = error.get().getErrors();
            for (ApiValidationError apiValidationError : detail) {
                if (messageId.equals(apiValidationError.getMessageId())) {
                    return error;
                }
            }
            error.get().getErrors().add(resFactory.failDetail(messageId));
            return Optional.of(ApiError.from(detail));
        } else {
            List<ApiValidationError> detail = new ArrayList<>();
            detail.add(resFactory.failDetail(messageId));
            return Optional.of(new ApiError(detail));
        }
    }
}
