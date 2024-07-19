package overcloud.blog.usecase.common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import overcloud.blog.usecase.common.response.ApiValidationError;
import overcloud.blog.usecase.common.response.ResFactory;
import org.springframework.stereotype.Component;
import overcloud.blog.usecase.common.response.ApiError;
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
            detail.add(resFactory.failDetail(messageId));
            error.get().getErrors().add(resFactory.failDetail(messageId));
            return Optional.of(ApiError.from(detail));
        } else {
            return Optional.of(new ApiError(List.of(resFactory.failDetail(messageId))));
        }
    }
}
