package overcloud.blog.usecase.user.update_user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.utils.validation.LetterAndNumber;

//@JsonTypeName("user")
@Getter
@Setter
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UpdateUserRequest {
    @JsonProperty("username")
    @NotBlank(message = UpdateUserResMsg.USERNAME_NOTBLANK)
    @Size(min = 6, max = 32, message = UpdateUserResMsg.USERNAME_SIZE)
    @LetterAndNumber(message = UpdateUserResMsg.USERNAME_LETTERANDNUMBER)
    private String username;

    @JsonProperty("bio")
    @Size(min = 8, max = 1000, message = UpdateUserResMsg.BIO_SIZE)
    private String bio;

    @JsonProperty("image")
    private String image;
}
