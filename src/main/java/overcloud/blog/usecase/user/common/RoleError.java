package overcloud.blog.usecase.user.common;

import overcloud.blog.usecase.common.validation.ResMsg;

public enum RoleError implements ResMsg {

    ROLENAME_SIZE("role.rolename.size", "Role name length must between 1 to 20 characters"),

    ROLE_LIST_NOT_EMPTY("role.rolelist.not-empty", "Role list must not be empty"),
    UPDATE_ROLE_NOT_EXIST("role.rolelist.not-exist", "Update role not exist!"),
    ROLE_EXISTED("role.existed", "Role is existed!"),
    UPDATE_ROLE_FAILED("role.update-fail", "Updating role failed!"),
    ROLE_ASSIGNMENT_FAILED("role.assignment", "Role assignment failed!");

    private String messageId;

    private String errorMessage;

    RoleError(String messageId, String errorMessage) {
        this.messageId = messageId;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessageId() {
        return this.messageId;
    }

    @Override
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }

    @Override
    public void setMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
