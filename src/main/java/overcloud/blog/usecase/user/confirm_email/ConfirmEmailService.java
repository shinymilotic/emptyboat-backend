package overcloud.blog.usecase.user.confirm_email;

public interface ConfirmEmailService {
    Void confirmEmail(ConfirmEmailRequest confirmToken);
}
