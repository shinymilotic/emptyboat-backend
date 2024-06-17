package overcloud.blog.usecase.user.confirm_email;

public interface ConfirmEmailService {
    boolean confirmEmail(ConfirmEmailRequest confirmToken);
}
