package overcloud.blog.usecase.user.confirm_email;

import overcloud.blog.response.RestResponse;

public interface ConfirmEmailService {
    RestResponse<Void> confirmEmail(ConfirmEmailRequest confirmToken);
}
