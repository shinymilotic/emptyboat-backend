package overcloud.blog.infrastructure.exceptionhandling.dto;

import overcloud.blog.infrastructure.LowerCaseClassNameResolver;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ApiError {
    private HttpStatus status;
    private String message;

    private List<ApiErrorDetail> apiErrorDetails;

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
        this.apiErrorDetails = new ArrayList<>();
    }


    private void addApiErrorDetail(ApiErrorDetail subError) {
        if (apiErrorDetails == null) {
            apiErrorDetails = new ArrayList<>();
        }
        apiErrorDetails.add(subError);
    }
}
