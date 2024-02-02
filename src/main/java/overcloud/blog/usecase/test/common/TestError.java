package overcloud.blog.usecase.test.common;

import overcloud.blog.infrastructure.validation.Error;

public enum TestError implements Error {
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
    public String getErrorMessage() {
        return message;
    }

    @Override
    public void setErrorMessage(String message) {
        this.message = message;
    }
}
