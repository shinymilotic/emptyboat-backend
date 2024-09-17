package overcloud.blog.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class RestResponse<T> {    
    private String code;
    private String message;
    private T data;
}
