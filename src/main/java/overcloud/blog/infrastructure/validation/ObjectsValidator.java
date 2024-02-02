package overcloud.blog.infrastructure.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;

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
            return Optional.of(ApiError.from(violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .map((id) -> ApiErrorDetail.from(id, messageSource.getMessage(id, null, Locale.getDefault())))
                    .collect(Collectors.toList())));
        }

        return Optional.empty();
    }
}
