package overcloud.blog.infrastructure.validation;

public interface Error {

    void setMessageId(String id);
    String getMessageId();
    void setErrorMessage(String id);
    String getErrorMessage();
}
