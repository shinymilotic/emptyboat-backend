package overcloud.blog.usecase.auth.confirm_email;

public interface ConfirmEmailService {
    boolean confirmEmail(ConfirmEmailRequest confirmToken);
}
