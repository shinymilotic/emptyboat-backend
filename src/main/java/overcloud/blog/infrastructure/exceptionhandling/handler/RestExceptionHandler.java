package overcloud.blog.infrastructure.exceptionhandling.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import overcloud.blog.application.user.service.impl.RegisterInfoExistException;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiErrorDetail;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiValidationError;
import overcloud.blog.infrastructure.exceptionhandling.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = "Validation error";
        ApiError apiError = new ApiError(BAD_REQUEST, message);
        List<ApiErrorDetail> apiErrorDetails = apiError.getApiErrorDetails();
        List<ApiValidationError> fieldErrors = ApiValidationError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        List<ApiValidationError> globalErrors = ApiValidationError.addValidationError(ex.getBindingResult().getGlobalErrors());

        fieldErrors.forEach(error -> apiErrorDetails.add(error));
        globalErrors.forEach(error -> apiErrorDetails.add(error));

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {
        String message = "Validation error";
        ApiError apiError = new ApiError(BAD_REQUEST, message);
        List<ApiErrorDetail> apiErrorDetails = apiError.getApiErrorDetails();
        List<ApiValidationError> apiValidationError = ApiValidationError.addValidationErrors(ex.getConstraintViolations());
        apiValidationError.forEach(error -> apiErrorDetails.add(error));

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(RegisterInfoExistException.class)
    protected ResponseEntity<Object> handleRegisterInfoExist(RegisterInfoExistException ex) {
        return buildResponseEntity(ex.getApiError());
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}

