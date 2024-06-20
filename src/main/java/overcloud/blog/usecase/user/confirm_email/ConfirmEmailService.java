package overcloud.blog.usecase.user.confirm_email;

import overcloud.blog.usecase.common.response.RestResponse;

public interface ConfirmEmailService {
    RestResponse<Void> confirmEmail(ConfirmEmailRequest confirmToken);
}
