package overcloud.blog.usecase.test.common;


import overcloud.blog.usecase.common.validation.ResMsg;


public enum PracticeError implements ResMsg {
    PRACTICE_NOT_FOUND("practice.not-found", "Không tồn tại phần thực hành này!"),
    ;

    private String id;

    private String errorMessage;

    PracticeError(String id, String errorMessage) {
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
