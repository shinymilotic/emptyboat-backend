package overcloud.blog.usecase.user.update_user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.usecase.common.validation.annotation.LetterAndNumber;

//@JsonTypeName("user")
@Getter
@Setter
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UpdateUserRequest {
    @JsonProperty("username")
    @NotBlank(message = "user.register.username.not-blank")
    @Size(min = 6, max = 32, message = "user.register.username.size")
    @LetterAndNumber(message = "user.register.username.letter-and-number")
    private String username;

    @JsonProperty("bio")
    @Size(min = 8, max = 1000, message = "user.profile.bio.size")
    private String bio;

    @JsonProperty("image")
    private String image;
}
