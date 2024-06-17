package overcloud.blog.usecase.test.common;

import overcloud.blog.core.validation.ResMsg;

public enum TestError implements ResMsg {
    ANSWER_EMPTY("test.answer.empty", "Answer must not empty!"),
    ;
    private String id;

    private String message;

    TestError(String id, String message) {
        this.id = id;
        this.message = message;
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
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
