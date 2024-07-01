package overcloud.blog.usecase.common.exceptionhandling;



import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.common.response.ApiError;

@Getter
@Setter
public class InvalidDataException extends RuntimeException {
    private ApiError response;

    public InvalidDataException(ApiError response) {
        this.response = response;
    }
}
