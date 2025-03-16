package overcloud.blog.usecase.admin.user.create_user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.LetterAndNumber;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateUserRequest {
    @NotBlank(message = UserResMsg.USER_REGISTER_USERNAME_NOTBLANK)
    @Size(min = 6, max = 32, message = UserResMsg.USER_REGISTER_USERNAME_SIZE)
    @LetterAndNumber(message = UserResMsg.USER_REGISTER_USERNAME_LETTERANDNUMBER)
    private String username;

    @NotBlank(message = UserResMsg.USER_REGISTER_EMAIL_NOTBLANK)
    @Email(message = UserResMsg.USER_REGISTER_EMAIL_VALID)
    private String email;

    @NotBlank(message = UserResMsg.USER_REGISTER_PASSWORD_NOTBLANK)
    @Size(min = 8, max = 64, message = UserResMsg.USER_REGISTER_PASSWORD_SIZE)
    private String password;

    private String bio;
    private String image;
    private boolean enabled;
}
