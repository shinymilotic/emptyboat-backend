package overcloud.blog.usecase.common.response;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.common.auth.AuthResMsg;
import overcloud.blog.usecase.common.validation.ResMsg;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class RestResponse<T> {
    
    private String code;

    private String message;

    private Collection<T> data;
}
