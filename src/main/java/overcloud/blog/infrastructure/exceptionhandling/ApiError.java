package overcloud.blog.infrastructure.exceptionhandling;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeName("apierror")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@NoArgsConstructor
public class ApiError {
    private String message;
                                                                                                                
    private List<ApiValidationError> apiErrorDetails;

    private ApiError(String message) {
        super();
        this.message = message;
        this.apiErrorDetails = new ArrayList<>();
    }

    // Factory method
    public static ApiError from(String message) {
        return new ApiError(message);
    }

    public void addApiValidationErrorDetail(ApiValidationError subError) {
        if (apiErrorDetails == null) {
            apiErrorDetails = new ArrayList<>();
        }
        apiErrorDetails.add(subError);
    }
}
