package overcloud.blog.usecase.common.exceptionhandling;



import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.common.response.ApiError;
import overcloud.blog.usecase.common.response.RestResponse;

@Getter
@Setter
public class InvalidDataException extends RuntimeException {

    private RestResponse<ApiError> response;

    public InvalidDataException(RestResponse<ApiError> response) {
        this.response = response;
    }
}
