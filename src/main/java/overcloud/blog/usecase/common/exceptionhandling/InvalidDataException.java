package overcloud.blog.usecase.common.exceptionhandling;


import java.util.List;

import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.common.response.ApiValidationError;
import overcloud.blog.usecase.common.response.RestResponse;

@Getter
@Setter
public class InvalidDataException extends RuntimeException {

    private RestResponse<ApiValidationError> response;

    public InvalidDataException(RestResponse<ApiValidationError> response) {
        this.response = response;
    }
}
