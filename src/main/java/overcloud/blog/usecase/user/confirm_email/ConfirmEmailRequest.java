package overcloud.blog.usecase.user.confirm_email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmEmailRequest {
    @JsonProperty("confirmToken")
    private String confirmToken;
}
