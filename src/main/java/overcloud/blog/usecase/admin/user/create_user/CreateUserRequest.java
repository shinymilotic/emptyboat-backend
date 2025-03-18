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

import static overcloud.blog.usecase.admin.user.create_user.CreateUserResMsg.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = USERNAME_NOTBLANK)
    @Size(min = 6, max = 32, message = USERNAME_SIZE)
    @LetterAndNumber(message = USERNAME_LETTERANDNUMBER)
    private String username;
    @NotBlank(message = EMAIL_NOTBLANK)
    @Email(message = EMAIL_VALID)
    private String email;
    @NotBlank(message = PASSWORD_NOTBLANK)
    @Size(min = 8, max = 64, message = PASSWORD_SIZE)
    private String password;
    private String bio;
    private String image;
    private boolean enabled;
}
