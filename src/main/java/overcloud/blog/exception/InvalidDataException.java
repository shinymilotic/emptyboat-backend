package overcloud.blog.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidDataException extends RuntimeException {
    private Object response;

    public InvalidDataException(Object response) {
        this.response = response;
    }
}
