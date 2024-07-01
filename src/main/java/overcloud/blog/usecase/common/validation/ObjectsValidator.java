package overcloud.blog.usecase.common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import overcloud.blog.usecase.common.response.ApiValidationError;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import overcloud.blog.usecase.common.response.ApiError;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
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
                    .map((id) -> new ApiValidationError(id, messageSource.getMessage(id, null, Locale.getDefault())))
                    .collect(Collectors.toList());

            return Optional.of(new ApiError(errors));
        }

        return Optional.empty();
    }
}
