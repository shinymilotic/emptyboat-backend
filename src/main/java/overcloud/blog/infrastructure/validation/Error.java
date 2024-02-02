package overcloud.blog.infrastructure.validation;

public interface Error {

    String getMessageId();

    void setMessageId(String messageId);

    String getErrorMessage();

    void setErrorMessage(String errorMessage);
}
