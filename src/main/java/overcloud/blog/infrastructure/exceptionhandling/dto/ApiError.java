package overcloud.blog.infrastructure.exceptionhandling.dto;

import overcloud.blog.infrastructure.LowerCaseClassNameResolver;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ApiError {
    private String message;
                                                                                                                
    private List<ApiErrorDetail> apiErrorDetails;

    private ApiError(String message) {
        super();
        this.message = message;
        this.apiErrorDetails = new ArrayList<>();
    }

    // Factory method
    public static ApiError from(String message) {
        return new ApiError(message);
    }
    public void addApiErrorDetail(ApiErrorDetail subError) {
        if (apiErrorDetails == null) {
            apiErrorDetails = new ArrayList<>();
        }
        apiErrorDetails.add(subError);
    }

    public void addApiValidationErrorDetail(ApiValidationError subError) {
        if (apiErrorDetails == null) {
            apiErrorDetails = new ArrayList<>();
        }
        apiErrorDetails.add(subError);
    }
}
