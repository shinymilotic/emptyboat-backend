package overcloud.blog.infrastructure.validation;

public interface Error {

    void setMessageId(String messageId);
    String getMessageId();
    void setErrorMessage(String errorMessage);
    String getErrorMessage();
}
