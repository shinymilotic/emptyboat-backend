package overcloud.blog.usecase.user.common;

import overcloud.blog.usecase.common.validation.ResMsg;


public enum UserError implements ResMsg {
    USER_NOT_FOUND("user.get-current-user.not-found", "Bạn phải đăng nhập trước!"),
    USER_USERNAME_EXIST("user.username.exists", "Tên tài khoản đã tồn tại!"),
    USER_EMAIL_EXIST("user.email.exists", "Email này đã được sử dụng!"),
    USER_EMAIL_NO_EXIST("user.email.no-exists", "Tên đăng nhập hoặc mật khẩu không tồn tại!"),
    USER_NON_ENABLED("user.enabled.non", "Tài khoản chưa được kích hoạt");

    private String id;

    private String errorMessage;

    UserError(String id, String errorMessage) {
        this.id = id;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessageId() {
        return id;
    }

    @Override
    public void setMessageId(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public void setMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
