package overcloud.blog.infrastructure.exceptionhandling.dto;

import jakarta.validation.ConstraintViolation;
import lombok.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiErrorDetail {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public ApiValidationError() {
    }

    public ApiValidationError(String object, String message) {
        super();
    }

    public static ApiValidationError addValidationError(String object, String field, Object rejectedValue, String message) {
        ApiValidationError apiValidationError = new ApiValidationError();
        apiValidationError.setObject(object);
        apiValidationError.setField(field);
        apiValidationError.setRejectedValue(rejectedValue);
        apiValidationError.setMessage(message);

        return apiValidationError;
    }

    public static ApiValidationError addValidationError(String object, String message) {
        ApiValidationError apiValidationError = new ApiValidationError();
        apiValidationError.setObject(object);
        apiValidationError.setMessage(message);

        return apiValidationError;
    }

    public static ApiValidationError addValidationError(FieldError fieldError) {
        ApiValidationError apiValidationError = new ApiValidationError();
        apiValidationError.setObject(fieldError.getObjectName());
        apiValidationError.setField(fieldError.getField());
        apiValidationError.setRejectedValue(fieldError.getRejectedValue());
        apiValidationError.setMessage(fieldError.getDefaultMessage());

        return apiValidationError;

    }

    public static List<ApiValidationError> addValidationErrors(List<FieldError> fieldErrors) {
        List<ApiValidationError> apiValidationErrors = new ArrayList<>();
        for (FieldError fieldError: fieldErrors) {
            ApiValidationError apiValidationError = addValidationError(fieldError);
            apiValidationErrors.add(apiValidationError);
        }

        return apiValidationErrors;
    }

    private static ApiValidationError addValidationError(ObjectError objectError) {

        return ApiValidationError.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public static List<ApiValidationError> addValidationError(List<ObjectError> globalErrors) {
        List<ApiValidationError> apiValidationErrors = new ArrayList<>();

        for (ObjectError globalError: globalErrors) {
            ApiValidationError apiValidationError = addValidationError(globalError);
            apiValidationErrors.add(apiValidationError);
        }

        return apiValidationErrors;
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private static ApiValidationError addValidationError(ConstraintViolation<?> cv) {
        return ApiValidationError.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                cv.getPropertyPath().toString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }

    public static List<ApiValidationError> addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        List<ApiValidationError> apiValidationErrors = new ArrayList<>();

        for (ConstraintViolation constraint : constraintViolations) {
            apiValidationErrors.add(ApiValidationError.addValidationError(constraint));
        }

        return apiValidationErrors;
    }

}